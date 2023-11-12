package com.example.pennypal.database;

import java.util.Date;

public class Expense {

    private Integer id;
    private String title;
    private Double amount;
    private String category;
    private String paymentMethod;
    private String description;
    private Date date;
    private Date updateDate;


    public Expense() {
    }

    public Expense(Integer id, String title, Double amount, String category, String paymentMethod, String description, Date date, Date updateDate) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.date = date;
        this.updateDate = updateDate;
    }

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
