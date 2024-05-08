package com.example.christcanteenonlineorderingsystem.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.christcanteenonlineorderingsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final String TAG = "AdminLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button loginButton = findViewById(R.id.adminLoginButton);
        loginButton.setOnClickListener(listener -> {

            EditText Email = findViewById(R.id.adminUsername);
            EditText Password = findViewById(R.id.adminLoginPassword);

            if ((Email.getText() != null && !Email.getText().toString().equals("")) && (Password.getText() != null && !Password.getText().toString().equals(""))) {
                CollectionReference docRef = db.collection("vendors");
                Query query = docRef.whereEqualTo("vendorEmail", Email.getText().toString());

                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int results = task.getResult().size();
                        if (results > 0) {
                            QuerySnapshot document = task.getResult();
                            DocumentSnapshot documentSnapshot = document.getDocuments().get(0);
                            mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                                    .addOnCompleteListener(this, task2 -> {
                                        if (task2.isSuccessful()) {

                                            SharedPreferences sharedPref = AdminLogin.this.getApplicationContext().getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("vendorName", documentSnapshot.getString("shopName"));
                                            editor.apply();

                                            String vendorName = sharedPref.getString("vendorName", "");
                                            Log.d(TAG, "call from sharedPref: " + vendorName);

                                            Log.d(TAG, "onCreate: " + documentSnapshot.getString("shopName"));
                                            startActivity(new Intent(AdminLogin.this, AdminMainActivity.class));
                                        } else {
                                            Toast.makeText(AdminLogin.this, "Authentication failed, Check Email and Password", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            showAlert("Error", "User Not Found, Check email and try again");
                        }

                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "Email or Password cannot be empty", Toast.LENGTH_LONG).show();

            }
        });


        findViewById(R.id.adminSignupActivity).setOnClickListener(Listener -> {
            startActivity(new Intent(AdminLogin.this, AdminSignup.class));
        });

    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
