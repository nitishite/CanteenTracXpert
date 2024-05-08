package com.example.christcanteenonlineorderingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        Button loginButton = findViewById(R.id.button5);
        loginButton.setOnClickListener(listener -> {
            EditText Email = findViewById(R.id.username);
            EditText Password = findViewById(R.id.password);
            try {
                mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnFailureListener(failureListener -> {
                    Toast.makeText(getApplicationContext(), failureListener.getMessage(), Toast.LENGTH_LONG);
                }).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IllegalArgumentException e) {
                // auth signup () requires @NonNull
                Toast.makeText(LoginActivity.this, "Missing credentials!", Toast.LENGTH_LONG).show();
            }
        });

    }
}