package com.example.christcanteenonlineorderingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        ((Button) findViewById(R.id.logout)).setOnClickListener(listener -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, StartActivity.class));
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(listener -> {
            startActivity(new Intent(MainActivity.this, CanteenScreen.class));
        });


        TextView textView = findViewById(R.id.central_Block);
        textView.setOnClickListener(listener -> {
            Intent intent = new Intent(MainActivity.this, CentralBlockActivity.class);
            intent.putExtra("blockName", "Central Block");
            startActivity(intent);
        });

        findViewById(R.id.audi_block_button).setOnClickListener(listener -> {
            Intent intent = new Intent(MainActivity.this, AudiBlock.class);
            intent.putExtra("blockName", "Audi Block");
            startActivity(intent);
        });

        findViewById(R.id.opposite_central_block_button).setOnClickListener(listener -> {
            Intent intent = new Intent(MainActivity.this, OppositeCentralBlock.class);
            intent.putExtra("blockName", "Opposite to Central Block");
            startActivity(intent);
        });

        findViewById(R.id.block3_button).setOnClickListener(listener -> {
            Intent intent = new Intent(MainActivity.this, Block3.class);
            intent.putExtra("blockName", "Block 3");
            startActivity(intent);
        });

        findViewById(R.id.block4_button).setOnClickListener(listener -> {
            Intent intent = new Intent(MainActivity.this, Block4.class);
            intent.putExtra("blockName", "Block 4");
            startActivity(intent);
        });

        findViewById(R.id.block_1_2_button).setOnClickListener(listener -> {
            Intent intent = new Intent(MainActivity.this, Block12.class);
            intent.putExtra("blockName", "Block 1 & 2");
            startActivity(intent);
        });

    }
}