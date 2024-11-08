package com.example.vannerapp.UI.AdminDash.Create;

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
import com.example.vannerapp.UI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button volverBtn = findViewById(R.id.regvolver_btn);
        Button registrarBtn = findViewById(R.id.registrar_btn);
        EditText emailField = findViewById(R.id.mail_txt);
        EditText passwordField = findViewById(R.id.pass_txt);
        EditText passConfirm = findViewById(R.id.pass_confirm_txt);
        CheckBox terms = findViewById(R.id.crear_check);
        registrarBtn.setOnClickListener(v -> {

            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String passConf= passConfirm.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty() && passConf.equals(password) && terms.isChecked()) {
                registerUser(email, password);

            } else {
                Toast.makeText(Register.this, "Complete los campos correctamente", Toast.LENGTH_SHORT).show();
            }

        });

        volverBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
        });

    }
    private void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH", "createUserWithEmail:success");
                            Toast.makeText(Register.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w("AUTH", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Este correo ya está en uso.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}