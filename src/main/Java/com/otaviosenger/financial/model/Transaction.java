package com.otaviosenger.financial.model;

import java.math.BigDecimal;

import java.sql.Date;

public class Transaction {
    private int id;
    private String description;
    private BigDecimal amount;
    private Date date;
    private Category category;

    //constructors
    public Transaction(int id, String description, BigDecimal amount, Date date, Category category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public Transaction(String description, BigDecimal amount, Date date, Category category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public Transaction() {
    }
    //getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", category=" + category +
                '}';
    }
}
