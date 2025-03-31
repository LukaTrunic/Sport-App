package rs.ac.singidunum.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import rs.ac.singidunum.models.Game;

public class GameDao {

    private SQLiteDatabase database; // Database instance
    private SQLiteOpenHelper dbHelper; // Database helper instance

    // Constructor that initializes the database helper and gets the writable database
    public GameDao(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Method to insert a new game into the database
    public void insertGame(Game game) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, game.getTitle());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, game.getDescription());
        values.put(DatabaseHelper.COLUMN_LOCATION, game.getLocation());
        values.put(DatabaseHelper.COLUMN_DATE, game.getDate());
        values.put(DatabaseHelper.COLUMN_START_TIME, game.getStartTime());
        values.put(DatabaseHelper.COLUMN_END_TIME, game.getEndTime());
        values.put(DatabaseHelper.COLUMN_PLAYERS_NEEDED, game.getPlayersNeeded());
        values.put(DatabaseHelper.COLUMN_MAX_PLAYERS, game.getMaxPlayers());
        values.put(DatabaseHelper.COLUMN_PRICE, game.getPrice());
        values.put(DatabaseHelper.COLUMN_AGE_RANGE, game.getAgeRange());
        values.put(DatabaseHelper.COLUMN_ADDITIONAL_INFO, game.getAdditionalInfo());
        values.put(DatabaseHelper.COLUMN_SPORT, game.getSport());
        values.put(DatabaseHelper.COLUMN_CREATOR_USERNAME, game.getCreatorUsername());
        database.insert(DatabaseHelper.TABLE_GAMES, null, values); // Insert the values into the database
    }

    // Method to get all games from the database
    public List<Game> getAllGames() {
        List<Game> gameList = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_LOCATION,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_START_TIME,
                DatabaseHelper.COLUMN_END_TIME,
                DatabaseHelper.COLUMN_PLAYERS_NEEDED,
                DatabaseHelper.COLUMN_MAX_PLAYERS,
                DatabaseHelper.COLUMN_PRICE,
                DatabaseHelper.COLUMN_AGE_RANGE,
                DatabaseHelper.COLUMN_ADDITIONAL_INFO,
                DatabaseHelper.COLUMN_SPORT,
                DatabaseHelper.COLUMN_CREATOR_USERNAME
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_GAMES, columns, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Game game = new Game(
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SPORT)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_TIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_TIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOCATION)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAYERS_NEEDED)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAX_PLAYERS)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE_RANGE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDITIONAL_INFO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATOR_USERNAME))
                    );
                    gameList.add(game); // Add each game to the game list
                } while (cursor.moveToNext());
            }
            cursor.close(); // Close the cursor
        }

        return gameList; // Return the list of games
    }

    // Method to get games created by a specific user
    public List<Game> getGamesByUsername(String username) {
        List<Game> gameList = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_LOCATION,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_START_TIME,
                DatabaseHelper.COLUMN_END_TIME,
                DatabaseHelper.COLUMN_PLAYERS_NEEDED,
                DatabaseHelper.COLUMN_MAX_PLAYERS,
                DatabaseHelper.COLUMN_PRICE,
                DatabaseHelper.COLUMN_AGE_RANGE,
                DatabaseHelper.COLUMN_ADDITIONAL_INFO,
                DatabaseHelper.COLUMN_SPORT,
                DatabaseHelper.COLUMN_CREATOR_USERNAME
        };
        String selection = DatabaseHelper.COLUMN_CREATOR_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = database.query(DatabaseHelper.TABLE_GAMES, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Game game = new Game(
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SPORT)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_TIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_TIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOCATION)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PLAYERS_NEEDED)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAX_PLAYERS)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE_RANGE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDITIONAL_INFO)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATOR_USERNAME))
                    );
                    gameList.add(game); // Add each game to the game list
                } while (cursor.moveToNext());
            }
            cursor.close(); // Close the cursor
        }

        return gameList; // Return the list of games
    }

    // Method to close the database helper
    public void close() {
        dbHelper.close(); // Close the database helper
    }
}
