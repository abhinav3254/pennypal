package com.example.pennypal.utils;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import androidx.fragment.app.DialogFragment;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.pennypal.database.DatabaseHelper;
import com.example.pennypal.database.Expense;
import com.example.pennypal.utils.spinner.CustomSpinnerAdapter;
import com.example.pennypal.utils.spinner.CustomSpinnerItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.example.pennypal.R;


// Import statements...


/**
 * MyDialogFragment represents a dialog fragment responsible for displaying a form
 * to add a new expense. This class extends DialogFragment and includes methods to
 * initialize UI elements, handle user input, and save new expenses to the database.
 * It contains methods to create a new instance of the dialog, set a listener for expense
 * saving events, and handle the dialog lifecycle, such as creating the view and managing
 * user interactions.
 *
 * The class includes functionalities to:
 * - Initialize UI elements and set up necessary adapters.
 * - Handle the creation of the dialog's view and configure user input elements.
 * - Capture user input for adding new expenses, such as title, amount, date, etc.
 * - Save the entered expense details to the database when the save button is clicked.
 * - Display a date picker for selecting the expense date.
 * - Reset input fields after saving an expense or when the dialog is dismissed.
 * - Communicate with the parent fragment or activity using an expense saving listener.
 * - Show a dialog to add new expenses and handle expense saving events.
 *
 * This class serves as a modular dialog component that encapsulates the functionality
 * required for adding a new expense entry within a dialog interface.
 */
public class MyDialogFragment extends DialogFragment {

    // Existing code...

    private DatabaseHelper databaseHelper;

    private OnExpenseSavedListener expenseSavedListener;

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


