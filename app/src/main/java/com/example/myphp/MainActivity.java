package com.example.myphp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText bid, title, price;
    ListView l1;
    List<String> r1 = new ArrayList<String>();
    JSONArray jp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bid = findViewById(R.id.edtbid);
        title = findViewById(R.id.edttitle);
        price = findViewById(R.id.edtprice);
        l1 = findViewById(R.id.listview1);
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String rec = (String) r1.get(i);
                String cols[] = rec.split(" : ");
                bid.setText(cols[0]);
                title.setText(cols[1]);
                price.setText(cols[2]);
            }
        });
    }

    public void onInsert(View view) {
        String urlstring = "http://10.0.2.2/ap1/insert.php";
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response.toString() + "Inserted", Toast.LENGTH_LONG).show();

            }
        };
        Response.ErrorListener errorL = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.e(error.toString(), "onErrorResponse: ");
            }
        };
        StringRequest sr = new StringRequest(Request.Method.POST, urlstring, response, errorL) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap();
                parameter.put("bookid", bid.getText().toString());
                parameter.put("title", title.getText().toString());
                parameter.put("price", price.getText().toString());
                return parameter;
            }
        };
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(sr);
    }

    public void ondisplay(View view) {

        String urlstring = "http://10.0.2.2/ap1/display.php";
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res", response);


                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                try {
                    r1.clear();
                    jp = new JSONArray(response);
                    for (int i = 0; i < jp.length(); i++) {
                        JSONObject record = jp.getJSONObject(i);

                        r1.add(record.get("bid").toString() +
                                " : " + record.get("title").toString() +
                                " : " + record.get("price").toString());
//
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, r1.toString(), Toast.LENGTH_SHORT).show();
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, r1);
                l1.setAdapter(adapter);
                l1.getDisplay();


            }
        };
        Response.ErrorListener errorL = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest sr = new StringRequest(Request.Method.POST, urlstring, response, errorL);


        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(sr);

    }

    public void onDelete(View view) {
        String Url = "http://10.0.2.2/ap1/delete.php";
        StringRequest deleteRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                Log.d("result", response);
                // Handle response after successful deletion
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.e(error.toString(), "onErrorResponse: ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("bookid", bid.getText().toString());

                return params;
            }
        };

        // Add the delete request to the RequestQueue
        RequestQueue deleteQueue = Volley.newRequestQueue(MainActivity.this);
        deleteQueue.add(deleteRequest);
    }

    public void onUpdate(View view) {
        String updateUrl = "http://10.0.2.2/ap1/update.php";

        StringRequest updateRequest = new StringRequest(Request.Method.POST, updateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                // Handle response after successful update
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.e(error.toString(), "onErrorResponse: ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // Assuming you have the book ID, new title, and new price stored in variables
                params.put("bid", bid.getText().toString());
                params.put("new_title", title.getText().toString());
                params.put("new_price", price.getText().toString());
                return params;
            }
        };

        // Add the update request to the RequestQueue
        RequestQueue updateQueue = Volley.newRequestQueue(MainActivity.this);
        updateQueue.add(updateRequest);
    }

    public void onSearch(View view) {
        String Url = "http://10.0.2.2/ap1/search.php";
        StringRequest searchRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                Log.d("result", response);
                try {
                    jp = new JSONArray(response);
                    JSONObject record = jp.getJSONObject(0);
                    title.setText(record.get("title").toString());
                    price.setText(record.get("price").toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                Log.e(error.toString(), "onErrorResponse: ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("bookid", bid.getText().toString());

                return params;
            }
        };

        // Add the delete request to the RequestQueue
        RequestQueue searchQueue = Volley.newRequestQueue(MainActivity.this);
        searchQueue.add(searchRequest);
    }
}

