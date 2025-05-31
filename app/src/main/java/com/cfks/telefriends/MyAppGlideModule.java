package com.cfks.telefriends;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by Kyrylo Avramenko on 9/17/2018.
 */

@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    // Intentionally empty.
    @Override
    public boolean isManifestParsingEnabled() {
        // TODO: Implement this method
        return false;//super.isManifestParsingEnabled();
    }
    
}
