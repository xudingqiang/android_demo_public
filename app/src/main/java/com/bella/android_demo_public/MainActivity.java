package com.bella.android_demo_public;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bella.android_demo_public.activity.AnimationTestActivity;
import com.bella.android_demo_public.activity.EglTestActivity;
import com.bella.android_demo_public.activity.EventbusTestActivity;
import com.bella.android_demo_public.activity.GaussianBlurActivity;
import com.bella.android_demo_public.activity.LibpagDemoActivity;
import com.bella.android_demo_public.activity.NetTestActivity;
import com.bella.android_demo_public.activity.PdfTestActivity;
import com.bella.android_demo_public.activity.RecyclerviewSelectionTestActivity;
import com.bella.android_demo_public.activity.RoomTestActivity;
import com.bella.android_demo_public.activity.TestKotlinActivity;
import com.bella.android_demo_public.bean.MessageEvent;
import com.bella.android_demo_public.dialog.DlgUpdateShow;
import com.bella.android_demo_public.utils.LogTool;
import com.bella.android_demo_public.utils.SPUtils;
import com.bella.android_demo_public.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context;
    LinearLayout layoutParent;

    public static final int REQUEST_CODE = 1000;

    public static final int REQUEST_CODE_1 = 1001;

    public static final int REQUEST_CODE_2 = 1002;

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        // 循环遍历父类，获取所有声明的字段
        while (clazz != null) {
            // 获取当前类声明的所有字段
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                fields.add(field);
            }
            // 继续获取父类的字段
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        EventBus.getDefault().register(this);
        // 设置Activity的窗口风格
        Window window = getWindow();
//        window.setStatusBarColor(getResources().getColor(R.color.white));

//        WindowInsetsController insetsController = window.getInsetsController();
//        if (insetsController != null) {
////            insetsController.hide(WindowInsets.Type.statusBars());  // 隐藏状态栏
//            // 或者
//            insetsController.show(WindowInsets.Type.statusBars());  // 显示状态栏
//        }

        View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
                viewTreeObserver.removeOnPreDrawListener(this);

                // 确保在视图准备好之后进行操作
                WindowInsetsController insetsController = getWindow().getInsetsController();
                if (insetsController != null) {
                    insetsController.show(WindowInsets.Type.statusBars()); // 显示状态栏
                }

                return true;
            }
        });
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        window.setDecorFitsSystemWindows(false);
        View customTitleBar = LayoutInflater.from(context).inflate(R.layout.layout_file_controller, null);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;
        window.addContentView(customTitleBar, layoutParams);


//        LockingContentObserver mObserver = new LockingContentObserver(new ContentLock(),  () -> {
//            Log.i(TAG,"ContentLock........ ");
//        });

        try {

            ContentObserver contentObserver = new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    LogTool.i("Desltop path " + selfChange);
                }

                @Override
                public void onChange(boolean selfChange, @Nullable Uri uri) {
                    super.onChange(selfChange, uri);
                }

                @Override
                public void onChange(boolean selfChange, @Nullable Uri uri, int flags) {
                    super.onChange(selfChange, uri, flags);
                }

                @Override
                public void onChange(boolean selfChange, @NonNull Collection<Uri> uris, int flags) {
                    super.onChange(selfChange, uris, flags);
                }


                @Override
                public boolean deliverSelfNotifications() {
                    return super.deliverSelfNotifications();
                }
            };

            String path = "content://com.android.externalstorage.documents/document/primary:Desktop";
            Uri uri = Uri.parse(path);
            context.getContentResolver().registerContentObserver(uri, true, contentObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_main);

        initView();

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide(); // 隐藏 ActionBar
//            // getSupportActionBar().show(); // 显示 ActionBar
//        }

