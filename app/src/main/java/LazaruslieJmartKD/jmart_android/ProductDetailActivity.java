package LazaruslieJmartKD.jmart_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import LazaruslieJmartKD.jmart_android.model.Product;
import LazaruslieJmartKD.jmart_android.request.RequestFactory;


/**
 * class ProductDetailActivity
 *
 * @author (Lazaruslie Karsono)
 */

public class ProductDetailActivity extends AppCompatActivity {

    public static final String Extra_Price = "LazaruslieJmartKD.jmart_android.Extra_Price";
    private Product fetchedProduct;
    private static final Gson gson = new Gson();
    private Button Purchase_Btn;
    private Button Back_Btn;
    private TextView Product_Detail;
    private TextView Price_Detail;
    private TextView Weight_Detail;
    private TextView Discount_Detail;
    private TextView Category_Detail;
    private TextView Shipment_Detail;
    private TextView Condition_Detail;
    private TextView Seller_Id;
    private double productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        RequestQueue queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        int productId = intent.getIntExtra(MainActivity.Extra_ProductID, 0);

        Purchase_Btn = findViewById(R.id.PurchaseBtn);
        Back_Btn = findViewById(R.id.BackBtn);
        Product_Detail = findViewById(R.id.ProductDetail);
        Price_Detail = findViewById(R.id.PriceDetail);
        Weight_Detail = findViewById(R.id.WeightDetail);
        Discount_Detail = findViewById(R.id.DiscountDetail);
        Category_Detail = findViewById(R.id.CategoryDetail);
        Shipment_Detail = findViewById(R.id.ShipmentDetail);
        Condition_Detail = findViewById(R.id.ConditionDetail);
        Seller_Id = findViewById(R.id.SellerIdDetail);

        StringRequest fetchProductDataRequest = RequestFactory.getById("product", productId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fetchedProduct = gson.fromJson(response, Product.class);
                Product_Detail.setText(fetchedProduct.name);
                if(fetchedProduct.name.length() >= 28){
                    Product_Detail.setTextSize(18.5f);
                    Product_Detail.setMaxEms(10);
                }
                Price_Detail.setText(String.valueOf(Math.round(fetchedProduct.price * 100.00)/100.00));
                productPrice = Double.parseDouble(Price_Detail.getText().toString());
                Weight_Detail.setText(String.valueOf(fetchedProduct.weight));
                Discount_Detail.setText(String.valueOf(Math.round(fetchedProduct.discount * 100.00)/100.00));
                Category_Detail.setText(fetchedProduct.category.toString());
                switch (fetchedProduct.shipmentPlans){
                    case 0:
                        Shipment_Detail.setText("INSTANT");
                        break;
                    case 1:
                        Shipment_Detail.setText("SAME_DAY");
                        break;
                    case 2:
                        Shipment_Detail.setText("NEXT_DAY");
                        break;
                    case 4:
                        Shipment_Detail.setText("KARGO");
                        break;
                    default:
                        Shipment_Detail.setText("REGULER");
                        break;
                }
                Condition_Detail.setText(fetchedProduct.conditionUsed ? "Used" : "New");
                Seller_Id.setText(String.valueOf(fetchedProduct.accountId));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        queue.add(fetchProductDataRequest);

        Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Purchase_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra(MainActivity.Extra_ProductID, productId);
                intent.putExtra(Extra_Price, productPrice);
                startActivity(intent);
            }
        });
    }

}