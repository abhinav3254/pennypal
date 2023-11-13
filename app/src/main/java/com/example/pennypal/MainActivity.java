package com.example.pennypal;

import android.content.DialogInterface;
import android.os.Bundle;
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

import com.example.pennypal.database.DatabaseHelper;
import com.example.pennypal.excel.ExcelExporter;
import com.example.pennypal.pdfexport.PdfExporter;
import com.google.android.material.bottomnavigation.BottomNavigationView;



/**
 *
 * @author abhinavkumar
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
    private void exportDataAsExcel() {
        // Assuming you have an instance of DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Call the ExcelExporter class to export data as Excel
        ExcelExporter.exportDataToExcel(this, databaseHelper);
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



    // Method to export data as PDF
    private void exportDataAsPDF() {
        // Assuming you have an instance of DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Call the PdfExporter class to export data as PDF
        PdfExporter.exportDataToPdf(this, databaseHelper);
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

    @Override
    public void onBackPressed() {
        // Close the drawer on back press if it's open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