//        WindowInsetsController controller = getWindow().getInsetsController();
//        if (controller != null) {
//            controller.hide(WindowInsets.Type.systemBars());
//            controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
//        }


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 如果权限未授予，则请求权限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.MANAGE_EXTERNAL_STORAGE}, REQUEST_CODE_1);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 如果权限未授予，则请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }


        File file = new File("mnt/sdcard/Desktop/  v个VVv.txt");
        if (file.exists()) {
            LogTool.i("11111111111111");
        } else {
            LogTool.i("0000000000000");
        }

    }


    // 创建快捷方式的代码
    public void createShortcut(Context context) {
//        // 检查系统是否支持快捷方式
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
//            ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
//
//            // 构建快捷方式
//            Intent shortcutIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"));
//            shortcutIntent.setClass(context, NetTestActivity.class);  // 设定目标 Activity
//
//            ShortcutInfo shortcut = new ShortcutInfo.Builder(context, "id_example_shortcut")
//                    .setShortLabel("Example Shortcut")
//                    .setLongLabel("This is a longer description of the shortcut.")
//                    .setIcon(Icon.createWithResource(context, R.mipmap.ic_launcher))  // 设置图标
//                    .setIntent(shortcutIntent)
//                    .build();
//
//            // 提交快捷方式
//            if (shortcutManager != null) {
//                LogTool.i("shortcutManager is not null.......... ");
//                shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
//                LogTool.i("shortcutManager 11111111111111111111111");
//            }else {
//                LogTool.e("shortcutManager is  null.......... ");
//            }
//        }else {
//            LogTool.i("Build.VERSION.SDK_INT............ "+Build.VERSION.SDK_INT );
//        }

        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);
        if (shortcutManager.isRequestPinShortcutSupported()) {
            // Create a shortcut to be pinned
            ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(context, "id_pin_shortcut")
                    .setShortLabel("Pin this shortcut")
                    .setLongLabel("Pin this shortcut to your home screen.")
                    .setIcon(Icon.createWithResource(context, R.drawable.icon_grid))
                    .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com")))
                    .build();

            // Request pinning the shortcut
            Intent pinnedShortcutCallbackIntent =
                    shortcutManager.createShortcutResultIntent(pinShortcutInfo);
            PendingIntent successCallback = PendingIntent.getBroadcast(context, 0, pinnedShortcutCallbackIntent, PendingIntent.FLAG_IMMUTABLE
            );

            shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());
        }
    }

    private void initView() {
        TextView test1 = findViewById(R.id.test1);
        test1.setText("libpag动画测试");
        test1.setBackgroundColor(Color.RED);
        test1.setOnClickListener(view -> {
            startActivity(new Intent(context, LibpagDemoActivity.class));
        });

        ComponentName componentName;

        TextView test2 = findViewById(R.id.test2);
        test2.setText("EGL测试");
        test2.setOnClickListener(view -> {
            startActivity(new Intent(context, EglTestActivity.class));

        });

        TextView test3 = findViewById(R.id.test3);
        test3.setText("Room测试");
        test3.setOnClickListener(view -> {
            SPUtils.putUserInfo(context, "bella", "test");
            startActivity(new Intent(context, RoomTestActivity.class));


            Intent intent = new Intent("com.fde.statusbar.SHOW_OR_HIDE");
            intent.putExtra("mode", 2);
            intent.setPackage("com.android.systemui");
            sendBroadcast(intent);
        });

        TextView test4 = findViewById(R.id.test4);
        test4.setText("RecyclerviewSelection测试");
        test4.setOnClickListener(view -> {
            startActivity(new Intent(context, RecyclerviewSelectionTestActivity.class));

            Intent intent = new Intent("com.fde.statusbar.SHOW_OR_HIDE");
            intent.putExtra("mode", 1);
            intent.setPackage("com.android.systemui");
            sendBroadcast(intent);
        });

        TextView test5 = findViewById(R.id.test5);
        test5.setText("GPS设置");
        test5.setOnClickListener(view -> {
//            startActivity(new Intent(context, GpsSetActivity.class));
            Intent intent = new Intent("com.fde.statusbar.SHOW_OR_HIDE");
            intent.putExtra("mode", 4);
            intent.setPackage("com.android.systemui");
            sendBroadcast(intent);
        });

        TextView test6 = findViewById(R.id.test6);
        test6.setText("网络测试");
        test6.setOnClickListener(view -> {
            startActivity(new Intent(context, NetTestActivity.class));

//            Intent intent = new Intent("com.fde.power");
//            intent.setPackage("com.fde.fde_linux_app_launcher");
            Intent intent = new Intent("com.fde.statusbar.SHOW_OR_HIDE");
            intent.putExtra("mode", 3);
            intent.setPackage("com.android.systemui");
            sendBroadcast(intent);



//            Intent intent = new Intent();
////            ComponentName componentName2 = new ComponentName("com.fde.fde_linux_app_launcher", "com.fde.fde_linux_app_launcher.MainActivity");
//            ComponentName componentName2 = new ComponentName("com.android.documentsui", "com.android.documentsui.ui.StartActivity");
//            intent.setComponent(componentName2);
//            intent.putExtra("openParams", "openParams###222#1111");
//            intent.putExtra("fromOther", "Launcher");
//            intent.putExtra("vnc_activity_name", "name");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
        });

        TextView test7 = findViewById(R.id.test7);
        test7.setText("Kotlin测试");
        test7.setOnClickListener(view -> {
//            LogTool.i("setOnClickListener............" + getPackageName());
            startActivity(new Intent(context, TestKotlinActivity.class));
//            AppUtils.INSTANCE.uninstallApp(context,getPackageName());
//            AppUtils.INSTANCE.createShortcut(context,getPackageName(),"许什么心愿");
            createShortcut(context);
//            String t1 = Utils.getPackageNameByAppName(context, "设置");
//            String t2 = Utils.getPackageNameByAppName(context, "settings");
//            LogTool.i("test1............" + t1 + ", test2 " + t2);
        });

        test7.setOnContextClickListener(new View.OnContextClickListener() {
            @Override
            public boolean onContextClick(View view) {
                LogTool.i("onContextClick............" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }
        });


        TextView test8 = findViewById(R.id.test8);
        test8.setText("EventBus测试");
        test8.setOnClickListener(view -> {
            startActivity(new Intent(context, EventbusTestActivity.class));

            String name = "PhyFusion测试";
            LogTool.i("isChinese: " + Utils.containsChinese(name));

//            Intent intent = new Intent();
//            ComponentName componentName = new ComponentName("com.bella.android_demo_public", "com.bella.android_demo_public.activity.EventbusTestActivity");
//            intent.setComponent(componentName);
////            intent.putExtra("App", name);
////            intent.putExtra("vnc_activity_name", name);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
        });

        TextView test9 = findViewById(R.id.test9);
        test9.setText("属性动画测试");

        test9.setOnClickListener(view -> {
            startActivity(new Intent(context, AnimationTestActivity.class));


//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_MAIN);
//            String title = "Test";
//            Icon launcherIcon =
//                    Icon.createWithResource(this, R.mipmap.icon_xserver);
//            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, title)
//                    .setShortLabel(title)
//                    .setIcon(launcherIcon)
//                    .setIntent(intent)
//                    .build();
//            getSystemService(ShortcutManager.class).requestPinShortcut(shortcutInfo, null);

//            ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(context, "shortcut_id")
//                    .setShortLabel("My Shortcut")
//                    .setIntent(new Intent(context, MainActivity.class)
//                            .setAction(Intent.ACTION_VIEW)
//                            .setPackage(context.getPackageName())) // Ensure correct package
//                    .build();
//            ShortcutManagerCompat.pushDynamicShortcut(context, shortcut);


//            ShortcutInfoCompat shortcut = new ShortcutInfoCompat.Builder(context, "myshortcutid")
//                    .setShortLabel("Website")
//                    .setLongLabel("Open the website")
//                    .setIcon(IconCompat.createWithResource(context, R.drawable.icon_grid))
//
//                    .setIntent(new Intent(context, MainActivity.class)
//                            .setAction(Intent.ACTION_VIEW)
//                            .setPackage(context.getPackageName())) // Ensure correct package
//                    .build();
//
//            ShortcutManagerCompat.pushDynamicShortcut(context, shortcut);

//            Map<String, List<Map<String, String>>> map =  Utils.getAllIntentFilterData(context);
//
//            LogTool.i("map : "+map);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            PackageManager packageManager = context.getPackageManager();
//            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
//            LogTool.i("resolveInfoList : "+resolveInfoList.size());

//            Intent targetIntent = new Intent();
//            ComponentName componentName = new ComponentName("com.fde.x11", "com.fde.x11.AppListActivity");
//            intent.setComponent(componentName);
//            intent.putExtra("App", "Launcher");
////            intent.putExtra("vnc_activity_name", name);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);


//            Intent intent = new Intent();
//            ComponentName componentName = new ComponentName("com.android.documentsui", "com.android.documentsui.ui.RenameDialogActivity");
//            intent.setComponent(componentName);
//            intent.putExtra("oldFileName", "Launcher");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);


            String title = "test.txt";
            boolean isEnd = title.endsWith(".txt");
            LogTool.i("isEnd " + isEnd);



//            AppUtils.INSTANCE.uninstallApp(context, getPackageName());
//
//            try {
//                // 获取 ResolverActivity 类
//                Class<?> resolverActivityClass = Class.forName("com.android.internal.app.ResolverActivity");
//
//                // 创建 ResolverActivity 的 Intent
//                Intent resolverIntent = new Intent();
//                resolverIntent.setClassName("android", "com.android.internal.app.ResolverActivity");
//                resolverIntent.putExtra(Intent.EXTRA_INTENT, targetIntent); // 设置目标 Intent
//                resolverIntent.putExtra(Intent.EXTRA_TITLE, "选择应用");   // 设置标题
//                resolverIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
//
//                // 启动 ResolverActivity
//                context.startActivity(resolverIntent);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(context, "启动选择器失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }


//            String path = "/mnt/sdcard/Desktop/sunlogin.desktop";
//            Uri uri = Uri.parse(path);
//            Intent targetIntent = new Intent(Intent.ACTION_VIEW);
//            targetIntent.setDataAndType(uri, "application/vnd.desktop");
//            targetIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Intent chooser = Intent.createChooser(targetIntent, "选择应用打开 PDF 文件");
//            chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(chooser);

//            Intent intent = new Intent();
//            ComponentName componentName = new ComponentName("com.fde.fde_linux_app_launcher", "com.fde.fde_linux_app_launcher.MainActivity");
//            intent.setComponent(componentName);
//            intent.putExtra("openParams", "openParams");
//            intent.putExtra("fromOther", "Launcher");
//            intent.putExtra("vnc_activity_name", "name");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

        });


        LinearLayout layoutParent = new LinearLayout(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_file_controller, layoutParent, false);
        layoutParent.addView(view);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParent.setTag("DecorCaptionView");
        getWindow().addContentView(layoutParent, params);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.parseGpsData(MainActivity.this);
            }
        }).start();


        TextView test10 = findViewById(R.id.test10);
        test10.setText("更新测试");
        test10.setOnClickListener(v -> {


//            startActivity(new Intent(context, UpdateVersionActivity.class));
            DlgUpdateShow dlgUpdate = new DlgUpdateShow(this, "您得更新FDE-X11版本", "https://gitee.com/openfde/FDE-X11/releases/download/fde_w_dri3/FDE-X11.apk");
            if (!dlgUpdate.isShowing()) {
                dlgUpdate.show();
            }
        });

        TextView test11 = findViewById(R.id.test11);
        test11.setText("高斯模糊测试");
        test11.setOnClickListener(v -> {
            startActivity(new Intent(context, GaussianBlurActivity.class));

        });

        TextView test12 = findViewById(R.id.test12);
        test12.setText("pfd测试");
        test12.setOnClickListener(v -> {
            startActivity(new Intent(context, PdfTestActivity.class));

        });

    }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        // 处理事件
        LogTool.i("onMessageEvent  " + event.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.show(WindowInsets.Type.statusBars()); // 显示状态栏
                insetsController.hide(WindowInsets.Type.navigationBars());

            }
        }
    }
}