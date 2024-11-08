package com.example.vannerapp.UI.UserDash;

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
import com.example.vannerapp.UI.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userhome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn_offers_ico), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView vannerCl = findViewById(R.id.vanner_cl);
        Button btnLogout = findViewById(R.id.btn_logout);
        Button btnClientes = findViewById(R.id.btn_clientes);
        Button btnEntrenadores = findViewById(R.id.btn_entrenadores);
        Button btnPerfil = findViewById(R.id.btn_perfil);

        Intent intent = getIntent();
        String email = intent.getStringExtra("USER_EMAIL");

        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()){
                System.out.println("El Perfil ya tiene un mail asignado");
                System.out.println(email);
                vannerCl.setText("Bienvenido, " + email);
            }else{
                System.out.println("El Perfil no tiene un mail asignado");
                Intent intentMod = new Intent(Home.this, ModPerfil.class);
                intentMod.putExtra("email", email);
                startActivity(intentMod);
                finish();
            }
        });

        btnLogout.setOnClickListener(v -> {
            Intent intents = new Intent(Home.this, MainActivity.class);
            startActivity(intents);
            finish();
        });

        btnClientes.setOnClickListener(v -> {

        });

        btnPerfil.setOnClickListener(v -> {
            Intent intents = new Intent(Home.this, Perfil.class);
            intents.putExtra("AuthMail", email);
            startActivity(intents);

        });

        btnEntrenadores.setOnClickListener(v -> {
            Intent intents = new Intent(Home.this, Offers.class);
            startActivity(intents);
        });

    }
}
