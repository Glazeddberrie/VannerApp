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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterAdmin extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_admin), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button volverBtn = findViewById(R.id.btn_volverAdmin);
        Button registrarBtn = findViewById(R.id.btn_registrarAdmin);
        EditText txtEmail = findViewById(R.id.txt_emailAdmin);
        EditText txtPassword = findViewById(R.id.txt_contraAdmin);
        EditText txtPassConfirm = findViewById(R.id.txt_checkContraAdmin);
        EditText txtNombre = findViewById(R.id.txt_nombreAdmin);
        EditText txtRut = findViewById(R.id.txt_rutAdmin);
        EditText txtApellido = findViewById(R.id.txt_apellidoAdmin);
        registrarBtn.setOnClickListener(v -> {

            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String passConf = txtPassConfirm.getText().toString().trim();
            String nombre = txtNombre.getText().toString().trim();
            String rut = txtRut.getText().toString().trim();
            String apellido = txtApellido.getText().toString().trim();
            // Validar campos
            if (!email.isEmpty() && !password.isEmpty() && password.equals(passConf)) {
                registerUser(email, password, nombre, apellido, rut);
            } else {
                Toast.makeText(RegisterAdmin.this, "Complete los campos correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        volverBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterAdmin.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser(String email, String password, String nombre, String apellido, String rut) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH", "createUserWithEmail:success");
                            Toast.makeText(RegisterAdmin.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();

                            String userId = mAuth.getCurrentUser().getUid();
                            Map<String, Object> adminData = new HashMap<>();
                            adminData.put("email", email);
                            adminData.put("nombre", nombre);
                            adminData.put("apellido", apellido);
                            adminData.put("rut", rut);

                            db.collection("admin").document(userId)
                                    .set(adminData)
                                    .addOnSuccessListener(aVoid -> Log.d("FIRESTORE", "Datos guardados exitosamente"))
                                    .addOnFailureListener(e -> Log.w("FIRESTORE", "Error al guardar datos", e));

                            Intent intent = new Intent(RegisterAdmin.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w("AUTH", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterAdmin.this, "Este correo ya está en uso.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}