package com.ezyedu.vendor.model;

public class SeperateCourse
{
    String category_label;
    String course_title;
    String course_description;
    String course_duration;
    Double initial_price;
    Double Discount_price;
    String start_date;
    String course_hash_id;

    public SeperateCourse(String category_label, String course_title, String course_description, String course_duration, Double initial_price, Double discount_price, String start_date, String course_hash_id) {
        this.category_label = category_label;
        this.course_title = course_title;
        this.course_description = course_description;
        this.course_duration = course_duration;
        this.initial_price = initial_price;
        Discount_price = discount_price;
        this.start_date = start_date;
        this.course_hash_id = course_hash_id;
    }


    public String getCategory_label() {
        return category_label;
    }

    public String getCourse_title() {
        return course_title;
    }

    public String getCourse_description() {
        return course_description;
    }

    public String getCourse_duration() {
        return course_duration;
    }

    public Double getInitial_price() {
        return initial_price;
    }

    public Double getDiscount_price() {
        return Discount_price;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getCourse_hash_id() {
        return course_hash_id;
    }
}
