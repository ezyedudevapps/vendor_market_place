package com.ezyedu.vendor.model;

public class Order_Info
{
    String name;
    String image;
    Double price;
    String description;
    int qty;

    public Order_Info(String name, String image, Double price, String description, int qty) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getQty() {
        return qty;
    }
}
