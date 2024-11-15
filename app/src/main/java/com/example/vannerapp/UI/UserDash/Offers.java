package com.example.vannerapp.UI.UserDash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Offers extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_offers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.offers), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String descripcion = intent.getStringExtra("descripcion");
        String sueldo = intent.getStringExtra("sueldo");
        String empresa = intent.getStringExtra("empresa");

        TextView txtTitulo = findViewById(R.id.txt_tituloOferta);
        TextView nombreOferta = findViewById(R.id.txt_nombreOferta);
        TextView sueldoOferta = findViewById(R.id.txt_sueldoOferta);
        Button btnAceptar = findViewById(R.id.btn_aceptarOferta);
        Button btnRechazar = findViewById(R.id.btn_rechazarOferta);
        Button btnVolver = findViewById(R.id.button3);

        txtTitulo.setText(descripcion);
        sueldoOferta.setText("Sueldo: " + sueldo);
        nombreOferta.setText("Empresa: " + empresa);

        btnVolver.setOnClickListener(v -> finish());

        validateAnuncio(descripcion, empresa);
    }
    private void validateAnuncio(String descripcion, String empresa) {
        db.collection("anuncios")
                .whereEqualTo("descripcion", descripcion)
                .whereEqualTo("empresa", empresa)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Double experiencia = document.getDouble("experiencia");
                            TextView experienciaOferta = findViewById(R.id.txt_experienciaOferta);
                            experienciaOferta.setText("Años de experiencia: " + String.format("%d", experiencia.intValue()));

                            String direccion = document.getString("direccion");
                            TextView direccionOferta = findViewById(R.id.txt_direccionOferta);
                            direccionOferta.setText("Dirección: " + direccion);

                            String horarios = document.getString("horarios");
                            TextView horariosOferta = findViewById(R.id.txt_horariosOferta);
                            horariosOferta.setText("Horarios: " + horarios);

                            String tiempo = document.getString("tiempo");
                            TextView tiempoOferta = findViewById(R.id.txt_tiempoOferta);
                            tiempoOferta.setText("Jornada: " + tiempo);

                            String vacantes = document.getString("vacantes");
                            TextView vacantesOferta = findViewById(R.id.txt_vacantesOferta);
                            vacantesOferta.setText("Vacantes: " + vacantes);

                            String detalles = document.getString("detalles");
                            TextView detallesOferta = findViewById(R.id.txt_detallesOferta);
                            detallesOferta.setText(detalles);
                        }
                    } else {
                        System.out.println("No se encontró un anuncio con estos datos.");
                    }
                });
    }
}