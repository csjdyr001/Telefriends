package com.cfks.telefriends.utils;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.cfks.telefriends.WatchType;

public class WatchUtils {
    private static Uri XTC_CHECK = Uri.parse("content://com.xtc.provider/BaseDataProvider/watchId/1");
    private static Uri ZITENG_CHECK = Uri.parse("content://com.zmapp.watch/zmaeesp");
    private static Uri QIHOO_CHECK = Uri.parse("content://com.qihoo.datacenter.WatchDataCenterProvider");
        
    public static int checkWatchType(Context ctx) {
        ContentResolver cr = ctx.getContentResolver();
        int result = WatchType.WATCH_OTHER;
        Cursor cursor = cr.query(ZITENG_CHECK,new String[]{"userid1"}, null, null, null);
        String zt = "";
        if(cursor.moveToFirst()){
            try {
                zt = cursor.getString(0);
                if (zt != null) {
                    zt = String.valueOf(Integer.valueOf(Integer.parseInt(zt)));
                } 
            } catch (Exception e2) {}
        }
    	if(!TextUtils.isEmpty(cr.getType(XTC_CHECK))) {
    		result = WatchType.WATCH_XTC;
    	}else if(!TextUtils.isEmpty(zt)) {
    		result = WatchType.WATCH_ZITENG;
    	}else{
            result = WatchType.WATCH_OTHER;
        }
        return result;
    }

    public String getWatchId(Context ctx,int watchType){
        return "";
    }
}
