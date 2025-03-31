package rs.ac.singidunum.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import rs.ac.singidunum.R;

public class ProfileActivity extends AppCompatActivity {

    // Declare TextViews and Button for profile information and logout functionality
    private TextView tvUsername, tvAge, tvBiography, tvBasketballLevel, tvFootballLevel, tvVolleyballLevel, tvPadelLevel;
    private Button btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize TextViews and Button
        tvUsername = findViewById(R.id.tvUsername);
        tvAge = findViewById(R.id.tvAge);
        tvBiography = findViewById(R.id.tvBiography);
        tvBasketballLevel = findViewById(R.id.tvBasketballLevel);
        tvFootballLevel = findViewById(R.id.tvFootballLevel);
        tvVolleyballLevel = findViewById(R.id.tvVolleyballLevel);
        tvPadelLevel = findViewById(R.id.tvPadelLevel);
        btnLogout = findViewById(R.id.btnLogout);

        // Retrieve user information from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = prefs.getString("username", "");
        int age = prefs.getInt("age", 0);
        String biography = prefs.getString("biography", "");
        int basketballLevel = prefs.getInt("basketballLevel", 0);
        int footballLevel = prefs.getInt("footballLevel", 0);
        int volleyballLevel = prefs.getInt("volleyballLevel", 0);
        int padelLevel = prefs.getInt("padelLevel", 0);

        // Set the retrieved user information to the respective TextViews
        tvUsername.setText(username);
        tvAge.setText(String.format("(%d)", age));
        tvBiography.setText(biography);
        tvBasketballLevel.setText(String.format("Basketball: Level %d", basketballLevel));
        tvFootballLevel.setText(String.format("Football: Level %d", footballLevel));
        tvVolleyballLevel.setText(String.format("Volleyball: Level %d", volleyballLevel));
        tvPadelLevel.setText(String.format("Padel: Level %d", padelLevel));

        // Set the logout button's onClick listener
        btnLogout.setOnClickListener(view -> {
            // Clear the SharedPreferences to log out the user
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            // Navigate back to the LoginActivity
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the back button press in the toolbar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
