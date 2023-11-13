package com.example.pennypal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pennypal.database.DatabaseHelper;
import com.example.pennypal.database.Expense;
import com.example.pennypal.recyclerview.MyAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * The HomeFragment class represents the fragment responsible for displaying a list of expenses.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Expense> expenseList;
    private DatabaseHelper databaseHelper;
    private EditText searchEditText;


    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews(view);
        setupRecyclerView();
        retrieveExpenses();
        setupSearchEditText();
        swipeGesture();
        rootView = view;
        return view;
    }

    /**
     * Initializes views in the fragment.
     *
     * @param view The fragment's root view.
     */
    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
    }

    /**
     * Sets up the RecyclerView with the necessary configurations and adapters.
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        databaseHelper = new DatabaseHelper(requireActivity());
        adapter = new MyAdapter(expenseList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Retrieves expenses from the database and updates the adapter.
     */
    private void retrieveExpenses() {
        expenseList = databaseHelper.getAllExpenses();
        adapter.updateData(expenseList);
    }

    /**
     * Sets up a TextWatcher for the search input field to filter expenses based on user input.
     */
    private void setupSearchEditText() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Do something before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Do something when text changes
                Log.d("TEXT3254", "onTextChanged: ");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do something after text changes
                String searchText = editable.toString();
                filterExpenses(searchText);
            }
        });
    }

    /**
     * Filters expenses based on the search text and updates the adapter.
     *
     * @param searchText The text entered by the user in the search field.
     */
    private void filterExpenses(String searchText) {
        // Implement your logic to filter expenses based on searchText
        // For example, you can update the expenseList and call adapter.updateData(expenseList)
        Log.d("afterTextChanged3254", "filterExpenses: " + searchText);

        // Use the searchExpenses method to get filtered results
        List<Expense> filteredExpenses = databaseHelper.searchExpenses(searchText);

        // Update the adapter with the filtered data
        adapter.updateData(filteredExpenses);
    }


    private void swipeGesture() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Get the swiped item from your data set
                final Expense swipedExpense = adapter.getItem(position);

                // Delete the item from the database
                deleteExpenseFromDatabase(swipedExpense);

                // Remove the item from the adapter
                adapter.removeItem(position);

                // Show a Snackbar with an undo option
                Snackbar snackbar = Snackbar.make(recyclerView, "Expense deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Undo action clicked, restore the deleted item
                                restoreExpense(swipedExpense);
                            }
                        });

                // Set callback to handle dismissal and undo action
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        // Handle Snackbar dismissal
                        if (event != DISMISS_EVENT_ACTION) {
                            // Snackbar dismissed without undo, perform any cleanup if needed
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        // Handle Snackbar shown
                    }
                });

                // Show the Snackbar
                snackbar.show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    /**
     * Deletes an expense record from the database.
     *
     * @param expense The Expense object representing the record to be deleted.
     */
    private void deleteExpenseFromDatabase(Expense expense) {
        // Implement your database deletion logic here using your DatabaseHelper
        // For example, assuming you have a deleteExpense method in your DatabaseHelper:
        databaseHelper.deleteExpense(expense.getId());
    }

    /**
     * Restores a deleted expense record in the database.
     *
     * @param expense The Expense object representing the record to be restored.
     */
    private void restoreExpense(Expense expense) {
        // Add the expense back to the database
        long restoredItemId = databaseHelper.insertExpense(expense);

        // Check if the item was successfully added back to the database
        if (restoredItemId != -1) {
            // Fetch the restored expense from the database using its new ID
            Expense restoredExpense = databaseHelper.getExpenseById(restoredItemId);

            // Update the adapter with the restored data
            adapter.addItem(restoredExpense);

            // Show a Snackbar indicating successful restoration
            showSnackbar("Expense restored", rootView);
        } else {
            // Handle the case where the item couldn't be restored
            // This could include notifying the user or logging an error
            Log.e("RestoreExpense", "Failed to restore expense: " + expense.getId());

            // Show a Snackbar indicating the failure to restore the expense
            showSnackbar("Failed to restore expense", rootView);
        }
    }

    /**
     * Shows a Snackbar with a specified message.
     *
     * @param message The message to be displayed in the Snackbar.
     * @param view    The View to find a parent from to locate the Snackbar within the view hierarchy.
     */
    private void showSnackbar(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

}
