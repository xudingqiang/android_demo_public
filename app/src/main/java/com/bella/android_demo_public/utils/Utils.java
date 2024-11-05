package com.bella.android_demo_public.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by TJZM-14
 * AUTHOR: xudingqiang@teejo.com.cn
 * DATE: 2018/1/24
 * DESC:一般工具类
 */

public class Utils {



    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     *
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Map<String, Object> json2Map(String jsonStr) {
        HashMap<String, Object> rsMap = new HashMap();
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> sMap = gson.fromJson(jsonStr, type);
        Iterator it = sMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object obj = sMap.get(key);
            if (obj instanceof LinkedTreeMap) {
                rsMap.put(key, json2Map(gson.toJson(obj)));
            } else if (obj instanceof ArrayList) {
                rsMap.put(key, json2Arr(gson.toJson(obj)));
            } else {
                rsMap.put(key, obj);
            }
        }
        return rsMap;
    }

    public static List<Object> json2Arr(String jsonStr) {
        ArrayList<Object> rsList = new ArrayList<Object>();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType();
        ArrayList<String> sList = gson.fromJson(jsonStr, type);
        Iterator it = sList.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof LinkedTreeMap) {
                rsList.add(json2Map(gson.toJson(obj)));
            } else if (obj instanceof ArrayList) {
                rsList.add(json2Arr(gson.toJson(obj)));
            } else {
                rsList.add(obj);
            }
        }
        return rsList;
    }

    public static String getMD5String(String input, Context cont) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toUpperCase();
    }


    public static List<Map<String, Object>> arr2List(Context context, int rId) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            String[] items = context.getResources().getStringArray(rId);
            for (int i = 0; i < items.length; i++) {
                Map<String, Object> mp = new HashMap<>();
                mp.put("id", items[i].split(",")[0]);
                mp.put("content", items[i].split(",")[1]);
                mp.put("isSelected", false);
                list.add(mp);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String keyGetValue(Context context, int key, int rId) {
        try {
            String[] items = context.getResources().getStringArray(rId);
            for (int i = 0; i < items.length; i++) {
                if (key == StringUtils.ToInt(items[i].split(",")[0])) {
                    return items[i].split(",")[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getDeviceId(Context context) {
        String serial = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String imei = tm.getImei(); // Requires permission READ_PHONE_STATE
                serial = imei == null ? tm.getMeid() : imei; // Requires permission READ_PHONE_STATE
            } else {
                serial = tm.getDeviceId(); // Requires permission READ_PHONE_STATE
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    public static int getMaxList(List<Map<String, Object>> list, String key) {

        return 0;
    }


}
