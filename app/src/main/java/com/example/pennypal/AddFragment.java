package com.example.pennypal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pennypal.database.DatabaseHelper;
import com.example.pennypal.database.Expense;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddFragment extends Fragment {

    private DatabaseHelper databaseHelper;

    private TextInputEditText titleEditText;
    private TextInputEditText amountEditText;
    private Spinner paymentMethodSpinner;
    private Spinner categoryMethodSpinner;
    private TextInputEditText descriptionEditText;
    private MaterialTextView showDatePickerTextView;

    private String customSelectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        final MaterialButton showDatePickerButton = view.findViewById(R.id.showDatePickerButton);
        MaterialTextView showDatePickerTextView = view.findViewById(R.id.showDatePickerTextView);

        TextInputLayout titleLayout = view.findViewById(R.id.title);
        titleEditText = titleLayout.findViewById(R.id.textInputEditText);

        amountEditText = view.findViewById(R.id.amount);
        paymentMethodSpinner = view.findViewById(R.id.paymentMethodSpinner);
        categoryMethodSpinner = view.findViewById(R.id.categoryMethodSpinner);
        descriptionEditText = view.findViewById(R.id.description);
        showDatePickerTextView = view.findViewById(R.id.showDatePickerTextView);

        MaterialButton saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick();
            }
        });

        MaterialTextView finalShowDatePickerTextView = showDatePickerTextView;
        showDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customSelectedDate = showDatePicker(finalShowDatePickerTextView);
            }
        });

        setupSpinners();

        return view;
    }

    private void setupSpinners() {
        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        paymentMethodSpinner.setAdapter(adapter);
        categoryMethodSpinner.setAdapter(adapter);
    }

    private String showDatePicker(MaterialTextView materialTextView) {
        MaterialDatePicker<Long> builder = MaterialDatePicker.Builder.datePicker().build();

        builder.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                String formattedDate = formatDate(selection);
                materialTextView.setText(formattedDate);
                customSelectedDate = formattedDate;
            }
        });

        builder.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");

        return customSelectedDate; // Return the selected date
    }

    private String formatDate(Long dateInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(dateInMillis));
    }

    private void onSaveButtonClick() {
        String title = titleEditText.getText().toString();
        String amount = amountEditText.getText().toString();
        String paymentMethod = paymentMethodSpinner.getSelectedItem().toString();
        String categoryMethod = categoryMethodSpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString();
//        String selectedDate = showDatePickerTextView.getText().toString();
        String selectedDate = customSelectedDate;


        Expense expense = new Expense();
        expense.setTitle(title);
        expense.setAmount(Double.parseDouble(amount));
        expense.setPaymentMethod(paymentMethod);
        expense.setCategory(categoryMethod);
        expense.setDescription(description);
        expense.setUpdateDate(new Date());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = dateFormat.parse(selectedDate);
            System.out.println("Parsed Date: " + date);
            expense.setDate(date);
        } catch (ParseException e) {
            expense.setDate(new Date());
            e.printStackTrace();
        }

        databaseHelper = new DatabaseHelper(getContext());

        Log.d("3254", "onSaveButtonClick: "+expense.toString());

        long value =  databaseHelper.insertExpense(expense);

        if (value>0) {
            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }


    }
}
