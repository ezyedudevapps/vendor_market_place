package com.ezyedu.vendor.model;

public class Courses
{
    String tittle;
    String date;
    String logo;
    String hash_id;

    public Courses(String tittle, String date, String logo, String hash_id) {
        this.tittle = tittle;
        this.date = date;
        this.logo = logo;
        this.hash_id = hash_id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDate() {
        return date;
    }

    public String getLogo() {
        return logo;
    }

    public String getHash_id() {
        return hash_id;
    }
}
