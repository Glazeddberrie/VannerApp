package com.example.vannerapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vannerapp.R;
import com.example.vannerapp.UI.UserDash.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
            String emailText = email.getText().toString().trim();
            String passwordText = pass.getText().toString().trim();

            if (!emailText.isEmpty() && !passwordText.isEmpty()) {
                signIn(emailText, passwordText);
            } else {
                Toast.makeText(MainActivity.this, "Complete los campos correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        btnReg.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });
    }

    public void signIn(String email, String password) {
        EditText log_email = findViewById(R.id.txt_log_email);
        EditText log_pass = findViewById(R.id.txt_log_pass);
        TextView error = findViewById(R.id.text_errorfield);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String userEmail = user.getEmail();
                            Log.d("Auth", "Usuario autenticado: " + userEmail);

                            checkIfBanned(userEmail);

                        }
                    } else {
                        Log.e("Auth", "Error en la autenticación :(");
                        Toast.makeText(MainActivity.this, "Error en la autenticación, ingrese sus datos correctamente", Toast.LENGTH_SHORT).show();
                        error.setText("Error en la autenticación, ingrese sus datos correctamente");
                    }
                });
    }

    private void checkIfBanned(String email) {
        db.collection("users").whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isBanned = false;

                        for (DocumentSnapshot document : task.getResult()) {
                            isBanned = document.getBoolean("baneado") != null && document.getBoolean("baneado");
                        }

                        if (isBanned) {
                            Toast.makeText(MainActivity.this, "Este usuario está baneado", Toast.LENGTH_SHORT).show();
                            auth.signOut();
                        } else {
                            checkIfCompanyBanned(email);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error al verificar el estado del usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfCompanyBanned(String email) {
        db.collection("companies").whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isCompanyBanned = false;

                        for (DocumentSnapshot document : task.getResult()) {
                            isCompanyBanned = document.getBoolean("baneado") != null && document.getBoolean("baneado");
                        }

                        if (isCompanyBanned) {
                            Toast.makeText(MainActivity.this, "Esta empresa está baneada", Toast.LENGTH_SHORT).show();
                            auth.signOut();
                        } else {
                            Toast.makeText(MainActivity.this, "Usuario correcto, Bienvenido a Vanner!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            intent.putExtra("USER_EMAIL", email);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error al verificar el estado de la empresa", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}