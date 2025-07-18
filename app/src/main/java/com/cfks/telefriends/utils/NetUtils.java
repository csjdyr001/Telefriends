package com.cfks.telefriends.utils;
import android.app.Activity;
import android.widget.Toast;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import org.json.JSONObject;

public class NetUtils {
    
    // 定义回调接口
    public interface NetCallback {
        void onSucceed(JSONObject json) throws Exception;
    }
    
    public static void post(final Activity ctx,String url,String token,FormEncodingBuilder params,final NetCallback netcallback){
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = params.build();

        // 创建请求
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization","Bearer " + token)
                .build();

        // 异步发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // 在主线程显示错误提示
                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ctx, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    ctx.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                String responseData = response.body().string();
                                JSONObject json = new JSONObject(responseData);
                                netcallback.onSucceed(json);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ctx, "请求失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    ctx.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, "请求失败: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    
    public static void post(final Activity ctx,String url,final NetCallback callback){
        post(ctx,url,"",new FormEncodingBuilder(),callback);
    }
    
    public static void post(final Activity ctx,String url,String token,final NetCallback callback){
        post(ctx,url,token,new FormEncodingBuilder(),callback);
    }
    
    public static void post(final Activity ctx,String url,FormEncodingBuilder builder,final NetCallback callback){
        post(ctx,url,"",builder,callback);
    }
}
