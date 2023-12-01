package com.example.pennypal.utils.piechart;

/**
 *
 * @author abhinavkumar
 * Represents a model for a segment in a custom pie chart.
 * Contains information about the segment name, color, and total amount.
 */
public class CustomPieModel {
    // Fields to store segment information
    private String name; // Name of the segment
    private int color;   // Color associated with the segment
    private double totalAmount; // Total amount represented by the segment

    /**
     * Constructor to initialize the CustomPieModel.
     * @param name        The name of the segment.
     * @param color       The color associated with the segment.
     * @param totalAmount The total amount represented by the segment.
     */
    public CustomPieModel(String name, int color, double totalAmount) {
        this.name = name;
        this.color = color;
        this.totalAmount = totalAmount;
    }

    /**
     * Getter method for retrieving the name of the segment.
     * @return The name of the segment.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method to set the name of the segment.
     * @param name The name of the segment.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for retrieving the color associated with the segment.
     * @return The color associated with the segment.
     */
    public int getColor() {
        return color;
    }

    /**
     * Setter method to set the color associated with the segment.
     * @param color The color associated with the segment.
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Getter method for retrieving the total amount represented by the segment.
     * @return The total amount represented by the segment.
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Setter method to set the total amount represented by the segment.
     * @param totalAmount The total amount represented by the segment.
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
