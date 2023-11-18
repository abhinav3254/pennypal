package com.example.pennypal.excel;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.pennypal.database.DatabaseHelper;
import com.example.pennypal.database.Expense;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * The ExcelExporter class facilitates exporting data to Excel format.
 * It provides methods to generate Excel files from various data sources.
 *
 *
 * @author abhinavkumar
 */
public class ExcelExporter {

    /**
     * Exports expense data to an Excel file.
     *
     * @param context         The context of the calling activity or application.
     * @param databaseHelper  An instance of DatabaseHelper to fetch expense data.
     */
    public static void exportDataToExcel(Context context, DatabaseHelper databaseHelper) {
        // Create a progress dialog to indicate the export process
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Exporting data as Excel...");
        progressDialog.setCancelable(false); // Prevent dismissing by tapping outside
        progressDialog.show();

        // Perform export operation in a background thread
        new Thread(() -> {
            try {
                // Create a new Excel workbook and sheet
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Expense Data");

                // Fetch expense data from the database
                List<Expense> expenseList = databaseHelper.getAllExpenses();

                // Populate the Excel sheet with expense data (header and rows)

                // Create a file for the exported Excel data
                File file = createFile(context, "abhinav_excel.xlsx");
                FileOutputStream fileOut = new FileOutputStream(file);
                workbook.write(fileOut);
                fileOut.close();

                // Dismiss the progress dialog and display a success message on the UI thread
                progressDialog.dismiss();
                String successMessage = "Excel file exported successfully to: " + file.getPath();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show());

            } catch (IOException e) {
                e.printStackTrace();
                // Dismiss the progress dialog and display an error message on the UI thread
                progressDialog.dismiss();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> Toast.makeText(context, "Error exporting Excel file", Toast.LENGTH_SHORT).show());
                Log.e("ExcelExporter", "Error exporting Excel file: " + e.getMessage());
            }
        }).start();
    }



    /**
     * Creates a file with the given fileName in the specified directory.
     *
     * @param context   The context of the calling activity or application.
     * @param fileName  The name of the file to be created.
     * @return A File object representing the created file.
     * @throws IOException If an I/O error occurs while creating the file.
     */
    private static File createFile(Context context, String fileName) throws IOException {
        // Get the download directory for the application
        File directory = getDownloadDirectory(context, "PennyPal");

        // Create a File object with the specified fileName in the obtained directory
        return new File(directory, fileName);
    }


    /**
     * Retrieves the download directory for storing files.
     *
     * @param context       The context of the calling activity or application.
     * @param subDirectory  The name of the subdirectory within the download directory (optional).
     * @return A File object representing the download directory or a subdirectory within it.
     */
    private static File getDownloadDirectory(Context context, String subDirectory) {
        // Get the external storage's public download directory
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // If a subdirectory name is provided, create a File object representing that subdirectory
        // Note: If not needed, 'subDirectory' parameter can be removed from the method signature
        if (subDirectory != null && !subDirectory.isEmpty()) {
            directory = new File(directory, subDirectory);
        }

        return directory;
    }

}
