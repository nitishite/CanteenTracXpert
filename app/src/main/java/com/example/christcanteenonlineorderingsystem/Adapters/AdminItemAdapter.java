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
import com.example.christcanteenonlineorderingsystem.admin.AdminMainActivity;
import com.example.christcanteenonlineorderingsystem.model.AdminItem;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdminItemAdapter extends RecyclerView.Adapter<AdminItemAdapter.AdminItemViewHolder> {

    private Context context;
    public List<AdminItem> foodItemList;
    private List<AdminItem> tempList;

    private OnItemClickListener itemClickListener;
    private static final String TAG = "AdminItemAdapter";
    FirebaseStorage storage;

    String filterText;

    public void filter(String filterText) {
        this.filterText = filterText;
        tempList = foodItemList.stream().filter(menuItem -> menuItem.getName().toLowerCase().contains(filterText.toLowerCase())).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void cleanFilter() {
        this.filterText = "";
        tempList = foodItemList;
        notifyDataSetChanged();
    }

    // Interface for item click listener
    public interface OnItemClickListener {
        void deleteItem(AdminItem foodItem);

        void showEditDialog(AdminItem foodItem, AdminMainActivity.onComplete complete);
    }

    public AdminItemAdapter(Context context, List<AdminItem> foodItemMap, OnItemClickListener itemClickListener) {
        this.context = context;
        this.foodItemList = foodItemMap;
        this.tempList = foodItemMap;
        this.itemClickListener = itemClickListener;
        storage = FirebaseStorage.getInstance();
        Log.d(TAG, "AdminItemAdapter: " + foodItemList.size());
    }

    @NonNull
    @Override
    public AdminItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_item, parent, false);
        return new AdminItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminItemViewHolder holder, int position) {
        AdminItem foodItem = tempList.get(position);

        holder.nameTextView.setText(foodItem.getName());
        holder.priceTextView.setText("₹" + foodItem.getPrice());

        String itemNameInFirestore = foodItem.getName().replace(" ", "_").toLowerCase();

        StorageReference storageRef = storage.getReference().child("fooditems/" + itemNameInFirestore + ".png");

        // Create a local file to save the downloaded file
        String itemName = itemNameInFirestore.replace("/", "_");

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


        // Set click listener on the TextView
        holder.deleteButton.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.deleteItem(foodItem);
                int positionOfItem = foodItemList.indexOf(foodItem);
                foodItemList.remove(positionOfItem);
                tempList = foodItemList;
                if (!Objects.equals(filterText, "")) {
                    filter(filterText);
                }
                this.notifyItemRemoved(positionOfItem);
            }
        });

        holder.editButton.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.showEditDialog(foodItem, () -> {
                    this.notifyItemChanged(position);
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + tempList.size());
        return tempList.size();
    }

    public static class AdminItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        Button deleteButton;
        Button editButton;
        ImageView itemImage;

        public AdminItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            priceTextView = itemView.findViewById(R.id.cost);
            deleteButton = itemView.findViewById(R.id.button2);
            editButton = itemView.findViewById(R.id.editButton);
            itemImage = itemView.findViewById(R.id.item_image);
        }
    }


}
