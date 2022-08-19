package com.ezyedu.vendor.model;

public class AllFeeds
{
    String tittle;
    String image;
    String category;
    String hash_id;
    int status;


    public AllFeeds(String tittle, String image, String category, String hash_id, int status) {
        this.tittle = tittle;
        this.image = image;
        this.category = category;
        this.hash_id = hash_id;
        this.status = status;
    }

    public String getTittle() {
        return tittle;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getHash_id() {
        return hash_id;
    }

    public int getStatus() {
        return status;
    }
}
