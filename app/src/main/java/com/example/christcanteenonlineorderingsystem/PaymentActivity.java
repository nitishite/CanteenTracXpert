package com.example.christcanteenonlineorderingsystem;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        double total = getIntent().getExtras().getDouble("total");

        ((TextView) findViewById(R.id.textView18)).setText("Total: " + (total));

        findViewById(R.id.buttonSubmitPayment).setOnClickListener(listener -> {
//            Intent intent = new Intent(MainActivity.this, Block12.class);
//            intent.putExtra("blockName", "Block 1 & 2");
//            startActivity(intent);
            Toast.makeText(PaymentActivity.this, "Payment done successfully", Toast.LENGTH_SHORT).show();
        });
    }
}