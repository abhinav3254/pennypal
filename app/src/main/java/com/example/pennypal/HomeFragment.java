package com.example.pennypal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pennypal.database.DatabaseHelper;
import com.example.pennypal.database.Expense;
import com.example.pennypal.recyclerview.MyAdapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews(view);
        setupRecyclerView();
        retrieveExpenses();
        setupSearchEditText();
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
}
