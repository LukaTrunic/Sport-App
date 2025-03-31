package rs.ac.singidunum.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import rs.ac.singidunum.R;
import rs.ac.singidunum.models.Game;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> gameList;  // List to hold game data
    private final OnGameClickListener onGameClickListener;  // Interface to handle game item clicks
    private final Context context;  // Context for accessing resources

    // Interface to handle clicks on game items
    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    // Constructor to initialize adapter with game list, click listener, and context
    public GameAdapter(List<Game> gameList, OnGameClickListener onGameClickListener, Context context) {
        this.gameList = gameList;
        this.onGameClickListener = onGameClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual game items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        // Bind the game data to the ViewHolder
        Game game = gameList.get(position);
        holder.bind(game, onGameClickListener);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return gameList.size();
    }

    // Method to update the game list and refresh the RecyclerView
    public void updateGameList(List<Game> updatedGameList) {
        this.gameList = updatedGameList;
        notifyDataSetChanged();
    }

    // ViewHolder class to hold and manage the views for each game item
    static class GameViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvGameSport, tvGameDateTime, tvGameLocation, tvGamePlayers, tvGamePrice, tvGameAdditionalInfo, tvCreatorUsername;
        private final Button btnJoinGame;

        // Constructor to initialize the views
        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGameSport = itemView.findViewById(R.id.tvGameSport);
            tvGameDateTime = itemView.findViewById(R.id.tvGameDateTime);
            tvGameLocation = itemView.findViewById(R.id.tvGameLocation);
            tvGamePlayers = itemView.findViewById(R.id.tvGamePlayers);
            tvGamePrice = itemView.findViewById(R.id.tvGamePrice);
            tvGameAdditionalInfo = itemView.findViewById(R.id.tvGameAdditionalInfo);
            tvCreatorUsername = itemView.findViewById(R.id.tvCreatorUsername);
            btnJoinGame = itemView.findViewById(R.id.btnJoinGame);
        }

        // Method to bind game data to the views
        public void bind(Game game, OnGameClickListener onGameClickListener) {
            tvGameSport.setText(game.getSport());
            tvGameDateTime.setText(String.format("%s - %s (%s)", game.getStartTime(), game.getEndTime(), game.getDate()));
            tvGameLocation.setText(game.getLocation());
            tvGamePlayers.setText(String.format("%d (%d players needed)", game.getMaxPlayers(), game.getPlayersNeeded()));
            tvGamePrice.setText(String.format("%.2f RSD", game.getPrice()));
            tvGameAdditionalInfo.setText(game.getAdditionalInfo());
            tvCreatorUsername.setText("Created by: " + game.getCreatorUsername());

            // Set click listener for the join game button
            btnJoinGame.setOnClickListener(v -> onGameClickListener.onGameClick(game));
        }
    }
}
