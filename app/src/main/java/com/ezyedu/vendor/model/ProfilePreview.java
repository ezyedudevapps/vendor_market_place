package com.ezyedu.vendor.model;

public class ProfilePreview
{
    String logo,tittle,instagram,facebook,twitter,youtube,tittok,open,close,web,mail,address;
    Double rating;
    int mon,tues,wednes,thurs,fri,sat,sun;

    public ProfilePreview(String logo, String tittle, String instagram, String facebook, String twitter, String youtube, String tittok, String open, String close, String web, String mail, String address, Double rating, int mon, int tues, int wednes, int thurs, int fri, int sat, int sun) {
        this.logo = logo;
        this.tittle = tittle;
        this.instagram = instagram;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.tittok = tittok;
        this.open = open;
        this.close = close;
        this.web = web;
        this.mail = mail;
        this.address = address;
        this.rating = rating;
        this.mon = mon;
        this.tues = tues;
        this.wednes = wednes;
        this.thurs = thurs;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }


    public String getLogo() {
        return logo;
    }

    public String getTittle() {
        return tittle;
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

    public String getTittok() {
        return tittok;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }

    public String getWeb() {
        return web;
    }

    public String getMail() {
        return mail;
    }

    public String getAddress() {
        return address;
    }

    public Double getRating() {
        return rating;
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
