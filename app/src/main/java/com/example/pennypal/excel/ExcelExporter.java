package com.example.pennypal.excel;

import android.content.Context;
import android.os.Environment;
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

public class ExcelExporter {

    public static void exportDataToExcel(Context context, DatabaseHelper databaseHelper) {
        // Create a new Excel workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Create a sheet in the workbook
        XSSFSheet sheet = workbook.createSheet("Expense Data");

        // Get the list of expenses from the database
        List<Expense> expenseList = databaseHelper.getAllExpenses();

        // Add header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Title");
        headerRow.createCell(1).setCellValue("Amount");
        headerRow.createCell(2).setCellValue("Category");
        headerRow.createCell(3).setCellValue("Payment Method");
        headerRow.createCell(4).setCellValue("Description");
        headerRow.createCell(5).setCellValue("Date");
        headerRow.createCell(6).setCellValue("Update Date");

        // Add data rows
        for (int i = 0; i < expenseList.size(); i++) {
            Expense expense = expenseList.get(i);
            Row dataRow = sheet.createRow(i + 1);
            dataRow.createCell(0).setCellValue(expense.getTitle());
            dataRow.createCell(1).setCellValue(expense.getAmount());
            dataRow.createCell(2).setCellValue(expense.getCategory());
            dataRow.createCell(3).setCellValue(expense.getPaymentMethod());
            dataRow.createCell(4).setCellValue(expense.getDescription());
            dataRow.createCell(5).setCellValue(expense.getDate().toString());
            dataRow.createCell(6).setCellValue(expense.getUpdateDate().toString());
        }

        // Save the workbook to a file
        try {
            File file = createFile(context, "abhinav_excel.xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(context, "Excel file exported successfully to: " + file.getPath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("ExcelExporter", "Error exporting Excel file: " + e.getMessage());
            Toast.makeText(context, "Error exporting Excel file", Toast.LENGTH_SHORT).show();
        }
    }

    private static File createFile(Context context, String fileName) throws IOException {
        File directory = getDownloadDirectory(context, "PennyPal");
        return new File(directory, fileName);
    }

    private static File getDownloadDirectory(Context context, String subDirectory) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return directory;
    }
}
