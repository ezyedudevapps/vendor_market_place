package com.ezyedu.vendor.model;

public class Gallery_Images
{
    int id;
    String img;

    public Gallery_Images(int id, String img) {
        this.id = id;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }
}
