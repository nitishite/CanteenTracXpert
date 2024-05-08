package com.example.christcanteenonlineorderingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christcanteenonlineorderingsystem.Adapters.MenuAdapter;
import com.example.christcanteenonlineorderingsystem.model.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CanteenMenu extends AppCompatActivity {

    FirebaseFirestore db;
    String TAG = "CanteenMenu";

    Map<String, ArrayList<Double>> cart;

    String canteenName, blockName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_menu);

        ((Button) findViewById(R.id.button6)).setOnClickListener(listener -> {
            startActivity(new Intent(CanteenMenu.this, MainActivity.class));
        });
        ((Button) findViewById(R.id.cart_button)).setOnClickListener(listener -> {
            startActivity(new Intent(CanteenMenu.this, CartActivity.class));
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("canteenName") && intent.hasExtra("blockName")) {
            canteenName = (String) intent.getExtras().get("canteenName");
            blockName = (String) intent.getExtras().get("blockName");
        }

        ((TextView) findViewById(R.id.canteen_name)).setText(canteenName);

        ((TextView) findViewById(R.id.canteen_name)).setText(canteenName);

        Log.d(TAG, "onCreate: " + canteenName + " : " + blockName);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        List<MenuItem> dataList = new ArrayList<>(); // Your dynamic data
        MenuAdapter adapter = new MenuAdapter(this, this::addToCart, dataList, getCacheDir());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.search_item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() > 0) {
                    adapter.filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() == 0) {
                    adapter.cleanFilter();
                }
                return false;
            }
        });


        db = FirebaseFirestore.getInstance();

        loadItems(dataList, adapter, canteenName, blockName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCart();

    }

    void loadItems(List<MenuItem> dataList, MenuAdapter adapter, String canteenName, String
            blockName) {
        Log.d(TAG, "loadItems: before Load iTems for canteen: |" + canteenName + "| and Block: |" + blockName + "| ");

        CollectionReference foodItems = db.collection("foodItems");
        Query query = foodItems.whereArrayContains("BlockName", blockName).whereEqualTo("StoreName", canteenName).orderBy("Name");
//        Query query = foodItems.whereArrayContains("BlockName", blockName).whereEqualTo("StoreName", canteenName);

        query
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "loadItems: Data acquired Successfully : " + task.getResult().size());

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.get("Name") + " : " + document.get("Price") + " : " + document.get("Variant"));


                            Number price = (Number) document.get("Price");
                            if (price == null)
                                continue;
                            Double price_val = 0.0;
                            try {
                                price_val = price.doubleValue();
                            } catch (Exception ex) {
                                price_val = price.doubleValue();
                            } finally {
                                dataList.add(new MenuItem((String) document.get("Name"), price_val));
                            }

                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }


    void loadCart() {
        cart = new HashMap<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("cart").document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, ArrayList<Double>> items = (Map<String, ArrayList<Double>>) document.get("items");
                    if (items == null) return;

                    for (Map.Entry<String, ArrayList<Double>> outerEntry : items.entrySet()) {
                        String innerKey = outerEntry.getKey();
                        ArrayList<Double> innerValue = outerEntry.getValue();
                        Log.d(TAG, "Log: " + innerKey);
                        cart.put(innerKey, innerValue);
                    }
                } else {
                    // create document
                }
            }
        });
    }

    void addToCart(MenuItem item) {
        long items = 1;
        try {
            items += cart.get(item.getName()).get(1);
        } catch (NullPointerException e) {
            // item does not exist in cart yet
        }

        long finalItems = items;
        cart.put(item.getName(), new ArrayList<Double>() {{
            add(item.getPrice());
            add((double) finalItems);
        }});

        Map<String, Map<String, ArrayList<Double>>> cartObject = new HashMap<>();
        cartObject.put("items", cart);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db.collection("cart")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .set(cartObject)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Intent i = new Intent(CanteenMenu.this, CartActivity.class);
//                    CartParcelable cartParcelable = new CartParcelable(cart);

//                    i.putExtra("cart", cartParcelable);
                    startActivity(i);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }


}