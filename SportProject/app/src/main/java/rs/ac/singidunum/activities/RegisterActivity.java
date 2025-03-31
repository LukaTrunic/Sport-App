package rs.ac.singidunum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import rs.ac.singidunum.R;
import rs.ac.singidunum.models.User;
import rs.ac.singidunum.data.UserDao;

public class RegisterActivity extends AppCompatActivity {

    // Declare EditTexts, Spinners, and Buttons for user input and actions
    private EditText etUsername, etPassword, etAge, etBiography;
    private Spinner spGender, spBasketballLevel, spFootballLevel, spVolleyballLevel, spPadelLevel;
    private Button btnRegister, btnBackToLogin;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize the views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etAge = findViewById(R.id.etAge);
        etBiography = findViewById(R.id.etBiography);
        spGender = findViewById(R.id.spGender);
        spBasketballLevel = findViewById(R.id.spBasketballLevel);
        spFootballLevel = findViewById(R.id.spFootballLevel);
        spVolleyballLevel = findViewById(R.id.spVolleyballLevel);
        spPadelLevel = findViewById(R.id.spPadelLevel);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        // Set up the gender spinner with data from resources
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);

        // Set up the level spinners with data from resources
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this,
                R.array.level_array, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spBasketballLevel.setAdapter(levelAdapter);
        spFootballLevel.setAdapter(levelAdapter);
        spVolleyballLevel.setAdapter(levelAdapter);
        spPadelLevel.setAdapter(levelAdapter);

        // Initialize UserDao for database operations
        userDao = new UserDao(this);

        // Set up the register button click listener
        btnRegister.setOnClickListener(view -> {
            // Get user input from EditTexts and Spinners
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String biography = etBiography.getText().toString().trim();

            // Check if any input field is empty
            if (username.isEmpty() || password.isEmpty() || ageStr.isEmpty() || biography.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate and parse age input
            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(RegisterActivity.this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected gender and levels from Spinners
            String gender = spGender.getSelectedItem().toString();
            int basketballLevel = Integer.parseInt(spBasketballLevel.getSelectedItem().toString());
            int footballLevel = Integer.parseInt(spFootballLevel.getSelectedItem().toString());
            int volleyballLevel = Integer.parseInt(spVolleyballLevel.getSelectedItem().toString());
            int padelLevel = Integer.parseInt(spPadelLevel.getSelectedItem().toString());

            // Check if the username is already taken
            if (userDao.isUsernameTaken(username)) {
                Toast.makeText(RegisterActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new User object with the input data
            User user = new User(username, password, age, gender, biography, basketballLevel, footballLevel, volleyballLevel, padelLevel);

            // Add the new user to the database
            userDao.addUser(user);

            // Show a success message
            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to the LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the back to login button click listener
        btnBackToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
