package com.cfks.telefriends.db;

import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Kyrylo Avramenko on 8/1/2018.
 */

public class User {
    private final int uid;

    public String email;

    @Nullable
    public String bio;

    @Nullable
    public String username;

    public String picUrl;


    public User(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }
}
