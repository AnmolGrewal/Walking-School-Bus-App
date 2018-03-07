package com.cmpt276.project.walkinggroupapp.model;

/**
 * This class stores information to find a user in server.
 * It contains a user id and a hypertext reference.
 */
public class UserReference {
    private Long id;
    private String href;


    public long getId() {
        return id;
    }

    public String getHref() {
        return href;
    }

    public boolean equals(Long id) {
        return this.id.equals(id);
    }
}
