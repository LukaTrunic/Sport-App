package rs.ac.singidunum.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import rs.ac.singidunum.fragments.MyTermsFragment;
import rs.ac.singidunum.R;
import rs.ac.singidunum.adapters.ViewPagerAdapter;
import rs.ac.singidunum.fragments.AvailableTermsFragment;
import rs.ac.singidunum.models.Game;

public class MainActivity extends AppCompatActivity {

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
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateGameActivity.class);
            startActivityForResult(intent, CREATE_GAME_REQUEST);
        });

        // Periodically check and remove expired ads
        new Thread(() -> {
            while (true) {
                runOnUiThread(() -> {
                    availableTermsFragment.removeExpiredGames();
                    myTermsFragment.removeExpiredGames();
                });
                try {
                    Thread.sleep(60000); // Check every minute
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        if (requestCode == CREATE_GAME_REQUEST && resultCode == RESULT_OK && data != null) {
            // Handle the result of the CreateGameActivity
            Game newGame = (Game) data.getSerializableExtra("newGame");
            if (newGame != null) {
                // Add the new game to both fragments
                availableTermsFragment.addGame(newGame);
                myTermsFragment.addGame(newGame);
            }
        }
    }
}
