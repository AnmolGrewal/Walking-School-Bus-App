package com.cmpt276.project.walkinggroupapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zherenx on 2018-04-05.
 */

public class Gamification {

    private Integer currentAvatar;

    private List<Integer> ownedAvatars = new ArrayList<>();

    public Gamification() {
        currentAvatar = 0;
        ownedAvatars.add(0);
    }


    public Integer getCurrentAvatar() {
        return currentAvatar;
    }

    public void setCurrentAvatar(Integer currentAvatar) {
        this.currentAvatar = currentAvatar;
    }

    public List<Integer> getOwnedAvatars() {
        return ownedAvatars;
    }

    public void setOwnedAvatars(List<Integer> ownedAvatars) {
        this.ownedAvatars = ownedAvatars;
    }
}
