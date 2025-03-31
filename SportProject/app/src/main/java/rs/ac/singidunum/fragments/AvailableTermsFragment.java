package rs.ac.singidunum.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import rs.ac.singidunum.models.Game;
import rs.ac.singidunum.adapters.GameAdapter;
import rs.ac.singidunum.data.GameDao;
import rs.ac.singidunum.activities.GameDetailsActivity;
import rs.ac.singidunum.utils.GameFilterUtils;
import rs.ac.singidunum.R;

public class AvailableTermsFragment extends Fragment {

    // RecyclerView for displaying the list of available games
    private RecyclerView recyclerViewAvailable;
    // Adapter for managing game data in the RecyclerView
    private GameAdapter gameAdapter;
    // List to hold game data
    private List<Game> gameList;
    // Data access object for interacting with the game database
    private GameDao gameDao;
    // Spinner for filtering games by sport
    private Spinner spinnerFilter;
    // SwipeRefreshLayout for enabling pull-to-refresh functionality
    private SwipeRefreshLayout swipeRefreshLayout;

    // Inflates the layout for this fragment
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_terms, container, false);

        // Initialize RecyclerView and set its layout manager
        recyclerViewAvailable = view.findViewById(R.id.recyclerViewAvailable);
        recyclerViewAvailable.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Spinner and SwipeRefreshLayout
        spinnerFilter = view.findViewById(R.id.spinnerFilter);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutAvailable);

        // Initialize GameDao and fetch all games from the database
        gameDao = new GameDao(getContext());
        gameList = gameDao.getAllGames();
        if (gameList == null) {
            gameList = new ArrayList<>();
        } else {
            // Filter out expired games
            GameFilterUtils.filterExpiredGames(gameList);
        }

        // Initialize GameAdapter and set it to the RecyclerView
        gameAdapter = new GameAdapter(gameList, this::onGameClicked, getContext());
        recyclerViewAvailable.setAdapter(gameAdapter);

        // Set item selected listener for the Spinner to filter games by sport
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSport = parent.getItemAtPosition(position).toString();
                filterGamesBySport(selectedSport);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set refresh listener for SwipeRefreshLayout to refresh the game list
        swipeRefreshLayout.setOnRefreshListener(this::refreshGames);

        return view;
    }

    // Handles game item click to open the game details
    private void onGameClicked(Game game) {
        Intent intent = new Intent(getContext(), GameDetailsActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }

    // Adds a new game to the list if it's not expired and notifies the adapter
    public void addGame(Game game) {
        if (gameList == null) {
            gameList = new ArrayList<>();
        }
        if (!GameFilterUtils.isGameExpired(game)) {
            gameList.add(game);
            gameAdapter.notifyDataSetChanged();
            Log.d("AvailableTermsFragment", "Game added and adapter notified");
        }
    }

    // Refreshes the list of games by fetching from the database and filtering expired ones
    private void refreshGames() {
        gameList = gameDao.getAllGames();
        if (gameList == null) {
            gameList = new ArrayList<>();
        } else {
            GameFilterUtils.filterExpiredGames(gameList);
        }
        String selectedSport = spinnerFilter.getSelectedItem().toString();
        filterGamesBySport(selectedSport);
        swipeRefreshLayout.setRefreshing(false);
    }

    // Filters the games based on the selected sport and updates the adapter
    private void filterGamesBySport(String sport) {
        List<Game> filteredGames = GameFilterUtils.filterGamesBySport(gameList, sport);
        gameAdapter.updateGameList(filteredGames);
    }

    // Removes expired games from the list and updates the adapter
    public void removeExpiredGames() {
        if (gameList != null) {
            GameFilterUtils.filterExpiredGames(gameList);
            filterGamesBySport(spinnerFilter.getSelectedItem().toString());
        }
    }

    // Closes the database connection when the fragment is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        gameDao.close();
    }
}
