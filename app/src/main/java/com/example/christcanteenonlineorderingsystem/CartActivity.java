package com.example.christcanteenonlineorderingsystem;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.christcanteenonlineorderingsystem.model.CartParcable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    String TAG = "CartActivity";

    private LinearLayout item_list;

    private CartParcable cartParcable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = FirebaseFirestore.getInstance();

        item_list = findViewById(R.id.item_list);

        findViewById(R.id.button4).setOnClickListener(listener -> {
            Intent i = new Intent(CartActivity.this, OrderActivity.class);

            i.putExtra("cart", cartParcable);
            startActivity(i);
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("cart").document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    cartParcable = new CartParcable((Map<String, ArrayList<Double>>) document.get("items"));
                    if (cartParcable.getCart() == null) return;

                    for (Map.Entry<String, ArrayList<Double>> outerEntry : cartParcable.getCart().entrySet()) {
                        String innerKey = outerEntry.getKey();
                        ArrayList<Double> innerValue = outerEntry.getValue();
                        System.out.println("Inner Key: " + innerKey + ", Value: " + innerValue);

                        LinearLayout horizontalLayout = new LinearLayout(this);
                        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        horizontalLayout.setLayoutParams(params);
                        horizontalLayout.setGravity(Gravity.CENTER);

                        int paddingInDp = 10;
                        final float scale = getResources().getDisplayMetrics().density;
                        int paddingInPixels = (int) (paddingInDp * scale + 0.5f);

                        TextView itemName = new TextView(this);
                        itemName.setWidth(50);
                        itemName.setText(innerKey);
                        itemName.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
                        itemName.setTypeface(null, Typeface.BOLD);

                        TextView itemCount = new TextView(this);

                        Button decrement = new Button(this);
                        decrement.setText("-");
                        decrement.setOnClickListener(listener -> {
                            innerValue.set(1, innerValue.get(1) - 1);
                            if (innerValue.get(1) > 0) {
                                itemCount.setText(String.valueOf(innerValue.get(1)));
                                updateRecord(itemName.getText().toString(), innerValue.get(1), innerValue.get(0));
                            } else {
                                deleteItem(itemName.getText().toString());
                                item_list.removeView(horizontalLayout);
                                cartParcable.getCart().remove(innerKey);
                            }
                        });

                        Button increment = new Button(this);
                        increment.setText("+");
                        increment.setOnClickListener(listener -> {
                            innerValue.set(1, innerValue.get(1) + 1);
                            itemCount.setText(String.valueOf(innerValue.get(1)));
                            updateRecord(itemName.getText().toString(), innerValue.get(1), innerValue.get(0));
                        });

                        itemCount.setText(String.valueOf(innerValue.get(1)));
                        itemCount.setGravity(Gravity.CENTER);

                        LinearLayout.LayoutParams itemNameParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 15);

                        LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

                        LinearLayout.LayoutParams countParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 8);

                        horizontalLayout.addView(itemName, itemNameParam);
                        horizontalLayout.addView(decrement, buttonParam);
                        horizontalLayout.addView(itemCount, countParam);
                        horizontalLayout.addView(increment, buttonParam);

                        item_list.addView(horizontalLayout);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }

    void updateRecord(String itemName, Double itemCount, Double cost) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("cart").document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()));
        docRef.update(FieldPath.of("items", itemName), new ArrayList<Double>() {{
                    add(cost);
                    add(itemCount);
                }}).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    void deleteItem(String itemName) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("cart").document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()));

        Map<String, Object> updates = new HashMap<>();
        updates.put(("items." + itemName), FieldValue.delete());
        docRef.update(updates).addOnCompleteListener(listener -> Log.d(TAG, "DocumentSnapshot successfully updated!"));
    }

}