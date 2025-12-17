package rs.ac.singidunum.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import rs.ac.singidunum.adapters.ViewPagerAdapter;
import rs.ac.singidunum.fragments.MyTermsFragment;
import rs.ac.singidunum.R;
import rs.ac.singidunum.fragments.AvailableTermsFragment;
import rs.ac.singidunum.models.Game;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_NOTIF = 42;
    // Request code for creating a new game
    private static final int CREATE_GAME_REQUEST = 1;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private AvailableTermsFragment availableTermsFragment;
    private MyTermsFragment myTermsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ask for notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{ Manifest.permission.POST_NOTIFICATIONS },
                        REQ_NOTIF
                );
            }
        }

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the tab layout and view pager
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);

        // Create instances of the fragments
        availableTermsFragment = new AvailableTermsFragment();
        myTermsFragment = new MyTermsFragment();

        // Fetch logged-in username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = prefs.getString("username", "");

        // Set arguments for MyTermsFragment
        Bundle args = new Bundle();
        args.putString("username", username);
        myTermsFragment.setArguments(args);

        // Add fragments to the adapter
        viewPagerAdapter.addFragment(availableTermsFragment, "Available Terms");
        viewPagerAdapter.addFragment(myTermsFragment, "My Terms");

        // Set the adapter for the view pager
        viewPager.setAdapter(viewPagerAdapter);

        // Link the tab layout with the view pager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(viewPagerAdapter.getPageTitle(position))).attach();

        // Set up the FloatingActionButton to create a new game
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(MainActivity.this, CreateGameActivity.class),
                        CREATE_GAME_REQUEST
                )
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_chats_list) {
            startActivity(new Intent(this, ChatsListActivity.class));
            return true;
        }
        // Handle action bar item clicks
        if (item.getItemId() == R.id.action_profile) {
            // Navigate to ProfileActivity
            Intent intent = new Intent(this, ProfileActivity.class);
            // Pass user information to ProfileActivity
            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            String username = prefs.getString("username", "");
            int age = prefs.getInt("age", 0);
            String gender = prefs.getString("gender", "");
            String biography = prefs.getString("biography", "");
            String sports = prefs.getString("sports", "");

            intent.putExtra("username", username);
            intent.putExtra("age", age);
            intent.putExtra("gender", gender);
            intent.putExtra("biography", biography);
            intent.putExtra("sports", sports);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_sports_news) {
            // Navigate to SportsNewsActivity
            Intent intent = new Intent(this, SportsNewsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // we don’t need to push the newGame into the UI manually,
        // our Firestore snapshot‐listeners will reload/update both lists
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_NOTIF) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        "Notifications enabled 👍",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Permission denied — game reminders will be silent.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}
