package com.example.vannerapp.UI.UserDash.Create;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.example.vannerapp.UI.MainActivity;
import com.example.vannerapp.UI.UserDash.Home;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ModPerfilFirst extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modperfilfirst);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.modperfilfirst), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

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
        //ingrese imagen aqui xd

        buttonActua.setOnClickListener(v -> {
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", email);

            userData.put("nombre", nombre.getText().toString());
            userData.put("apellido", apellido.getText().toString());

            userData.put("direccion", direccion.getText().toString());
            userData.put("telefono", telefono.getText().toString());
            userData.put("egreso", egreso.getText().toString());
            userData.put("titulo", titulo.getText().toString());

            double experienciaValue = Double.parseDouble(experiencia.getText().toString());
            userData.put("experiencia", experienciaValue);

            userData.put("bio", bio.getText().toString());

            userData.put("rut", rut.getText().toString());

            db.collection("users")
                    .add(userData)
                    .addOnSuccessListener(documentReference -> {
                        Intent intentHome = new Intent(ModPerfilFirst.this, MainActivity.class);
                        startActivity(intentHome);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.w("FirestoreError", "Error adding document", e);
                    });
        });

        buttonSalir.setOnClickListener(v -> {
            Intent intentExit = new Intent(ModPerfilFirst.this, Home.class);
            intentExit.putExtra("USER_EMAIL", email);
            startActivity(intentExit);
            finish();
        });





    }


}