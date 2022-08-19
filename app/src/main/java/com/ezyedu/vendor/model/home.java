package com.ezyedu.vendor.model;

public class home
{
    int id;
    String name;
    String image;
    Double rating;
    int rating_count;
    int pending;
    int processed;
    int completed;
    int unread_chat;
    int new_review;
    int prending_users;
    int idea_views;
    int event_views;

    public home(int id, String name, String image, Double rating, int rating_count, int pending, int processed, int completed, int unread_chat, int new_review, int prending_users, int idea_views, int event_views) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.rating = rating;
        this.rating_count = rating_count;
        this.pending = pending;
        this.processed = processed;
        this.completed = completed;
        this.unread_chat = unread_chat;
        this.new_review = new_review;
        this.prending_users = prending_users;
        this.idea_views = idea_views;
        this.event_views = event_views;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public Double getRating() {
        return rating;
    }

    public int getRating_count() {
        return rating_count;
    }

    public int getPending() {
        return pending;
    }

    public int getProcessed() {
        return processed;
    }

    public int getCompleted() {
        return completed;
    }

    public int getUnread_chat() {
        return unread_chat;
    }

    public int getNew_review() {
        return new_review;
    }

    public int getPrending_users() {
        return prending_users;
    }

    public int getIdea_views() {
        return idea_views;
    }

    public int getEvent_views() {
        return event_views;
    }
}
