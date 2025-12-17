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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import rs.ac.singidunum.models.Game;
import rs.ac.singidunum.adapters.GameAdapter;
import rs.ac.singidunum.activities.GameDetailsActivity;
import rs.ac.singidunum.utils.GameFilterUtils;
import rs.ac.singidunum.R;

public class AvailableTermsFragment extends Fragment {
    private RecyclerView recyclerView;
    private GameAdapter  gameAdapter;
    private List<Game>   gameList = new ArrayList<>();
    private Spinner      spinnerFilter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_available_terms, container, false);
        recyclerView    = v.findViewById(R.id.recyclerViewAvailable);
        spinnerFilter   = v.findViewById(R.id.spinnerFilter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gameAdapter = new GameAdapter(gameList, this::onGameClicked, getContext());
        recyclerView.setAdapter(gameAdapter);

        // 1) Firestore real-time listener instead of GameDao
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("games")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener((snap, err) -> {
                    if (err != null || snap == null) return;
                    gameList.clear();
                    for (DocumentSnapshot doc : snap.getDocuments()) {
                        Game g = doc.toObject(Game.class);
                        g.setId(doc.getId());
                        if (!GameFilterUtils.isGameExpired(g)) {
                            gameList.add(g);
                        }
                    }
                    // apply current sport-filter
                    String sport = spinnerFilter.getSelectedItem().toString();
                    List<Game> filtered = GameFilterUtils.filterGamesBySport(gameList, sport);
                    gameAdapter.updateGameList(filtered);
                });

        // 2) Spinner filter
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View vv, int pos, long id) {
                String sport = p.getItemAtPosition(pos).toString();
                List<Game> filtered = GameFilterUtils.filterGamesBySport(gameList, sport);
                gameAdapter.updateGameList(filtered);
            }
            @Override public void onNothingSelected(AdapterView<?> p){}
        });

        return v;
    }

    private void onGameClicked(Game game) {
        startActivity(new Intent(getContext(), GameDetailsActivity.class)
                .putExtra("GAME_ID", game.getId()));
    }

    // you can still keep addGame(...) if you want manual inserts
    public void addGame(Game game) {
        if (!GameFilterUtils.isGameExpired(game)) {
            gameList.add(game);
            gameAdapter.notifyDataSetChanged();
        }
    }

}

