package com.example.pennypal.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pennypal.R;
import com.example.pennypal.database.Expense;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * The MyAdapter class is a RecyclerView adapter responsible for managing and displaying Expense data.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static List<Expense> data; // Replace 'String' with the type of your data

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
//        holder.textViewCategory.setText(expense.getCategory());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(expense.getDate());
//        holder.textViewDate.setText(formattedDate);
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
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
//            textViewCategory = itemView.findViewById(R.id.textViewCategory);
//            textViewDate = itemView.findViewById(R.id.textViewDate);

            // Set click listener for the item view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the position of the clicked item
            int position = getAdapterPosition();

            // Ensure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // Retrieve the clicked Expense object
                Expense clickedExpense = data.get(position);

                // Open the bottom sheet with the clicked Expense data
                openBottomSheet(clickedExpense);
            }
        }

        /**
         * Opens a bottom sheet with options for the given expense.
         *
         * @param expense The expense for which the bottom sheet is opened.
         */
        private void openBottomSheet(Expense expense) {
            // Check if the context is an instance of FragmentActivity
            showBottomSheetDialog(expense);
        }

        /**
         * Shows a bottom sheet dialog with options such as copy, share, download, and delete.
         */
        private void showBottomSheetDialog(Expense expense) {
            // Create a new BottomSheetDialog
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(itemView.getContext());

            // Set the content view to the custom layout for the bottom sheet
            bottomSheetDialog.setContentView(R.layout.fragment_bottom_sheet);

            // Initialize views from the bottom sheet layout
            LinearLayout copy = bottomSheetDialog.findViewById(R.id.copyLinearLayout);
            LinearLayout share = bottomSheetDialog.findViewById(R.id.shareLinearLayout);
            LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
            LinearLayout delete = bottomSheetDialog.findViewById(R.id.delete);

            TextView downloadText = bottomSheetDialog.findViewById(R.id.titleText);
            TextView amount = bottomSheetDialog.findViewById(R.id.amount);
            TextView category = bottomSheetDialog.findViewById(R.id.category);
            TextView paymentMethod = bottomSheetDialog.findViewById(R.id.paymentMethod);
            TextView date = bottomSheetDialog.findViewById(R.id.date);
            TextView description = bottomSheetDialog.findViewById(R.id.description);


            downloadText.setText(expense.getTitle());
            amount.setText(expense.getAmount().toString());
            category.setText(expense.getCategory());
            paymentMethod.setText(expense.getPaymentMethod());
            date.setText(expense.getDate().toString());
            description.setText(expense.getDescription());


            // Show the bottom sheet dialog
            bottomSheetDialog.show();
        }
    }

    /**
     * Update the data in the adapter and refresh the RecyclerView.
     *
     * @param newData The new list of Expense objects.
     */
    public void updateData(List<Expense> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    /**
     * Get the expense item at the specified position.
     *
     * @param position The position of the item in the data set.
     * @return The Expense object at the specified position.
     */
    public Expense getItem(int position) {
        return data.get(position);
    }

    /**
     * Remove an item at the given position from the data set and refresh the RecyclerView.
     *
     * @param position The position of the item to be removed.
     */
    public void removeItem(int position) {
        if (position >= 0 && position < data.size()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Add a new expense item to the adapter and refresh the RecyclerView.
     *
     * @param newExpense The new Expense object to be added.
     */
    public void addItem(Expense newExpense) {
        data.add(newExpense);
        notifyItemInserted(data.size() - 1);
    }
}
