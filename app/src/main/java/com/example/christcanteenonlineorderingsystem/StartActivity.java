package com.example.christcanteenonlineorderingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.christcanteenonlineorderingsystem.admin.AdminLogin;
import com.example.christcanteenonlineorderingsystem.admin.AdminMainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            SharedPreferences sharedPref = StartActivity.this.getApplicationContext().getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
            String vendorName = sharedPref.getString("vendorName", null);
            if (vendorName == null) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(StartActivity.this, AdminMainActivity.class));
            }
        }

        findViewById(R.id.userLoginActivity).setOnClickListener(listener -> {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
        });

        findViewById(R.id.userSignupActivity).setOnClickListener(Listener -> {
            startActivity(new Intent(StartActivity.this, SignupActivity.class));
        });

        findViewById(R.id.adminActivity).setOnClickListener(Listener -> {
            startActivity(new Intent(StartActivity.this, AdminLogin.class));
        });

    }
}