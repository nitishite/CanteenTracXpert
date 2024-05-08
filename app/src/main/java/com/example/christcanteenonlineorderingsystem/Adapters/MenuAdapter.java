package com.example.christcanteenonlineorderingsystem.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.christcanteenonlineorderingsystem.R;
import com.example.christcanteenonlineorderingsystem.model.MenuItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<MenuItem> dataList;
    private List<MenuItem> tempList;
    FirebaseFirestore db;
    private static final String TAG = "MenuAdapter";

    private NavigateInterface navigateInterface;

    private final File cacheDir;

    FirebaseStorage storage;

    private final Context context;
    public String queryString;

    public MenuAdapter(Context context, NavigateInterface navigateInterface, List<MenuItem> dataList, File cacheDir) {
        db = FirebaseFirestore.getInstance();
        this.dataList = dataList;
        this.tempList = dataList;
        this.navigateInterface = navigateInterface;
        this.cacheDir = cacheDir;
        this.context = context;
        storage = FirebaseStorage.getInstance();
        queryString = "";
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
//        view.getContext().getApplicationContext()
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        String itemName = tempList.get(position).getName();
        String itemCost = String.valueOf(tempList.get(position).getPrice());
        holder.itemName.setText(itemName);
        holder.itemCost.setText("₹" + itemCost);
        holder.addToCart.setOnClickListener(listener -> {
            navigateInterface.onNavigate(tempList.get(position));
        });

        String itemNameInFirestore = itemName.replace(" ", "_").toLowerCase();

        StorageReference storageRef = storage.getReference().child("fooditems/" + itemNameInFirestore + ".png");

        // Create a local file to save the downloaded file
        itemName = itemNameInFirestore.replace("/", "_");

        File localFile = new File(context.getFilesDir(), itemName + ".png");
        Log.d(TAG, "onBindViewHolder: Loaded Image for: " + itemName + ", Local File Exists: " + localFile.exists());

        // Start downloading
        holder.itemImage.setImageResource(R.drawable.ich_burger);
        String finalItemName = itemName;
        if (!localFile.exists()) {
            storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                // Load the downloaded file into the ImageView using Glide
                Glide.with(context)
                        .load(localFile)
                        .into(holder.itemImage);
            }).addOnFailureListener(e -> {
                // Handle any errors
                Log.d(TAG, finalItemName + " does not exist");
//                Toast.makeText(holder.itemImage.getContext(), "Error Occured: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        } else {
            Glide.with(context)
                    .load(localFile)
                    .into(holder.itemImage);
        }
    }

    @Override
    public int getItemCount() {
        return tempList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemCost;
        Button addToCart;
        ImageView itemImage;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemCost = itemView.findViewById(R.id.cost);
            addToCart = itemView.findViewById(R.id.addToCart);
            itemImage = itemView.findViewById(R.id.imageView12);
        }
    }

    public void filter(String filterText) {
        tempList = dataList.stream().filter(menuItem -> menuItem.getName().toLowerCase().contains(filterText.toLowerCase())).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void cleanFilter() {
        tempList = dataList;
        notifyDataSetChanged();
    }

//    private static int loadImage(String fileName) {
//        if (Objects.equals(fileName, "Idly"))
//            return R.drawable.dan_gold;
//        return R.drawable.food_item_2;
//    }
//    private static int loadImage(String fileName) {
//        if (Objects.equals(fileName, "Paneer Thali"))
//            return R.drawable.dan_gold;
//        return R.drawable.paneer_thali;
//    }

    private int loadImage(String fileName) {
        Log.d("MenuAdapter", fileName);
        switch (fileName) {
            case "Idly":
                return R.drawable.idly;
            case "Paneer Thali":
                return R.drawable.paneer_thali;
            case "Chicken Egg Dosa":
                return R.drawable.chicken_egg_dosa;
            case "Frid Egg":
                return R.drawable.frid_egg;
            case "Egg Roast":
                return R.drawable.egg_roast;
            case "Cold Coffee With Ice Creame":
                return R.drawable.cold_coffee_ice_cream;
            case "Corn Flacks":
                return R.drawable.corn_flacks;
            case "Masala Dosa":
                return R.drawable.masala_dosa;
            case "Chicken Noodle":
                return R.drawable.chicken_noodle;
            case "Cold Orange":
                return R.drawable.cold_orange;
            case "Grilled Chicken Sandwich":
                return R.drawable.grilled_chicken_sandwich;
            case "Medu Vada":
                return R.drawable.medu_vada;
            case "Kerala Paratha Veg Curry":
                return R.drawable.kerala_paratha_veg_curry;
            case "Onion Uthappam":
                return R.drawable.onion_uthappam;
            case "Set Dosa":
                return R.drawable.set_dosa;
            case "Paneer Chilly Dosa":
                return R.drawable.paneer_chilly_dosa;
            case "Chicken Paratha":
                return R.drawable.chicken_paratha;
            case "Egg Noodle":
                return R.drawable.egg_noodle;
            case "Poha":
                return R.drawable.poha;
            case "Masala Vada(2 pieces)":
                return R.drawable.masala_vada;
            case "Pepper Chicken":
                return R.drawable.pepper_chicken;
            case "Hot Chocolate":
                return R.drawable.hot_chocolate;
            case "Egg Kothu Parata":
                return R.drawable.egg_kothu_parata;
            case "Burger":
                return R.drawable.burger;
            case "Boiled Egg":
                return R.drawable.boiled_egg;
            case "Samosa":
                return R.drawable.samosa;
            case "Filter Coffee":
                return R.drawable.filter_coffee;
            case "Chicken Frankie Roll":
                return R.drawable.chicken_frankie_roll;
            case "Onion Dosa":
                return R.drawable.onion_dosa;
            case "Upma":
                return R.drawable.upma;
            case "Green Tea":
                return R.drawable.green_tea;
            case "Paneer Roll":
                return R.drawable.paneer_roll;
            case "Chola Bhatura":
                return R.drawable.chola_bhatura;
            case "Bread Butter":
                return R.drawable.bread_butter;
            case "Badam milk":
                return R.drawable.badam_milk;
            case "Tea":
                return R.drawable.tea;

            case "Boneless Biriyani":
                return R.drawable.boneless_biriyani;
            case "Chicken Biriyani":
                return R.drawable.chicken_biriyani;

            case "French Fries":
                return R.drawable.french_fries;
            case "Non Veg Jungli Fungli":
                return R.drawable.jungli_fungli;
            case "Cold Chocolate":
                return R.drawable.cold_chocolate;
            case "Chocolate Shake":
                return R.drawable.chocolate_shake;
            case "Cold Mocchachino":
                return R.drawable.cold_mocchachino;
            case "Oreo Shake":
                return R.drawable.oreo_shake1;
            case "Chicken Sandwich":
                return R.drawable.chicken_sandwich;

            case "Kerala Paratha P.B.M":
                return R.drawable.kerala_paratha;
            case "Chicken Thali":
                return R.drawable.chicken_thali;
            case "Veg Sandwich":
                return R.drawable.veg_sandwich;

            case "Milk":
                return R.drawable.milk;
            case "Hot Chocolate (Jumbo)":
                return R.drawable.hot_chocolate;
            case "Coffee (Regular)":
                return R.drawable.coffee;
            case "Vanilla Ice Cream":
                return R.drawable.vanilla_ice_cream;
            case "Black Tea (Regular)":
                return R.drawable.black_tea;
            case "Coffee (Jumbo)":
                return R.drawable.coffee;


            default:
                return R.drawable.chapathi;
        }
    }
}