package com.example.christcanteenonlineorderingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christcanteenonlineorderingsystem.Adapters.CartAdapter;
import com.example.christcanteenonlineorderingsystem.model.CartParcable;

import java.util.ArrayList;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private static final String TAG = "OrderActivity`";

    Map<String, ArrayList<Double>> cart;

    private CartParcable cartParcelable;

    Double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("cart")) {
            cartParcelable = intent.getParcelableExtra("cart");
            if (cartParcelable != null)
                cart = cartParcelable.getCart();
        }

        setContentView(R.layout.activity_order);

        findViewById(R.id.payNow).setOnClickListener(listener -> {
            Intent inte = new Intent(OrderActivity.this, PaymentActivity.class);
            Log.d(TAG, "onCreate: " + total);
            inte.putExtra("total", total);
            startActivity(inte);
        });

        RecyclerView list = findViewById(R.id.item_list_order);
        list.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter adapter = new CartAdapter(cart);
        list.setAdapter(adapter);
        TextView order_total = findViewById(R.id.order_total);
        total = cart.values().stream()
                .mapToDouble(values -> values.get(0) * values.get(1))
                .sum();
        order_total.setText("₹" + String.valueOf(total));

    }
}