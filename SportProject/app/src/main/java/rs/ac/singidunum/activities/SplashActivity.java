package rs.ac.singidunum.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rs.ac.singidunum.activities.LoginActivity;
import rs.ac.singidunum.activities.MainActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent i = new Intent(this,
                (user!=null ? MainActivity.class : LoginActivity.class));
        startActivity(i);
        finish();
    }
}

