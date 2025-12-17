package rs.ac.singidunum.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import rs.ac.singidunum.R;
import rs.ac.singidunum.models.Game;

public class CreateGameActivity extends AppCompatActivity {

    private EditText etGameTitle, etGameDescription, etGameDate,
            etStartTime, etEndTime, etPlayersNeeded,
            etMaxPlayers, etPrice, etAgeRange, etAdditionalInfo;
    private AutoCompleteTextView etGameLocation;
    private Spinner spnSport;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private static final SimpleDateFormat INPUT_FORMAT =
            new SimpleDateFormat("d/M/yyyy HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etGameTitle       = findViewById(R.id.etGameTitle);
        etGameDescription = findViewById(R.id.etGameDescription);
        etGameLocation    = findViewById(R.id.etGameLocation);
        etGameDate        = findViewById(R.id.etGameDate);
        etStartTime       = findViewById(R.id.etStartTime);
        etEndTime         = findViewById(R.id.etEndTime);
        etPlayersNeeded   = findViewById(R.id.etPlayersNeeded);
        etMaxPlayers      = findViewById(R.id.etMaxPlayers);
        etPrice           = findViewById(R.id.etPrice);
        etAgeRange        = findViewById(R.id.etAgeRange);
        etAdditionalInfo  = findViewById(R.id.etAdditionalInfo);
        spnSport          = findViewById(R.id.spinnerSport);

        findViewById(R.id.btnDecreasePlayersNeeded)
                .setOnClickListener(v -> updatePlayerCount(etPlayersNeeded, -1));
        findViewById(R.id.btnIncreasePlayersNeeded)
                .setOnClickListener(v -> updatePlayerCount(etPlayersNeeded, +1));
        findViewById(R.id.btnDecreaseMaxPlayers)
                .setOnClickListener(v -> updatePlayerCount(etMaxPlayers, -1));
        findViewById(R.id.btnIncreaseMaxPlayers)
                .setOnClickListener(v -> updatePlayerCount(etMaxPlayers, +1));

        ArrayAdapter<CharSequence> sportAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.sports_array,
                android.R.layout.simple_spinner_item
        );
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSport.setAdapter(sportAdapter);

        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.location_suggestions)
        );
        etGameLocation.setAdapter(locAdapter);

        etGameDate   .setOnClickListener(v -> showDatePicker());
        etStartTime  .setOnClickListener(v -> showTimePicker(etStartTime));
        etEndTime    .setOnClickListener(v -> showTimePicker(etEndTime));

        db   = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Button btnSubmit = findViewById(R.id.btnSubmitGame);
        btnSubmit.setOnClickListener(v -> {
            if (!validateInputs()) return;

            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            String creatorUsername = prefs.getString("username", "Unknown");
            String creatorUid      = auth.getUid();
            String sport           = spnSport.getSelectedItem().toString();
            String title           = etGameTitle.getText().toString();
            String desc            = etGameDescription.getText().toString();
            String loc             = etGameLocation.getText().toString();
            String dateStr         = etGameDate.getText().toString();    // e.g. "20/6/2025"
            String startStr        = etStartTime.getText().toString();   // e.g. "18:00"
            String endStr          = etEndTime.getText().toString();     // e.g. "20:30"
            int    needed          = Integer.parseInt(etPlayersNeeded.getText().toString());
            int    maxp            = Integer.parseInt(etMaxPlayers.getText().toString());
            double price           = Double.parseDouble(etPrice.getText().toString());
            String ageRange        = etAgeRange.getText().toString();
            String info            = etAdditionalInfo.getText().toString();

            // parse endTimestamp from date + end time
            Date endTimestamp;
            try {
                endTimestamp = INPUT_FORMAT.parse(dateStr + " " + endStr);
            } catch (ParseException e) {
                // fallback to server time if parse fails
                endTimestamp = null;
            }

            Game newGame = new Game(
                    title, desc, sport,
                    startStr, endStr, dateStr, loc,
                    needed, maxp, price,
                    ageRange, info,
                    creatorUsername,
                    creatorUid
            );

            Map<String,Object> m = new HashMap<>();
            m.put("title",        title);
            m.put("description",  desc);
            m.put("sport",        sport);
            m.put("startTime",    startStr);
            m.put("endTime",      endStr);
            m.put("date",         dateStr);
            m.put("location",     loc);
            m.put("playersNeeded",needed);
            m.put("maxPlayers",   maxp);
            m.put("price",        price);
            m.put("ageRange",     ageRange);
            m.put("additionalInfo",info);
            m.put("startTimestamp", FieldValue.serverTimestamp());
            if (endTimestamp != null) {
                m.put("endTimestamp", endTimestamp);
            } else {
                m.put("endTimestamp", FieldValue.serverTimestamp());
            }
            m.put("creatorUsername", creatorUsername);
            m.put("creatorUid",      creatorUid);

            db.collection("games")
                    .add(m)
                    .addOnSuccessListener(docRef -> {
                        String gameId = docRef.getId();
                        newGame.setId(gameId);

                        // auto-join creator
                        Map<String,Object> joinRec = new HashMap<>();
                        joinRec.put("title",   title);
                        joinRec.put("joinedAt", FieldValue.serverTimestamp());
                        db.collection("users")
                                .document(creatorUid)
                                .collection("joinedGames")
                                .document(gameId)
                                .set(joinRec);

                        Intent result = new Intent();
                        result.putExtra("game", newGame);
                        setResult(RESULT_OK, result);
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this,
                                    "Could not save game: "+e.getMessage(),
                                    Toast.LENGTH_LONG).show()
                    );
        });
    }

    private boolean validateInputs() {
        if (etGameTitle.getText().toString().isEmpty()
                || etGameDescription.getText().toString().isEmpty()
                || etGameLocation.getText().toString().isEmpty()
                || etGameDate.getText().toString().isEmpty()
                || etStartTime.getText().toString().isEmpty()
                || etEndTime.getText().toString().isEmpty()
                || etPlayersNeeded.getText().toString().isEmpty()
                || etMaxPlayers.getText().toString().isEmpty()
                || etPrice.getText().toString().isEmpty()
                || etAgeRange.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (dp,y,m,d)-> etGameDate.setText(d + "/" + (m+1) + "/" + y),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePicker(EditText target) {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(
                this,
                (tp,h,min)-> target.setText(h + ":" + String.format("%02d", min)),
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void updatePlayerCount(EditText editText, int delta) {
        String s = editText.getText().toString();
        int current = 0;
        try { current = Integer.parseInt(s); } catch (NumberFormatException ignore){}
        editText.setText(String.valueOf(Math.max(0, current+delta)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
