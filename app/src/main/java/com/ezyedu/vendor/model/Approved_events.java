package com.ezyedu.vendor.model;

public class Approved_events
{
    String tittle;
    String image;
    String sd;
    String ed;

    public Approved_events(String tittle, String image, String sd, String ed) {
        this.tittle = tittle;
        this.image = image;
        this.sd = sd;
        this.ed = ed;
    }

    public String getTittle() {
        return tittle;
    }

    public String getImage() {
        return image;
    }

    public String getSd() {
        return sd;
    }

    public String getEd() {
        return ed;
    }
}
