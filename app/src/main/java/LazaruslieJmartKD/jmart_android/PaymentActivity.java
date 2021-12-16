package LazaruslieJmartKD.jmart_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import LazaruslieJmartKD.jmart_android.model.Account;
import LazaruslieJmartKD.jmart_android.model.Product;
import LazaruslieJmartKD.jmart_android.request.CreatePaymentRequest;
import LazaruslieJmartKD.jmart_android.request.RequestFactory;

/**
 * class PaymentActivity
 *
 * @author (Lazaruslie Karsono)
 */

public class PaymentActivity extends AppCompatActivity {
    public static final String Extra_Amount = "LazaruslieJmartKD.jmart_android.Extra_Amount";
    public static final String Extra_Address = "LazaruslieJmartKD.jmart_android.Extra_Address";
    private Product fetchedProduct;
    private static final Gson gson = new Gson();
    private Button SubmitBtn;
    private Button CancelBtn;
    private TextView ProductName_Payment;
    private TextView Category_Payment;
    private TextView Price_Detail;
    private TextView Discount_Detail;
    private TextView Seller_ID;
    private TextView Total_Price;
    private TextView Balance_Payment;
    private TextView Shipment_Payment;
    private EditText Amount_Payment;
    private EditText Shipment_Address;
    private double productPrice;
    private byte shipmentPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        RequestQueue queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        int productId = intent.getIntExtra(MainActivity.Extra_ProductID, 0);
        productPrice = intent.getDoubleExtra(ProductDetailActivity.Extra_Price, 0);
        Account currentLogged = LoginActivity.getLoggedAccount();

        ProductName_Payment = findViewById(R.id.ProdNamePay);
        Category_Payment = findViewById(R.id.CategoryPayment);
        Price_Detail = findViewById(R.id.PriceDetailPayment);
        Discount_Detail = findViewById(R.id.DiscountDetailPayment);
        Seller_ID = findViewById(R.id.SellerIdPayment);
        Total_Price = findViewById(R.id.TotalPrice);
        Balance_Payment = findViewById(R.id.BalancePayment);
        Shipment_Payment = findViewById(R.id.ShipmentPayment);
        Shipment_Address = findViewById(R.id.ShipmentAddress);
        Amount_Payment = findViewById(R.id.AmountPayment);
        Amount_Payment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int getNewAmount;
                try{
                    getNewAmount = Integer.parseInt(Amount_Payment.getText().toString());
                }catch(NumberFormatException e){
                    getNewAmount = 0;
                }
                if (!(getNewAmount > 0)) {
                    Amount_Payment.setText(String.valueOf(1));
                    Total_Price.setText(String.valueOf(productPrice * 1));
                }else{
                    Total_Price.setText(String.valueOf(getNewAmount * productPrice));
                }
            }
        });

        StringRequest fetchProductDataRequest = RequestFactory.getById("product", productId, new Response.Listener<String>() {
            int amount = Integer.parseInt(Amount_Payment.getText().toString());

            @Override
            public void onResponse(String response) {
                fetchedProduct = gson.fromJson(response, Product.class);
                ProductName_Payment.setText(fetchedProduct.name);
                Category_Payment.setText(fetchedProduct.category.toString());
                double productPrice = Math.round((fetchedProduct.price * 100.00)/100.00);
                double productDiscount = Math.round((fetchedProduct.discount * 100.00)/100.0);
                Price_Detail.setText(String.valueOf(productPrice));
                Discount_Detail.setText(String.valueOf(productDiscount));
                Seller_ID.setText(String.valueOf(fetchedProduct.accountId));
                Total_Price.setText(String.valueOf(amount * (productPrice - productDiscount)));
                Balance_Payment.setText(String.valueOf(currentLogged.balance));
                shipmentPlans = fetchedProduct.shipmentPlans;

                switch (shipmentPlans){
                    case 0:
                        Shipment_Payment.setText("INSTANT");
                        break;
                    case 1:
                        Shipment_Payment.setText("SAME_DAY");
                        break;
                    case 2:
                        Shipment_Payment.setText("NEXT_DAY");
                        break;
                    case 4:
                        Shipment_Payment.setText("KARGO");
                        break;
                    default:
                        Shipment_Payment.setText("REGULER");
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        queue.add(fetchProductDataRequest);
        SubmitBtn = findViewById(R.id.SubmitPaymentBtn);
        CancelBtn = findViewById(R.id.CancelBtn);
        SubmitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InvoiceHistoryActivity.class);
                intent.putExtra(Extra_Amount, Integer.parseInt(Amount_Payment.getText().toString()));
                if(isEmpty(Shipment_Address)){
                    Toast.makeText(getApplicationContext(), "Shipment address can't be empty.", Toast.LENGTH_LONG).show();
                }
                else {
                    intent.putExtra(Extra_Address, Shipment_Address.getText().toString());
                    CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(String.valueOf(LoginActivity.getLoggedAccount().id), String.valueOf(productId), Amount_Payment.getText().toString(), Shipment_Address.getText().toString(), String.valueOf(shipmentPlans),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Toast.makeText(getApplicationContext(), "Payment has been submitted. Waiting seller's response", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Create payment unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Create payment unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(createPaymentRequest);
                }
            }
        });
        CancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}