package com.cfks.telefriends.settings_screen;

import android.app.Application;
import android.support.annotation.NonNull;

import com.cfks.telefriends.UserRepoViewModel;
import com.cfks.telefriends.db.User;
import com.cfks.telefriends.db.UserRepository;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kyrylo Avramenko on 8/27/2018.
 */
public class ChangeBioViewModel extends UserRepoViewModel {

    public ChangeBioViewModel(@NonNull Application application, @NonNull UserRepository userRepo) {
        super(application, userRepo);
    }

    String getBio(){
        return userRepo.getCurrUser().bio;
    }

    void updateBio(String bio){

        User currUser = userRepo.getCurrUser();
        currUser.bio = bio;

        updateUser(currUser);
    }
}
