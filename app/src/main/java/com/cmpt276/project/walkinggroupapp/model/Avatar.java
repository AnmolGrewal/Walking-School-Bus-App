package com.cmpt276.project.walkinggroupapp.model;


/**
 * class for storing information about an avatar
 */
public class Avatar {


    private int id;
    private int price;


    public Avatar(int id, int price) {
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
