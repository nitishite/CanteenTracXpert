package com.example.christcanteenonlineorderingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Block4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block4);

        String blockName = getIntent().getStringExtra("blockName");


        findViewById(R.id.block4_ich).setOnClickListener(listener -> {
            Intent intent = new Intent(Block4.this, CanteenMenu.class);
            intent.putExtra("canteenName", "ICH");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });

        findViewById(R.id.block4_bf).setOnClickListener(listener -> {
            Intent intent = new Intent(Block4.this, CanteenMenu.class);
            intent.putExtra("canteenName", "Bhagwati Foods");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });

        findViewById(R.id.block4_bhk).setOnClickListener(listener -> {
            Intent intent = new Intent(Block4.this, CanteenMenu.class);
            intent.putExtra("canteenName", "Brunch House Kitchen");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });


    }
}