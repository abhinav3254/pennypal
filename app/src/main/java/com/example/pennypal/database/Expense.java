package com.example.pennypal.database;

import java.util.Date;

/**
 * The Expense class represents a financial expense with details such as title, amount, category, etc.
 */
public class Expense {

    private Integer id;               // Unique identifier for the expense
    private String title;             // Title or description of the expense
    private Double amount;            // Amount spent for the expense
    private String category;          // Category or type of the expense
    private String paymentMethod;     // Payment method used for the expense
    private String description;       // Additional description of the expense
    private Date date;                // Date when the expense occurred
    private Date updateDate;          // Date when the expense was last updated

    /**
     * Default constructor for the Expense class.
     */
    public Expense() {
    }

    /**
     * Parameterized constructor for the Expense class.
     *
     * @param id          Unique identifier for the expense.
     * @param title       Title or description of the expense.
     * @param amount      Amount spent for the expense.
     * @param category    Category or type of the expense.
     * @param paymentMethod Payment method used for the expense.
     * @param description Additional description of the expense.
     * @param date        Date when the expense occurred.
     * @param updateDate  Date when the expense was last updated.
     */
    public Expense(Integer id, String title, Double amount, String category,
                   String paymentMethod, String description, Date date, Date updateDate) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.date = date;
        this.updateDate = updateDate;
    }

    /**
     * Returns a string representation of the Expense object.
     *
     * @return String representation of the Expense object.
     */
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", updateDate=" + updateDate +
                '}';
    }

    // Getters and setters for private fields

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
