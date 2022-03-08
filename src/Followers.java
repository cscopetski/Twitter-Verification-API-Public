package com.starfruitinteractive.StarfruitBackend;

import java.util.ArrayList;
import java.util.List;

public class Followers {
    private List<String> followers = new ArrayList<String> (); 

    public void LoadFollowers(List<String> list) {
        this.followers = list;
    }

    public void UpdateFollowers(List<String> list) {
        for (int i=0; i < list.size(); i++) {
            String userName = list.get(i);

            if (!this.followers.contains(userName)) {
                this.followers.add(userName);
            }
        }
    }

    public boolean IsUserFollowing(String Username) {
        boolean IsFollowing = this.followers.contains(Username);
        return IsFollowing;
    }
}
