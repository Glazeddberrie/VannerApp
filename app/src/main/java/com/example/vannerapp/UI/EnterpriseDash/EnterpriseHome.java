package com.example.vannerapp.UI.EnterpriseDash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.example.vannerapp.UI.EnterpriseDash.Create.CreateOffer;
import com.example.vannerapp.UI.EnterpriseDash.Update.EditOffer;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EnterpriseHome extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enterprisehome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.enterprisehome), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String email = intent.getStringExtra("AuthMail");

        Button btnEnterpriseCrear = findViewById(R.id.btn_enterprise_crear);
        Button btnEnterpriseManage = findViewById(R.id.btn_enterprise_manage);
        Button btnEnterpriseVolver = findViewById(R.id.btn_volver);

        btnEnterpriseVolver.setOnClickListener(v -> finish());

        db.collection("empresas").whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String nombreEmpresa = document.getString("nombre");

                        btnEnterpriseCrear.setOnClickListener(v -> {
                            Intent createOfferIntent = new Intent(EnterpriseHome.this, CreateOffer.class);
                            createOfferIntent.putExtra("AuthMail", email);
                            createOfferIntent.putExtra("nombreEmpresa", nombreEmpresa);
                            startActivity(createOfferIntent);
                        });

                        btnEnterpriseManage.setOnClickListener(v -> {
                            Intent manageIntent = new Intent(EnterpriseHome.this, EditOffer.class);
                            manageIntent.putExtra("AuthMail", email);
                            manageIntent.putExtra("nombreEmpresa", nombreEmpresa);
                            startActivity(manageIntent);
                        });

                    } else {
                        Toast.makeText(this, "No se pudo recuperar el nombre de la empresa", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al recuperar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}