package com.ezyedu.vendor.model;

public class completed_orders
{
    int order_id;
    int order_status;
    String date;
    String course_name;
    int qty;
    int courses_count;

    public completed_orders(int order_id, int order_status, String date, String course_name, int qty, int courses_count) {
        this.order_id = order_id;
        this.order_status = order_status;
        this.date = date;
        this.course_name = course_name;
        this.qty = qty;
        this.courses_count = courses_count;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getOrder_status() {
        return order_status;
    }

    public String getDate() {
        return date;
    }

    public String getCourse_name() {
        return course_name;
    }

    public int getQty() {
        return qty;
    }

    public int getCourses_count() {
        return courses_count;
    }
}
