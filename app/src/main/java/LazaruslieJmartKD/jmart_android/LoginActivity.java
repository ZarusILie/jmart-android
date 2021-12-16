package LazaruslieJmartKD.jmart_android;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import LazaruslieJmartKD.jmart_android.model.Account;
import LazaruslieJmartKD.jmart_android.model.Store;
import LazaruslieJmartKD.jmart_android.request.LoginRequest;

/**
 * class LoginActivity
 *
 * @author (Lazaruslie Karsono)
 */

public class LoginActivity extends AppCompatActivity {

    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;
    private TextView regisNow_TV;
    private EditText Email_TV;
    private EditText Pass_TV;
    private Button loginBtn_TV;

    public static Account getLoggedAccount() {
        return loggedAccount;
    }

    public static void reloadLoggedAccount(String response) {
        loggedAccount = gson.fromJson(response, Account.class);
    }

    public static void insertLoggedAccountStore(String response) {
        Store newStore = gson.fromJson(response, Store.class);
        loggedAccount.store = newStore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email_TV = findViewById(R.id.log_Email);
        Pass_TV = findViewById(R.id.log_Pass);
        loginBtn_TV = findViewById(R.id.loginBtn);
        regisNow_TV = findViewById(R.id.regisNow);

        RequestQueue queue = Volley.newRequestQueue(this);

        loginBtn_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email_TV.getText().toString();
                String password = Pass_TV.getText().toString();
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
        regisNow_TV.setOnClickListener(new View.OnClickListener(){
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