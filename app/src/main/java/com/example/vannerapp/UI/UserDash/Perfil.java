package com.example.vannerapp.UI.UserDash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.BreakIterator;

public class Perfil extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.preview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String AuthEmail = intent.getStringExtra("AuthMail");

        System.out.println(AuthEmail);

        retrieveUserData(AuthEmail);

    }

    private void retrieveUserData(String AuthEmail) {
        TextView nombre_trabajador = findViewById(R.id.nombre_trabajador);
        db.collection("users").whereEqualTo("email", AuthEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userId = document.getId();
                        String name = document.getString("nombre");
                        String lName = document.getString("apellido");

                        nombre_trabajador.setText(name + " " + lName);

                        Log.d("Firestore", "User ID: " + userId + ", Name: " + name + ", Last Name: " + lName);
                    }
                } else {
                    Log.d("Firestore", "No user found with that email.");
                }
            } else {
                Log.e("Firestore", "Error retrieving documents: ", task.getException());
            }
        });

    }
}