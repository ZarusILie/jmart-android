package LazaruslieJmartKD.jmart_android;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import LazaruslieJmartKD.jmart_android.request.RegisterRequest;

/**
 * class RegisterActivity
 *
 * @author (Lazaruslie Karsono)
 */


public class RegisterActivity extends AppCompatActivity {

    private EditText name_regis;
    private EditText email_regis;
    private EditText pass_regis;
    private Button regisButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_regis = findViewById(R.id.nameRegis);
        email_regis = findViewById(R.id.emailRegis);
        pass_regis = findViewById(R.id.passRegis);
        regisButton = findViewById(R.id.regisBtn);

        RequestQueue queue = Volley.newRequestQueue(this);

        regisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_regis.getText().toString();
                String password = pass_regis.getText().toString();
                String name = name_regis.getText().toString();
                RegisterRequest registerRequest = new RegisterRequest(name, email, password, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj != null){
                                Toast.makeText(getApplicationContext(), "Register successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Register unsuccessful", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Register unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Register unsuccessful", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(registerRequest);
            }
        });
    }
}