package com.cfks.telefriends.utils;

import android.os.AsyncTask;
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

    public static void uploadImage(File imageFile, String outputFormat, UploadCallback callback) {
        new UploadTask(imageFile, outputFormat, callback).execute();
    }

    private static class UploadTask extends AsyncTask<Void, Void, String> {
        private File imageFile;
        private String outputFormat;
        private UploadCallback callback;
        private Exception exception;

        public UploadTask(File imageFile, String outputFormat, UploadCallback callback) {
            this.imageFile = imageFile;
            this.outputFormat = outputFormat;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("image", imageFile.getName(),
                            RequestBody.create(MediaType.parse("image/*"), imageFile))
                    .addFormDataPart("outputFormat", outputFormat)
                    .build();

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                return response.body().string();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (exception != null) {
                callback.onFailure(exception.getMessage());
                return;
            }

            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.optBoolean("success", false);
                
                if (success) {
                    String url = jsonResponse.optString("url", "");
                    String deleteUrl = jsonResponse.optString("delete_url", "");
                    String message = jsonResponse.optString("message", "上传成功");
                    callback.onSuccess(url, deleteUrl, message);
                } else {
                    String errorMessage = jsonResponse.optString("message", "上传失败");
                    callback.onFailure(errorMessage);
                }
            } catch (Exception e) {
                callback.onFailure("解析响应失败: " + e.getMessage());
            }
        }
    }
}