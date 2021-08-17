package uk.ac.tees.aad.W9493488;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences=getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        requestQueue= Volley.newRequestQueue(this);

        TextView register =  findViewById(R.id.createaccounttext);
        Button login =  findViewById(R.id.buttonReg);
         email =  findViewById(R.id.emailReg);
         password = findViewById(R.id.passwordReg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkforuser();
            }
        });
    }

    private void checkforuser() {

        StringRequest request  = new StringRequest(Request.Method.GET, getString(R.string.endpoint)+"/login?email="+email.getText()+"&password="+password.getText(), new Response.Listener<String>() {
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
        if (!response.equals("")){
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("login","yes");
            editor.putString("name",jsonObject.get("name").getAsString());
            editor.putString("email",jsonObject.get("email").getAsString());
            editor.putString("mobile",jsonObject.get("mobile").getAsString());
            editor.apply();
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
        }else
        {
            Toast.makeText(getApplicationContext(),"Entered Wrong Details",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
    }
}
