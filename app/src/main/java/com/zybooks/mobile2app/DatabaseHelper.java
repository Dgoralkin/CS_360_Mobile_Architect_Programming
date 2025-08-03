package com.zybooks.mobile2app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;


/* *********************************************************************************************
 *  TODO: The DatabaseHelper() class acts as an interface between the application and the SQLite database.
 *   It will store the data for the users and items in the database in two tables.
 * ********************************************************************************************/
public class DatabaseHelper extends SQLiteOpenHelper {

    // Variables
    private static final String TAG = "PrintLog";
    private static final String DATABASE_NAME = "mobile2app.db";
    private static final int DATABASE_VERSION = 1;

    // User table variables
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Items table variables
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_ID = "id";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    public static final String COLUMN_SKU = "sku";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";


    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // The onCreate() method is called when the database is created for the first time.
    // It is used to create the tables that will store the data.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table for users if it doesn't exist
        try {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL)";

            // Create table for items if it doesn't exist
            String createItemsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " (" +
                    COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE_PATH + " TEXT, " +
                    COLUMN_SKU + " TEXT NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL" + ")";

            // Execute SQL statements
            db.execSQL(createUsersTable);
            db.execSQL(createItemsTable);

            Log.d(TAG, "Tables created successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Database creation failed", e);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For development only; in production use ALTER TABLE
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }


    // ---------- GENERAL DATABASE METHODS ----------
    public boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }


    // ---------- USER TABLE METHODS ----------

    // Check user credentials in the database
    // Returns true if credentials are valid and user exists, false otherwise
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        boolean valid = cursor.moveToFirst();
        cursor.close();
        return valid;
    }

    // Register a new user
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // Return true if insert was successful
        return result != -1;
    }
    // ---------- END OF USER TABLE METHODS ----------
}