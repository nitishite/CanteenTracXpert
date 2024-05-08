package com.example.christcanteenonlineorderingsystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CanteenScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_screen);

        findViewById(R.id.eleven_stall).setOnClickListener(listener -> {
            Intent intent = new Intent(CanteenScreen.this, CanteenMenu.class);
            startActivity(intent);
        });
    }
}