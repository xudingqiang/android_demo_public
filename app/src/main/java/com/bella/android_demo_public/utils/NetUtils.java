package com.bella.android_demo_public.utils;

import android.util.Log;

import com.bella.android_demo_public.bean.ItemInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetUtils {
    protected static final String TAG = "NetUtils";

    private static final String ADDRESS = "http://127.0.0.1:18080";

    public static List<ItemInfo> getLinuxDesktopApp() {
        List<ItemInfo> list = null;
        try {
            list = new ArrayList<>();
            URL url = new URL(
                    ADDRESS + "/api/v1/desktopapps?page=" + 1 + "&page_size=" + 100 + "&refresh=true");

            Log.i(TAG, "getLinuxDesktopApp url " + url);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(3000);
            connection.connect();
            int code = connection.getResponseCode();

            String res = "";
            if (code == 200) { // 
                // 
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = null;

                while ((line = reader.readLine()) != null) {
                    res += line + "\n";
                }
                reader.close();
            }
            connection.disconnect();

            Log.i(TAG, "getLinuxDesktopApp res " + res);


            JSONObject jsonResponse = new JSONObject(res);
            JSONObject data = jsonResponse.getJSONObject("data");
            // 解析data中的data数组
            JSONArray dataArray = data.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                ItemInfo info = new ItemInfo();
                info.setType(item.getString("Type"));
                info.setIconPath(item.getString("IconPath"));
//                info.setCellX(item.getString("Type"));
//                info.setCellY(item.getString("Type"));
                info.setTitle(item.getString("Name"));
//                Map<String, Object> map = new HashMap<>();
//                map.put("Type", item.getString("Type"));
//                map.put("Path", item.getString("Path"));
//                map.put("IconPath", item.getString("IconPath"));
//                map.put("Name", item.getString("Name"));
//                map.put("ZhName", item.getString("ZhName"));
//                String fileName = item.getString("FileName");
//                int lastIndex = fileName.lastIndexOf('/');
//                if (lastIndex != -1) {
//                    map.put("FileName", fileName.substring(lastIndex + 1));
//                } else {
//                    map.put("FileName", fileName);
//                }
                list.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


}
