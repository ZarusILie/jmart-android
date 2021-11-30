package LazaruslieJmartKD.jmart_android;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.util.JsonReader;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import LazaruslieJmartKD.jmart_android.model.Account;
import LazaruslieJmartKD.jmart_android.request.LoginRequest;

public class LoginActivity extends AppCompatActivity {

    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;
    private TextView regisNow_tv;
    private EditText Email_tv;
    private EditText Pass_tv;
    private Button loginBtn_tv;

    public static Account getLoggedAccount() {
        return loggedAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email_tv = findViewById(R.id.log_Email);
        Pass_tv = findViewById(R.id.log_Pass);
        loginBtn_tv = findViewById(R.id.loginBtn);
        regisNow_tv = findViewById(R.id.regisNow);

        RequestQueue queue = Volley.newRequestQueue(this);

        loginBtn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email_tv.getText().toString();
                String password = Pass_tv.getText().toString();
                LoginRequest loginRequest = new LoginRequest(email, password, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loggedAccount = gson.fromJson(response, Account.class);
                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Login unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred.", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(loginRequest);
            }
        });
        regisNow_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegisterActivity();
            }
        });
    }

    public void openRegisterActivity(){
        startActivity(new Intent(this, RegisterActivity.class));
    }

}