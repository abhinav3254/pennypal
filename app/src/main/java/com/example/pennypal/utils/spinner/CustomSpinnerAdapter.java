package com.example.pennypal.utils.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pennypal.R;

import java.util.List;


/**
 *
 * @author abhinavkumar
 * Custom adapter for populating a spinner with custom data items.
 * Extends the BaseAdapter class to provide data to the spinner.
 */
public class CustomSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<CustomSpinnerItem> itemList;
    private LayoutInflater inflater;


    /**
     * Constructor for the CustomSpinnerAdapter.
     *
     * @param context  The context of the calling activity or fragment.
     * @param itemList The list of CustomSpinnerItem objects.
     */
    public CustomSpinnerAdapter(Context context, List<CustomSpinnerItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
    }


    /**
     * Gets the number of items in the data set represented by this adapter.
     *
     * @return The total number of items in the data set
     */
    @Override
    public int getCount() {
        return itemList.size();
    }


    /**
     * Gets the data item associated with the specified position in the data set.
     *
     * @param position The position of the item within the adapter's data set
     * @return The data item at the specified position
     */
    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }


    /**
     * Gets the row ID associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set
     * @return The ID of the item at the specified position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * This method creates a custom view for the spinner item at the specified position.
     * If the view is not already inflated, it inflates the layout and initializes the ViewHolder.
     * It then updates the view with the data for the current item and returns the converted view.
     *
     * @param position    The position of the item within the adapter's data set
     * @param convertView The old view to reuse, if possible
     * @param parent      The parent that this view will eventually be attached to
     * @return The updated view representing the spinner item at the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Variable to hold the converted view
        View view = convertView;
        // ViewHolder to hold views of a single spinner item
        ViewHolder holder;

        // If the view is null, inflate the layout and initialize the ViewHolder
        if (view == null) {
            view = inflater.inflate(R.layout.custom_spinner_item, parent, false);
            holder = new ViewHolder();
            holder.itemImage = view.findViewById(R.id.image_view_spinner);
            holder.itemName = view.findViewById(R.id.text_view_spinner);
            view.setTag(holder);
        } else {
            // If view is not null, reuse the ViewHolder and view
            holder = (ViewHolder) view.getTag();
        }

        // Get the current item in the list
        CustomSpinnerItem currentItem = itemList.get(position);
        // Set the image and text for the current item in the spinner
        holder.itemImage.setImageResource(currentItem.getImageResource());
        holder.itemName.setText(currentItem.getItemName());

        // Return the converted view with updated data
        return view;
    }



    /**
     * ViewHolder pattern for efficient view recycling.
     */
    static class ViewHolder {
        ImageView itemImage;
        TextView itemName;
    }
}

