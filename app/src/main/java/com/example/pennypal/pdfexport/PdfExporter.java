package com.example.pennypal.pdfexport;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

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
import java.util.List;

public class PdfExporter {

    private static final int REQUEST_CODE_OPEN_DIRECTORY = 100;

    public static void exportDataToPdf(Context context, DatabaseHelper databaseHelper) {
        // Request access to a directory on external storage if the app does not have it.
        if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // You should request the WRITE_EXTERNAL_STORAGE permission here.
            // For simplicity, you can assume the permission is already granted for now.
        }

        // Specify the file name for the PDF
        String fileName = "database_export.pdf";

        // Create a directory to store the exported PDF file
        File directory;
        if (context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) != null) {
            directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        } else {
            directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        }

        String filePath = directory.getAbsolutePath() + File.separator + fileName;

        try {
            // Create a PdfWriter instance
            PdfWriter writer = new PdfWriter(filePath);

            // Initialize PDF document
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Retrieve column names from the database helper
            List<String> columnNames = databaseHelper.getTableColumns();

            // Create a table with column names as headers
            Table table = new Table(columnNames.size());
            PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

            // Add column names as headers using a for loop
            for (int i = 0; i < columnNames.size(); i++) {
                table.addHeaderCell(new Cell().add(new Paragraph(columnNames.get(i))).setFont(font).setBackgroundColor(new DeviceRgb(140, 221, 8)));
            }

            // Retrieve data from the database helper and add it to the table
            List<List<String>> data = databaseHelper.getTableData();
            for (List<String> row : data) {
                for (String cell : row) {
                    table.addCell(new Cell().add(new Paragraph(cell)));
                }
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            Log.d("PdfExporter", "PDF exported successfully to: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("PdfExporter", "Error exporting PDF: " + e.getMessage());
        }
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == RESULT_OK) {
            // The user has granted access to a directory on external storage.
            Uri directoryUri = intent.getData();
            DatabaseHelper databaseHelper = new DatabaseHelper(context);

            // Export the database to the selected directory.
            exportDataToPdf(context, databaseHelper);
        }
    }
}
