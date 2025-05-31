package com.cfks.telefriends.settings_screen;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.cfks.telefriends.UserRepoViewModel;
import com.cfks.telefriends.db.UserRepository;
import com.cfks.telefriends.utils.RxImageHelper;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kyrylo Avramenko on 9/25/2018.
 */
public class ZoomPhotoViewModel extends UserRepoViewModel {

    private String currPicPath;

    public ZoomPhotoViewModel(@NonNull Application application, UserRepository userRepo) {
        super(application, userRepo);
    }
    public String getCurrPicPath() {
        return currPicPath;
    }

    public void setCurrPicPath(String currPicPath) {
        this.currPicPath = currPicPath;
    }


    Completable addToGallery(final Context context){
        return RxImageHelper.addToGallery(currPicPath, context)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io());
    }
}
