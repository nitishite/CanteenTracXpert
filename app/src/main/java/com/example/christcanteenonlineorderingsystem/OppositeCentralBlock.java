package com.example.christcanteenonlineorderingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class OppositeCentralBlock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opposite_central_block);

        String blockName = getIntent().getStringExtra("blockName");

//        ((TextView) findViewById(R.id.block_name)).setText(blockName);


        findViewById(R.id.nandini_canteen).setOnClickListener(listener -> {
            Intent intent = new Intent(OppositeCentralBlock.this, CanteenMenu.class);
            intent.putExtra("canteenName", "Nandini");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });


        findViewById(R.id.fresheteria_canteen).setOnClickListener(listener -> {
            Intent intent = new Intent(OppositeCentralBlock.this, CanteenMenu.class);
            intent.putExtra("canteenName", "Fresheteria");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });

        findViewById(R.id.foodcourt_canteen).setOnClickListener(listener -> {
            Intent intent = new Intent(OppositeCentralBlock.this, CanteenMenu.class);
            intent.putExtra("canteenName", "Food Court");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });



    }
}