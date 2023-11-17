package com.example.pennypal;



import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.pennypal.database.CategoryTotal;
import com.example.pennypal.database.DatabaseHelper;
import com.example.pennypal.utils.piechart.CustomPieModel;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AnalyticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);

        // Call the method to set up and populate the PieChart
        setupPieChart(view);

        // Get the parent layout where you want to add category views
        LinearLayout parentLayout = view.findViewById(R.id.parentLayout); // Replace with your actual parent layout ID

        // Assuming categoryTotals is the list you got from the database
        List<CategoryTotal> categoryTotals = getCategoryTotalsFromDatabase();

        // Call the method to create category views and add them to the parent layout
        createCategoryViews(categoryTotals, parentLayout);

        return view;
    }


    private void setupPieChart(View view) {
        PieChart pieChart = view.findViewById(R.id.piechart);

        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());

        // Retrieve expenses grouped by category from the database
        List<CategoryTotal> categoryTotals = databaseHelper.getExpensesGroupedByCategory();

        if (categoryTotals != null && !categoryTotals.isEmpty()) {
            // Clear any existing slices in the PieChart
            pieChart.clearChart();

            List<Integer> colors = Arrays.asList(
                    R.color.yellow, R.color.pink, R.color.blue, R.color.red, R.color.orange
            );

            List<CustomPieModel> customPieModelList = new ArrayList<>();

            List<String> categoryNameList = new ArrayList<>();

            int count = 0;
            // Iterate through the CategoryTotal objects and add slices to the PieChart
            for (CategoryTotal categoryTotal : categoryTotals) {
                if (count > 4) {
                    break;
                }

                String categoryName = categoryTotal.getCategory();
                categoryNameList.add(categoryName);
                double totalAmount = categoryTotal.getTotalAmount();

                int color = ContextCompat.getColor(view.getContext(), colors.get(count));
                customPieModelList.add(new CustomPieModel(categoryName, color, totalAmount));
                count++;
            }

            // Add pie slices to the PieChart using the customPieModelList
            for (CustomPieModel customPieModel : customPieModelList) {
                pieChart.addPieSlice(new PieModel(
                        customPieModel.getName(),
                        (float) customPieModel.getTotalAmount(),
                        customPieModel.getColor())
                );
            }

            setUpNameOfCategory(categoryNameList,view);

            // Start animation for the PieChart
            pieChart.startAnimation();
        }
    }

    public void setUpNameOfCategory(List<String> list, View view) {
        TextView textView1 = view.findViewById(R.id.chartTextBox1);
        TextView textView2 = view.findViewById(R.id.chartTextBox2);
        TextView textView3 = view.findViewById(R.id.chartTextBox3);
        TextView textView4 = view.findViewById(R.id.chartTextBox4);
        TextView textView5 = view.findViewById(R.id.chartTextBox5);

        View view1 = view.findViewById(R.id.chartView1);
        View view2 = view.findViewById(R.id.chartView2);
        View view3 = view.findViewById(R.id.chartView3);
        View view4 = view.findViewById(R.id.chartView4);
        View view5 = view.findViewById(R.id.chartView5);

        if (list.size() >= 1) {
            textView1.setText(list.get(0));
            textView1.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
        } else {
            textView1.setVisibility(View.INVISIBLE);
            view1.setVisibility(View.INVISIBLE);
        }

        if (list.size() >= 2) {
            textView2.setText(list.get(1));
            textView2.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        } else {
            textView2.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);
        }

        if (list.size() >= 3) {
            textView3.setText(list.get(2));
            textView3.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
        } else {
            textView3.setVisibility(View.INVISIBLE);
            view3.setVisibility(View.INVISIBLE);
        }

        if (list.size() >= 4) {
            textView4.setText(list.get(3));
            textView4.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
        } else {
            textView4.setVisibility(View.INVISIBLE);
            view4.setVisibility(View.INVISIBLE);
        }

        if (list.size() >= 5) {
            textView5.setText(list.get(4));
            textView5.setVisibility(View.VISIBLE);
            view5.setVisibility(View.VISIBLE);
        } else {
            textView5.setVisibility(View.INVISIBLE);
            view5.setVisibility(View.INVISIBLE);
        }
    }



    private int getRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private List<CategoryTotal> getCategoryTotalsFromDatabase() {
        // Implement database query logic to retrieve category totals
        // Replace this with your actual database fetching mechanism
        return null;
    }

    private void createCategoryViews(List<CategoryTotal> categoryTotals, LinearLayout parentLayout) {
        if (categoryTotals != null) {
            for (CategoryTotal categoryTotal : categoryTotals) {
                LinearLayout newLayout = new LinearLayout(requireContext());
                newLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                newLayout.setOrientation(LinearLayout.HORIZONTAL);
                newLayout.setGravity(Gravity.CENTER_VERTICAL);
                newLayout.setPadding(2, 2, 2, 2);
                newLayout.setId(View.generateViewId()); // Generate a unique ID for each LinearLayout

                View colorView = new View(requireContext());
                colorView.setLayoutParams(new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.color_view_width),
                        getResources().getDimensionPixelSize(R.dimen.color_view_height)
                ));
                colorView.setBackgroundColor(getRandomColor());

                TextView textView = new TextView(requireContext());
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));
                textView.setPadding(getResources().getDimensionPixelSize(R.dimen.text_view_padding),
                        getResources().getDimensionPixelSize(R.dimen.text_view_padding),
                        getResources().getDimensionPixelSize(R.dimen.text_view_padding),
                        getResources().getDimensionPixelSize(R.dimen.text_view_padding));
                textView.setText(categoryTotal.getCategory()); // Replace with your category name getter

                newLayout.addView(colorView);
                newLayout.addView(textView);

                parentLayout.addView(newLayout);
            }
        }
    }
}
