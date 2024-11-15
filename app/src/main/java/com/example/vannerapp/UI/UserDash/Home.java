package com.example.vannerapp.UI.UserDash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.example.vannerapp.UI.AdminDash.AdminHome;
import com.example.vannerapp.UI.EnterpriseDash.EnterpriseHome;
import com.example.vannerapp.UI.UserDash.Create.ModPerfilFirst;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private void checkEmailInCollections(String email) {
        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                onEmailFound("users", email);
            } else {
                db.collection("admin").whereEqualTo("email", email).get().addOnCompleteListener(adminTask -> {
                    if (adminTask.isSuccessful() && !adminTask.getResult().isEmpty()) {
                        onEmailFound("admin", email);
                        Intent intent = new Intent(Home.this, AdminHome.class);
                        startActivity(intent);
                        finish();
                    } else {
                        db.collection("empresas").whereEqualTo("email", email).get().addOnCompleteListener(empresasTask -> {
                            if (empresasTask.isSuccessful() && !empresasTask.getResult().isEmpty()) {
                                onEmailFound("empresas", email);
                                Intent intent = new Intent(Home.this, EnterpriseHome.class);
                                intent.putExtra("AuthMail", email);

                                startActivity(intent);
                                finish();
                            } else {
                                onEmailNotFound(email);
                            }
                        });
                    }
                });
            }
        });
    }

    private void onEmailFound(String collection, String email) {
        System.out.println("El Perfil ya tiene un email asignado en la colección " + collection);

    }

    private void onEmailNotFound(String email) {
        System.out.println("El Perfil no tiene un email asignado en ninguna colección");
        Intent intentMod = new Intent(Home.this, ModPerfilFirst.class);
        intentMod.putExtra("email", email);
        startActivity(intentMod);
        finish();
    }

    private void fetchAnunciosAndDisplay() {
        db.collection("anuncios").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<String> anunciosList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String descripcion = document.getString("descripcion");
                    String sueldo = document.getString("sueldo");
                    String empresa = document.getString("empresa");
                    if (descripcion != null && sueldo != null && empresa != null) {
                        String anuncio = descripcion + "\nSueldo: " + sueldo + "\nEmpresa: " + empresa;
                        anunciosList.add(anuncio);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Home.this, android.R.layout.simple_list_item_1, anunciosList);
                ListView listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    String selectedAnuncio = anunciosList.get(position);

                    String[] anuncioData = selectedAnuncio.split("\n");
                    String descripcion = anuncioData[0];
                    String sueldo = anuncioData[1].replace("Sueldo: ", "");
                    String empresa = anuncioData[2].replace("Empresa: ", "");

                    Intent intent = new Intent(Home.this, Offers.class);

                    intent.putExtra("descripcion", descripcion);
                    intent.putExtra("sueldo", sueldo);
                    intent.putExtra("empresa", empresa);
                    startActivity(intent);
                });
            } else {
                System.out.println("Error obteniendo anuncios: " + task.getException());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userhome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userhome_anchor), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView vannerCl = findViewById(R.id.vanner_cl);
        Button btnPerfil = findViewById(R.id.btn_perfil);

        Button btnVolver = findViewById(R.id.btn_volver);

        Intent intent = getIntent();
        String email = intent.getStringExtra("USER_EMAIL");

        checkEmailInCollections(email);
        vannerCl.setText("Bienvenido, " + email);

        btnVolver.setOnClickListener(v -> finish());

        btnPerfil.setOnClickListener(v -> {
            Intent intents = new Intent(Home.this, Perfil.class);
            intents.putExtra("AuthMail", email);
            startActivity(intents);

        });

        fetchAnunciosAndDisplay();
    }
}