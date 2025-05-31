package com.cfks.telefriends.utils;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.cfks.telefriends.db.AppDatabase;
import com.cfks.telefriends.db.LocalDatabase;
import com.cfks.telefriends.db.UserRepository;

/**
 * Created by Kyrylo Avramenko on 8/2/2018.
 */
public class Injection {

    private static LocalDatabase localDb;

    public static LocalDatabase provideUserRepo(@NonNull Application application) {

        if (localDb == null){
            localDb = new LocalDatabase(application);
		}
        return localDb;
    }

}
