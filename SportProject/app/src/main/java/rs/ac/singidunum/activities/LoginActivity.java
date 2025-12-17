package rs.ac.singidunum.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rs.ac.singidunum.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_login);

        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);
        btnRegister= findViewById(R.id.btnRegister);
        auth       = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pwd   = etPassword.getText().toString().trim();
            if (email.isEmpty() || pwd.length()<6) {
                Toast.makeText(this, "Enter email & 6+ char pwd", Toast.LENGTH_SHORT).show();
                return;
            }
            auth.signInWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener(this, task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(this,
                                    "Login failed: "+task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        FirebaseUser fu = auth.getCurrentUser();
                        if (fu != null) {
                            String uid  = fu.getUid();
                            String name = fu.getDisplayName();
                            // (Note: if you never set displayName on registration, you could
                            //  alternatively fetch your “users/{uid}” doc from Firestore and read
                            //  the “username” field.)

                            // ✅ 2) persist UID + username into SharedPreferences
                            SharedPreferences prefs =
                                    getSharedPreferences("UserSession", MODE_PRIVATE);
                            prefs.edit()
                                    .putString("uid",      uid)
                                    .putString("username", name != null ? name : "")
                                    .apply();
                        }
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    });
        });

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}

