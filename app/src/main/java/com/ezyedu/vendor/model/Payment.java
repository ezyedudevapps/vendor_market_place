package com.ezyedu.vendor.model;

public class Payment
{
    String Order_id;
    Double amount;
    String date;

    public Payment(String order_id, Double amount, String date) {
        Order_id = order_id;
        this.amount = amount;
        this.date = date;
    }

    public String getOrder_id() {
        return Order_id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
