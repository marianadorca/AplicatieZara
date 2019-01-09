package com.example.madalina.aplicatiezara;


import android.graphics.Bitmap;

// contine infomatiile despre un produs
public class DataModel {


    String name;
    String price;
    Bitmap image;

    public DataModel(String name, String price, Bitmap image) {
        this.name = name;
        this.price = price;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public Bitmap getImage() {
        return image;
    }
}