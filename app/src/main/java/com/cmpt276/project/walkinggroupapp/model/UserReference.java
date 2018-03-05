package com.cmpt276.project.walkinggroupapp.model;

/**
 * Created by zherenx on 2018-03-03.
 */

public class UserReference {
    private int id;
    private String href;


    public int getId() {
        return id;
    }

    public String getHref() {
        return href;
    }

    public boolean equals(int id) {
        return this.id == id;
    }
}
