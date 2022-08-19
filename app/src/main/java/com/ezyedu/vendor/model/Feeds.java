package com.ezyedu.vendor.model;

public class Feeds
{
    String blog_title;
    String blog_hash_id;
    String published_date;
    String content;
    String blog_image;


    public Feeds(String blog_title, String blog_hash_id, String published_date, String content, String blog_image) {
        this.blog_title = blog_title;
        this.blog_hash_id = blog_hash_id;
        this.published_date = published_date;
        this.content = content;
        this.blog_image = blog_image;
    }

    public String getBlog_title() {
        return blog_title;
    }

    public String getBlog_hash_id() {
        return blog_hash_id;
    }

    public String getPublished_date() {
        return published_date;
    }

    public String getContent() {
        return content;
    }

    public String getBlog_image() {
        return blog_image;
    }
}
