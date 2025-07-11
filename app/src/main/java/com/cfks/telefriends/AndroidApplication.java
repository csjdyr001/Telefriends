package com.cfks.telefriends;
import android.app.Application;
import android.widget.Toast;
import com.yc.toollib.tool.*;
import com.yc.toollib.crash.*;

@android.support.annotation.Keep
public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: Implement this method
        if(getApplicationContext().getPackageName().endsWith(".debug")){
            Toast.makeText(getApplicationContext(), "这是debug版本", Toast.LENGTH_SHORT).show();
        }
        CrashHandler.getInstance().init(this, new CrashListener() {
            //重启app
            @Override
            public void againStartApp() {
                CrashToolUtils.startCrashListActivity(AndroidApplication.this);
            }
            //自定义上传crash，支持开发者上传自己捕获的crash数据
            @Override
            public void recordException(Throwable ex) {
                //自定义上传crash，支持开发者上传自己捕获的crash数据
                //StatService.recordException(getApplication(), ex);
            }
        });
    }
}