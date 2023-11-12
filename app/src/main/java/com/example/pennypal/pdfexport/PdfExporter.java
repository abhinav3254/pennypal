package com.example.pennypal.pdfexport;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
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

// ... (previous imports)

public class PdfExporter {

    private static final int REQUEST_CODE_OPEN_DIRECTORY = 100;
    private static final String DATE_FORMAT_PATTERN = "yyyy:MM:dd HH:mm:ss";

    public static void exportDataToPdf(Context context, DatabaseHelper databaseHelper) {
        if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // You should request the WRITE_EXTERNAL_STORAGE permission here.
            // For simplicity, you can assume the permission is already granted for now.
        }

        String fileName = "abhinav_expense.pdf";
        File directory;
        if (context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) != null) {
            directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        }

        String filePath = directory.getAbsolutePath() + File.separator + fileName;

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

            Log.d("PdfExporter", "PDF exported successfully to: " + filePath);
            Toast.makeText(context, "PDF exported successfully to: " + filePath, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error exporting PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("PdfExporter", "Error exporting PDF: " + e.getMessage());
        }
    }

    private static boolean isDate(String value) {
        // TODO: Implement your date detection logic
        // For simplicity, assuming all strings are dates
        return true;
    }

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

    private static boolean isDateColumn(String columnName) {
        // Specify the columns that should be formatted as dates
        return columnName.equals("_date") || columnName.equals("_update_date");
    }
}
