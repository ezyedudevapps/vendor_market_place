package com.ezyedu.vendor.model;

public class MyRequest
{
    String name;
    String image;
    String username;

    public MyRequest(String name, String image, String username) {
        this.name = name;
        this.image = image;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }
}
