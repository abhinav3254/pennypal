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


/**
 *
 * @author abhinavkumar
 * A Fragment class representing the analytics view.
 *
 * This Fragment is responsible for displaying analytics data in various forms,
 * such as a PieChart illustrating expenses by category and a list of category views.
 * It utilizes a PieChart to visualize expense data and dynamically generates
 * category views based on the retrieved data from the database.
 *
 * The onCreateView method sets up the layout and initializes the PieChart.
 * It retrieves expense data grouped by category and populates the PieChart accordingly.
 * Additionally, it creates category views based on the retrieved data and adds them to
 * the parent layout for display purposes.
 *
 * Supporting methods like 'setupPieChart' handle the logic of preparing data for
 * visualization on the PieChart, while 'createCategoryViews' dynamically generates
 * category views based on the retrieved category totals from the database.
 *
 * This Fragment assumes the presence of specific layout elements, such as a PieChart
 * with the ID R.id.piechart and a parent layout (LinearLayout) with the ID R.id.parentLayout
 * to display category views.
 *
 * Developers using this Fragment should ensure that the necessary layout elements
 * and associated IDs are available for proper functionality.
 */
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



    /**
     * Sets up and populates the provided PieChart view with data from the database.
     *
     * This method initializes the PieChart view and retrieves expense data grouped by category from the database.
     * If data is available, it proceeds to clear any existing slices in the PieChart and prepares lists
     * for storing colors, custom pie models, and category names. It iterates through the retrieved
     * CategoryTotal objects, adding slices to the PieChart based on the category data.
     * It limits the number of slices to a maximum of five.
     *
     * The PieChart slices are represented by custom pie models, each having a category name, color, and total amount.
     * Colors for the slices are fetched from predefined resources. If fewer than five categories are available,
     * it iterates through the available categories, adding them as slices to the PieChart.
     *
     * Additionally, it sets up the names of categories in TextViews and controls their visibility based on the
     * number of categories retrieved. Finally, it triggers the start of animation for the PieChart.
     *
     * @param view The view containing the PieChart and associated elements to be populated.
     *             This method assumes the presence of a PieChart view with ID R.id.piechart and related TextViews
     *             and Views (as used by setUpNameOfCategory method) within the provided view.
     */
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


    /**
     * Sets up the names of categories in TextViews and their corresponding visibility based on the provided list of names.
     *
     * This method populates TextViews with category names retrieved from the provided list.
     * It ensures the visibility of TextViews and associated Views (possibly separators or indicators)
     * based on the number of items in the list. The method receives a list of category names and a parent View.
     * It assumes the presence of five TextViews (textView1 to textView5) and corresponding Views
     * (view1 to view5) in the provided parent View.
     *
     * The method iterates through the provided list and assigns each category name to the corresponding TextView.
     * It sets the visibility of TextViews and Views based on the number of items in the list:
     *   - For each category name in the list, if the index is within the range of available TextViews,
     *     it sets the text of the respective TextView to the category name and sets their visibility to VISIBLE.
     *     It also sets the corresponding View's visibility to VISIBLE as an indicator or separator.
     *   - If the index exceeds the available TextViews, it sets the visibility of additional TextViews and Views to INVISIBLE.
     *
     * @param list A list of category names to be displayed in TextViews.
     * @param view The parent View containing the TextViews and Views for category names and their indicators.
     *             This view should contain the TextViews (textView1 to textView5) and Views (view1 to view5).
     */
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



    /**
     * Generates a random color and returns it as an integer value.
     *
     * This method generates a random color by using the RGB (Red, Green, Blue) format
     * and constructs an integer representation of that color using the Color.argb method.
     * It creates a new Random object to generate random values for each color component.
     *
     * The Color.argb method requires the alpha (transparency) value and the RGB values.
     * It generates a fully opaque color by setting the alpha to 255 (fully opaque).
     * The RGB values are randomly generated for each color component (Red, Green, Blue)
     * using random.nextInt(256), which returns a random number from 0 to 255 inclusive.
     *
     * @return An integer representation of a randomly generated color using the ARGB format.
     *         The alpha value is fully opaque (255), and the RGB components are randomly generated.
     */
    private int getRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }


    /**
     * This method is a placeholder to fetch category totals from a database.
     * It currently returns null and serves as a template for implementing
     * the database query logic to retrieve category totals.
     *
     * To implement this method:
     * - Utilize your actual database helper class to execute a query.
     * - Fetch category totals from your database.
     * - Create a list of CategoryTotal objects with category names and their respective totals.
     *
     * Example implementation steps:
     * - Initialize your DatabaseHelper class.
     * - Execute a query to retrieve category totals from the database.
     * - Iterate through the results and construct CategoryTotal objects.
     * - Populate a list with these CategoryTotal objects and return it.
     *
     * Ensure to replace this placeholder with your actual database fetching mechanism
     * and modify the return type to return a list of CategoryTotal objects
     * representing the totals for each category in your application.
     *
     * @return A list of CategoryTotal objects representing category totals fetched from the database.
     */
    private List<CategoryTotal> getCategoryTotalsFromDatabase() {
        // Implement database query logic to retrieve category totals
        // Replace this with your actual database fetching mechanism
        return null;
    }



    /**
     * Creates and adds dynamic category views to the parent layout based on the provided category totals.
     *
     * @param categoryTotals List of CategoryTotal objects representing expenses categorized by type.
     * @param parentLayout   Parent LinearLayout where the category views will be added dynamically.
     *                       It's expected that this layout exists in the current UI.
     */
    private void createCategoryViews(List<CategoryTotal> categoryTotals, LinearLayout parentLayout) {
        if (categoryTotals != null) {
            // Iterate through the list of CategoryTotal objects
            for (CategoryTotal categoryTotal : categoryTotals) {
                // Create a new LinearLayout to represent a category view
                LinearLayout newLayout = new LinearLayout(requireContext());
                newLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                newLayout.setOrientation(LinearLayout.HORIZONTAL);
                newLayout.setGravity(Gravity.CENTER_VERTICAL);
                newLayout.setPadding(2, 2, 2, 2);
                newLayout.setId(View.generateViewId()); // Generate a unique ID for each LinearLayout

                // Create a View to display a colored box representing the category
                View colorView = new View(requireContext());
                colorView.setLayoutParams(new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.color_view_width),
                        getResources().getDimensionPixelSize(R.dimen.color_view_height)
                ));
                colorView.setBackgroundColor(getRandomColor());

                // Create a TextView to display the category name
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

                // Add the color view and text view to the new layout
                newLayout.addView(colorView);
                newLayout.addView(textView);

                // Add the new category view to the parent layout
                parentLayout.addView(newLayout);
            }
        }
    }

}
