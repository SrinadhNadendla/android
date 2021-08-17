package uk.ac.tees.aad.W9493488;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", Context.MODE_PRIVATE);

                if(sharedPreferences.getString("login","no").equals("no"))
                {
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }else{
                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                }

            }
        },2000);
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
