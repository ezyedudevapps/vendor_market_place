package com.ezyedu.vendor.model;

public class ProductPreview
{
    String course_hash_id,image,tittle,duration,date,institution_name,label;

    public ProductPreview(String course_hash_id, String image, String tittle, String duration, String date, String institution_name, String label) {
        this.course_hash_id = course_hash_id;
        this.image = image;
        this.tittle = tittle;
        this.duration = duration;
        this.date = date;
        this.institution_name = institution_name;
        this.label = label;
    }

    public String getCourse_hash_id() {
        return course_hash_id;
    }

    public String getImage() {
        return image;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public String getInstitution_name() {
        return institution_name;
    }

    public String getLabel() {
        return label;
    }
}
