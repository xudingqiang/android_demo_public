package com.bella.android_demo_public.activity;

import android.app.Instrumentation;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.bean.MessageEvent;
import com.bella.android_demo_public.svg.SVG;
import com.bella.android_demo_public.utils.LogTool;
import com.bella.android_demo_public.utils.SimpleIniParser;
import com.bella.android_demo_public.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventbusTestActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView backgroundImageView;
    ImageView foregroundImageView;
    ImageView imgApp;
    TextView textView;
    Button textClick;


    private void simulateKeyPress(int keyCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Instrumentation instrumentation = new Instrumentation();
                instrumentation.sendKeyDownUpSync(keyCode);  // 模拟按下和松开
            }
        }).start();
    }

    public void sendKeyEvent(int keyCode) {
        try {
            Instrumentation instrumentation = new Instrumentation();
            instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//            KeyCharacterMap.deviceHasKey()
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.i("EventbusTestActivity onCreate.........");
        setContentView(R.layout.activity_eventbus_test);
        textView = findViewById(R.id.textView);
        textClick = findViewById(R.id.textClick);
        backgroundImageView = findViewById(R.id.backgroundImageView);
        foregroundImageView = findViewById(R.id.foregroundImageView);
        imgApp = findViewById(R.id.imgApp);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Map<String,Object> mp;

//        Instrumentatio().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);

        textClick.setOnClickListener(view -> {
//            simulateKeyPress(KeyEvent.KEYCODE_BACK);
//            simulateKeyPress(KeyEvent.KEYCODE_POWER);
//            simulateKeyPress(KeyEvent.KEYCODE_WAKEUP);
//            sendKeyEvent(KeyEvent.KEYCODE_POWER);
//            sendKeyEvent(KeyEvent.KEYCODE_WAKEUP);
//            sendKeyEvent(KeyEvent.KEYCODE_BACK);



            try {
                PackageManager pm = getPackageManager();
                ApplicationInfo appInfo = pm.getApplicationInfo("com.android.oobe", 0);
                if (appInfo != null) {
                    String packageName = appInfo.packageName;
                    LogTool.i("AppInfo", "Package name: " + packageName);

                    try {
                        pm.getPackageInfo("com.android.oobe", PackageManager.GET_ACTIVITIES);
                        LogTool.d("SystemAppLauncher", "Package exists.");
                    } catch (PackageManager.NameNotFoundException e) {
                        LogTool.e("SystemAppLauncher", "Package not found: " + e.getMessage());
                    }
//                    Intent intent = new Intent();
//                    ComponentName componentName = new ComponentName("com.android.oobe", "com.android.oobe.LanguageActivity");
//                    intent.setComponent(componentName);
//                    int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_SINGLE_TOP;
//                    flags |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
//                    flags |= Intent.FLAG_ACTIVITY_NEW_TASK;
//                    intent.setFlags(flags);
//                    startActivity(intent);
                } else {
                    LogTool.e("AppInfo", "ApplicationInfo is null for the given package name.");
                }
            } catch (PackageManager.NameNotFoundException e) {
                LogTool.e("AppInfo", "Package not found: " + e.getMessage());
            }
        });

        textView.setOnClickListener(view -> {
            EventBus.getDefault().post(new MessageEvent("Hello from EventBus!"));
            String str = "com.ss.android.ugc.aweme_fde.desktop";
            IntentFilter intentFilter;
            String md5 = Utils.getMD5(str);
            LogTool.i("str " + str.length() + " ,md5 " + md5);
            addPng();

        });

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher_background);
        Bitmap backgroundBitmap;

        backgroundBitmap = vectorToBitmap(this, R.mipmap.ic_doc_video);
        Bitmap bb = vectorToBitmap(this, R.mipmap.bg_android_bak);
        backgroundBitmap = scaleBitmap(backgroundBitmap, 200, 200);
        bb = scaleBitmap(bb, 200, 200);
        Bitmap b = overlayBitmaps(backgroundBitmap,bb);
        setBitmapToTextView(textView, b);

//        backgroundBitmap = vectorToBitmap(this, R.drawable.ic_launcher_background);
//        Bitmap bitmap = Utils.getBitmapFromImageView(foregroundImageView);
////        Bitmap bb = Utils.svgToBitmap(this,"phyfusion.svg");
//        SVG svg = Utils.loadSvgFromAssets(this, "terminal.svg");
//        if (svg != null) {
//            Bitmap bb = Utils.svgToBitmap(svg);
//            overlayBitmaps(backgroundBitmap, bitmap, foregroundImageView);
//            int width = 36;
//            try {
//                width = (int) svg.getDocumentWidth();
//                width = (96 - width)/2;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Bitmap b = overlayBitmaps(backgroundBitmap, bb,width);
//            setBitmapToTextView(textView, b);
//        } else {
//            LogTool.e("SVG is null .........");
//        }

        RadialGradient radialGradient;


//        textView.setBackground(getResources().getDrawable(R.drawable.ic_launcher_background));

        try (InputStream is = getAssets().open("deepin-terminal.desktop")) {
            Map<String, Map<String, String>> config = new SimpleIniParser().parse(is);
            Map<String,String> mmm = config.get("Desktop Entry");
            String str="";
//            String value = config.get("Desktop Entry").get("Name[zh_CN]");
//            for (Map.Entry<String, String> entry : mmm.entrySet()) {
//                String key = entry.getKey();
//                String value = entry.getValue();
//                LogTool.w("Key: " + key + ", Value: " + value);
//                str += "Key: " + key + ", Value: " + value+"\n";
//                textView.setText(str);
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        getAllApp(this);
    }

    private void addPng() {
        try {
            String packageName = "com.tencent.android.qqdownloader";//getPackageName();
            PackageManager packageManager = getPackageManager();
            LogTool.i("packageName........1..... " + packageName);
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            LogTool.i("packageName.......2...... " + packageName);
            Drawable icon = packageManager.getApplicationIcon(appInfo);
            LogTool.i("packageName........3..... " + packageName);
            String appName = packageManager.getApplicationLabel(appInfo).toString();
            String md5 = Utils.getMD5(appInfo.packageName);

            String path = "/mnt/sdcard/Pictures/WeiXin/" + md5 + ".png";
            LogTool.i("createAllAndroidIconToLinux md5 : " + md5 + ",path: " + path + ",packageName: " + packageName);
            File file = new File(path);
            if (!file.exists() && !path.contains(" ")) {
                drawableToPng(this, icon, path);
            }
            LogTool.i("addPng............. ");
//            List<ApplicationInfo> apps = new ArrayList<>();// packageManager.getInstalledApplications(0);
//            apps.addAll(getThirdPartyApps(this));
//            LogTool.i("apps size " + apps.size());
//            for (ApplicationInfo appInfo : apps) {
//                Drawable icon = packageManager.getApplicationIcon(appInfo);
//                String appName = packageManager.getApplicationLabel(appInfo).toString();
//                String md5 = appName;//Utils.getMD5(appInfo.packageName);
//
//                String path = "/mnt/sdcard/Pictures/WeiXin/" + md5 + ".png";
//                Log.i("bella", "createAllAndroidIconToLinux md5 : " + md5 + ",path: " + path + ",packageName: " + packageName);
//                File file = new File(path);
//                if (!file.exists() && !path.contains(" ")) {
//                    drawableToPng(this, icon, path);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ApplicationInfo> getThirdPartyApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> thirdPartyApps = new ArrayList<>();

        // 获取所有已安装应用信息
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : installedApps) {
            // 判断应用是否为第三方应用（通常通过检查是否属于系统应用）
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                thirdPartyApps.add(appInfo);
            }
        }

        return thirdPartyApps;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogTool.w("onKeyUp keyCode: "+keyCode + ",action: "+event.getAction());
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int toolType = event.getToolType(0);
        int action = event.getAction() ;
        LogTool.w("onTouchEvent toolType : "+toolType + ",action: "+action);
        return super.onTouchEvent(event);
    }

    private List<ApplicationInfo> getAllApp(Context context) {
        LauncherApps launcherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
        UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        List<UserHandle> userHandles = userManager.getUserProfiles();
        List<LauncherActivityInfo> list = new ArrayList<>();
        for (UserHandle userHandle : userHandles) {
            list.addAll(launcherApps.getActivityList(null, userHandle));
        }

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> listApps = new ArrayList<>();
        Log.i("bella", "getAllApp list  size:   " + list.size());
        for (LauncherActivityInfo li : list) {
            String appName = packageManager.getApplicationLabel(li.getApplicationInfo()).toString();
            Drawable icon = packageManager.getApplicationIcon(li.getApplicationInfo());
            String packageName = li.getApplicationInfo().packageName;
            // Log.i("bella","getAllApp list  li:  getName: " + li.getName() +"  ,appName: "+appName + ",packageName "+packageName + " ,name : "+li.getApplicationInfo().name);
            listApps.add(li.getApplicationInfo());
            drawableToPng(this,icon,"/mnt/sdcard/Pictures/"+packageName+".png");


        }
        return listApps;
    }

    public void drawableToPng(Context context, Drawable drawable, String filePath) {
        Bitmap bitmapT = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable 1111");
        } else if (drawable instanceof VectorDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable 2222");
        } else if (drawable instanceof StateListDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable 3333");
        } else if (drawable instanceof LayerDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable 4444");
        } else if (drawable instanceof LevelListDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable 5555");
        } else if (drawable instanceof ShapeDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable 6666");
        } else if (drawable instanceof GradientDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable 7777");
        } else if (drawable instanceof InsetDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable 8888");
        } else if (drawable instanceof RippleDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable 9999");
        } else if (drawable instanceof ColorDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable aaaa");
        } else if (drawable instanceof AdaptiveIconDrawable) {
            Log.i("bella", "createAllAndroidIconToLinux drawable bbbb");
        } else {
            Log.i("bella", "createAllAndroidIconToLinux drawable cccc");
        }
        if (drawable instanceof AdaptiveIconDrawable) {
            AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) drawable;
            bitmapT = adaptiveIconToBitmap(adaptiveIconDrawable);
            bitmapT = scaleBitmap(bitmapT, 64, 64);
            Bitmap b2 = vectorToBitmap(context, R.mipmap.flag_android);
            b2 = scaleBitmap(b2, 36, 36);
            bitmap = overlayBitmaps(bitmapT,b2);
        } else {
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                bitmapT = bitmapDrawable.getBitmap();
                bitmapT = scaleBitmap(bitmapT, 64, 64);
                Bitmap b2 = vectorToBitmap(context, R.mipmap.flag_android);
                b2 = scaleBitmap(b2, 36, 36);
                bitmap = overlayBitmaps(bitmapT,b2);
            } else {
                bitmap = bitmapT;
            }
        }
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        // 保存Bitmap到PNG文件
        File file = new File(filePath);
        FileOutputStream outputStream = null;

        try {
            if(!file.exists()){
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap adaptiveIconToBitmap(AdaptiveIconDrawable adaptiveIconDrawable) {
        int width = adaptiveIconDrawable.getIntrinsicWidth();
        int height = adaptiveIconDrawable.getIntrinsicHeight();

        // 创建一个与 AdaptiveIcon 大小相同的 Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // 获取前景和背景
        Drawable background = adaptiveIconDrawable.getBackground();
        Drawable foreground = adaptiveIconDrawable.getForeground();

        // 绘制背景和前景
        if (background != null) {
            background.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            background.draw(canvas);
        }
        if (foreground != null) {
            foreground.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            foreground.draw(canvas);
        }

        return bitmap;
    }

    public void setBitmapToTextView(TextView textView, Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        textView.setCompoundDrawables(null, null, null, drawable);
    }

    public Bitmap vectorToBitmap(Context context, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId, null);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap scaleBitmap(Bitmap originalBitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
    }

    public void overlayBitmaps(Bitmap bitmap1, Bitmap bitmap2, ImageView imageView) {
        // 创建一个与第一个 Bitmap 相同大小的空白 Bitmap
        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());

        // 创建 Canvas，将第一个 Bitmap 作为底图
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, 0, 0, null);  // 将 bitmap1 绘制到 canvas 上

        // 将第二个 Bitmap 绘制到 Canvas 上，叠加在第一个 Bitmap 上
        canvas.drawBitmap(bitmap2, 30, 36, null);  // 将 bitmap2 绘制到 canvas 上

        // 将叠加后的 Bitmap 设置到 ImageView
        imageView.setImageBitmap(overlayBitmap);
    }

    public Bitmap overlayBitmaps(Bitmap bitmap1, Bitmap bitmap2 ) {
        // 创建一个与第一个 Bitmap 相同大小的空白 Bitmap
//        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), bitmap1.getConfig());
        // 创建 Canvas，将第一个 Bitmap 作为底图
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, 0,0, null);  // 将 bitmap1 绘制到 canvas 上
        // 将第二个 Bitmap 绘制到 Canvas 上，叠加在第一个 Bitmap 上
        canvas.drawBitmap(bitmap2, 0,0, null);  // 将 bitmap2 绘制到 canvas 上
        // 将叠加后的 Bitmap 设置到 ImageView
        return overlayBitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}