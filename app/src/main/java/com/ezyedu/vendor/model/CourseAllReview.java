package com.ezyedu.vendor.model;

public class CourseAllReview
{
    String username;
    String user_image;
    int user_rating;
    String user_description;

    public CourseAllReview(String username, String user_image, int user_rating, String user_description) {
        this.username = username;
        this.user_image = user_image;
        this.user_rating = user_rating;
        this.user_description = user_description;
    }


    public String getUsername() {
        return username;
    }

    public String getUser_image() {
        return user_image;
    }

    public int getUser_rating() {
        return user_rating;
    }

    public String getUser_description() {
        return user_description;
    }
}
