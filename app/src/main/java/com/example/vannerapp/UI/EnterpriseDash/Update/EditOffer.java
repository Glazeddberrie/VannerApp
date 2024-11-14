package com.example.vannerapp.UI.EnterpriseDash.Update;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EditOffer extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listViewOffers;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrar_anuncio);  // Asegúrate de que el layout sea el correcto
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.administrateoffers), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listViewOffers = findViewById(R.id.listViewOffers);
        offerList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, offerList);
        listViewOffers.setAdapter(adapter);

        Intent intent = getIntent();
        String email = intent.getStringExtra("AuthMail");
        String nombreEmpresa = intent.getStringExtra("nombreEmpresa");

        loadOffers(email, nombreEmpresa);
    }

    private void loadOffers(String email, String nombreEmpresa) {
        db.collection("anuncios")
                .whereEqualTo("email", email)
                .whereEqualTo("empresa", nombreEmpresa)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        offerList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String descripcion = document.getString("descripcion");
                            String sueldo = document.getString("sueldo");
                            String empresa = document.getString("empresa");

                            // Formato personalizado
                            String offerDetails = descripcion + "\nSueldo: " + sueldo + "\nEmpresa: " + empresa;
                            offerList.add(offerDetails);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar anuncios", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}