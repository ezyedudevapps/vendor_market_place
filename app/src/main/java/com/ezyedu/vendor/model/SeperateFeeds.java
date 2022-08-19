package com.ezyedu.vendor.model;

public class SeperateFeeds
{
    String image;
    String tittle;
    String label;
    String description;
    String ven_logo;
    String address;
    String name;

    public SeperateFeeds(String image, String tittle, String label, String description, String ven_logo, String address, String name) {
        this.image = image;
        this.tittle = tittle;
        this.label = label;
        this.description = description;
        this.ven_logo = ven_logo;
        this.address = address;
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public String getTittle() {
        return tittle;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getVen_logo() {
        return ven_logo;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
