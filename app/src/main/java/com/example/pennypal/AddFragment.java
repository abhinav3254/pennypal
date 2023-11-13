package com.example.pennypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pennypal.database.DatabaseHelper;
import com.example.pennypal.database.Expense;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



/**
 * The AddFragment class represents the fragment responsible for adding new expenses.
 * It includes UI elements for capturing expense details, such as title, amount, date, etc.
 * The user can interact with spinners and a date picker to input information, and the data
 * is then saved to a SQLite database using the DatabaseHelper class.
 */
public class AddFragment extends Fragment {

    // Database helper instance to manage expenses
    private DatabaseHelper databaseHelper;

    // UI elements
    private TextInputEditText titleEditText, amountEditText, descriptionEditText;
    private Spinner paymentMethodSpinner, categoryMethodSpinner;
    private MaterialTextView showDatePickerTextView;
    private String customSelectedDate;

    /**
     * Called when the fragment should create its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    /**
     * Called after onCreateView() when the view hierarchy has been created.
     * Initializes UI elements, sets up spinners, and configures button click listeners.
     *
     * @param view               The fragment's root view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Initialize UI elements
        initializeViews(view);

        // Setup spinners with default items
        setupSpinners();

        // Setup click listeners for buttons
        setupButtonClickListeners();
    }

    /**
     * Initialize UI elements by finding views in the layout.
     *
     * @param view The fragment's root view.
     */
    private void initializeViews(View view) {
        titleEditText = view.findViewById(R.id.textInputEditText);
        amountEditText = view.findViewById(R.id.amount);
        paymentMethodSpinner = view.findViewById(R.id.paymentMethodSpinner);
        categoryMethodSpinner = view.findViewById(R.id.categoryMethodSpinner);
        descriptionEditText = view.findViewById(R.id.description);
        showDatePickerTextView = view.findViewById(R.id.showDatePickerTextView);
    }

    /**
     * Setup spinners with default items.
     */
    private void setupSpinners() {
        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        paymentMethodSpinner.setAdapter(adapter);
        categoryMethodSpinner.setAdapter(adapter);
    }

    /**
     * Setup click listeners for buttons.
     * Sets click listeners for the date picker button and the save button.
     */
    private void setupButtonClickListeners() {
        MaterialButton showDatePickerButton = requireView().findViewById(R.id.showDatePickerButton);
        MaterialTextView finalShowDatePickerTextView = showDatePickerTextView;

        // Set click listener for the date picker button
        showDatePickerButton.setOnClickListener(view -> customSelectedDate = showDatePicker(finalShowDatePickerTextView));

        // Set click listener for the save button
        requireView().findViewById(R.id.saveButton).setOnClickListener(view -> onSaveButtonClick());
    }

    /**
     * Display a date picker dialog and update the selected date in the TextView.
     *
     * @param materialTextView The TextView to display the selected date.
     * @return The selected date in string format.
     */
    private String showDatePicker(MaterialTextView materialTextView) {
        MaterialDatePicker<Long> builder = MaterialDatePicker.Builder.datePicker().build();

        builder.addOnPositiveButtonClickListener(selection -> {
            materialTextView.setText(formatDate(selection));
            customSelectedDate = formatDate(selection);
        });

        builder.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");

        return customSelectedDate;
    }

    /**
     * Format a date in milliseconds to the "yyyy-MM-dd" format.
     *
     * @param dateInMillis The date in milliseconds.
     * @return The formatted date string.
     */
    private String formatDate(Long dateInMillis) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(dateInMillis));
    }

    /**
     * Handle the click event of the save button.
     * Create an Expense object, set its properties, and insert it into the database.
     */
    private void onSaveButtonClick() {
        // Create an Expense object and set its properties
        Expense expense = new Expense();
        expense.setTitle(titleEditText.getText().toString());
        expense.setAmount(Double.parseDouble(amountEditText.getText().toString()));
        expense.setPaymentMethod(paymentMethodSpinner.getSelectedItem().toString());
        expense.setCategory(categoryMethodSpinner.getSelectedItem().toString());
        expense.setDescription(descriptionEditText.getText().toString());
        expense.setUpdateDate(new Date());

        // Parse the selected date and set it in the Expense object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(customSelectedDate);
            System.out.println("Parsed Date: " + date);
            expense.setDate(date);
        } catch (ParseException e) {
            // If parsing fails, set the current date
            expense.setDate(new Date());
            e.printStackTrace();
        }

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(getContext());

        // Insert the expense into the database and get the result
        long value = databaseHelper.insertExpense(expense);

        // Show a toast message based on the insertion result
        boolean isSuccess = value > 0;
        Toast.makeText(getContext(), isSuccess ? "Added" : "Something went wrong", Toast.LENGTH_SHORT).show();

        // Reset input fields and refresh the current fragment only if the insertion was successful
        resetInputFields(isSuccess);

    }

    /**
     * Reset input fields and refresh the current fragment.
     *
     * @param success Indicates whether the insertion was successful or not.
     */
    private void resetInputFields(boolean success) {
        // Clear input fields
        titleEditText.getText().clear();
        amountEditText.getText().clear();
        paymentMethodSpinner.setSelection(0);
        categoryMethodSpinner.setSelection(0);
        descriptionEditText.getText().clear();
        showDatePickerTextView.setText("");

        // Reset customSelectedDate to null
        customSelectedDate = null;

        // Refresh the fragment only if the insertion was successful
        if (success) {
            FragmentTransaction ft = requireFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }




}
