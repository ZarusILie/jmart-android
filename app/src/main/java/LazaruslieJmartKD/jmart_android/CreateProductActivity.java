package LazaruslieJmartKD.jmart_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import LazaruslieJmartKD.jmart_android.request.CreateProductRequest;

/**
 * class CreateProductActivity
 *
 * @author (Lazaruslie Karsono)
 */

public class CreateProductActivity extends AppCompatActivity {

    private EditText CreateProductName_ET;
    private EditText CreateProductWeight_ET;
    private EditText CreateProductPrice_ET;
    private EditText CreateProductDiscount_ET;
    private Button CreateProductBtn;
    private Button CancelProductBtn;
    private RadioGroup radio_conditionList;
    private boolean newProductCondition = true;
    private Spinner Spin_createCategory;
    private Spinner Spin_createShipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        RequestQueue queue = Volley.newRequestQueue(this);

        CreateProductName_ET = findViewById(R.id.createProductName_ET);
        CreateProductPrice_ET = findViewById(R.id.createProductPrice_ET);
        CreateProductWeight_ET = findViewById(R.id.createProductWeight_ET);
        CreateProductDiscount_ET = findViewById(R.id.createProductDiscount_ET);
        Spin_createCategory = findViewById(R.id.spin_createCategory);
        Spin_createShipment = findViewById(R.id.spin_createShipment);
        radio_conditionList = findViewById(R.id.radio_conditionList);
        radio_conditionList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_conditionNew:
                        newProductCondition = true;
                        break;
                    case R.id.radio_conditionUsed:
                        newProductCondition = false;
                        break;
                }
            }
        });
        CreateProductBtn = findViewById(R.id.createProductBtn);
        CreateProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountId = String.valueOf(LoginActivity.getLoggedAccount().id);
                String productName = CreateProductName_ET.getText().toString();
                String productPrice = CreateProductPrice_ET.getText().toString();
                String productWeight = CreateProductWeight_ET.getText().toString();
                String productDiscount = CreateProductDiscount_ET.getText().toString();
                String productCategory = Spin_createCategory.getSelectedItem().toString();
                String productShipment = Spin_createShipment.getSelectedItem().toString();

                switch (productShipment) {
                    case "INSTANT":
                        productShipment = String.valueOf(0);
                        break;
                    case "SAME_DAY":
                        productShipment = String.valueOf(1);
                        break;
                    case "NEXT_DAY":
                        productShipment = String.valueOf(2);
                        break;
                    case "REGULER":
                        productShipment = String.valueOf(3);
                        break;
                    case "KARGO":
                        productShipment = String.valueOf(4);
                        break;
                    default:
                        productShipment = String.valueOf(3);
                        break;
                }
                System.out.println(productCategory + "  " + productShipment);
                CreateProductRequest createProductRequest = new CreateProductRequest(accountId, productName, productWeight,
                        String.valueOf(newProductCondition), productPrice, productDiscount, productCategory, productShipment,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Toast.makeText(getApplicationContext(), "Create product successful", Toast.LENGTH_LONG).show();
                                    finish();
                                    //If succesful, go back to/and reload the Main Activity
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Create product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Create product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(createProductRequest);
            }
        });
        CancelProductBtn = findViewById(R.id.cancelProductBtn);
        CancelProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}