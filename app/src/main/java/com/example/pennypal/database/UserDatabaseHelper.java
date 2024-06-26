package com.example.pennypal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author abhinavkumar
 * Helper class to manage user-related database operations.
 * Handles user table creation, insertion, updating, deletion, and retrieval.
 */
public class UserDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "expense.db";
        private static final int DATABASE_VERSION = 1;


    // User table constants
        private static final String USER_TABLE_NAME = "user";
        private static final String USER_COLUMN_ID = "_id";
        private static final String USER_COLUMN_NAME = "name";
        private static final String USER_COLUMN_GENDER = "gender";
        private static final String USER_COLUMN_DOB = "dob";
        private static final String USER_COLUMN_OCCUPATION = "occupation";

    // SQL statement to create user table
        private static final String CREATE_USER_TABLE =
                "CREATE TABLE " + USER_TABLE_NAME + " (" +
                        USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        USER_COLUMN_NAME + " TEXT," +
                        USER_COLUMN_GENDER + " TEXT," +
                        USER_COLUMN_DOB + " INTEGER," + // Assuming date will be stored as UNIX timestamp
                        USER_COLUMN_OCCUPATION + " TEXT" +
                        ")";

        /**
         * Constructor for UserDatabaseHelper.
         * @param context The context.
         */
        public UserDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create the expense table
            db.execSQL(CREATE_USER_TABLE);

            // Create the user table
            db.execSQL(CREATE_USER_TABLE);
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Inserts a new user record into the user table.
     * @param user The user to insert.
     * @return The row ID of the newly inserted user, or -1 if an error occurs.
     */
        public long insertUser(User user) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(USER_COLUMN_NAME, user.getName());
            values.put(USER_COLUMN_GENDER, user.getGender());
            values.put(USER_COLUMN_DOB, user.getDob().getTime()); // Storing date as UNIX timestamp
            values.put(USER_COLUMN_OCCUPATION, user.getUserOccupation());

            long newRowId = db.insert(USER_TABLE_NAME, null, values);
            db.close();

            return newRowId;
        }

    /**
     * Updates an existing user record in the user table.
     * @param user The user to update.
     * @return The number of rows affected.
     */
        public int updateUser(User user) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(USER_COLUMN_NAME, user.getName());
            values.put(USER_COLUMN_GENDER, user.getGender());
            values.put(USER_COLUMN_DOB, user.getDob().getTime());
            values.put(USER_COLUMN_OCCUPATION, user.getUserOccupation());

            int rowsAffected = db.update(USER_TABLE_NAME, values, USER_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(user.getId())});

            db.close();

            return rowsAffected;
        }

    /**
     * Deletes a user record from the user table.
     * @param userId The ID of the user to delete.
     * @return The number of rows deleted.
     */
        public int deleteUser(int userId) {
            SQLiteDatabase db = this.getWritableDatabase();

            int rowsDeleted = db.delete(USER_TABLE_NAME, USER_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(userId)});

            db.close();

            return rowsDeleted;
        }

    /**
     * Retrieves all users from the user table.
     * @return A list of User objects representing all users in the database.
     */
        public List<User> getAllUsers() {
            List<User> users = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + USER_TABLE_NAME;

            try (SQLiteDatabase db = this.getReadableDatabase()) {
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        User user = new User();
                        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(USER_COLUMN_ID)));
                        user.setName(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_NAME)));
                        user.setGender(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_GENDER)));
                        user.setDob(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(USER_COLUMN_DOB))));
                        user.setUserOccupation(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_OCCUPATION)));

                        users.add(user);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return users;
        }

    }


