package com.example.pennypal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "expense";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "_title";
    private static final String COLUMN_AMOUNT = "_amount";
    private static final String COLUMN_CATEGORY = "_category";
    private static final String COLUMN_PAYMENT_METHOD = "_payment_method";
    private static final String COLUMN_DESCRIPTION = "_description";
    private static final String COLUMN_DATE = "_date";
    private static final String COLUMN_UPDATE_DATE = "_update_date";

    // SQL statement for table creation
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_AMOUNT + " REAL," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_PAYMENT_METHOD + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_DATE + " INTEGER," +
                    COLUMN_UPDATE_DATE + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }

    // Method for inserting data
    public long insertExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, expense.getTitle());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_CATEGORY, expense.getCategory());
        values.put(COLUMN_PAYMENT_METHOD, expense.getPaymentMethod());
        values.put(COLUMN_DESCRIPTION, expense.getDescription());
        values.put(COLUMN_DATE, expense.getDate().getTime());
        values.put(COLUMN_UPDATE_DATE, expense.getUpdateDate().getTime());

        // Insert the data
        long newRowId = db.insert(TABLE_NAME, null, values);

        // Don't forget to close the database
        db.close();

        return newRowId;
    }

    // Method for updating data
    public int updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, expense.getTitle());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_CATEGORY, expense.getCategory());
        values.put(COLUMN_PAYMENT_METHOD, expense.getPaymentMethod());
        values.put(COLUMN_DESCRIPTION, expense.getDescription());
        values.put(COLUMN_DATE, expense.getDate().getTime());
        values.put(COLUMN_UPDATE_DATE, expense.getUpdateDate().getTime());

        // Update the data
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(expense.getId())});

        // Don't forget to close the database
        db.close();

        return rowsAffected;
    }

    // Method for deleting data
    public int deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the data
        int rowsDeleted = db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(expenseId)});

        // Don't forget to close the database
        db.close();

        return rowsDeleted;
    }

    // Method for fetching all expenses
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase(); // Change to getReadableDatabase

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Expense expense = new Expense();
                    expense.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    expense.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    expense.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)));
                    expense.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                    expense.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD)));
                    expense.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                    expense.setDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE))));
                    expense.setUpdateDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_UPDATE_DATE))));

                    expenses.add(expense);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the cursor and database in a finally block
            if (db != null) {
                db.close();
            }
        }

        return expenses;
    }
}
