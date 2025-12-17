package rs.ac.singidunum.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import rs.ac.singidunum.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etUsername, etPassword, etAge, etBiography;
    private Spinner  spGender, spBasketballLevel, spFootballLevel, spVolleyballLevel, spPadelLevel;
    private Button   btnRegister, btnBack;

    private FirebaseAuth      auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1) Wire up views
        etEmail            = findViewById(R.id.etEmail);
        etUsername         = findViewById(R.id.etUsername);
        etPassword         = findViewById(R.id.etPassword);
        etAge              = findViewById(R.id.etAge);
        etBiography        = findViewById(R.id.etBiography);

        spGender           = findViewById(R.id.spGender);
        spBasketballLevel  = findViewById(R.id.spBasketballLevel);
        spFootballLevel    = findViewById(R.id.spFootballLevel);
        spVolleyballLevel  = findViewById(R.id.spVolleyballLevel);
        spPadelLevel       = findViewById(R.id.spPadelLevel);

        btnRegister        = findViewById(R.id.btnRegister);
        btnBack            = findViewById(R.id.btnBackToLogin);

        // 2) Firebase init
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        // 3) Populate spinners
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(
                this, R.array.level_array, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBasketballLevel.setAdapter(levelAdapter);
        spFootballLevel.setAdapter(levelAdapter);
        spVolleyballLevel.setAdapter(levelAdapter);
        spPadelLevel.setAdapter(levelAdapter);

        // 4) Button listeners
        btnRegister.setOnClickListener(v -> {
            String email    = etEmail.getText().toString().trim();
            String userName = etUsername.getText().toString().trim();
            String pwd      = etPassword.getText().toString().trim();
            String ageStr   = etAge.getText().toString().trim();
            String bio      = etBiography.getText().toString().trim();

            if (email.isEmpty() || userName.isEmpty() || pwd.length() < 6 || ageStr.isEmpty()) {
                Toast.makeText(this,
                        "Enter valid email, username, 6+-char pwd & age",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int age = Integer.parseInt(ageStr);
            String gender = spGender.getSelectedItem().toString();
            int bLvl = Integer.parseInt(spBasketballLevel.getSelectedItem().toString());
            int fLvl = Integer.parseInt(spFootballLevel.getSelectedItem().toString());
            int vLvl = Integer.parseInt(spVolleyballLevel.getSelectedItem().toString());
            int pLvl = Integer.parseInt(spPadelLevel.getSelectedItem().toString());

            // 5) Create Auth user
            auth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(this,
                                    "Register failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        // 6) Update displayName
                        FirebaseUser fu = auth.getCurrentUser();
                        if (fu != null) {
                            fu.updateProfile(new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build());
                        }

                        // 7) Write full profile
                        Map<String,Object> profile = new HashMap<>();
                        profile.put("username",        userName);
                        profile.put("email",           email);
                        profile.put("age",             age);
                        profile.put("gender",          gender);
                        profile.put("biography",       bio);
                        profile.put("basketballLevel", bLvl);
                        profile.put("footballLevel",   fLvl);
                        profile.put("volleyballLevel", vLvl);
                        profile.put("padelLevel",      pLvl);

                        db.collection("users")
                                .document(fu.getUid())
                                .set(profile)
                                .addOnSuccessListener(__ -> {
                                    // 8) Sign out immediately so they must log in
                                    auth.signOut();

                                    // 9) Notify and return to login
                                    Toast.makeText(this,
                                            "Registration successful! Please log in.",
                                            Toast.LENGTH_LONG).show();
                                    finish();  // this returns to your LoginActivity
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this,
                                            "Couldn’t save profile: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                });
                    });

        });

        btnBack.setOnClickListener(v -> finish());
    }
}
