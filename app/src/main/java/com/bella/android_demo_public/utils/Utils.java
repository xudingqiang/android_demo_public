package com.bella.android_demo_public.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Xml;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.bella.android_demo_public.BellaDataBase;
import com.bella.android_demo_public.R;
import com.bella.android_demo_public.bean.RegionInfo;
import com.caverock.androidsvg.SVG;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

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

    public static boolean isChineseLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.equals("zh");
    }

    public static void parseGpsData(Context context) {
        try {
            LogTool.i("parseGpsData start。。。。。。。。");
            InputStream inputStream = context.getResources().openRawResource(R.raw.gps);
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String jsonString = scanner.hasNext() ? scanner.next() : "";

            JSONArray chinaData = new JSONArray(jsonString);
//            Uri uri = Uri.parse(Constant.REGION_URI + "/REGION_INFO");
//            context.getContentResolver().delete(uri, null, null);
            BellaDataBase.getInstance(context).regionDao().deleteAll();
            int index = 0;
            for (int i = 0; i < chinaData.length(); i++) {
                JSONObject china = chinaData.getJSONObject(i);
                String countryId = "C_00" + i;
                String countryName = china.getJSONArray("name").getString(0);
                String countryEnName = china.getJSONArray("name").getString(1);
                JSONArray provinces = china.getJSONArray("provinces");
                for (int j = 0; j < provinces.length(); j++) {
                    JSONObject province = provinces.getJSONObject(j);
                    String provinceId = "P_00" + i + "00" + j;
                    String provinceName = province.getJSONArray("name").getString(0); // Get the province name
                    String provinceEnName = province.getJSONArray("name").getString(1);
                    JSONArray cities = province.getJSONArray("cities");
                    for (int k = 0; k < cities.length(); k++) {
                        JSONObject city = cities.getJSONObject(k);
                        String cityName = city.getJSONArray("name").getString(0); // Get the city name
                        String cityEnName = city.getJSONArray("name").getString(1);
                        String gpsCoordinates = city.getString("gps"); // Get the GPS coordinates
                        String cityId = "CI_00" + i + "00" + j + "00" + k;


                        RegionInfo regionInfo = new RegionInfo();
                        regionInfo.setCountryId(countryId);
                        regionInfo.setCountryName(countryName);
                        regionInfo.setCountryNameEn(countryEnName);

                        regionInfo.setProvinceId(provinceId);
                        regionInfo.setProvinceName(provinceName);
                        regionInfo.setProvinceNameEn(provinceEnName);

                        regionInfo.setCityId(cityId);
                        regionInfo.setCityName(cityName);
                        regionInfo.setCityNameEn(cityEnName);

                        regionInfo.setGps(gpsCoordinates);

                        regionInfo.setIsDel("0");
                        regionInfo.setCreateDate(getCurDateTime());
                        regionInfo.setEditDate(getCurDateTime());

                        BellaDataBase.getInstance(context).regionDao().insert(regionInfo);
                    }
                }
            }
            LogTool.i("parseGpsData finish。。。。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static String getCurDateTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }

    public static String getPackageNameByAppName(Context context, String appName) {
        PackageManager packageManager = context.getPackageManager();

        // 获取所有已安装的应用信息
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : apps) {
            String appLabel = (String) packageManager.getApplicationLabel(app);  // 获取应用的显示名称
            if (appLabel != null && appLabel.equalsIgnoreCase(appName)) {
                return app.packageName;  // 返回包名
            }
        }

        return null;  // 如果找不到匹配的应用，返回 null
    }

    public static String getMD5(String input) {
        try {
            // 创建一个 MessageDigest 实例，指定使用 MD5 算法
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // 计算 MD5 值，得到一个字节数组
            byte[] hashBytes = digest.digest(input.getBytes());

            // 转换字节数组为 16 进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap addTextWatermark(Bitmap source, String watermarkText) {
        int width = source.getWidth();
        int height = source.getHeight();

        // 创建一个新的Bitmap，大小与原始Bitmap相同
        Bitmap watermarkBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 创建画布并将原图绘制到画布上
        Canvas canvas = new Canvas(watermarkBitmap);
        canvas.drawBitmap(source, 0, 0, null);

        // 设置水印文本样式
        Paint paint = new Paint();
        paint.setColor(Color.RED);  // 设置水印文本颜色
//        paint.setAlpha(100);  // 设置透明度，100代表半透明
        paint.setTextSize(30f);  // 设置文本大小
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);  // 设置抗锯齿

        // 获取水印文本的边界框，用于计算文本位置
        Rect textBounds = new Rect();
        paint.getTextBounds(watermarkText, 0, watermarkText.length(), textBounds);
        int textWidth = textBounds.width();
        int textHeight = textBounds.height();

        // 设置文本的位置（右下角）
        float x = width - textWidth - 20f;  // 距离右侧20像素
        float y = height - textHeight - 20f;  // 距离底部20像素

        // 在Bitmap上绘制文本水印
        canvas.drawText(watermarkText, x, y, paint);

        return watermarkBitmap;
    }

    public static Bitmap getBitmapFromImageView(ImageView imageView) {
        // 获取 ImageView 中的 Drawable
        Drawable drawable = imageView.getDrawable();


        Bitmap backgroundBitmap;
        if (drawable instanceof BitmapDrawable) {
            backgroundBitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            // 如果不是 BitmapDrawable 类型，则需要手动转换
            backgroundBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(backgroundBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return backgroundBitmap;
    }

    public static Bitmap addBackgroundToBitmap(Bitmap background, Bitmap foreground) {
        // 创建一个新的 Bitmap，宽高是背景图的宽高
        Bitmap resultBitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());

        // 创建 Canvas 来绘制图像
        Canvas canvas = new Canvas(resultBitmap);

        // 绘制背景图
        canvas.drawBitmap(background, 0, 0, null);

        // 如果需要，可以在背景上绘制前景图（例如一个头像或小图标）
        float left = (background.getWidth() - foreground.getWidth()) / 2; // 居中显示前景图
        float top = (background.getHeight() - foreground.getHeight()) / 2;
        canvas.drawBitmap(foreground, left, top, null);

        return resultBitmap;
    }

    public static Bitmap svgToBitmap(Context context, String svgFileName) {
        try {
            // 从 assets 文件夹读取 SVG 文件
            InputStream inputStream = context.getAssets().open(svgFileName);
            // 使用 XmlPullParser 解析 SVG
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            // 解析 SVG 内容并转换为 Picture 对象
            Picture picture = parseSvgToPicture(parser);
            // 创建一个 Bitmap 对象
            Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
            // 使用 Canvas 渲染 Picture 到 Bitmap
            Canvas canvas = new Canvas(bitmap);
            PictureDrawable pictureDrawable = new PictureDrawable(picture);
            pictureDrawable.setBounds(0, 0, picture.getWidth(), picture.getHeight());
            pictureDrawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Picture parseSvgToPicture(XmlPullParser parser) {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(500, 500);//new Canvas();  // 设置适当的尺寸
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    if ("svg".equals(tagName)) {
                        // 这里可以设置宽高等属性
                    } else if ("path".equals(tagName)) {
                        // 解析 path 元素，获取路径数据
                        String pathData = parser.getAttributeValue(null, "d");
                        if (pathData != null) {
                            // 使用 Path 绘制路径（此处省略解析 path 的具体实现）
                            canvas.drawPath(parsePathData(pathData), null);
                        }
                    }
                }
                eventType = parser.next();
            }
            picture.endRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picture;
    }

    // 简单的路径解析方法示例，你可以根据需要扩展
    private static Path parsePathData(String pathData) {
        Path path = new Path();
        // 这里需要使用一些解析库来解析 SVG 的 path 数据
        // 或者你可以自行解析 d 属性的路径命令
        return path;
    }

    // 从 assets 文件夹加载 SVG 文件
    public static SVG loadSvgFromAssets(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            return SVG.getFromInputStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将 SVG 转换为 Bitmap
    public static Bitmap svgToBitmap(SVG svg) {
        // 获取 SVG 的宽度和高度
        int width = (int) svg.getDocumentWidth();
        int height = (int) svg.getDocumentHeight();
        // 创建一个 Bitmap
        Bitmap bitmap = Bitmap.createBitmap(36, 36, Bitmap.Config.ARGB_8888);

        // 使用 Canvas 将 SVG 渲染到 Bitmap 上
        Canvas canvas = new Canvas(bitmap);
        svg.renderToCanvas(canvas);

        return bitmap;
    }


    public static Map<String, List<Map<String, String>>> getAllIntentFilterData(Context context) {
        Map<String, List<Map<String, String>>> result = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();

        try {
            // 获取所有已安装的应用包信息
            List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_INTENT_FILTERS);

            for (PackageInfo packageInfo : packageInfoList) {
                if (packageInfo.activities != null) {
                    for (ActivityInfo activityInfo : packageInfo.activities) {
                        Intent intent = new Intent();
                        intent.setClassName(activityInfo.packageName, activityInfo.name);

                        // 获取 Activity 的 Intent-Filters
                        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
                        List<Map<String, String>> dataList = new ArrayList<>();

                        for (ResolveInfo resolveInfo : resolveInfoList) {
                            IntentFilter filter = resolveInfo.filter;
                            if (filter != null && filter.countDataSchemes() > 0) {
                                for (int i = 0; i < filter.countDataSchemes(); i++) {
                                    Map<String, String> dataMap = new HashMap<>();
                                    String scheme = filter.getDataScheme(i);
                                    dataMap.put("scheme", scheme);

                                    if (filter.countDataAuthorities() > 0) {
                                        for (int j = 0; j < filter.countDataAuthorities(); j++) {
                                            IntentFilter.AuthorityEntry authority = filter.getDataAuthority(j);
                                            dataMap.put("host", authority.getHost());
//                                            dataMap.put("port", authority.getPort() != null ? authority.getPort() : "default");
                                        }
                                    }

                                    if (filter.countDataPaths() > 0) {
                                        for (int k = 0; k < filter.countDataPaths(); k++) {
                                            dataMap.put("path", filter.getDataPath(k).getPath());
                                        }
                                    }
                                    dataList.add(dataMap);
                                }
                            }
                        }
                        if (!dataList.isEmpty()) {
                            result.put(activityInfo.name, dataList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static boolean containsChinese(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        // 正则表达式匹配中文字符
        String regex = "[\\u4e00-\\u9fa5]";
        return str.matches(".*" + regex + ".*");
    }

    public static boolean installApk(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, "com.communist.book.fileprovider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
        return true;
    }

}
