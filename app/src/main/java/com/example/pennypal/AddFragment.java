package com.example.pennypal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        final MaterialButton showDatePickerButton = view.findViewById(R.id.showDatePickerButton);
        final MaterialTextView showDatePickerTextView = view.findViewById(R.id.showDatePickerTextView);

                showDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(showDatePickerTextView);
            }
        });

        return view;
    }


    //    for date picker
    private void showDatePicker(MaterialTextView materialTextView) {
        MaterialDatePicker<Long> builder = MaterialDatePicker.Builder.datePicker().build();

        builder.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Convert the selected date in milliseconds to a formatted string if needed
                String selectedDate = formatDate(selection);

                // Set the selected date to the MaterialTextView
                materialTextView.setText(selectedDate);
            }
        });

        builder.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
    }

    // Helper method to format the date if needed
    private String formatDate(Long dateInMillis) {
        // You can use SimpleDateFormat or any other method to format the date
        // For example:
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
         return sdf.format(new Date(dateInMillis));
//        return String.valueOf(dateInMillis);
    }

}