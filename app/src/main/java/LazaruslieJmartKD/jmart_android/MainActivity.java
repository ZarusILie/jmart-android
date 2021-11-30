package LazaruslieJmartKD.jmart_android;


import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView hello_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hello_tv = findViewById(R.id.hello);
        hello_tv.setText(LoginActivity.getLoggedAccount().name);
    }
}