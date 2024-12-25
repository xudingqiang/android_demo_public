package com.bella.android_demo_public.activity;

import android.app.PendingIntent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.utils.LogTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_test);

//        PendingIntent pendingIntent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
//        } else {
//            pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
//        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://127.0.0.1:18080/api/v1/apps?page=1&page_size=100")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    // 处理响应
                    String responseString = response.body().string();
                    Gson gson = new Gson();
                    Type type = new TypeToken<Map<String, Object>>() {
                    }.getType();
                    Map<String, Object> myObject = gson.fromJson(responseString, type);
                    Map<String, Object> dataMap = (Map<String, Object>) myObject.get("data");
                    List<Map<String, Object>> list = (List<Map<String, Object>>) dataMap.get("data");

                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            LogTool.i("list IconPath  " + list.get(i).get("IconPath") + " , Path " + list.get(i).get("Path") + " , Name " + list.get(i).get("Name"));
                        }
                    }

                    // 处理responseString
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}