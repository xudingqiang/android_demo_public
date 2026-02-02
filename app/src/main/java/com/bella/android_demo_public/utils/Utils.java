package com.bella.android_demo_public.utils;

import android.app.Activity;
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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.bella.android_demo_public.BellaDataBase;
import com.bella.android_demo_public.R;
import com.bella.android_demo_public.bean.RegionInfo;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    public static boolean isFreeformMaximized(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            // 仅支持 Android 11 (API 30) 及以上
            return false;
        }

        WindowManager windowManager = activity.getWindowManager();

        // 获取当前窗口边界
        WindowMetrics currentWindowMetrics = windowManager.getCurrentWindowMetrics();
        Rect currentBounds = currentWindowMetrics.getBounds();

        // 获取最大窗口边界
        WindowMetrics maxWindowMetrics = windowManager.getMaximumWindowMetrics();
        Rect maxBounds = maxWindowMetrics.getBounds();

        // 比较边界是否相等（考虑导航栏/状态栏差异）
        return currentBounds.equals(maxBounds);
    }


    // 判断是否全屏（沉浸模式）
    public static boolean isFullscreenMode(Activity activity) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();

        // 方法1：检查系统UI可见性标志
        int uiVisibility = decorView.getSystemUiVisibility();
        boolean hideSystemBars = (uiVisibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0 &&
                (uiVisibility & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0;

        // 方法2：检查窗口标志（双重确认）
        boolean windowFullscreen = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;

        return hideSystemBars && windowFullscreen;
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
    public static com.bella.android_demo_public.svg.SVG loadSvgFromAssets(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            return com.bella.android_demo_public.svg.SVG.getFromInputStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将 SVG 转换为 Bitmap
    public static Bitmap svgToBitmap(com.bella.android_demo_public.svg.SVG svg) {
        // 获取 SVG 的宽度和高度
//        int width = (int) svg.getDocumentWidth() ;
//        int height = (int) svg.getDocumentHeight();

        int width = 36;
        int height = 36;
        // 获取 SVG 的宽度和高度
        try {
            width = (int) svg.getDocumentWidth();
            height = (int) svg.getDocumentHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (width <= 0 || height <= 0) {
            width = height = 36;
        }
        // 创建一个 Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

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


    public static int parseValue(Context context, InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Element rootElement = document.getDocumentElement();
            NodeList itemList = document.getElementsByTagName("item");
//            CompatibleDatabaseHelper db = new CompatibleDatabaseHelper(context);
            for (int i = 0; i < itemList.getLength(); i++) {
                Element keycodeElement = (Element) itemList.item(i);
                String date = keycodeElement.getAttribute("update_date");
                String isdel = keycodeElement.getAttribute("isdel");
                String keycode = keycodeElement.getAttribute("key_code");
                NodeList packageList = keycodeElement.getElementsByTagName("package");
                for (int j = 0; j < packageList.getLength(); j++) {
                    Element packageElement = (Element) packageList.item(j);
                    String packagename = packageElement.getAttribute("name");
                    NodeList activityList = packageElement.getElementsByTagName("activity");
                    for (int k = 0; k < activityList.getLength(); k++) {
                        Element activityElement = (Element) activityList.item(k);
                        String activityName = activityElement.getAttribute("name");
                        String value = activityElement.getTextContent().replaceAll("\\s", "");
//                        activityElement.getElementsByTagName("defaultvalue").item(0).getTextContent().replaceAll("\\s", "");
                        LogTool.w("keycode: " + keycode + " ,packagename: " + packagename + " ,activityName: " + activityName + " ,value: " + value);
                    }
//
                    //Slog.wtf("parseValue", "name " + name + ",packagename " + packagename + ",date  " + date + ",isDel " + isdel);
//                    Map<String, Object> resMap = db.queryCompatibleByPackageNameAndKeyCode(packagename, name);
//                    if ("true".equals(isdel)) {
//                        db.deleteCompatible(packagename, name);
//                    } else if (resMap == null || resMap.get("PACKAGE_NAME") == null) {
//                        db.insertCompatible(packagename, name, defaultvalue);
//                    } else {
//                        String queryDate = resMap.get("FIELDS1").toString();
//                        if (!date.equals(queryDate)) {
//                            db.updateCompatible(packagename, name, defaultvalue, date);
//                        }
//                    }

                }
            }
//            db.readCompatibles();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        // 1. 创建一个新的 Bitmap 作为输出
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // 2. 定义画笔和矩形区域
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        // 3. 开启抗锯齿
        paint.setAntiAlias(true);

        // 4. 先在 Canvas 上画一个圆角矩形 (作为蒙版)
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        // 5. 设置混合模式为 SRC_IN (只显示源图像在目标图形内的部分)
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 6. 将原始 Bitmap 画到圆角蒙版上
        canvas.drawBitmap(bitmap, rect, rect, paint);
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(new Date(timestamp));
        return output;
    }


    public static void triggerSystemMediaScan(Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File externalDir = Environment.getExternalStorageDirectory();
        File externalDir = new File(Environment.getExternalStorageDirectory() + "/Download");
        LogTool.w("getAbsolutePath: " + externalDir.getAbsolutePath());
        Uri contentUri = Uri.fromFile(externalDir);
        LogTool.w("contentUri: " + contentUri.getPath());
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);

    }

    public static float getRatioHeight(){
        String h = SystemProperties.get("waydroid.display_height");
        BigDecimal bdResult = new BigDecimal(h).divide(new BigDecimal(1080), 3, RoundingMode.HALF_UP);
        return bdResult.floatValue();
    }


    /**
     * 通过 InputStream 读取文件头判断 MIME 类型
     * @param inputStream 输入流（不会被关闭）
     * @return MIME 类型
     */
    public static String getMimeTypeFromStream(InputStream inputStream) throws IOException {
        // 标记流位置，以便读取后重置
        if (!inputStream.markSupported()) {
            // 如果不支持标记，则包装为 BufferedInputStream
            inputStream = new BufferedInputStream(inputStream);
        }

        // 标记当前位置，最多读取 64 字节
        inputStream.mark(64);

        try {
            byte[] header = new byte[64];
            int bytesRead = inputStream.read(header, 0, 64);

            // 重置流到标记位置
            inputStream.reset();

            if (bytesRead < 2) {
                return "application/octet-stream"; // 默认类型
            }

            return detectMimeType(header, bytesRead);

        } catch (IOException e) {
            inputStream.reset();
            return "application/octet-stream";
        }
    }




    /**
     * 根据文件头字节检测 MIME 类型
     */
    private static String detectMimeType(byte[] header, int length) {
        // 图片类型
        if (isJPEG(header)) return "image/jpeg";
        if (isPNG(header)) return "image/png";
//        if (isGIF(header)) return "image/gif";
//        if (isBMP(header)) return "image/bmp";
//        if (isWebP(header)) return "image/webp";

        // 视频类型
//        if (isMP4(header)) return "video/mp4";
//        if (isAVI(header)) return "video/x-msvideo";
//        if (isMOV(header)) return "video/quicktime";

        // 音频类型
//        if (isMP3(header)) return "audio/mpeg";
//        if (isWAV(header)) return "audio/wav";
//        if (isFLAC(header)) return "audio/flac";

        // 文档类型
        if (isPDF(header)) return "application/pdf";
        if (isZIP(header)) return "application/zip";
        if (isDOCX(header)) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
//        if (isXLSX(header)) return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        // 文本类型
        if (isText(header)) return "text/plain";

        return "application/octet-stream"; // 默认
    }

    // 各种文件类型的魔数检测方法
    private static boolean isJPEG(byte[] header) {
        return header[0] == (byte) 0xFF && header[1] == (byte) 0xD8;
    }

    private static boolean isPNG(byte[] header) {
        return header[0] == (byte) 0x89 &&
                header[1] == (byte) 0x50 &&
                header[2] == (byte) 0x4E &&
                header[3] == (byte) 0x47;
    }

    private static boolean isPDF(byte[] header) {
        // PDF 文件以 "%PDF" 开头
        return header[0] == (byte) 0x25 &&
                header[1] == (byte) 0x50 &&
                header[2] == (byte) 0x44 &&
                header[3] == (byte) 0x46;
    }

    private static boolean isZIP(byte[] header) {
        // ZIP 文件以 "PK" 开头
        return header[0] == (byte) 0x50 &&
                header[1] == (byte) 0x4B;
    }

    private static boolean isMP4(byte[] header) {
        // MP4 文件在特定位置有 "ftyp" 字样
        if (header.length >= 12) {
            return header[4] == 'f' && header[5] == 't' &&
                    header[6] == 'y' && header[7] == 'p';
        }
        return false;
    }

    private static boolean isText(byte[] header) {
        // 简单的文本文件检测：检查前几个字节是否都是可打印ASCII字符
        for (int i = 0; i < Math.min(header.length, 100); i++) {
            if (header[i] == 0) return false; // 二进制文件通常包含0字节
            if (header[i] < 9 || (header[i] > 13 && header[i] < 32)) {
                return false; // 非打印字符
            }
        }
        return true;
    }

    private static boolean isDOCX(byte[] header) {
        // DOCX 实际上是 ZIP 文件，但通常包含特定结构
        // 这里简单检查是否是 ZIP 并且包含特定文件
        return isZIP(header);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        }
        return false;
    }

}
