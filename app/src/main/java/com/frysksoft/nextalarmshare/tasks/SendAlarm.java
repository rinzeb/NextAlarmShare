package com.frysksoft.nextalarmshare.tasks;

import android.os.AsyncTask;
import android.util.Log;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;

public class SendAlarm extends AsyncTask<String, Void, String> {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected String doInBackground(String... args) {
        Response response = null;
        try {
            String url = args[0];
            String haTime = args[1];
            String entity = args[2];
            String password = args[3];
            OkHttpClient client = new OkHttpClient();
            String json = "{\"entity_id\":\"" + entity + "\", \"time\":\"" + haTime + "\"}";
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                .url(url)
                .header("X-HA-Access", password)
                .post(body)
                .build();
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected void onPostExecute(String response) {
        Log.i("NextAlarmShare", response);
    }
}