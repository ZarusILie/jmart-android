package LazaruslieJmartKD.jmart_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import LazaruslieJmartKD.jmart_android.request.RegisterStoreRequest;

public class AboutMeActivity extends AppCompatActivity {

    private TextView UserName;
    private TextView UserEmail;
    private TextView BalanceUser;
    private Button TopUpBtn;
    private Button RegisStoreBtn;
    private EditText TopUp;
    private CardView StoreExists_CV;
    private CardView RegisStore_CV;
    private EditText StoreName_ET;
    private EditText StoreAddress_ET;
    private EditText StorePhoneNumber_ET;
    private Button RegisStoreCancelBtn;
    private Button RegisStoreConfBtn;
    private TextView StoreName_TV;
    private TextView StoreAddress_TV;
    private TextView StorePhoneNumber_TV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        RequestQueue queue = Volley.newRequestQueue(this);

        UserName = findViewById(R.id.userName);
        UserEmail = findViewById(R.id.emailUser);
        BalanceUser = findViewById(R.id.balanceUser);
        TopUp = findViewById(R.id.topUp_ET);
        UserName.setText(LoginActivity.getLoggedAccount().name);
        UserEmail.setText(LoginActivity.getLoggedAccount().email);
        BalanceUser.setText(String.valueOf(LoginActivity.getLoggedAccount().balance));

        //Top Up button
        TopUpBtn = findViewById(R.id.topUpBtn);
        TopUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String balance = TopUp.getText().toString();
                String URL = "http://10.0.2.2:6969/account/"+LoginActivity.getLoggedAccount().id+"/topUp";
                StringRequest topUpRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LoginActivity.reloadLoggedAccount(response);
                        try {
                            Toast.makeText(getApplicationContext(), "Top Up successful", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(getIntent());
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Top Up unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Top Up unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("balance", balance);
                        return params;
                    }
                };
                queue.add(topUpRequest);
            }
        });

        RegisStoreBtn = findViewById(R.id.regisStoreBtn);
        RegisStore_CV = findViewById(R.id.regisStore_CV);
        StoreExists_CV = findViewById(R.id.storeExists_CV);
        StoreName_ET = findViewById(R.id.storeName_ET);
        StoreAddress_ET = findViewById(R.id.storeAddress_ET);
        StorePhoneNumber_ET = findViewById(R.id.storePhoneNumber_ET);
        RegisStoreCancelBtn = findViewById(R.id.regisStoreCancelBtn);
        RegisStoreConfBtn = findViewById(R.id.regisStoreConfBtn);
        if(LoginActivity.getLoggedAccount().store != null){
            RegisStoreBtn.setVisibility(View.GONE);
            StoreExists_CV.setVisibility(View.VISIBLE);
            //Show the existing store
            StoreName_TV = findViewById(R.id.storeName_TV);
            StoreAddress_TV = findViewById(R.id.storeAddress_TV);
            StorePhoneNumber_TV = findViewById(R.id.storePhoneNumber_TV);
            StoreName_TV.setText(LoginActivity.getLoggedAccount().store.name);
            StoreAddress_TV.setText(LoginActivity.getLoggedAccount().store.address);
            StorePhoneNumber_TV.setText(LoginActivity.getLoggedAccount().store.phoneNumber);
        }

        RegisStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisStoreBtn.setVisibility(View.INVISIBLE);
                RegisStore_CV.setVisibility(View.VISIBLE);
            }
        });
        RegisStoreCancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RegisStoreBtn.setVisibility(View.VISIBLE);
                RegisStore_CV.setVisibility(View.INVISIBLE);
            }
        });
        RegisStoreConfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = StoreName_ET.getText().toString();
                String address = StoreAddress_ET.getText().toString();
                String phoneNumber = StorePhoneNumber_ET.getText().toString();
                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(LoginActivity.getLoggedAccount().id, name, address ,phoneNumber, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LoginActivity.insertLoggedAccountStore(response);
                        try {
                            Toast.makeText(getApplicationContext(), "Register Store successful", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Register Store unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Register Store unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(registerStoreRequest);
            }
        });
    }
}