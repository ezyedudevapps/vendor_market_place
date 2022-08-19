package com.ezyedu.vendor.model;

public class Vendor_category {
    String label,image,hash_id,cat_hash,cat_label,sub_cat_label,sub_cat_hash;
    int id;

    public Vendor_category(String label, String image, String hash_id, String cat_hash, String cat_label, String sub_cat_label, String sub_cat_hash, int id) {
        this.label = label;
        this.image = image;
        this.hash_id = hash_id;
        this.cat_hash = cat_hash;
        this.cat_label = cat_label;
        this.sub_cat_label = sub_cat_label;
        this.sub_cat_hash = sub_cat_hash;
        this.id = id;
    }


    public String getLabel() {
        return label;
    }

    public String getImage() {
        return image;
    }

    public String getHash_id() {
        return hash_id;
    }

    public String getCat_hash() {
        return cat_hash;
    }

    public String getCat_label() {
        return cat_label;
    }

    public String getSub_cat_label() {
        return sub_cat_label;
    }

    public String getSub_cat_hash() {
        return sub_cat_hash;
    }

    public int getId() {
        return id;
    }
}
