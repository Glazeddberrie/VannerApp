package com.example.vannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnReg = findViewById(R.id.start_button);
        Button btnLog = findViewById(R.id.login_start);
        EditText email = findViewById(R.id.txt_log_email);
        EditText pass = findViewById(R.id.txt_log_pass);

        btnLog.setOnClickListener(v -> {
            if (!email.getText().toString().trim().isEmpty() || !pass.getText().toString().trim().isEmpty()) {
                signIn(email.getText().toString().trim(), pass.getText().toString().trim());
            }else{
                Toast.makeText(MainActivity.this, "Complete los campos correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        btnReg.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });
    }
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String userEmail = user.getEmail();
                            Log.d("Auth", "Usuario autenticado: " + userEmail);
                            Toast.makeText(MainActivity.this, "Usuario correcto, Bienvenido a Vanner!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            intent.putExtra("USER_EMAIL", email);
                            startActivity(intent);
                        }
                    } else {
                        Log.e("Auth", "Error en la autenticación :(");
                        Toast.makeText(MainActivity.this, "Error en la autenticación, ingrese sus datos correctamente :(", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}