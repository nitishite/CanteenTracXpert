package com.example.christcanteenonlineorderingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Block12 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block12);

        String blockName = getIntent().getStringExtra("blockName");

//        ((TextView) findViewById(R.id.block_name)).setText(blockName);


        findViewById(R.id.canteen_18).setOnClickListener(listener -> {
            Intent intent = new Intent(Block12.this, CanteenMenu.class);
            intent.putExtra("canteenName", "Canteen18");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });
    }
}