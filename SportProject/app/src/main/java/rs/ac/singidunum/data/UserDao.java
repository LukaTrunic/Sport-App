package rs.ac.singidunum.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import rs.ac.singidunum.models.User;

public class UserDao extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "games.db";
    private static final int DATABASE_VERSION = 4;

    // User table and columns
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_BIOGRAPHY = "biography";
    private static final String COLUMN_BASKETBALL_LEVEL = "basketball_level";
    private static final String COLUMN_FOOTBALL_LEVEL = "football_level";
    private static final String COLUMN_VOLLEYBALL_LEVEL = "volleyball_level";
    private static final String COLUMN_PADEL_LEVEL = "padel_level";

    // Constructor
    public UserDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate method to create the tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_AGE + " INTEGER,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_BIOGRAPHY + " TEXT,"
                + COLUMN_BASKETBALL_LEVEL + " INTEGER,"
                + COLUMN_FOOTBALL_LEVEL + " INTEGER,"
                + COLUMN_VOLLEYBALL_LEVEL + " INTEGER,"
                + COLUMN_PADEL_LEVEL + " INTEGER" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // SQL statement to create games table
        String CREATE_GAMES_TABLE = "CREATE TABLE games (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT, " +
                "location TEXT, " +
                "date TEXT, " +
                "start_time TEXT, " +
                "end_time TEXT, " +
                "players_needed INTEGER, " +
                "max_players INTEGER, " +
                "price REAL, " +
                "age_range TEXT, " +
                "additional_info TEXT, " +
                "sport TEXT, " +
                "creator_username TEXT" +
                ")";
        db.execSQL(CREATE_GAMES_TABLE);
    }

    // onUpgrade method to handle database version upgrades
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS games");
        onCreate(db);
    }

    // Method to add a user to the database
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_AGE, user.getAge());
        values.put(COLUMN_GENDER, user.getGender());
        values.put(COLUMN_BIOGRAPHY, user.getBiography());
        values.put(COLUMN_BASKETBALL_LEVEL, user.getBasketballLevel());
        values.put(COLUMN_FOOTBALL_LEVEL, user.getFootballLevel());
        values.put(COLUMN_VOLLEYBALL_LEVEL, user.getVolleyballLevel());
        values.put(COLUMN_PADEL_LEVEL, user.getPadelLevel());

        db.insert(TABLE_USERS, null, values); // Insert the user into the database
        db.close(); // Close the database
    }

    // Method to check if a username is already taken
    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID}, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0; // Check if the cursor returned any results
        cursor.close(); // Close the cursor
        return exists; // Return true if username is taken, false otherwise
    }

    // Method to get a user from the database by username
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_AGE, COLUMN_GENDER, COLUMN_BIOGRAPHY, COLUMN_BASKETBALL_LEVEL, COLUMN_FOOTBALL_LEVEL, COLUMN_VOLLEYBALL_LEVEL, COLUMN_PADEL_LEVEL}, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIOGRAPHY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BASKETBALL_LEVEL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOTBALL_LEVEL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VOLLEYBALL_LEVEL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PADEL_LEVEL))
            );
            cursor.close(); // Close the cursor
            return user; // Return the user object
        } else {
            return null; // Return null if user not found
        }
    }
}
