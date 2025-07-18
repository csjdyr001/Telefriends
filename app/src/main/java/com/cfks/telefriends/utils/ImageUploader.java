package com.cfks.telefriends.utils;

import android.app.Activity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;

public class ImageUploader {

  private static final String API_URL = "https://img.scdn.io/api/v1.php";
  private static final OkHttpClient client = new OkHttpClient();

  public interface UploadCallback {
    void onSuccess(String url, String deleteUrl, String message);
    void onFailure(String error);
  }

  public static void uploadImage(Activity act, File imageFile, String outputFormat, UploadCallback callback) {
    try {
      RequestBody requestBody = new MultipartBuilder()
        .type(MultipartBuilder.FORM)
        .addFormDataPart("image", imageFile.getName(),
          RequestBody.create(MediaType.parse("media/type"), imageFile))
        .addFormDataPart("outputFormat", outputFormat)
        .build();

      Request request = new Request.Builder()
        .url(API_URL)
        .post(requestBody)
        .build();

      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        callback.onFailure("Unexpected code " + response);
        return;
      }

      String responseBody = response.body().string();
      JSONObject jsonResponse = new JSONObject(responseBody);
      boolean success = jsonResponse.optBoolean("success", false);

      if (success) {
        String url = jsonResponse.optString("url", "");
        String deleteUrl = jsonResponse.optString("delete_url", "");
        String message = jsonResponse.optString("message", "上传成功");
        act.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            callback.onSuccess(url, deleteUrl, message);
          }
        });
      } else {
        String errorMessage = jsonResponse.optString("message", "上传失败");
        act.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            callback.onFailure(errorMessage);
          }
        });
      }
    } catch (Exception e) {
      act.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          callback.onFailure(e.getMessage());
        }
      });
    }
  }
}