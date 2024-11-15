package com.example.vannerapp.UI.EnterpriseDash.Create;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class CreateOffer extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crearanuncio_sii);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.crearanuncio), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String email = intent.getStringExtra("AuthMail");
        String nombreEmpresa = intent.getStringExtra("nombreEmpresa");

        Button btn_crear = findViewById(R.id.btn_crear_anuncio);
        Button btn_volver = findViewById(R.id.btn_volver_anuncio);

        TextView text_descripcion = findViewById(R.id.text_descripcion_anuncio);
        TextView text_direccion = findViewById(R.id.text_direccion_anuncio);
        TextView text_email = findViewById(R.id.text_mail_anuncio);
        TextView text_empresa = findViewById(R.id.text_empresa_anuncio);
        TextView text_horario = findViewById(R.id.text_horario_anuncio);
        TextView text_experiencia = findViewById(R.id.text_experiencia_anuncio);
        TextView text_numero = findViewById(R.id.text_numero_anuncio);
        TextView text_sueldo = findViewById(R.id.text_sueldo_anuncio);
        TextView text_tiempo = findViewById(R.id.text_tiempo_anuncio);
        TextView text_vacantes = findViewById(R.id.text_vacantes_anuncio);
        TextView text_detalles = findViewById(R.id.text_detalles_anuncio);

        text_email.setText(email);
        text_empresa.setText(nombreEmpresa);

        btn_crear.setOnClickListener(v -> {
            String descripcion = text_descripcion.getText().toString();
            String direccion = text_direccion.getText().toString();
            String emailAnuncio = text_email.getText().toString();
            String empresa = text_empresa.getText().toString();
            String horarios = text_horario.getText().toString();
            String experienciaStr = text_experiencia.getText().toString();
            String numero = text_numero.getText().toString();
            String sueldo = text_sueldo.getText().toString();
            String tiempo = text_tiempo.getText().toString();
            String vacantes = text_vacantes.getText().toString();
            String detalles = text_detalles.getText().toString();

            if (descripcion.isEmpty() || direccion.isEmpty() || emailAnuncio.isEmpty() ||
                    empresa.isEmpty() || horarios.isEmpty() || experienciaStr.isEmpty() ||
                    numero.isEmpty() || sueldo.isEmpty() || tiempo.isEmpty() || vacantes.isEmpty() || detalles.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double experiencia;
            try {
                experiencia = Double.parseDouble(experienciaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La experiencia debe ser un número válido", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> anuncioData = new HashMap<>();
            anuncioData.put("descripcion", descripcion);
            anuncioData.put("direccion", direccion);
            anuncioData.put("email", emailAnuncio);
            anuncioData.put("empresa", empresa);
            anuncioData.put("horarios", horarios);
            anuncioData.put("experiencia", experiencia);
            anuncioData.put("numero", numero);
            anuncioData.put("sueldo", sueldo);
            anuncioData.put("tiempo", tiempo);
            anuncioData.put("vacantes", vacantes);
            anuncioData.put("detalles", detalles);

            db.collection("anuncios").add(anuncioData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Anuncio creado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al crear el anuncio: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btn_volver.setOnClickListener(v -> finish());
    }
}