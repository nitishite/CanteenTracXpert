package com.example.christcanteenonlineorderingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CentralBlockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central_block);

        String blockName = getIntent().getStringExtra("blockName");

//        ((TextView) findViewById(R.id.block_name)).setText(blockName);


        findViewById(R.id.eleven_canteen).setOnClickListener(listener -> {
            Intent intent = new Intent(CentralBlockActivity.this, CanteenMenu.class);
            intent.putExtra("canteenName", "#e11even");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });


        findViewById(R.id.christ_bakery_canteen).setOnClickListener(listener -> {
            Intent intent = new Intent(CentralBlockActivity.this, CanteenMenu.class);
            intent.putExtra("canteenName", "Christ Bakery");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });

        findViewById(R.id.mingos_canteen).setOnClickListener(listener -> {
            Intent intent = new Intent(CentralBlockActivity.this, CanteenMenu.class);
            intent.putExtra("canteenName", "Mingo's");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });

        findViewById(R.id.michaels_canteen).setOnClickListener(listener -> {
            Intent intent=new Intent(CentralBlockActivity.this, CanteenMenu.class);
            intent.putExtra("canteenName","Michael's Corner");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(listener -> {
            startActivity(new Intent(this, CanteenScreen.class));
        });

    }
}