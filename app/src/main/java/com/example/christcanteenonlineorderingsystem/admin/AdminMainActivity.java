package com.example.christcanteenonlineorderingsystem.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christcanteenonlineorderingsystem.Adapters.AdminItemAdapter;
import com.example.christcanteenonlineorderingsystem.R;
import com.example.christcanteenonlineorderingsystem.model.AdminItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminMainActivity extends AppCompatActivity implements AdminItemAdapter.OnItemClickListener {

    private static final String TAG = "AdminMainPage";
    private FirebaseFirestore db;
    private String vendorName;

    private List<AdminItem> items;
    private AdminItemAdapter adapter;

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton addItemImage;

    private Uri addItemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);

        ((SearchView) findViewById(R.id.vendor_search)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        findViewById(R.id.button7).setOnClickListener(listener -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPref = AdminMainActivity.this.getApplicationContext().getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("vendorName");
            editor.apply();

            finish();
        });

        findViewById(R.id.add_food_item).setOnClickListener(listener -> {
            showAddItemDialog();
        });


        db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPref = AdminMainActivity.this.getApplicationContext().getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
        vendorName = sharedPref.getString("vendorName", "");
        ((TextView) findViewById(R.id.vendorname)).setText(vendorName);

        items = new ArrayList<>();

        adapter = new AdminItemAdapter(this, items, this);


        RecyclerView recyclerView = findViewById(R.id.recycler_admin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        loadData(vendorName);
    }


    void loadData(String vendorName) {
        CollectionReference foodItemsCollection = db.collection("foodItems");

        // Query documents by StoreName
        Query query = foodItemsCollection.whereEqualTo("StoreName", vendorName);

        // Fetch data
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    // Access fields from the document
                    String name = document.getString("Name");
                    String storeName = document.getString("StoreName");
                    List<String> blockName = (List<String>) document.get("BlockName");
                    String variant = document.getString("Variant");
                    long availableQuantity = document.getLong("AvailableQuantity");
                    double price = document.getDouble("Price");

                    AdminItem foodItem = new AdminItem(document.getId(), name, blockName, variant, availableQuantity, price);

                    // Add FoodItem to the map with document ID as key
                    items.add(foodItem);
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    @Override
    public void deleteItem(AdminItem foodItem) {
        Log.d(TAG, "deleteItem: " + foodItem.getName());
        db.collection("foodItems").document(foodItem.getId()).delete().addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!")).addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    private void updateItem(AdminItem foodItem, long availableQuantity, double price) {
        Log.d("AdminMainActivity", foodItem.getId());
        db.collection("foodItems").document(foodItem.getId()).update("AvailableQuantity", availableQuantity, "Price", price).addOnSuccessListener(aVoid -> {
            // Item updated successfully
            Toast.makeText(AdminMainActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // Error updating item
            Log.e(TAG, "Error updating item", e);
            Toast.makeText(AdminMainActivity.this, "Failed to update item", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void showEditDialog(AdminItem foodItem, onComplete complete) {
        // Create a dialog builder
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_food_item, null);
        dialogBuilder.setView(dialogView);

        // Get references to EditText fields
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);

        // Set initial values in EditText fields
        editTextQuantity.setText(String.valueOf(foodItem.getAvailableQuantity()));
        editTextPrice.setText(String.valueOf(foodItem.getPrice()));

        // Set dialog buttons
        dialogBuilder.setPositiveButton("Save", (dialog, which) -> {
            // Get new values from EditText fields
            String newQuantityStr = editTextQuantity.getText().toString();
            String newPriceStr = editTextPrice.getText().toString();

            long newQuantity = 0;
            double newPrice = 0;
            try {
                // Convert EditText values to appropriate types
                newQuantity = Long.parseLong(newQuantityStr);
                newPrice = Double.parseDouble(newPriceStr);

                foodItem.setAvailableQuantity(newQuantity);
                foodItem.setPrice(newPrice);

                Log.d("AdminMainActivity", foodItem.toString());

                // Update the FoodItem object with new values
                updateItem(foodItem, foodItem.getAvailableQuantity(), foodItem.getPrice());

                // Perform your update operation here, for example, update Firestore
                // UpdateFirestore(foodItem);
                complete.callOnComplete();
                // Notify user that item has been updated
                Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException ignored) {

            }

        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public interface onComplete {
        public void callOnComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "onActivityResult: " + selectedImageUri);
            // You can now use this selectedImageUri to display the image or perform any other operations
            // For example, you can set the image to an ImageView
            addItemUri = selectedImageUri;
            addItemImage.setImageURI(selectedImageUri);
        }
    }

    public boolean validateData(String quantityStr, String blockName, String name, String priceStr, String variant) {
        quantityStr = quantityStr.trim();
        blockName = blockName.trim();
        name = name.trim();
        priceStr = priceStr.trim();
        variant = variant.trim();

        if (quantityStr.isEmpty() || blockName.isEmpty() || name.isEmpty() || priceStr.isEmpty() || variant.isEmpty()) {
            // Display an error message or handle empty fields as needed
            return false;
        }

        try {
            long quantity = Long.parseLong(quantityStr);
            if (quantity <= 0) {
                // Quantity must be a positive number
                return false;
            }
        } catch (NumberFormatException e) {
            // Quantity is not a valid number
            return false;
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price <= 0) {
                // Price must be a positive number
                return false;
            }
        } catch (NumberFormatException e) {
            // Price is not a valid number
            return false;
        }

        // All data is valid
        return true;
    }


    private void showAddItemDialog() {
        // Create a dialog builder
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input, null);
        dialogBuilder.setView(dialogView);

        // Get references to EditText fields
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        EditText editTextBlockName = dialogView.findViewById(R.id.editTextBlockName);
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        EditText editTextVariant = dialogView.findViewById(R.id.editTextVariant);
        addItemImage = dialogView.findViewById(R.id.imagepicker);
        addItemImage.setOnClickListener(listener -> {
            Intent intent = new Intent();
            intent.setType("image/png");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        // Set dialog buttons
        dialogBuilder.setPositiveButton("Add", (dialog, which) -> {

            boolean result = validateData(editTextQuantity.getText().toString(), editTextBlockName.getText().toString(), editTextName.getText().toString(), editTextPrice.getText().toString(), editTextVariant.getText().toString());

            if (!result) {
                Toast.makeText(AdminMainActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get input values from EditText fields
            long quantity = Long.parseLong(editTextQuantity.getText().toString());
            String blockName = editTextBlockName.getText().toString();
            String name = editTextName.getText().toString();
            double price = Double.parseDouble(editTextPrice.getText().toString());
            String variant = editTextVariant.getText().toString();


            // Perform your desired operation here with the input values
            // For example, you can create a FoodItem object and pass it to a function to save in Firestore
            AdminItem foodItem = new AdminItem();
            foodItem.setAvailableQuantity(quantity);
            foodItem.setBlockName(Arrays.asList(blockName.split(",")));
            foodItem.setName(name);
            foodItem.setPrice(price);
            foodItem.setVariant(variant);

            // Call a function to save the FoodItem in Firestore
            saveFoodItemToFirestore(foodItem);
            Log.d(TAG, "showAddItemDialog: Item added to firestore, now uploading image");
            if (addItemUri != null)
                uploadImageToFirestore(addItemUri, name);

            // Notify user that item has been saved
            Toast.makeText(AdminMainActivity.this, "Item saved successfully", Toast.LENGTH_SHORT).show();
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public static File convertJpgToPng(Context context, Uri jpgFile, String filename) {
        try {
            // Decode the JPEG file to a Bitmap
            Bitmap jpgBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(jpgFile));

            // Create a PNG file
            File pngFile = createPngFile(context, filename);

            // Convert the JPEG Bitmap to a PNG Bitmap
            Bitmap pngBitmap = Bitmap.createBitmap(jpgBitmap.getWidth(), jpgBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(pngBitmap);
            canvas.drawBitmap(jpgBitmap, 0, 0, null);

            // Save the PNG Bitmap to the PNG file
            FileOutputStream outputStream = new FileOutputStream(pngFile);
            pngBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            Log.d(TAG, "convertJpgToPng: Successfully converted to png");
            return pngFile;
        } catch (IOException e) {
            Log.e(TAG, "Error converting JPEG to PNG", e);
            return null;
        }
    }

    public static File createPngFile(Context context, String PNG_FILE_NAME) throws IOException {
        // Get the directory for the app's private files
        File directory = context.getFilesDir();

        // Create a new file in the private directory
        File pngFile = new File(directory, PNG_FILE_NAME);
        if (!pngFile.createNewFile()) {
            throw new IOException("File already exists");
        }
        return pngFile;
    }


    private void uploadImageToFirestore(Uri selectedImageUri, String name) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Todo: extract imagename from uri
//        Log.d(TAG, "uploadImageToFirestore: " + selectedImageUri.getLastPathSegment());
        // TODO: if jpg, convert to pnf
        File image = convertJpgToPng(getApplicationContext(), selectedImageUri, name.replace(" ", "_") + ".png");
        if (selectedImageUri.getLastPathSegment() == null) {
            Log.d(TAG, "uploadImageToFirestore: Uri null, returning");
            return;
        }
        // Create a reference to the location where the image will be saved in Firebase Storage
        StorageReference imagesRef = storageRef.child("fooditems/" + name.toLowerCase() + ".png");

        Log.d(TAG, "uploadImageToFirestore, exists: " + image.exists() + ", Name: " + image.getName() + ", path: " + image.getAbsolutePath() + ", size: " + image.getTotalSpace());
        try {
            Log.d(TAG, "uploadImageToFirestore: l1");
            // Open the input stream for the file
            FileInputStream stream = new FileInputStream(image);

            Log.d(TAG, "uploadImageToFirestore: l2");
            // Upload the file to Firebase Storage
            UploadTask uploadTask = imagesRef.putStream(stream);

            Log.d(TAG, "uploadImageToFirestore: l3");
            // Monitor the upload task
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // File uploaded successfully
                Log.d(TAG, "File uploaded successfully");
            }).addOnFailureListener(err -> {
                Log.e(TAG, "uploadImageToFirestore: " + err.getMessage());
            });

        } catch (FileNotFoundException e) {
            // File not found
            Log.e(TAG, "File not found: " + e.getMessage());
        }


//        // Upload the image to Firebase Storage
//        UploadTask uploadTask = imagesRef.putFile(selectedImageUri);
//
//        uploadTask.addOnFailureListener(exception -> {
//            // Handle unsuccessful uploads
//            Toast.makeText(AdminMainActivity.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//        }).addOnSuccessListener(taskSnapshot -> {
//            // Handle successful uploads
//            Toast.makeText(AdminMainActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
//        });


    }

    private void saveFoodItemToFirestore(AdminItem foodItemData) {
        Map<String, Object> foodItem = new HashMap<>();
        foodItem.put("AvailableQuantity", foodItemData.getAvailableQuantity());
        foodItem.put("BlockName", foodItemData.getBlockName());
        foodItem.put("Name", foodItemData.getName());
        foodItem.put("Price", foodItemData.getPrice());
        foodItem.put("StoreName", vendorName);
        foodItem.put("Variant", foodItemData.getVariant());

        db.collection("foodItems").add(foodItem).addOnSuccessListener(documentReference -> {
            // Item added successfully
            Toast.makeText(AdminMainActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
            foodItemData.setId(documentReference.getId());
            items.add(foodItemData);
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            // Error adding item
            Log.e(TAG, "Error adding item", e);
            Toast.makeText(AdminMainActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
        });


    }


}