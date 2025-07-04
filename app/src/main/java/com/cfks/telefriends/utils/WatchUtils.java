package com.cfks.telefriends.utils;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.cfks.telefriends.WatchType;

public class WatchUtils {
  public static int getWatchType(Context ctx) {
    int result = WatchType.WATCH_OTHER;
    if (!TextUtils.isEmpty(getWatchId(ctx, WatchType.WATCH_XTC))) {
      result = WatchType.WATCH_XTC;
    } else if (!TextUtils.isEmpty(getWatchId(ctx, WatchType.WATCH_ZITENG))) {
      result = WatchType.WATCH_ZITENG;
    } else {
      result = WatchType.WATCH_OTHER;
    }
    return result;
  }

  public static String getWatchId(Context ctx, int watchType) {
    Uri XTC = Uri.parse("content://com.xtc.provider/BaseDataProvider/watchId/1");
    Uri ZITENG = Uri.parse("content://com.zmapp.watch/zmaeesp");
    Uri QIHOO = Uri.parse("content://com.qihoo.datacenter.WatchDataCenterProvider");

    ContentResolver cr = ctx.getContentResolver();
    String watchId = "";
    switch (watchType) {
      case WatchType.WATCH_XTC:
        watchId = cr.getType(XTC);
        break;
      case WatchType.WATCH_ZITENG:
        Cursor cursor = cr.query(ZITENG, new String[] {
          "userid1"
        }, null, null, null);
        String zt = "";
        try {
          if (cursor.moveToFirst()) {
            zt = cursor.getString(0);
            if (zt != null) {
              watchId = String.valueOf(Integer.valueOf(Integer.parseInt(zt)));
            }
          }
        } catch (Exception e) {
          watchId = "";
        }
        break;
    }
    return watchId;
  }
}