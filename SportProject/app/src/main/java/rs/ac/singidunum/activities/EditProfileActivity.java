package rs.ac.singidunum.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import rs.ac.singidunum.R;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView ivPhoto;
    private Button btnChangePhoto, btnSave;
    private EditText etUsername, etAge, etBiography;
    private Spinner spBasketball, spFootball, spVolleyball, spPadel;

    private FirebaseAuth     auth;
    private FirebaseFirestore db;
    private String            uid;
    private Uri               newPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // 1) Wire up views
        Toolbar tb       = findViewById(R.id.toolbar);
        ivPhoto          = findViewById(R.id.ivPhoto);
        btnChangePhoto   = findViewById(R.id.btnChangePhoto);
        btnSave          = findViewById(R.id.btnSave);
        etUsername       = findViewById(R.id.etUsername);
        etAge            = findViewById(R.id.etAge);
        etBiography      = findViewById(R.id.etBiography);
        spBasketball     = findViewById(R.id.spinnerBasketball);
        spFootball       = findViewById(R.id.spinnerFootball);
        spVolleyball     = findViewById(R.id.spinnerVolleyball);
        spPadel          = findViewById(R.id.spinnerPadel);

        // 2) create one adapter for all of them
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.level_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 3) Attach it:
        spBasketball.setAdapter(adapter);
        spFootball.  setAdapter(adapter);
        spVolleyball.setAdapter(adapter);
        spPadel.     setAdapter(adapter);

        // 2) Toolbar back arrow
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 3) Firebase init
        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            // no user -> go to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        uid = user.getUid();

        // 4) Prefill from Intent extras
        Intent in = getIntent();
        etUsername .setText(in.getStringExtra("username"));
        etAge      .setText(in.getStringExtra("age"));
        etBiography.setText(in.getStringExtra("biography"));

        // default to 1 if extra missing
        int bLvl = in.getIntExtra("basketballLevel", 1);
        int fLvl = in.getIntExtra("footballLevel",   1);
        int vLvl = in.getIntExtra("volleyballLevel", 1);
        int pLvl = in.getIntExtra("padelLevel",      1);

        // spinner positions are zero-based, so subtract 1
        spBasketball.setSelection(bLvl - 1);
        spFootball.  setSelection(fLvl - 1);
        spVolleyball.setSelection(vLvl - 1);
        spPadel.     setSelection(pLvl - 1);

        // 5) Change photo button
        btnChangePhoto.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pick, 100);
        });

        // 6) Save changes
        btnSave.setOnClickListener(v -> saveChanges());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // 1) Grab the URI
            newPhotoUri = data.getData();

            // 2) Tell Android we'll need to read this URI persistently
            getContentResolver().takePersistableUriPermission(
                    newPhotoUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            // 3) Show it immediately in our circular ImageView with Glide
            Glide.with(this)
                    .load(newPhotoUri)
                    .circleCrop()
                    .into(ivPhoto);
        }
    }

    private void saveChanges() {
        String newName = etUsername.getText().toString().trim();
        String ageStr  = etAge.getText().toString().trim();
        String bio     = etBiography.getText().toString().trim();
        int newBLvl = spBasketball.getSelectedItemPosition() + 1;
        int newFLvl = spFootball.  getSelectedItemPosition() + 1;
        int newVLvl = spVolleyball.getSelectedItemPosition() + 1;
        int newPLvl = spPadel.     getSelectedItemPosition() + 1;

        if (newName.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Username and age cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter a valid age", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1) Update FirebaseAuth displayName if changed
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && !newName.equals(user.getDisplayName())) {
            user.updateProfile(
                    new UserProfileChangeRequest.Builder()
                            .setDisplayName(newName)
                            .build()
            );
        }

        // put them into your Firestore map:
        Map<String,Object> updates = new HashMap<>();
        updates.put("username",        newName);
        updates.put("age",             age);
        updates.put("biography",       bio);
        updates.put("basketballLevel", newBLvl);
        updates.put("footballLevel",   newFLvl);
        updates.put("volleyballLevel", newVLvl);
        updates.put("padelLevel",      newPLvl);

        db.collection("users").document(uid)
                .update(updates)
                .addOnSuccessListener(__ -> {
                    // 3) if a new photo was picked, upload it
                    if (newPhotoUri != null) {
                        // get a *child* reference under your bucket
                        StorageReference photoRef =
                                FirebaseStorage.getInstance().getReference()
                                        .child("profilePhotos")
                                        .child(uid + ".jpg");

                        photoRef.putFile(newPhotoUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    // only once upload finishes do we request the URL
                                    photoRef.getDownloadUrl()
                                            .addOnSuccessListener(downloadUri -> {
                                                db.collection("users").document(uid)
                                                        .update("photoUrl", downloadUri.toString())
                                                        .addOnCompleteListener(___ -> {
                                                            // let the caller know “OK”
                                                            setResult(RESULT_OK);
                                                            finish();
                                                        });
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this,
                                                        "Couldn’t get download URL: " + e.getMessage(),
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this,
                                            "Photo upload failed: " + e.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                });
                    } else {
                        // no photo → we’re done
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,
                            "Profile update failed: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
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


