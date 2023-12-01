package com.example.pennypal.pdfexport;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.pennypal.database.DatabaseHelper;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author abhinavkumar 
 * The PdfExporter class provides methods for exporting data from a DatabaseHelper to a PDF file.
 */
public class PdfExporter {

    private static final int REQUEST_CODE_OPEN_DIRECTORY = 100;
    private static final String DATE_FORMAT_PATTERN = "yyyy:MM:dd HH:mm:ss";

    /**
     * Exports data from the provided DatabaseHelper to a PDF file in the Downloads directory.
     *
     * @param context         The context of the application.
     * @param databaseHelper  The DatabaseHelper containing the data to be exported.
     */
    public static void exportDataToPdf(Context context, DatabaseHelper databaseHelper) {
        if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // You should request the WRITE_EXTERNAL_STORAGE permission here.
            // For simplicity, you can assume the permission is already granted for now.
        }

        String fileName = "abhinav_expense.pdf";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        String filePath = directory.getAbsolutePath() + File.separator + fileName;

        // Create a Handler tied to the main Looper
        Handler handler = new Handler(Looper.getMainLooper());

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            List<String> columnNames = databaseHelper.getTableColumns();
            Table table = new Table(columnNames.size());
            PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

            for (int i = 0; i < columnNames.size(); i++) {
                table.addHeaderCell(new Cell().add(new Paragraph(columnNames.get(i))).setFont(font).setBackgroundColor(new DeviceRgb(140, 221, 8)));
            }

            List<List<String>> data = databaseHelper.getTableData();
            for (List<String> row : data) {
                for (int i = 0; i < row.size(); i++) {
                    String cell = row.get(i);

                    // Check if the column is a date and format accordingly
                    if (isDateColumn(columnNames.get(i)) && isDate(cell)) {
                        cell = formatDate(cell);
                    }

                    table.addCell(new Cell().add(new Paragraph(cell)));
                }
            }

            document.add(table);
            document.close();

            final String successMessage = "PDF exported successfully to: " + filePath;
            handler.post(() -> Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show());
            Log.d("PdfExporter", "PDF exported successfully to: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
            final String errorMessage = "Error exporting PDF: " + e.getMessage();
            handler.post(() -> Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show());
            Log.e("PdfExporter", errorMessage);
        }
    }


    /**
     * Handles the result of the file directory selection.
     *
     * @param context      The context of the application.
     * @param requestCode  The request code of the file directory selection.
     * @param resultCode   The result code of the file directory selection.
     * @param intent       The intent containing the selected directory URI.
     */
    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == RESULT_OK) {
            Uri directoryUri = intent.getData();
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            exportDataToPdf(context, databaseHelper);
        }
    }

    /**
     * Checks if the given value represents a date.
     *
     * @param value The value to be checked.
     * @return True if the value represents a date, false otherwise.
     */
    private static boolean isDate(String value) {
        // TODO: Implement your date detection logic
        // For simplicity, assuming all strings are dates
        return true;
    }

    /**
     * Formats the given date string according to the specified date format pattern.
     *
     * @param dateStr The date string to be formatted.
     * @return The formatted date string.
     */
    private static String formatDate(String dateStr) {
        try {
            long timestamp = Long.parseLong(dateStr);
            Date date = new Date(timestamp);

            SimpleDateFormat outputFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr; // Return the original string if an error occurs
        }
    }

    /**
     * Checks if the given column name represents a date column.
     *
     * @param columnName The column name to be checked.
     * @return True if the column represents a date column, false otherwise.
     */
    private static boolean isDateColumn(String columnName) {
        // Specify the columns that should be formatted as dates
        return columnName.equals("_date") || columnName.equals("_update_date");
    }
}
