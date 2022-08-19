package com.ezyedu.vendor.model;

public class VendorCategoryDescription
{
    int id;
    String tittle;
    String content;

    public VendorCategoryDescription(int id, String tittle, String content) {
        this.id = id;
        this.tittle = tittle;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getContent() {
        return content;
    }
}
