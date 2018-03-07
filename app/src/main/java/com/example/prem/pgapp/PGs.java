package com.example.prem.pgapp;

/**
 * Created by Prem on 03-Mar-18.
 */

public class PGs {
    private int id;
    private String title,_shortdesc;
    private double rating;
    private double price;
    private int image;

    public PGs(int id, String title, String _shortdesc, double rating, double price, int image) {
        this.id = id;
        this.title = title;
        this._shortdesc = _shortdesc;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String get_shortdesc() {
        return _shortdesc;
    }

    public double getRating() {
        return rating;
    }

    public double getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
