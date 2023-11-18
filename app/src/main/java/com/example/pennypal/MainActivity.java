package com.example.pennypal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pennypal.database.DatabaseHelper;
import com.example.pennypal.database.Expense;
import com.example.pennypal.excel.ExcelExporter;
import com.example.pennypal.pdfexport.PdfExporter;
import com.example.pennypal.recyclerview.MyAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


/**
 *
 * @author abhinav3254
 * The MainActivity class represents the main activity of the PennyPal app.
 * It includes a navigation drawer, bottom navigation view, and fragments for home, add, and profile.
 * Users can navigate between fragments using the bottom navigation view.
 * The navigation drawer allows access to different sections of the app.
 * Action bar items include settings, exporting data as PDF, and truncating the expense table.
 * The class implements BottomNavigationView.OnNavigationItemSelectedListener for handling bottom navigation item clicks.
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;


    private RecyclerView expenseRecyclerView;
    private MyAdapter expenseAdapter;

    // Define fragments
    HomeFragment homeFragment = new HomeFragment();
    AddFragment addFragment = new AddFragment();
    AnalyticsFragment analyticsFragment = new AnalyticsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ActionBarDrawerToggle with the DrawerLayout
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.btmNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        if (item.getItemId() == R.id.action_settings) {
            // Handle the settings action
            return true;
        } else if (item.getItemId() == R.id.export_pdf) {
            // Handle the export PDF action
            exportDataAsPDF();
            return true;
        } else if (item.getItemId() == R.id.export_excel) {
            // Handle the export PDF action
            exportDataAsExcel();
            return true;
        } else if (item.getItemId() == R.id.delete_all) {
            // Handle the export PDF action
            truncateTable();
            return true;
        } else {
            // Add more cases for other menu items if needed
            return super.onOptionsItemSelected(item);
        }
    }


    // Method to export data as Excel
    /**
     * Initiates the process of exporting data from the database to an Excel file.
     * It creates an instance of DatabaseHelper and uses the ExcelExporter class
     * to handle the export operation to Excel format.
     */
    private void exportDataAsExcel() {
        try {
            // Assuming you have an instance of DatabaseHelper
            DatabaseHelper databaseHelper = new DatabaseHelper(this);

            // Call the ExcelExporter class to export data as Excel
            ExcelExporter.exportDataToExcel(this, databaseHelper);
        } catch (Exception e) {
            Log.d("3254excel", "exportDataAsExcel: "+e.getMessage());
            Toast.makeText(this, "Error in exporting data"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Displays a confirmation dialog to prompt the user before truncating the expense table.
     * The dialog includes a title, message, and buttons for positive (Yes) and negative (No) actions.
     * If the user clicks "Yes," the expense table is truncated. If "No" is clicked, no action is taken.
     * This method is called when the user wants to delete all records in the expense table.
     */
    private void truncateTable() {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title and message
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete everything? This action cannot be undone.");

        // Add buttons for positive (Yes) and negative (No) actions
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Yes, proceed with truncating the table
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                databaseHelper.truncateExpenseTable();
                refreshData();
                Toast.makeText(MainActivity.this, "Delete Everything", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked No, do nothing or perform any other actions
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Fetches updated data from the database and updates the adapter.
     * If the adapter is null, initializes it with the updated data.
     * Refreshes the HomeFragment's data.
     *
     * @author @abhinav3254 18 Nov 2023
     */
    public void refreshData() {
        // Fetch data from the database again
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        List<Expense> updatedExpenseList = databaseHelper.getAllExpenses(); // Assuming this method fetches all expenses from the database

        // Update the adapter with the new data
        if (expenseAdapter != null) {
            expenseAdapter.updateData(updatedExpenseList); // You need to implement this method in your adapter to update its dataset
            expenseAdapter.notifyDataSetChanged();
        } else {
            // Reinitialize or initialize expenseAdapter if it's null
            // For example, if expenseAdapter is a RecyclerView adapter:
            expenseAdapter = new MyAdapter(updatedExpenseList); // Create a new instance of your adapter with updated data
            RecyclerView recyclerView = findViewById(R.id.recyclerView); // Replace with your RecyclerView ID
            recyclerView.setAdapter(expenseAdapter); // Set the adapter to the RecyclerView

            // Or if expenseAdapter is a custom adapter, adjust this section accordingly
        }

        // Refresh the HomeFragment
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
        if (homeFragment != null) {
            homeFragment.refreshFragmentData(); // Call the method to refresh the fragment's data
        }
    }



    // Method to export data as PDF
    /**
     * Initiates the process of exporting data from the database to a PDF file.
     * It displays a progress dialog indicating the export process, then starts
     * a background thread to perform the export operation. Upon completion (success
     * or error), it dismisses the progress dialog and displays an appropriate
     * toast message on the main UI thread.
     */
    private void exportDataAsPDF() {
        // Create and display a progress dialog to indicate the export process
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Exporting data as PDF...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Perform export operation in a background thread
        new Thread(() -> {
            try {
                // Create an instance of DatabaseHelper to access the database
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                // Invoke the PdfExporter class to export data to PDF
                PdfExporter.exportDataToPdf(MainActivity.this, databaseHelper);

                // Show a success message on the main UI thread
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Data exported as PDF", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                // Handle any exceptions occurred during the export process
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error exporting data", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle bottom navigation item selection
        if (item.getItemId() == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.add) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, addFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.analytics) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, analyticsFragment).commit();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Override of the onBackPressed method to handle the back button press.
     * Checks if the navigation drawer is open; if so, it closes it.
     * If the drawer is not open, it shows an exit confirmation dialog.
     */
    @Override
    public void onBackPressed() {
        // Check if the navigation drawer is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // If the drawer is open, close it
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // If the drawer is not open, show the exit confirmation dialog
            showExitConfirmationDialog();
        }
    }

    /**
     * Displays an exit confirmation dialog with a message asking the user if they are sure they want to exit.
     * If the user clicks "Yes," the app is exited by calling the finish() method.
     * If the user clicks "No," the dialog is dismissed.
     */
    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Yes, exit the app
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .show();
    }


}
