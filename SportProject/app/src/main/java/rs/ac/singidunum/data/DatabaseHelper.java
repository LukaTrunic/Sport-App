package rs.ac.singidunum.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version constants
    private static final String DATABASE_NAME = "games.db";
    private static final int DATABASE_VERSION = 4;

    // Table and column name constants for the games table
    public static final String TABLE_GAMES = "games";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_PLAYERS_NEEDED = "players_needed";
    public static final String COLUMN_MAX_PLAYERS = "max_players";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_AGE_RANGE = "age_range";
    public static final String COLUMN_ADDITIONAL_INFO = "additional_info";
    public static final String COLUMN_SPORT = "sport";
    public static final String COLUMN_CREATOR_USERNAME = "creator_username";

    // SQL statement to create the games table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_GAMES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_LOCATION + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_START_TIME + " TEXT, " +
                    COLUMN_END_TIME + " TEXT, " +
                    COLUMN_PLAYERS_NEEDED + " INTEGER, " +
                    COLUMN_MAX_PLAYERS + " INTEGER, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_AGE_RANGE + " TEXT, " +
                    COLUMN_ADDITIONAL_INFO + " TEXT, " +
                    COLUMN_SPORT + " TEXT, " +
                    COLUMN_CREATOR_USERNAME + " TEXT" +
                    ");";

    // Constructor that initializes the database helper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE); // Execute the SQL statement to create the table
    }

    // Method called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES); // Drop the old table if it exists
        onCreate(db); // Create the table again
    }

    // Method to check if a username is already taken
    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase(); // Get the database in read mode
        String query = "SELECT * FROM users WHERE username = ?"; // SQL query to check the username
        Cursor cursor = db.rawQuery(query, new String[]{username}); // Execute the query
        boolean exists = cursor.getCount() > 0; // Check if any results were returned
        cursor.close(); // Close the cursor
        db.close(); // Close the database
        return exists; // Return whether the username exists
    }
}
