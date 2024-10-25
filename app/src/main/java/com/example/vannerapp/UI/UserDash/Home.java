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

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userhome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.correo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView vannerCl = findViewById(R.id.vanner_cl);
        Button btnLogout = findViewById(R.id.btn_logout);

        Intent intent = getIntent();
        String email = intent.getStringExtra("USER_EMAIL");

        if (email != null) {
            vannerCl.setText("Bienvenido, " + email);
        } else {
            Toast.makeText(this, "Ocurrió un error, intentelo más tarde :(", Toast.LENGTH_SHORT).show();
            Intent intents = new Intent(Home.this, MainActivity.class);
            startActivity(intents);

        }

        btnLogout.setOnClickListener(v -> {
            Intent intents = new Intent(Home.this, MainActivity.class);
            startActivity(intents);
        });

    }
}
