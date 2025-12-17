package rs.ac.singidunum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import rs.ac.singidunum.R;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQ_EDIT = 1234;

    private ImageView ivProfileImage;
    private TextView  tvUsername, tvAge, tvBiography,
            tvBasketballLevel, tvFootballLevel,
            tvVolleyballLevel, tvPadelLevel;
    private Button    btnLogout, btnEditProfile;
    private int basketballLevel = 1,
            footballLevel   = 1,
            volleyballLevel = 1,
            padelLevel      = 1;

    private FirebaseAuth      auth;
    private FirebaseFirestore db;
    private String            uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // — Toolbar + Back arrow —
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // — Wire up views —
        ivProfileImage    = findViewById(R.id.ivProfileImage);
        tvUsername        = findViewById(R.id.tvUsername);
        tvAge             = findViewById(R.id.tvAge);
        tvBiography       = findViewById(R.id.tvBiography);
        tvBasketballLevel = findViewById(R.id.tvBasketballLevel);
        tvFootballLevel   = findViewById(R.id.tvFootballLevel);
        tvVolleyballLevel = findViewById(R.id.tvVolleyballLevel);
        tvPadelLevel      = findViewById(R.id.tvPadelLevel);
        btnLogout         = findViewById(R.id.btnLogout);
        btnEditProfile    = findViewById(R.id.btnEditProfile);

        // — Firebase init —
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();
        if (auth.getCurrentUser()==null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        uid = auth.getCurrentUser().getUid();

        // — Logout button —
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // — Edit-profile button —
        btnEditProfile.setOnClickListener(v -> {
            Intent i = new Intent(this, EditProfileActivity.class);
            i.putExtra("username",  tvUsername.getText().toString());
            // strip parentheses from age text "(29)" → "29"
            i.putExtra("age",       tvAge.getText().toString()
                    .replace("(", "")
                    .replace(")", ""));
            i.putExtra("biography", tvBiography.getText().toString());
            i.putExtra("basketballLevel", basketballLevel);
            i.putExtra("footballLevel",   footballLevel);
            i.putExtra("volleyballLevel", volleyballLevel);
            i.putExtra("padelLevel",      padelLevel);
            startActivityForResult(i, REQ_EDIT);
        });

        // — First load of profile data —
        loadProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // in case something changed while away
        loadProfile();
    }

    private void loadProfile() {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) return;

                    String username = doc.getString("username");
                    Long   age      = doc.getLong("age");
                    String bio      = doc.getString("biography");
                    Long   bLvl     = doc.getLong("basketballLevel");
                    Long   fLvl     = doc.getLong("footballLevel");
                    Long   vLvl     = doc.getLong("volleyballLevel");
                    Long   pLvl     = doc.getLong("padelLevel");
                    String photoUrl = doc.getString("photoUrl");

                    // 1) update your instance fields
                    basketballLevel = bLvl.intValue();
                    footballLevel   = fLvl.intValue();
                    volleyballLevel = vLvl.intValue();
                    padelLevel      = pLvl.intValue();

                    // 2) now update ALL UI elements
                    tvUsername       .setText(username);
                    tvAge            .setText("(" + age + ")");
                    tvBiography      .setText(bio);
                    tvBasketballLevel.setText("Basketball: Level " + bLvl);
                    tvFootballLevel  .setText("Football: Level "   + fLvl);
                    tvVolleyballLevel.setText("Volleyball: Level " + vLvl);
                    tvPadelLevel     .setText("Padel: Level "      + pLvl);

                    if (photoUrl != null && !photoUrl.isEmpty()) {
                        Glide.with(this)
                                .load(photoUrl)
                                .circleCrop()
                                .placeholder(R.drawable.ic_profile_placeholder)
                                .into(ivProfileImage);
                    }
                })

                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed loading profile: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT && resultCode == RESULT_OK) {
            // user just came back from EditProfileActivity, reload everything
            loadProfile();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
