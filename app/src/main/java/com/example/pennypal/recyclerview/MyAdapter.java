package com.example.pennypal.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pennypal.R;
import com.example.pennypal.database.Expense;

import java.util.List;

/**
 * The MyAdapter class is a RecyclerView adapter responsible for managing and displaying Expense data.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Expense> data; // Replace 'String' with the type of your data

    /**
     * Constructor for MyAdapter.
     *
     * @param data The list of Expense objects to be displayed.
     */
    public MyAdapter(List<Expense> data) {
        this.data = data;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense expense = data.get(position);

        // Set the data to the ViewHolder
        holder.textViewTitle.setText(expense.getTitle());
        holder.textViewAmount.setText(String.valueOf(expense.getAmount()));
        holder.textViewCategory.setText(expense.getCategory());
        holder.textViewDate.setText(expense.getDate().toString());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * The ViewHolder class represents each item's view in the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewAmount;
        public TextView textViewCategory;
        public TextView textViewDate;

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The view for each item in the RecyclerView.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}
