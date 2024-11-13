package com.example.vannerapp.UI.UserDash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.example.vannerapp.UI.UserDash.Update.ModPerfil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Perfil extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_preview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_preview), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();

        String AuthMail = intent.getStringExtra("AuthMail");
        Button btn_editPerfil = findViewById(R.id.btn_editPerfil);
        Button btn_volver = findViewById(R.id.btn_volver);

        System.out.println(AuthMail);

        retrieveUserData(AuthMail);

        btn_editPerfil.setOnClickListener(v -> {
            Intent intentEdit = new Intent(Perfil.this, ModPerfil.class);
            intentEdit.putExtra("AuthMail", AuthMail);
            startActivity(intentEdit);
            finish();
        });
    }

    private void retrieveUserData(String AuthEmail) {
        TextView nombre_trabajador = findViewById(R.id.nombre_trabajador);
        TextView text_numero = findViewById(R.id.text_numero);
        TextView text_direccion = findViewById(R.id.text_direccion);
        TextView text_egreso = findViewById(R.id.text_egreso);
        TextView text_titulo = findViewById(R.id.text_titulo);
        TextView text_experiencia = findViewById(R.id.text_experiencia);
        TextView descripcion_trabajador = findViewById(R.id.descripcion_trabajador);

        db.collection("users").whereEqualTo("email", AuthEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userId = document.getId();
                        String name = document.getString("nombre");
                        String lName = document.getString("apellido");

                        nombre_trabajador.setText(name + " " + lName);

                        text_numero.setText("Numero: " + document.getString("telefono"));
                        text_direccion.setText( "Direccion: " + document.getString("direccion"));
                        text_egreso.setText("Egreso de: " + document.getString("egreso"));
                        text_titulo.setText("Titulo: " + document.getString("titulo"));

                        Double experiencia = document.getDouble("experiencia");

                        if (experiencia != null) {
                            text_experiencia.setText("Años de experiencia: " + (int) experiencia.doubleValue());
                        } else {
                            text_experiencia.setText("0");
                        }
                        descripcion_trabajador.setText(document.getString("bio"));

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