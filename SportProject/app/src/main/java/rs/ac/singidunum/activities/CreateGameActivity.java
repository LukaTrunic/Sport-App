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
import java.util.Calendar;

import rs.ac.singidunum.models.Game;
import rs.ac.singidunum.data.GameDao;
import rs.ac.singidunum.R;

public class CreateGameActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText etGameTitle, etGameDescription, etGameDate, etStartTime, etEndTime, etPlayersNeeded, etMaxPlayers, etPrice, etAgeRange, etAdditionalInfo;
    private AutoCompleteTextView etGameLocation;
    private Spinner spnSport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize UI elements
        etGameTitle = findViewById(R.id.etGameTitle);
        etGameDescription = findViewById(R.id.etGameDescription);
        etGameLocation = findViewById(R.id.etGameLocation);
        etGameDate = findViewById(R.id.etGameDate);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        etPlayersNeeded = findViewById(R.id.etPlayersNeeded);
        etMaxPlayers = findViewById(R.id.etMaxPlayers);
        etPrice = findViewById(R.id.etPrice);
        etAgeRange = findViewById(R.id.etAgeRange);
        etAdditionalInfo = findViewById(R.id.etAdditionalInfo);
        spnSport = findViewById(R.id.spinnerSport);

        // Set up the spinner for selecting sports
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSport.setAdapter(adapter);

        // Set up autocomplete suggestions for locations
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.location_suggestions));
        etGameLocation.setAdapter(locationAdapter);

        // Set up date and time pickers
        etGameDate.setOnClickListener(view -> showDatePickerDialog());
        etStartTime.setOnClickListener(view -> showTimePickerDialog(etStartTime));
        etEndTime.setOnClickListener(view -> showTimePickerDialog(etEndTime));

        // Set up listeners for incrementing and decrementing player counts
        findViewById(R.id.btnDecreasePlayersNeeded).setOnClickListener(view -> updatePlayerCount(etPlayersNeeded, -1));
        findViewById(R.id.btnIncreasePlayersNeeded).setOnClickListener(view -> updatePlayerCount(etPlayersNeeded, 1));
        findViewById(R.id.btnDecreaseMaxPlayers).setOnClickListener(view -> updatePlayerCount(etMaxPlayers, -1));
        findViewById(R.id.btnIncreaseMaxPlayers).setOnClickListener(view -> updatePlayerCount(etMaxPlayers, 1));

        // Set up the submit button
        Button btnSubmitGame = findViewById(R.id.btnSubmitGame);
        btnSubmitGame.setOnClickListener(view -> {
            // Validate inputs before proceeding
            if (validateInputs()) {
                // Get the username of the game creator from shared preferences
                SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                String creatorUsername = prefs.getString("username", "Unknown");

                // Create a new Game object
                Game newGame = new Game(
                        etGameTitle.getText().toString(),
                        etGameDescription.getText().toString(),
                        spnSport.getSelectedItem().toString(),
                        etStartTime.getText().toString(),
                        etEndTime.getText().toString(),
                        etGameDate.getText().toString(),
                        etGameLocation.getText().toString(),
                        Integer.parseInt(etPlayersNeeded.getText().toString()),
                        Integer.parseInt(etMaxPlayers.getText().toString()),
                        Double.parseDouble(etPrice.getText().toString()),
                        etAgeRange.getText().toString(),
                        etAdditionalInfo.getText().toString(),
                        creatorUsername
                );

                // Insert the new game into the database
                GameDao gameDao = new GameDao(this);
                gameDao.insertGame(newGame);
                gameDao.close();

                // Return the new game to the previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("game", newGame);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    // Validate the input fields
    private boolean validateInputs() {
        if (etGameTitle.getText().toString().isEmpty() || etGameDescription.getText().toString().isEmpty() ||
                etGameLocation.getText().toString().isEmpty() || etGameDate.getText().toString().isEmpty() ||
                etStartTime.getText().toString().isEmpty() || etEndTime.getText().toString().isEmpty() ||
                etPlayersNeeded.getText().toString().isEmpty() || etMaxPlayers.getText().toString().isEmpty() ||
                etPrice.getText().toString().isEmpty() || etAgeRange.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Show a date picker dialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
            etGameDate.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    // Show a time picker dialog
    private void showTimePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String time = hourOfDay + ":" + String.format("%02d", minute1);
            editText.setText(time);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    // Update the player count
    private void updatePlayerCount(EditText editText, int change) {
        int currentCount = Integer.parseInt(editText.getText().toString());
        int newCount = currentCount + change;
        if (newCount >= 0) {
            editText.setText(String.valueOf(newCount));
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
