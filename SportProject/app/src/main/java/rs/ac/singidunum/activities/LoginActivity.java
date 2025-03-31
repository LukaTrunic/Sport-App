package rs.ac.singidunum.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import rs.ac.singidunum.R;
import rs.ac.singidunum.models.User;
import rs.ac.singidunum.data.UserDao;

public class LoginActivity extends AppCompatActivity {

    // Declare UI elements and the UserDao for database operations
    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Initialize UserDao for database operations
        userDao = new UserDao(this);

        // Set onClick listener for the login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // Retrieve the user from the database
                User user = userDao.getUser(username);

                // Validate user credentials
                if (user != null && user.getPassword().equals(password)) {
                    // Save user session details in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username", user.getUsername());
                    editor.putInt("age", user.getAge());
                    editor.putString("gender", user.getGender());
                    editor.putString("biography", user.getBiography());
                    editor.putInt("basketballLevel", user.getBasketballLevel());
                    editor.putInt("footballLevel", user.getFootballLevel());
                    editor.putInt("volleyballLevel", user.getVolleyballLevel());
                    editor.putInt("padelLevel", user.getPadelLevel());
                    editor.apply();

                    // Navigate to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Show error message if credentials are invalid
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set onClick listener for the register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
