package com.example.christcanteenonlineorderingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AudiBlock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audi_block);


        findViewById(R.id.ich).setOnClickListener(listener -> {
            String blockName = getIntent().getStringExtra("blockName");
            Intent intent = new Intent(AudiBlock.this, CanteenMenu.class);
            intent.putExtra("canteenName", "ICH");
            intent.putExtra("blockName", blockName);
            startActivity(intent);
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(listener -> {
            startActivity(new Intent(this, CanteenScreen.class));
        });
    }
}