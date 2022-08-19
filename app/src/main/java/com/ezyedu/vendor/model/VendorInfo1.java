package com.ezyedu.vendor.model;

public class VendorInfo1
{
    String tittle;
    String description;

    public VendorInfo1(String tittle, String description) {
        this.tittle = tittle;
        this.description = description;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDescription() {
        return description;
    }
}
