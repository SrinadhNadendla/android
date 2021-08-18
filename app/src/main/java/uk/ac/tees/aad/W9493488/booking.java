package uk.ac.tees.aad.W9493488;

import  androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class booking extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String addressDetails;
    String coordinates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        TextView address = findViewById(R.id.addressText);
        Button pickup = findViewById(R.id.pickup);
        TextView latlng = findViewById(R.id.latng);


        Intent intent = getIntent();
        latlng.setText(intent.getStringExtra("latlng"));
        coordinates = intent.getStringExtra("latlng");
        address.setText(intent.getStringExtra("address"));
        addressDetails = intent.getStringExtra("address");
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker mDatePickerDialogFragment;
                mDatePickerDialogFragment = new DatePicker();
                mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        RequestQueue requestQueue  = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");


        StringRequest request  = new StringRequest(Request.Method.GET, getString(R.string.endpoint)+"/booking?email="+
                email+"&address="+
                addressDetails+"&latlng="+
                coordinates+"&date="+dayOfMonth+"/"+month+"/"+year, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                validateData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

    private void validateData(String response) {
        if (response.equals("done")){
            startActivity(new Intent(getApplicationContext(),ConfirmBooking.class));
        }else
        {
            Toast.makeText(getApplicationContext(),"Entered Wrong Details",Toast.LENGTH_LONG).show();
        }
    }
}
