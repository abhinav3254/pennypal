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

public class CustomSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<CustomSpinnerItem> itemList;
    private LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context, List<CustomSpinnerItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.custom_spinner_item, parent, false);
            holder = new ViewHolder();
            holder.itemImage = view.findViewById(R.id.image_view_spinner);
            holder.itemName = view.findViewById(R.id.text_view_spinner);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        CustomSpinnerItem currentItem = itemList.get(position);
        holder.itemImage.setImageResource(currentItem.getImageResource());
        holder.itemName.setText(currentItem.getItemName());

        return view;
    }

    static class ViewHolder {
        ImageView itemImage;
        TextView itemName;
    }
}

