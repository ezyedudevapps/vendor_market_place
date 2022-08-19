package com.ezyedu.vendor.model;

public class VenSliderImages
{
    String image;
    String logo;
    int rating;
    String tittle;


    public VenSliderImages(String image, String logo, int rating, String tittle) {
        this.image = image;
        this.logo = logo;
        this.rating = rating;
        this.tittle = tittle;
    }

    public String getImage() {
        return image;
    }

    public String getLogo() {
        return logo;
    }

    public int getRating() {
        return rating;
    }

    public String getTittle() {
        return tittle;
    }
}
