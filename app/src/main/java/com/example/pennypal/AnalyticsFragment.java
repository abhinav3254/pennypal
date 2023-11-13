package com.example.pennypal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class AnalyticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);

        // Call the method to set up and populate the PieChart
        setupPieChart(view);

        return view;
    }

    private void setupPieChart(View view) {
        PieChart pieChart = view.findViewById(R.id.piechart);

        // Add data to the PieChart
        pieChart.addPieSlice(new PieModel("Category 1", 30, getResources().getColor(R.color.one)));
        pieChart.addPieSlice(new PieModel("Category 2", 45, getResources().getColor(R.color.two)));
        pieChart.addPieSlice(new PieModel("Category 3", 25, getResources().getColor(R.color.three)));

        pieChart.startAnimation();
    }
}
