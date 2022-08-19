package com.ezyedu.vendor.model;

public class SearchConnectionList
{
    String hash_id;
    String name;
    String username;
    String image;

    public SearchConnectionList(String hash_id, String name, String username, String image) {
        this.hash_id = hash_id;
        this.name = name;
        this.username = username;
        this.image = image;
    }

    public String getHash_id() {
        return hash_id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }
}
