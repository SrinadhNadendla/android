package uk.ac.tees.aad.W9493488;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class previousBookings extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    String email;
    ArrayList<String> listObj;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_bookings);

        Button btn = findViewById(R.id.backtodashboard);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Dashboard.class));
            }
        });

        list =  findViewById(R.id.listView);
        listObj = new ArrayList<String>();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listObj);
        list.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request  = new StringRequest(Request.Method.GET, getString(R.string.endpoint)+"/getBooking?email="+email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonArray jsonObject = new JsonParser().parse(response).getAsJsonArray();
                for (int i=0;i<jsonObject.size();i++) {
                    listObj.add(jsonObject.get(i).getAsJsonObject().get("date").getAsString() + " \n\n" + jsonObject.get(i).getAsJsonObject().get("address").getAsString() + " " );
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listObj);
                list.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }
}
