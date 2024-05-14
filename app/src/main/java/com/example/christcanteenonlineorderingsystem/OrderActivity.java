package com.example.christcanteenonlineorderingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christcanteenonlineorderingsystem.Adapters.CartAdapter;
import com.example.christcanteenonlineorderingsystem.model.CartParcable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class OrderActivity extends AppCompatActivity implements TimePickerDialogFragment.TimePickedListener {

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

            showTimePickerDialog();
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

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerDialogFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static boolean isValidTime(int hour, int minute) {
        // Get current time
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        // Calculate the time 15 minutes from now
        currentTime.add(Calendar.MINUTE, 15);
        int minHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minMinute = currentTime.get(Calendar.MINUTE);

        // Check if the given time is after 9 AM
        if (hour < 9 || (hour == 9 && minute < 0)) {
            return false;
        }
        // Check if the given time is before 4 PM
        if (hour >= 16) {
            return false;
        }
        // Check if the given time is at least 15 minutes after the current time
        if (hour < minHour || (hour == minHour && minute < minMinute)) {
            return false;
        }
        // If all conditions are met, return true
        return true;
    }


    @Override
    public void onTimePicked(int hourOfDay, int minute) {

        if (!isValidTime(hourOfDay, minute)) {
            Toast.makeText(OrderActivity.this, "Choose a time 15 minutes from now and between 9-4", Toast.LENGTH_SHORT).show();
            return;
        }

        // Handle the time picked event here
        Intent inte = new Intent(OrderActivity.this, PaymentActivity.class);
        inte.putExtra("total", total);
        inte.putExtra("hourOfDay", hourOfDay);
        inte.putExtra("minute", minute);
        startActivity(inte);
    }
}