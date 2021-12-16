package LazaruslieJmartKD.jmart_android;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import LazaruslieJmartKD.jmart_android.model.Product;

/**
 * class MainActivity
 *
 * @author (Lazaruslie Karsono)
 */

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    public static final String Extra_ProductID = "LazaruslieJmartKD.jmart_android.Extra_ProductID";
    private static final Gson gson = new Gson();
    MyRecyclerViewAdapter adapter;
    private TabLayout TabLayoutMain;
    private CardView Product_CV;
    private Button PrevBtn;
    private Button NextBtn;
    private Button GoBtn;
    private EditText Page_ET;
    private int page;
    private CardView Filter_CV;
    private EditText ProductName_ET;
    private EditText LowestPrice_ET;
    private EditText HighestPrice_ET;
    private CheckBox newCB;
    private CheckBox usedCB;
    private Button ApplyBtn;
    private Button ClearBtn;
    private Spinner Spin_filterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue queue = Volley.newRequestQueue(this);

        TabLayoutMain = findViewById(R.id.tabLayoutMain);
        Product_CV = findViewById(R.id.product_CV);
        Filter_CV = findViewById(R.id.filter_CV);

        //Tab Selector Listener
        TabLayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        Product_CV.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        Filter_CV.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        Product_CV.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        Filter_CV.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { } //Reselect Tab unused.
        });

        List<Product> productNames = new ArrayList<>();
        page = 0;
        fetchProduct(productNames, page, queue, false);
        RecyclerView recyclerView = findViewById(R.id.products_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, productNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        PrevBtn = findViewById(R.id.prevBtn);
        NextBtn = findViewById(R.id.nextBtn);
        GoBtn   = findViewById(R.id.goBtn);
        Page_ET = findViewById(R.id.page_ET);
        PrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page > 0){
                    page--;
                    fetchProduct(productNames, page, queue, true);
                }
            }
        });
        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                fetchProduct(productNames, page, queue, true);

            }
        });
        GoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = Integer.parseInt(Page_ET.getText().toString());
                fetchProduct(productNames, page, queue, true);
            }
        });
        //Filter CardView
        ProductName_ET = findViewById(R.id.productName_ET);
        LowestPrice_ET = findViewById(R.id.lowestPrice_ET);
        HighestPrice_ET = findViewById(R.id.highestPrice_ET);
        Spin_filterCategory = findViewById(R.id.spin_filterCategory);
        newCB = findViewById(R.id.new_CB);
        usedCB = findViewById(R.id.used_CB);
        String checkCondition;
        if(newCB.isChecked()){
            checkCondition = newCB.getText().toString();
        }
        if(usedCB.isChecked()){
            checkCondition = usedCB.getText().toString();
        }
        ApplyBtn = findViewById(R.id.applyBtn);
        ApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = ProductName_ET.getText().toString();
                String lowestPrice= LowestPrice_ET.getText().toString();
                String highestPrice = HighestPrice_ET.getText().toString();
                String category = Spin_filterCategory.getSelectedItem().toString();
                StringRequest filterRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:6969/product/getFiltered?pageSize=10&accountId="+LoginActivity.getLoggedAccount().id+"&search="+productName+"&minPrice="+lowestPrice+"&maxPrice="+highestPrice+"&category="+category, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonReader reader = new JsonReader(new StringReader(response));
                        try {
                            productNames.clear();
                            reader.beginArray();
                            while(reader.hasNext()){
                                productNames.add(gson.fromJson(reader, Product.class));
                            }
                            adapter.refresh(productNames);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Filter product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                        }
                        //After filtering, move back to display the product tab (set product visible, set Filter invisible)
                        Product_CV.setVisibility(View.VISIBLE);
                        Filter_CV.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Filtering Successful", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Filtering error, check again.", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(filterRequest);
            }
        });

        ClearBtn = findViewById(R.id.clearBtn);
        ClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Testing Clear", Toast.LENGTH_SHORT).show();
                ProductName_ET.setText("");
                LowestPrice_ET.setText("");
                HighestPrice_ET.setText("");
                newCB.setChecked(false);
                usedCB.setChecked(false);
                Spin_filterCategory.setSelection(0);
            }
        });
    }
    //Fetch Products Request Method
    public void fetchProduct(List<Product> productNames, int page, RequestQueue queue, boolean refreshAdapter){
        StringRequest fetchProductsRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:6969/product/page?page="+page+"&pageSize=10", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonReader reader = new JsonReader(new StringReader(response));
                try {
                    productNames.clear();
                    reader.beginArray();
                    while(reader.hasNext()){
                        productNames.add(gson.fromJson(reader, Product.class));
                    }
                    if(refreshAdapter){
                        adapter.refresh(productNames);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(fetchProductsRequest);
    }
    //RecycleView Item ClickListener
    @Override
    public void onItemClick(View view, int position) {
        int clickedItemId = adapter.getClickedItemId(position);
        Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
        intent.putExtra(Extra_ProductID, clickedItemId);
        startActivity(intent);
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        if(LoginActivity.getLoggedAccount().store == null){
            menu.getItem(1).setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search:
//                startActivity(new Intent(this, RegisterActivity.class));
                return true;
            case R.id.menu_add:
                startActivity(new Intent(this, CreateProductActivity.class));
                return true;
            case R.id.menu_aboutme:
                startActivity(new Intent(this, AboutMeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}