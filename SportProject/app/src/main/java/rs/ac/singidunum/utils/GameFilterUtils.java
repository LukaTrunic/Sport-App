package rs.ac.singidunum.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import rs.ac.singidunum.models.Game;

// Utility class for filtering games based on certain criteria
public class GameFilterUtils {

    // Filters out expired games from the provided list
    public static void filterExpiredGames(List<Game> games) {
        Iterator<Game> iterator = games.iterator();
        while (iterator.hasNext()) {
            Game game = iterator.next();
            if (isGameExpired(game)) {
                iterator.remove();
            }
        }
    }

    // Checks if a game is expired by comparing its start time with the current time
    public static boolean isGameExpired(Game game) {
        try {
            // Define the date format to parse game start time and date
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
            // Parse the start time and date of the game
            Date startDateTime = sdf.parse(game.getStartTime() + " " + game.getDate());
            // Return true if the start time is before the current time, indicating the game is expired
            return startDateTime != null && startDateTime.before(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
            return true; // Return true if there is a parsing error, treating the game as expired
        }
    }

    // Filters games based on the specified sport
    public static List<Game> filterGamesBySport(List<Game> games, String sport) {
        List<Game> filteredGames = new ArrayList<>();
        for (Game game : games) {
            // Add the game to the filtered list if it matches the specified sport or if the sport filter is "All Sports"
            if (sport.equals("All Sports") || game.getSport().equals(sport)) {
                filteredGames.add(game);
            }
        }
        return filteredGames;
    }
}
