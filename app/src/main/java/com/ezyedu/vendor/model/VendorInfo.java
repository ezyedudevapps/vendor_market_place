package com.ezyedu.vendor.model;

public class VendorInfo
{
    Integer vendor_id;
    String vendor_hash_id;
    String vendor_name;
    String vendor_address;
    String vendor_logo;
    String vendor_email;
    String vendor_phone;
    String website;
    String vendor_image;
    String vendor_rating;
    Integer is_chatting_allowed;
    String time;
    String instagram;
    String facebook;
    String twitter;
    String youtube;
    String tiktok;
    int mon,tues,wednes,thurs,fri,sat,sun;


    public VendorInfo(Integer vendor_id, String vendor_hash_id, String vendor_name, String vendor_address, String vendor_logo, String vendor_email, String vendor_phone, String website, String vendor_image, String vendor_rating, Integer is_chatting_allowed, String time, String instagram, String facebook, String twitter, String youtube, String tiktok, int mon, int tues, int wednes, int thurs, int fri, int sat, int sun) {
        this.vendor_id = vendor_id;
        this.vendor_hash_id = vendor_hash_id;
        this.vendor_name = vendor_name;
        this.vendor_address = vendor_address;
        this.vendor_logo = vendor_logo;
        this.vendor_email = vendor_email;
        this.vendor_phone = vendor_phone;
        this.website = website;
        this.vendor_image = vendor_image;
        this.vendor_rating = vendor_rating;
        this.is_chatting_allowed = is_chatting_allowed;
        this.time = time;
        this.instagram = instagram;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.tiktok = tiktok;
        this.mon = mon;
        this.tues = tues;
        this.wednes = wednes;
        this.thurs = thurs;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public String getVendor_hash_id() {
        return vendor_hash_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public String getVendor_address() {
        return vendor_address;
    }

    public String getVendor_logo() {
        return vendor_logo;
    }

    public String getVendor_email() {
        return vendor_email;
    }

    public String getVendor_phone() {
        return vendor_phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getVendor_image() {
        return vendor_image;
    }

    public String getVendor_rating() {
        return vendor_rating;
    }

    public Integer getIs_chatting_allowed() {
        return is_chatting_allowed;
    }

    public String getTime() {
        return time;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getTiktok() {
        return tiktok;
    }

    public int getMon() {
        return mon;
    }

    public int getTues() {
        return tues;
    }

    public int getWednes() {
        return wednes;
    }

    public int getThurs() {
        return thurs;
    }

    public int getFri() {
        return fri;
    }

    public int getSat() {
        return sat;
    }

    public int getSun() {
        return sun;
    }
}
