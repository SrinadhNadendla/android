package uk.ac.tees.aad.W9493488;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        mobile = findViewById(R.id.passwordReg);
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

    }

    @Override
    public void onBackPressed() {
      finishAffinity();
      startActivity(new Intent(Register.this,Login.class));
    }
}
