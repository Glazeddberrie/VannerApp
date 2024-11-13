package com.example.vannerapp.UI.AdminDash.Create;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.example.vannerapp.UI.AdminDash.AdminHome;
import com.example.vannerapp.UI.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterEnterprise extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crearempresa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.crearempresa), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnCrearEmpresa = findViewById(R.id.btn_crearEmpresa);
        Button btnVolver = findViewById(R.id.btn_volverEmpresa);

        EditText txtEmail = findViewById(R.id.txt_emailEmpresa);
        EditText txtPassword = findViewById(R.id.txt_contraEmpresa);
        EditText txtPasswordCheck = findViewById(R.id.txt_confirmarContraEmpresa);
        EditText txtRut = findViewById(R.id.txt_rutEmpresa);
        EditText txtNombre = findViewById(R.id.txt_nombreEmpresa);
        EditText txtTelefono = findViewById(R.id.txt_telefonoEmpresa);

        btnCrearEmpresa.setOnClickListener(v -> {
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String passwordCheck = txtPasswordCheck.getText().toString().trim();
            String rut = txtRut.getText().toString().trim();
            String nombre = txtNombre.getText().toString().trim();
            String telefono = txtTelefono.getText().toString().trim();

            if (!password.equals(passwordCheck)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty() || password.isEmpty() || rut.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();

                            Map<String, Object> empresaData = new HashMap<>();
                            empresaData.put("email", email);
                            empresaData.put("rut", rut);
                            empresaData.put("nombre", nombre);
                            empresaData.put("telefono", telefono);

                            db.collection("empresas").document(userId)
                                    .set(empresaData)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Empresa registrada correctamente", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                            Intent intent = new Intent(RegisterEnterprise.this, AdminHome.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Error al registrar usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterEnterprise.this, AdminHome.class);
            startActivity(intent);
        });
    }
}
