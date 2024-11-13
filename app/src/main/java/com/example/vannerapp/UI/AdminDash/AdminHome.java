package com.example.vannerapp.UI.AdminDash;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.example.vannerapp.UI.AdminDash.Create.RegisterAdmin;
import com.example.vannerapp.UI.AdminDash.Create.RegisterEnterprise;
import com.example.vannerapp.UI.UserDash.Home;
import com.example.vannerapp.UI.UserDash.Perfil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHome extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adminhome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminhome), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnCrearEmpresa = findViewById(R.id.btn_crearempresa);
        Button btnCrearAdmin = findViewById(R.id.btn_crearadmin);

        btnCrearAdmin.setOnClickListener(v -> {
            Intent intents = new Intent(AdminHome.this, RegisterAdmin.class);
            startActivity(intents);

        });

        btnCrearEmpresa.setOnClickListener(v -> {
            Intent intents = new Intent(AdminHome.this, RegisterEnterprise.class);
            startActivity(intents);

        });
    }
}