        List<CustomSpinnerItem> itemList = new ArrayList<>();
        itemList.add(new CustomSpinnerItem(R.drawable.payment_upi, "UPI"));
        itemList.add(new CustomSpinnerItem(R.drawable.payment_phonepay, "PhonePe"));
        itemList.add(new CustomSpinnerItem(R.drawable.payment_paytm, "Paytm"));
        itemList.add(new CustomSpinnerItem(R.drawable.payment_paypal, "PayPal"));
        itemList.add(new CustomSpinnerItem(R.drawable.payment_googlepay, "Google Pay"));
        itemList.add(new CustomSpinnerItem(R.drawable.payment_cred, "CRED"));
        itemList.add(new CustomSpinnerItem(R.drawable.payment_bharatpe, "BharatPe"));
        itemList.add(new CustomSpinnerItem(R.drawable.payment_amazonpay, "Amazon Pay"));
        itemList.add(new CustomSpinnerItem(R.drawable.payment_airtelpaymentsbank, "Airtel Payments Bank"));


//        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), itemList);
//// Add your CustomSpinnerItem objects to itemList...
//
//        CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(getContext(), itemList);
//        Spinner customSpinner = requireView().findViewById(R.id.your_custom_spinner_id);
//        customSpinner.setAdapter(customAdapter);
//
//
//
////        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};
////        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, items);
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        paymentMethodSpinner.setAdapter(adapter);
//        categoryMethodSpinner.setAdapter(adapter);

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), itemList);

        // Set up payment method spinner
        Spinner paymentMethodSpinner = requireView().findViewById(R.id.paymentMethodSpinner);
        paymentMethodSpinner.setAdapter(adapter);

        // Set up category method spinner
        Spinner categoryMethodSpinner = requireView().findViewById(R.id.categoryMethodSpinner);
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
     * Handles the click event of the save button.
     * Creates an Expense object and sets its properties based on user input.
     * Inserts the expense into the database, displays a message, and refreshes the fragment if successful.
     */
    private void onSaveButtonClick() {
        // Create an Expense object and set its properties
        Expense expense = new Expense();

        // Validate fields before setting expense properties
        if (areFieldsValid()) {
            // Set expense properties if fields are valid
            expense.setTitle(titleEditText.getText().toString());
            expense.setAmount(Double.parseDouble(amountEditText.getText().toString()));
            expense.setPaymentMethod(paymentMethodSpinner.getSelectedItem().toString());
            expense.setCategory(categoryMethodSpinner.getSelectedItem().toString());
            expense.setDescription(descriptionEditText.getText().toString());
            expense.setUpdateDate(new Date());
        } else {
            // If fields are not valid, exit the method
            return;
        }

        // Parse the selected date and set it in the Expense object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (customSelectedDate.isEmpty()) {
                // If no date is selected, set the current date
                Date date = dateFormat.parse(new Date().toString());
                expense.setDate(date);
            } else {
                // Parse the selected date and set it in the Expense object
                Date date = dateFormat.parse(customSelectedDate);
                expense.setDate(date);
            }
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

        // If the insertion was successful, dismiss the dialog
        if (isSuccess) {
            dismiss(); // Close the dialog
        }

        // After successful insertion, notify the HomeFragment to refresh if listener is not null
        if (isSuccess && expenseSavedListener != null) {
            expenseSavedListener.onExpenseSaved(); // Notify the listener in HomeFragment
            dismiss(); // Close the dialog
        }
    }


    /**
     * Validates the input fields before creating an Expense object.
     * Checks each field individually for emptiness or selection (in case of spinners).
     * Displays a Snackbar message for each validation error and returns false if any field is invalid.
     *
     * @return True if all fields are valid, otherwise false.
     */
    private boolean areFieldsValid() {
        boolean isValid = true;

        // Validate title field
        if (titleEditText.getText().toString().trim().isEmpty()) {
            isValid = false;
            showSnackbar("Title field can't be empty");
        }

        // Validate amount field
        if (amountEditText.getText().toString().trim().isEmpty()) {
            isValid = false;
            showSnackbar("Amount field can't be empty");
        }

        // Validate payment method spinner
        if (paymentMethodSpinner.getSelectedItem().toString().isEmpty()) {
            isValid = false;
            showSnackbar("Please select a payment method");
        }

        // Validate category method spinner
        if (categoryMethodSpinner.getSelectedItem().toString().isEmpty()) {
            isValid = false;
            showSnackbar("Please select a category");
        }

        // Validate description field
        if (descriptionEditText.getText().toString().trim().isEmpty()) {
            isValid = false;
            showSnackbar("Description field can't be empty");
        }

        return isValid;
    }


    /**
     * Displays a Snackbar message with the provided message string.
     * Shows a Snackbar at the root view of the fragment indicating a validation error.
     *
     * @param message The message to display in the Snackbar.
     */
    private void showSnackbar(String message) {
        View rootView = requireView(); // Change this to your root view
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
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


    /**
     * Creates and returns a new instance of MyDialogFragment.
     * Use this method to obtain a new instance of the dialog fragment.
     *
     * @return A new instance of MyDialogFragment.
     */
    public static MyDialogFragment newInstance() {
        return new MyDialogFragment();
    }


    /**
     * Displays the dialog by adding it to the specified FragmentManager.
     * This method shows the dialog using the given FragmentManager with a specific tag.
     *
     * @param manager The FragmentManager to add the dialog.
     */
    public void showDialog(FragmentManager manager) {
        show(manager, "MyDialog");
    }


    /**
     * Interface definition for a callback to be invoked when an expense is saved.
     * Implement this interface in the calling class to listen for expense save events.
     */
    public interface OnExpenseSavedListener {
        /**
         * Called when an expense is saved.
         * Implement this method to perform actions when an expense is saved.
         */
        void onExpenseSaved();
    }


    /**
     * Sets the expense saved listener for this dialog fragment.
     * Use this method to assign a listener to handle expense save events.
     *
     * @param listener The OnExpenseSavedListener instance to set as the listener.
     */
    public void setOnExpenseSavedListener(OnExpenseSavedListener listener) {
        this.expenseSavedListener = listener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            window.setWindowAnimations(R.style.DialogAnimation);
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setGravity(Gravity.CENTER);
            window.setDimAmount(0.8f);
            window.setBackgroundDrawableResource(android.R.color.background_light);

            // Create WindowManager.LayoutParams object
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            window.setAttributes(layoutParams);
        }
        return dialog;
    }


}



