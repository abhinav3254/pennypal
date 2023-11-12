// MyAdapter.java
package com.example.pennypal.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pennypal.R;
import com.example.pennypal.database.Expense;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Expense> data; // Replace 'String' with the type of your data

    public MyAdapter(List<Expense> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense expense = data.get(position);

        // Set the data to the ViewHolder
        holder.textViewTitle.setText(expense.getTitle());
        holder.textViewAmount.setText(String.valueOf(expense.getAmount()));
        holder.textViewCategory.setText(expense.getCategory());
        holder.textViewDate.setText(expense.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewAmount;
        public TextView textViewCategory;
        public TextView textViewDate;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}
