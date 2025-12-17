package rs.ac.singidunum.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.ac.singidunum.R;
import rs.ac.singidunum.models.Game;

public class GameDetailsActivity extends AppCompatActivity {

    private TextView tvGameTitle, tvGameDescription, tvGameLocation,
            tvGameDate, tvStartTime, tvEndTime,
            tvPlayersNeeded, tvMaxPlayers, tvPrice,
            tvAgeRange, tvAdditionalInfo, tvJoinedUsers;
    private Button   btnJoinGame;

    private FirebaseFirestore db;
    private FirebaseAuth      auth;
    private String            myUid, myName;
    private Game              game;
    private String            gameId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // init
        db    = FirebaseFirestore.getInstance();
        auth  = FirebaseAuth.getInstance();
        myUid = auth.getUid();
        myName= auth.getCurrentUser().getDisplayName();

        // bind views…
        tvGameTitle        = findViewById(R.id.tvGameTitle);
        tvGameDescription  = findViewById(R.id.tvGameDescription);
        tvGameLocation     = findViewById(R.id.tvGameLocation);
        tvGameDate         = findViewById(R.id.tvGameDate);
        tvStartTime        = findViewById(R.id.tvStartTime);
        tvEndTime          = findViewById(R.id.tvEndTime);
        tvPlayersNeeded    = findViewById(R.id.tvPlayersNeeded);
        tvMaxPlayers       = findViewById(R.id.tvMaxPlayers);
        tvPrice            = findViewById(R.id.tvPrice);
        tvAgeRange         = findViewById(R.id.tvAgeRange);
        tvAdditionalInfo   = findViewById(R.id.tvAdditionalInfo);
        tvJoinedUsers      = findViewById(R.id.tvJoinedUsers);
        btnJoinGame        = findViewById(R.id.btnJoinGame);

        // get the gameId instead of a full Game
        gameId = getIntent().getStringExtra("GAME_ID");
        if (gameId == null) {
            finish();
            return;
        }

        // 1) fetch the game document
        db.collection("games")
                .document(gameId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        Toast.makeText(this, "Game not found", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    // deserialize into your model (make sure your Game has a no‐arg ctor)
                    game = doc.toObject(Game.class);
                    game.setId(doc.getId());                // set the id
                    populateGameUI();                       // fill in the textviews

                    // 2) now listen for participants
                    db.collection("games")
                            .document(gameId)
                            .collection("participants")
                            .addSnapshotListener((snap, err) -> {
                                if (err != null || snap == null) return;
                                List<String> joined = new ArrayList<>();
                                boolean amJoined = false;
                                for (DocumentSnapshot p : snap.getDocuments()) {
                                    String name = p.getString("name");
                                    if (name != null) joined.add(name);
                                    if (p.getId().equals(myUid)) amJoined = true;
                                }
                                updateJoinedUsers(joined);

                                int spotsFilled = joined.size();              // current participants count
                                int maxSpots    = game.getPlayersNeeded();    // “Players Needed” from your model

                                // 3) enable/disable the join button based on creatorUid & amJoined
                                if (myUid.equals(game.getCreatorUid())) {
                                    btnJoinGame.setEnabled(false);
                                    btnJoinGame.setText("You created this game");
                                } else if (amJoined) {
                                    btnJoinGame.setEnabled(true);
                                    btnJoinGame.setText("Leave Game");
                                    btnJoinGame.setOnClickListener(v -> leaveGame());
                                } else if (spotsFilled >= maxSpots) {
                                    // game is full
                                    btnJoinGame.setEnabled(false);
                                    btnJoinGame.setText("Game Full");

                                }else {
                                    btnJoinGame.setEnabled(true);
                                    btnJoinGame.setText("Join Game");
                                    btnJoinGame.setOnClickListener(v -> joinGame());
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading game: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                });
    }

    private void populateGameUI() {
        tvGameTitle      .setText(game.getTitle());
        tvGameDescription.setText(game.getDescription());
        tvGameLocation   .setText(game.getLocation());
        tvGameDate       .setText(game.getDate());
        tvStartTime      .setText(game.getStartTime());
        tvEndTime        .setText(game.getEndTime());
        tvPlayersNeeded  .setText(String.valueOf(game.getPlayersNeeded()));
        tvMaxPlayers     .setText(String.valueOf(game.getMaxPlayers()));
        tvPrice          .setText(String.format("%.2f RSD", game.getPrice()));
        tvAgeRange       .setText(game.getAgeRange());
        tvAdditionalInfo .setText(
                game.getAdditionalInfo().isEmpty()
                        ? "No additional information"
                        : game.getAdditionalInfo()
        );
    }

    private void joinGame() {
        String gameId = game.getId();

        // (1) add under games/{gameId}/participants/{myUid}
        Map<String,Object> part = new HashMap<>();
        part.put("uid",  myUid);
        part.put("name", myName);
        db.collection("games")
                .document(gameId)
                .collection("participants")
                .document(myUid)
                .set(part);

        // (2) add under users/{myUid}/joinedGames/{gameId}
        Map<String,Object> joined = new HashMap<>();
        joined.put("title",    game.getTitle());
        joined.put("joinedAt", FieldValue.serverTimestamp());
        db.collection("users")
                .document(myUid)
                .collection("joinedGames")
                .document(gameId)
                .set(joined);

        // (3) remember last‐joined
        getSharedPreferences("sportiko_prefs", MODE_PRIVATE)
                .edit()
                .putString("last_joined_game", gameId)
                .apply();

        // (4) open chat
        startActivity(new Intent(this, ChatActivity.class)
                .putExtra("GAME_ID", gameId));
    }

    private void leaveGame() {
        String gameId = game.getId();

        // remove from participants
        db.collection("games")
                .document(gameId)
                .collection("participants")
                .document(myUid)
                .delete();

        // remove from user’s joinedGames
        db.collection("users")
                .document(myUid)
                .collection("joinedGames")
                .document(gameId)
                .delete();

        // if this was your “last joined”, clear it
        SharedPreferences prefs = getSharedPreferences("sportiko_prefs", MODE_PRIVATE);
        if (gameId.equals(prefs.getString("last_joined_game", ""))) {
            prefs.edit().remove("last_joined_game").apply();
        }

        // close the chat screen if you’re in it
        finish();
    }

    private void updateJoinedUsers(List<String> joinedUsers) {
        if (joinedUsers == null || joinedUsers.isEmpty()) {
            tvJoinedUsers.setText("No users have joined yet.");
        } else {
            StringBuilder sb = new StringBuilder("Joined Users:\n");
            for (String name : joinedUsers) {
                sb.append("• ").append(name).append("\n");
            }
            tvJoinedUsers.setText(sb.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
