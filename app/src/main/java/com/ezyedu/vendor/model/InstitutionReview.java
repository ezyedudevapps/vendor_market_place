package com.ezyedu.vendor.model;

public class InstitutionReview
{
    String username;
    String description;
    int rating;
    String image;

    public InstitutionReview(String username, String description, int rating, String image) {
        this.username = username;
        this.description = description;
        this.rating = rating;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public String getImage() {
        return image;
    }
}
