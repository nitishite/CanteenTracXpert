package com.example.christcanteenonlineorderingsystem.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christcanteenonlineorderingsystem.R;

import java.util.ArrayList;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Map<String, ArrayList<Double>> cart;
    private final ArrayList<String> keys;
    private static final String TAG = "CartAdapter";

    public CartAdapter(Map<String, ArrayList<Double>> cart) {
        this.cart = cart;
        this.keys = new ArrayList<>(cart.keySet());
        Log.d(TAG, "CartAdapter: " + cart.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String key = keys.get(position);
        ArrayList<Double> values = cart.get(key);
        assert values != null;
        Log.d(TAG, "CartAdapter: Bind " + key + ", " + values.size());
        holder.bind(key, values);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + cart.size());
        return cart.keySet().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView order_name;
        private final TextView order_items;
        private final TextView order_cost;
        private final TextView order_total_cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_name = itemView.findViewById(R.id.order_name);
            order_items = itemView.findViewById(R.id.order_items);
            order_cost = itemView.findViewById(R.id.order_cost);
            order_total_cost = itemView.findViewById(R.id.order_total_cost);
        }

        public void bind(String key, ArrayList<Double> values) {
            order_name.setText(key);
            order_items.setText("₹" + values.get(0).toString());
            order_cost.setText(values.get(1).toString());
            double item_total = values.get(0) * values.get(1);
            Log.d(TAG, "bind: " + key + ", " + item_total);
            order_total_cost.setText("₹" + values.get(0) * values.get(1));
        }
    }
}
