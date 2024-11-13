package com.example.vannerapp.UI.UserDash.Update;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.example.vannerapp.UI.UserDash.Home;
import com.example.vannerapp.UI.UserDash.Perfil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ModPerfil extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modperfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.modperfil), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String email = intent.getStringExtra("AuthMail");
        System.out.println(email);


        Button buttonSalir = findViewById(R.id.logout_button);
        Button buttonActua = findViewById(R.id.update_button);

        EditText nombre = findViewById(R.id.text_nombre_perfil);
        EditText apellido = findViewById(R.id.text_apellido_perfil);
        EditText direccion = findViewById(R.id.text_direccion_perfil);
        EditText telefono = findViewById(R.id.text_telefono_perfil);
        EditText egreso = findViewById(R.id.text_egreso_perfil);
        EditText titulo = findViewById(R.id.text_titulo_perfil);
        EditText experiencia = findViewById(R.id.text_experiencia_perfil);
        EditText bio = findViewById(R.id.text_bio_perfil);
        EditText rut = findViewById(R.id.text_rut_perfil);

        db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                Map<String, Object> userData = queryDocumentSnapshots.getDocuments().get(0).getData();

                if (userData != null) {
                    nombre.setText((String) userData.get("nombre"));
                    apellido.setText((String) userData.get("apellido"));
                    direccion.setText((String) userData.get("direccion"));
                    telefono.setText((String) userData.get("telefono"));
                    egreso.setText((String) userData.get("egreso"));
                    titulo.setText((String) userData.get("titulo"));
                    experiencia.setText(String.valueOf(userData.get("experiencia")));
                    bio.setText((String) userData.get("bio"));
                    rut.setText((String) userData.get("rut"));
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        });

        //ingrese imagen aqui
        buttonSalir.setOnClickListener(v -> {
            Intent intentExit = new Intent(ModPerfil.this, Home.class);
            intentExit.putExtra("email", email);
            startActivity(intentExit);
            finish();
        });



        buttonActua.setOnClickListener(v -> {
            db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    String docId = queryDocumentSnapshots.getDocuments().get(0).getId();

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("nombre", nombre.getText().toString());
                    updates.put("apellido", apellido.getText().toString());
                    updates.put("direccion", direccion.getText().toString());
                    updates.put("telefono", telefono.getText().toString());
                    updates.put("egreso", egreso.getText().toString());
                    updates.put("titulo", titulo.getText().toString());

                    try {
                        double experienciaValue = Double.parseDouble(experiencia.getText().toString());
                        updates.put("experiencia", experienciaValue);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid experience value", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    updates.put("bio", bio.getText().toString());
                    updates.put("rut", rut.getText().toString());

                    db.collection("users").document(docId).update(updates)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Perfil actualizado con exito!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}