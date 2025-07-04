package com.cfks.telefriends.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.functions.Action;

/**
 * Created by Kyrylo Avramenko on 9/26/2018.
 */
public class RxImageHelper {

    public static Completable addToGallery(@NonNull final String path, final Context context){
        return Completable.fromAction(new Action(){
            @Override
            public final void run() {
                ImageHelper.addToGallery(path, context,ImageHelper.getRotation(path));
            }
        });
    }
}
