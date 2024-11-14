package com.example.vannerapp.UI.AdminDash.Delete;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Delete extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView txtBanear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adminborrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminban), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtBanear = findViewById(R.id.txt_banear);
        Button btnBanear = findViewById(R.id.btn_banear);

        btnBanear.setOnClickListener(v -> {
            String email = txtBanear.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(Delete.this, "Por favor ingresa un correo", Toast.LENGTH_SHORT).show();
                return;
            }

            banearUsuario(email);
        });
    }

    private void banearUsuario(String email) {
        db.collection("empresas").whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean encontrado = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("empresas").document(document.getId())
                                    .update("baneado", true)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(Delete.this, "Usuario baneado exitosamente", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(Delete.this, "Error al banear al usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                            encontrado = true;
                            break;
                        }

                        if (!encontrado) {
                            db.collection("users").whereEqualTo("email", email)
                                    .get()
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task2.getResult()) {
                                                db.collection("users").document(document.getId())
                                                        .update("baneado", true)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(Delete.this, "Usuario baneado exitosamente", Toast.LENGTH_SHORT).show();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(Delete.this, "Error al banear al usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        } else {
                                            Toast.makeText(Delete.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(Delete.this, "Error al recuperar los usuarios: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}