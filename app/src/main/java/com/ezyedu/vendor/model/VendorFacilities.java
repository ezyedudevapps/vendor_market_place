package com.ezyedu.vendor.model;

public class VendorFacilities
{
    String tittle;
    String image;

    public VendorFacilities(String tittle, String image) {
        this.tittle = tittle;
        this.image = image;
    }

    public String getTittle() {
        return tittle;
    }

    public String getImage() {
        return image;
    }
}
