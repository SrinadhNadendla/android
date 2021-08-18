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

import org.w3c.dom.Text;

import java.time.LocalDate;

public class Register extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText password;
    EditText mobile;
    Button register;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.nameReg);
        email = findViewById(R.id.emailReg);
        password = findViewById(R.id.passwordReg);
        mobile = findViewById(R.id.mobileReg);
        register = findViewById(R.id.buttonReg);
        loginText = findViewById(R.id.loginTextBtn);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registertheUser();
            }
        });

    }

    private void registertheUser() {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String requestURL = getString(R.string.endpoint)+
                "register?email="
                +email.getText()+
                "&password="+password.getText()
                +"&mobile="+mobile.getText()
                +"&name="+name.getText();

        
        StringRequest request  = new StringRequest(Request.Method.GET,requestURL , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("")){
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("isLogin","yes");
                    editor.putString("name",jsonObject.get("name").getAsString());
                    editor.putString("email",jsonObject.get("email").getAsString());
                    editor.putString("mobile",jsonObject.get("mobile").getAsString());
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                }else
                {
                    Toast.makeText(getApplicationContext(),"Wrong Details",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Network Error"+error,Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {
      finishAffinity();
      startActivity(new Intent(Register.this,Login.class));
    }
}
