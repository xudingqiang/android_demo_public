package com.bella.android_demo_public.activity;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            // 模拟线程执行的任务

            // 模拟线程执行的任务
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
//                    .url("http://127.0.0.1:18080/api/v1/apps?page=1&page_size=100")
                    .url( "https://gitee.com/openfde/provision/releases/download/1.3.2/apps.json")
//                        .url("http://127.0.0.1:18080/api/v1/power/off")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                // 处理响应
                String responseString = response.body().string();
                LogTool.i("Thread responseString: " + responseString);
//                Gson gson = new Gson();
//                Type type = new TypeToken<Map<String, Object>>() {
//                }.getType();
//                Map<String, Object> myObject = gson.fromJson(responseString, type);
//                Map<String, Object> dataMap = (Map<String, Object>) myObject.get("data");
//                List<Map<String, Object>> list = (List<Map<String, Object>>) dataMap.get("data");
//
//                if (list != null) {
//                    for (int i = 0; i < list.size(); i++) {
//                        LogTool.i("list IconPath  " + list.get(i).get("IconPath") + " , Path " + list.get(i).get("Path") + " , Name " + list.get(i).get("Name"));
//                    }
//                }
//
//                Thread.sleep(5000); // 模拟耗时操作
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            return 42; // 返回结果
        }, executorService);

        future.thenAccept(result -> {

            LogTool.i("Thread returned: " + result);
        });

        // 关闭线程池


        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}