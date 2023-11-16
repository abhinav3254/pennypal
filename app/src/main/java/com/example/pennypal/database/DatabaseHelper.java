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
 * DatabaseHelper class manages the SQLite database for expense-related data.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    // Database properties
    private static final String DATABASE_NAME = "expense.db";
    private static final int DATABASE_VERSION = 1;


    // Table and column properties
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

    /**
     * Constructor for DatabaseHelper.
     *
     * @param context The application context.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Called when the database is created for the first time.
     *
     * @param db The SQLiteDatabase instance.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL(CREATE_TABLE);
    }


    /**
     * Called when the database needs to be upgraded.
     *
     * @param db         The SQLiteDatabase instance.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }


    /**
     * Inserts an expense record into the database.
     *
     * @param expense The Expense object to be inserted.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
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


    /**
     * Updates an existing expense record in the database.
     *
     * @param expense The Expense object with updated values.
     * @return The number of rows affected (0 for none).
     */
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

    /**
     * Deletes an expense record from the database.
     *
     * @param expenseId The ID of the expense to be deleted.
     * @return The number of rows affected (0 for none).
     */
    public int deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the data
        int rowsDeleted = db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(expenseId)});

        // Don't forget to close the database
        db.close();

        return rowsDeleted;
    }

    /**
     * Retrieves all expenses from the database.
     *
     * @return A list of Expense objects representing all expenses.
     */
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        // Change to getReadableDatabase

        try (SQLiteDatabase db = this.getReadableDatabase()) {
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
        }
        // Close the cursor and database in a finally block

        return expenses;
    }

    /**
     * Retrieves the names of all columns in the expense table.
     *
     * @return A list of column names.
     */
    public List<String> getTableColumns() {
        List<String> columns = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_NAME + ")", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    columns.add(columnName);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return columns;
    }

    /**
     * Retrieves all data from the expense table.
     *
     * @return A list of lists representing the entire table data.
     */
    public List<List<String>> getTableData() {
        List<List<String>> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    List<String> rowData = new ArrayList<>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        rowData.add(cursor.getString(i));
                    }
                    data.add(rowData);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return data;
    }


    /**
     * Truncates (deletes all records from) the expense table.
     */
    public void truncateExpenseTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Execute SQL query to delete all records
            db.execSQL("DELETE FROM " + TABLE_NAME);

            // (Optional) Reset auto-increment counter
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Don't forget to close the database
            db.close();
        }
    }


    /**
     * Searches for expenses in the database based on the given search query.
     *
     * @param query The search query to match against title, category, payment method, or description.
     * @return A list of Expense objects matching the search query.
     */
    public List<Expense> searchExpenses(String query) {
        List<Expense> searchResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to search in
        String[] columnsToSearch = {COLUMN_TITLE, COLUMN_CATEGORY, COLUMN_PAYMENT_METHOD, COLUMN_DESCRIPTION};

        // Build the selection clause with a LIKE query
        String selection = "";
        for (int i = 0; i < columnsToSearch.length; i++) {
            selection += columnsToSearch[i] + " LIKE '%" + query + "%'";
            if (i < columnsToSearch.length - 1) {
                selection += " OR ";
            }
        }

        // Execute the query
        Cursor cursor = db.query(TABLE_NAME, null, selection, null, null, null, null);

        try {
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

                    searchResults.add(expense);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return searchResults;
    }



    /**
     * Retrieves an expense from the database based on its ID.
     *
     * @param expenseId The ID of the expense to retrieve.
     * @return The Expense object with the specified ID, or null if not found.
     */
    public Expense getExpenseById(long expenseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(expenseId)},
                null,
                null,
                null
        );

        Expense expense = null;
        try {
            if (cursor.moveToFirst()) {
                expense = new Expense();
                expense.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                expense.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                expense.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)));
                expense.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                expense.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD)));
                expense.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                expense.setDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE))));
                expense.setUpdateDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_UPDATE_DATE))));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return expense;
    }



    /**
     * Retrieves the total expenses grouped by category from the SQLite database.
     * Returns a list of CategoryTotal objects representing each category's total expense.
     *
     * @return A list of CategoryTotal objects containing the total expenses grouped by category.
     */
    public List<CategoryTotal> getExpensesGroupedByCategory() {
        // Initialize a list to store CategoryTotal objects
        List<CategoryTotal> categoryTotals = new ArrayList<>();

        // Get a readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // Execute a SQL query to retrieve total expenses grouped by category
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CATEGORY + ", SUM(" + COLUMN_AMOUNT + ") " +
                "FROM " + TABLE_NAME +
                " GROUP BY " + COLUMN_CATEGORY, null);

        try {
            // If the cursor moves to the first row of the query result
            if (cursor.moveToFirst()) {
                // Iterate through the query results
                do {
                    // Retrieve category name and total amount from the cursor
                    String category = cursor.getString(0);
                    double totalAmount = cursor.getDouble(1);

                    // Create a CategoryTotal object and add it to the list
                    CategoryTotal categoryTotal = new CategoryTotal(category, totalAmount);
                    categoryTotals.add(categoryTotal);
                } while (cursor.moveToNext()); // Move to the next row until the end of the result set
            }
        } finally {
            // Close the cursor and the database
            cursor.close();
            db.close();
        }

        // Return the list containing CategoryTotal objects with expenses grouped by category
        return categoryTotals;
    }



}
