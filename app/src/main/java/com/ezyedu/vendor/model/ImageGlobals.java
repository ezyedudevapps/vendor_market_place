package com.ezyedu.vendor.model;

public class ImageGlobals{
    public static ImageGlobals instance = new ImageGlobals();

    public static ImageGlobals getInstance()
    {
        return instance;
    }

    public static void setInstance(ImageGlobals instance) {
        ImageGlobals.instance = instance;
    }

    public String image_index;

    public ImageGlobals()
    {

    }

    public String getIValue()
    {
        return image_index;
    }
    public void SetValue(String image_index)
    {
        this.image_index = image_index;
    }


}

