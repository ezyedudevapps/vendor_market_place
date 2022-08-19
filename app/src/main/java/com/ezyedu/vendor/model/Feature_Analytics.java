package com.ezyedu.vendor.model;

public class Feature_Analytics
{
    int ideas;
    int event;
    int banner;
    int message;
    int total_view;
    int compare;
    int ideas_save;
    int vendor_save;


    public Feature_Analytics(int ideas, int event, int banner, int message, int total_view, int compare, int ideas_save, int vendor_save) {
        this.ideas = ideas;
        this.event = event;
        this.banner = banner;
        this.message = message;
        this.total_view = total_view;
        this.compare = compare;
        this.ideas_save = ideas_save;
        this.vendor_save = vendor_save;
    }

    public int getIdeas() {
        return ideas;
    }

    public int getEvent() {
        return event;
    }

    public int getBanner() {
        return banner;
    }

    public int getMessage() {
        return message;
    }

    public int getTotal_view() {
        return total_view;
    }

    public int getCompare() {
        return compare;
    }

    public int getIdeas_save() {
        return ideas_save;
    }

    public int getVendor_save() {
        return vendor_save;
    }
}
