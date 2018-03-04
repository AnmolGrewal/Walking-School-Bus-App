package com.cmpt276.project.walkinggroupapp.list;

import com.cmpt276.project.walkinggroupapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage a collection of pots.
 */
public class userList {
    private List<User> users = new ArrayList<>();


    public void addUser(User user) {
        users.add(user);
    }

    public void changeUser(User user, int indexOfPotEditing) {
        validateIndexWithException(indexOfPotEditing);
        users.remove(indexOfPotEditing);
        users.add(indexOfPotEditing, user);
    }

    public int countPots() {
        return users.size();
    }
    public User getUser(int index) {
        validateIndexWithException(index);
        return users.get(index);
    }

    public List<User> getListPot(){
        return users;
    }

    public void userRemove(int position)
    {
        users.remove(position);
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countPots()) {
            throw new IllegalArgumentException();
        }

    }
}

