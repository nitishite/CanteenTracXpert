package com.example.christcanteenonlineorderingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Block3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block3);

        String blockName = getIntent().getStringExtra("blockName");


        findViewById(R.id.block3_mingos_canteen).setOnClickListener(listener -> {
            Intent intent = new Intent(Block3.this, CanteenMenu.class);
            intent.putExtra("canteenName", "Mingo's");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });


    }
}