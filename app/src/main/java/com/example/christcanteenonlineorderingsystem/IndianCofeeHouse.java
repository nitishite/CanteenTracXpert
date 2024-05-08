package com.example.christcanteenonlineorderingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class IndianCofeeHouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indian_cofee_house);

        findViewById(R.id.cart_button).setOnClickListener(listener -> {
            startActivity(new Intent(IndianCofeeHouse.this, CartActivity.class));
        });
    }
}