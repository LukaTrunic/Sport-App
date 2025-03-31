package rs.ac.singidunum.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import rs.ac.singidunum.R;
import rs.ac.singidunum.models.Game;

import java.util.List;

public class GameDetailsActivity extends AppCompatActivity {

    // Declare UI elements
    private TextView tvGameTitle, tvGameDescription, tvGameLocation, tvGameDate, tvStartTime, tvEndTime, tvPlayersNeeded, tvMaxPlayers, tvPrice, tvAgeRange, tvAdditionalInfo, tvJoinedUsers;
    private Button btnJoinGame;

    private String currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize UI elements
        tvGameTitle = findViewById(R.id.tvGameTitle);
        tvGameDescription = findViewById(R.id.tvGameDescription);
        tvGameLocation = findViewById(R.id.tvGameLocation);
        tvGameDate = findViewById(R.id.tvGameDate);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvPlayersNeeded = findViewById(R.id.tvPlayersNeeded);
        tvMaxPlayers = findViewById(R.id.tvMaxPlayers);
        tvPrice = findViewById(R.id.tvPrice);
        tvAgeRange = findViewById(R.id.tvAgeRange);
        tvAdditionalInfo = findViewById(R.id.tvAdditionalInfo);
        tvJoinedUsers = findViewById(R.id.tvJoinedUsers);
        btnJoinGame = findViewById(R.id.btnJoinGame);

        // Retrieve the logged-in user's username from shared preferences
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUser = prefs.getString("username", "Guest");

        // Retrieve the game object passed from the previous activity
        Game game = (Game) getIntent().getSerializableExtra("game");

        // Populate the UI elements with game details if game data is available
        if (game != null) {
            tvGameTitle.setText(game.getTitle());
            tvGameDescription.setText(game.getDescription());
            tvGameLocation.setText(game.getLocation());
            tvGameDate.setText(game.getDate());
            tvStartTime.setText(game.getStartTime());
            tvEndTime.setText(game.getEndTime());
            tvPlayersNeeded.setText(String.valueOf(game.getPlayersNeeded()));
            tvMaxPlayers.setText(String.valueOf(game.getMaxPlayers()));
            tvPrice.setText(String.format("%.2f RSD", game.getPrice())); // Convert double to String with two decimal places
            tvAgeRange.setText(game.getAgeRange());
            tvAdditionalInfo.setText(game.getAdditionalInfo().isEmpty() ? "No additional information" : game.getAdditionalInfo());
            updateJoinedUsers(game.getJoinedUsers());
        } else {
            // Close the activity if no game data is available
            finish();
        }

        // Set up the join game button click listener
        btnJoinGame.setOnClickListener(v -> {
            if (game != null) {
                List<String> joinedUsers = game.getJoinedUsers();
                if (joinedUsers.contains(currentUser)) {
                    joinedUsers.remove(currentUser); // Remove user if already joined
                } else {
                    joinedUsers.add(currentUser); // Add user if not already joined
                }
                updateJoinedUsers(joinedUsers);
            }
        });
    }

    // Method to update the list of joined users
    private void updateJoinedUsers(List<String> joinedUsers) {
        if (joinedUsers == null || joinedUsers.isEmpty()) {
            tvJoinedUsers.setText("No users have joined yet.");
        } else {
            StringBuilder joinedUsersText = new StringBuilder("Joined Users:\n");
            for (String user : joinedUsers) {
                joinedUsersText.append(user).append("\n");
            }
            tvJoinedUsers.setText(joinedUsersText.toString());
        }
    }

    // Handle the back button in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
