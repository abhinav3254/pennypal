package com.example.pennypal.utils.piechart;

public class CustomPieModel {

    private String name;
    private int color;
    private double totalAmount;

    public CustomPieModel(String name, int color, double totalAmount) {
        this.name = name;
        this.color = color;
        this.totalAmount = totalAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
