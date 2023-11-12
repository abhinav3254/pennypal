package com.example.pennypal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeViews(view);
        setupRecyclerView();
        retrieveExpenses();
        return view;
    }

    /**
     * Initializes views in the fragment.
     *
     * @param view The fragment's root view.
     */
    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
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
}
