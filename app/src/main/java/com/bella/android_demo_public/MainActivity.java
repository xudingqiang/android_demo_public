package com.bella.android_demo_public;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.Insets;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.window.WindowContainerTransaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.bella.android_demo_public.activity.AnimationTestActivity;
import com.bella.android_demo_public.activity.AppsListActivity;
import com.bella.android_demo_public.activity.EglTestActivity;
import com.bella.android_demo_public.activity.EventbusTestActivity;
import com.bella.android_demo_public.activity.GaussianBlurActivity;
import com.bella.android_demo_public.activity.GpsSetActivity;
import com.bella.android_demo_public.activity.LibpagDemoActivity;
import com.bella.android_demo_public.activity.MultiNestingRecycledviewActivity;
import com.bella.android_demo_public.activity.NetTestActivity;
import com.bella.android_demo_public.activity.PdfTestActivity;
import com.bella.android_demo_public.activity.PreferenceTestActivity;
import com.bella.android_demo_public.activity.RecyclerviewSelectionTestActivity;
import com.bella.android_demo_public.activity.RoomTestActivity;
import com.bella.android_demo_public.activity.SplitScreenMainActivity;
import com.bella.android_demo_public.activity.TestKotlinActivity;
import com.bella.android_demo_public.bean.MessageEvent;
import com.bella.android_demo_public.dialog.DlgUpdateShow;
import com.bella.android_demo_public.receiver.WifiBroadcastReceiver;
import com.bella.android_demo_public.utils.LogTool;
import com.bella.android_demo_public.utils.SPUtils;
import com.bella.android_demo_public.utils.Utils;
import com.bella.android_demo_public.view.NewOptionsPopupWindow;
import com.bella.android_demo_public.view.OptionsChildPopupWindow;

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
    LinearLayout rootView;
    ListView listView;
    NewOptionsPopupWindow popupWindow;
    OptionsChildPopupWindow popupChildWindow;
    MyFileObserver observer;
    private WifiBroadcastReceiver wifiBroadcastReceiver;


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

    private void setWindowLayoutParams() {
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.width = 540;
//        params.height = 960;
//
//        // 设置窗口类型为自由窗口
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        }

        // 隐藏标题栏
//        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

//        getWindow().setAttributes(params);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels; // 屏幕宽度的80%
        int height = displayMetrics.heightPixels; // 屏幕高度的60%

        View rootView = findViewById(android.R.id.content);
        float x = rootView.getScaleX();
        float y = rootView.getScaleY();

        float xx = rootView.getPivotX();
        float yy = rootView.getPivotY();

        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.width = width ;
//        params.height = height ;
//        getWindow().setAttributes(params);


        DisplayMetrics metrics = getResources().getDisplayMetrics();

        LogTool.w("content width " + params.width + " ,content height " + params.height);
        LogTool.w("windows width " + width + " ,windows height " + height);
        LogTool.w(" widthPixels " + metrics.widthPixels + " , heightPixels " + metrics.heightPixels);//500 1000
        rootView.setScaleX(rootView.getScaleY());
        rootView.setScaleY(rootView.getScaleX());


        params.width = 412;
        params.height = 713;

        metrics.widthPixels = params.width;
        metrics.heightPixels = 713;
        metrics.density = metrics.widthPixels / 360f;
        metrics.densityDpi = (int) (metrics.density * 160);
        getResources().updateConfiguration(null, metrics);


        getWindow().setAttributes(params);
//        getWindow().setLayout(540, 960);
        LogTool.w("windows metrics.heightPixels " + metrics.heightPixels + " ,heightPixels " + getResources().getDisplayMetrics().heightPixels);
        if (metrics.heightPixels < getResources().getDisplayMetrics().heightPixels) {
            LogTool.w("分屏模式开启");
        }

    }


    // 检查设备是否支持分屏（API 24+）
    public boolean isSplitScreenSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getPackageManager().hasSystemFeature(PackageManager.FEATURE_FREEFORM_WINDOW_MANAGEMENT)
                    || getPackageManager().hasSystemFeature(PackageManager.FEATURE_ACTIVITIES_ON_SECONDARY_DISPLAYS);
        }
        return false;
    }

    private String getLaunchSource() {
        // 方法1: getReferrer() (API 22+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Uri referrer = getReferrer();
            if (referrer != null) {
                return referrer.getHost();
            }
        }

        // 方法2: Intent extras
        Uri referrerUri = getIntent().getParcelableExtra(Intent.EXTRA_REFERRER);
        if (referrerUri != null) {
            return referrerUri.getHost();
        }

        String referrerName = getIntent().getStringExtra("android.intent.extra.REFERRER_NAME");
        if (referrerName != null) {
            return referrerName;
        }

        // 方法3: 其他方法或返回默认值
        return "unknown";
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        long lastTime = System.currentTimeMillis();
        setTitle("bella");



        // 获取当前 DisplayMetrics
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        // 强制设置为目标逻辑分辨率（例如 800x600）
        metrics.widthPixels = 1080;    // 逻辑宽度
        metrics.heightPixels = 1920;   // 逻辑高度
        metrics.density = metrics.widthPixels / 360f; // 基准宽度 360dp
        metrics.densityDpi = (int) (metrics.density * 160); // 计算DPI

        // 更新配置
        getResources().updateConfiguration(null, metrics);

        // 代码中
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName callingActivity = tasks.get(0).topActivity;
            String callingPackage = callingActivity.getPackageName();
            Log.w("bellaTest", "上一个应用包名: " + callingPackage);
        }

        LogTool.w("bellaTest", "onCreate start  " + getLaunchSource());
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_ACTION_BAR); // 或 FEATURE_CUSTOM_TITLE

        setTitle("Test");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        wifiBroadcastReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        registerReceiver(wifiBroadcastReceiver, filter);

        // 设置Activity的窗口风格
        Window window = getWindow();
//        // 设置窗口标志位，隐藏标题栏
//        window.requestFeature(Window.FEATURE_NO_TITLE);
////        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        getWindow().setStatusBarColor(Color.BLACK);
//        getWindow().setNavigationBarColor(Color.BLACK);

        startService(new Intent(context, MyService.class));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            WindowInsetsController controller = getWindow().getInsetsController();
//            if (controller != null) {
////                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
////                  controller.hide(WindowInsets.Type.navigationBars() |WindowInsets.Type.captionBar() |WindowInsets.Type.statusBars()  );
//            }
//        }

//        WindowInsetsController insetsController = window.getInsetsController();
//        if (insetsController != null) {
//            insetsController.hide(WindowInsets.Type.captionBar());  // 隐藏状态栏
//            // 或者
//            insetsController.show(WindowInsets.Type.captionBar());  // 显示状态栏
//        }


        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                // 获取标题栏的内边距
                Insets captionBarInsets = insets.getInsets(WindowInsets.Type.captionBar());
                Log.d("bellaTest", "WindowInsets Caption Bar Insets: " + captionBarInsets);


                // 根据标题栏的内边距调整视图布局
                // 例如，为视图添加内边距或外边距
                v.setPadding(
                        0, captionBarInsets.top, 0, 0
                );
                v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

//                v.setPaddingRelative(0,-30,0,0);


                // 返回处理后的内边距
                return insets;
            });
        }

        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setWindowLayoutParams();
//        window.setStatusBarColor(getResources().getColor(R.color.white));


//        View decorView = getWindow().getDecorView();
//        decorView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
//                viewTreeObserver.removeOnPreDrawListener(this);
//
//                // 确保在视图准备好之后进行操作
//                WindowInsetsController insetsController = getWindow().getInsetsController();
//                if (insetsController != null) {
//                    insetsController.setSystemBarsBehavior(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);// 显示状态栏
//                }
//
//                return true;
//            }
//        });
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        window.setDecorFitsSystemWindows(false);
//        View customTitleBar = LayoutInflater.from(context).inflate(R.layout.layout_file_controller, null);
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParams.gravity = Gravity.TOP;
//        window.addContentView(customTitleBar, layoutParams);

        LogTool.w("isRunding start ");
//        boolean isRunding = isServiceRunning(this, "XWindowService");
//        boolean isRunding = isAppRunning(this, "com.fde.x11");
        String isRunding = getForegroundApp(this);
        LogTool.w("isRunding " + isRunding);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
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

//        int h = getSupportActionBar().getHeight();
//        LogTool.w("h: "+h);
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
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent1.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent1);
            }
        }


        File file = new File("mnt/sdcard/Desktop/  v个VVv.txt");
        if (file.exists()) {
            LogTool.i("11111111111111");
        } else {
            LogTool.i("0000000000000");
        }

        String pathToMonitor = "/volumes/6dbf70eb-c233-4017-9fc8-e4e37bc3fe1d/home/xudingqiang/Desktop/";
        observer = new MyFileObserver(pathToMonitor);
        observer.startWatching();
//        try {
//            WatchService watchService = FileSystems.getDefault().newWatchService();
//            Path path = Paths.get("/volumes/6dbf70eb-c233-4017-9fc8-e4e37bc3fe1d/home/xudingqiang/Desktop/");
//            WatchKey key = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);
//            while (true) {
//                // 阻塞等待事件
//                key = watchService.take();
//
//                // 遍历事件
//                for (WatchEvent<?> event : key.pollEvents()) {
//                    WatchEvent.Kind<?> kind = event.kind();
//
//
//                    // 获取事件类型和文件名
//                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
//                    Path filename = ev.context();
//
//                    // 打印事件信息
//                    Log.w("test","事件类型："  + ", 文件名：" + filename);
//                }
//
//                // 重置key以接收后续事件
//                boolean valid = key.reset();
//                if (!valid) {
//                    break; // 如果key无效，退出循环
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        long curTime = System.currentTimeMillis();
        LogTool.w("subTime " + (curTime - lastTime));
        WindowContainerTransaction wct;


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Activity currentActivity;

    }


    public String getForegroundApp(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, time - 1000 * 10, time);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return null;
        }
        UsageStats recentStats = null;
        for (UsageStats usageStats : queryUsageStats) {
            if (recentStats == null || usageStats.getLastTimeUsed() > recentStats.getLastTimeUsed()) {
                recentStats = usageStats;
            }
        }
        return recentStats.getPackageName();
    }

    public boolean isAppRunning(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (tasks != null && tasks.size() > 0) {
            ComponentName topActivity = tasks.get(0).topActivity;
            return packageName.equals(topActivity.getPackageName());
        }
        return false;
    }

    public boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        LogTool.w("service 111111111 ");
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            LogTool.w("service.service.getClassName() " + service.service.getClassName());
            if (service.service.getClassName().contains(serviceName)) {
                return true; // 服务正在运行
            }
        }
        return false; // 服务未运行
    }

    private float dX;
    private float dY;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = event.getRawX() - rootView.getX();
                dY = event.getRawY() - rootView.getY();
                Log.w("bella", "onTouchEvent x: " + event.getX() + ", y:" + event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
//                rootView.animate()
//                        .x(event.getRawX() - dX)
//                        .y(event.getRawY() - dY)
//                        .setDuration(0)
//                        .start();

                return true;
            default:
                return false;
        }
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);

        LogTool.w("onMultiWindowModeChanged................. "+isInMultiWindowMode);
    }

    private void initView() {


        View viewParent = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        popupWindow = new NewOptionsPopupWindow(viewParent, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, this);


//        ListView listView = viewChild.findViewById(R.id.listView);
//
//


//
//        listView.setAdapter(adapter);
//

//
//        RelativeLayout relativeLayout = viewParent.findViewById(R.id.layout_system_theme);
//        relativeLayout.setOnClickListener(v->{
//            if(!MainActivity.this.isFinishing() && !MainActivity.this.isDestroyed() ){
//                v.setFocusable(true);
//                showPopupChildMenu(v);
//            }
//        });


        rootView = findViewById(R.id.rootView);
        TextView test1 = findViewById(R.id.test1);
        test1.setText("libpag动画测试");
        test1.setBackgroundColor(Color.RED);
        test1.setVisibility(View.VISIBLE);
        test1.setOnClickListener(view -> {
//            startActivity(new Intent(context, LibpagDemoActivity.class));

            Intent intent1 = new Intent(this, LibpagDemoActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
//            Intent intent2 = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
            Intent intent2 = new Intent(this, EglTestActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
            startActivities(new Intent[]{intent1, intent2});

//            ActivityOptions options = ActivityOptions.makeBasic();
//            options.setLaunchDisplayId(Display.DEFAULT_DISPLAY); // 在主屏幕显示
//            options.setSplitScreenCreateMode(1);
//
//            Intent intent = new Intent(this, LibpagDemoActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
//            startActivity(intent, options.toBundle());

        });


        TextView test2 = findViewById(R.id.test2);
        test2.setText("EGL测试");
        test2.setOnClickListener(view -> {
//            startActivity(new Intent(context, EglTestActivity.class));

            // Sharing an image file
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            shareIntent.addCategory(Intent.CATEGORY_DEFAULT);
            shareIntent.putExtra(Intent.EXTRA_STREAM, "content://com.android.externalstorage.documents/document/primary%3ADownload%2Fzz.dps");

            startActivity(Intent.createChooser(shareIntent, "Share image via"));
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
            startActivity(new Intent(context, GpsSetActivity.class));
//            Intent intent = new Intent("com.fde.statusbar.SHOW_OR_HIDE");
//            intent.putExtra("mode", 4);
//            intent.setPackage("com.android.systemui");
//            sendBroadcast(intent);
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
//        createShortcut(context);
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

//            Intent intent = new Intent("com.fde.fullscreen.ENABLE_OR_DISABLE");
//            intent.putExtra("mode", 1);
//            sendBroadcast(intent);

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
//        getWindow().addContentView(layoutParent, params);

//        getWindow().setWindowManager();
//        setWindow()

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


        TextView test13 = findViewById(R.id.test13);
        test13.setText("测试");
        test13.setOnClickListener(v -> {
//            Intent intent = new Intent();
////            ComponentName componentName = new ComponentName("com.android.documentsui", "com.android.documentsui.ui.OpenLinuxAppActivity");
//            ComponentName componentName = new ComponentName("com.bella.android_demo_public", "com.bella.android_demo_public.AppListActivity");
//            intent.setComponent(componentName);
//            intent.putExtra("openParams", "openParams");
//            intent.putExtra("fdeModel", "shell");
//            intent.putExtra("openParams", "openParams###222###1111###333");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

            showPopupChildMenu(v);
        });


        TextView test14 = findViewById(R.id.test14);
//        registerForContextMenu(test14);


        test14.setText("级联菜单");
        test14.setOnClickListener(v -> {
            //v.showContextMenu();
            showPopupMenu(v);
        });


        TextView test15 = findViewById(R.id.test15);
        test15.setText("应用列表");
        test15.setOnClickListener(v -> {
            startActivity(new Intent(context, AppsListActivity.class));
        });

        TextView test16 = findViewById(R.id.test16);
        test16.setText("多层嵌套列表");
        test16.setOnClickListener(v -> {
            startActivity(new Intent(context, MultiNestingRecycledviewActivity.class));
        });


        TextView test17 = findViewById(R.id.test17);
        test17.setText("分屏");
        test17.setOnClickListener(v -> {
            startActivity(new Intent(context, SplitScreenMainActivity.class));
        });

        DividerItemDecoration divider = new DividerItemDecoration(
                this, // Context
                DividerItemDecoration.HORIZONTAL // 分隔线的方向
        );

        // 设置分隔线的高度（1dp 转换为 px）
//        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));

        // 将分隔线添加到 RecyclerView
//        recyclerView.addItemDecoration(divider);

        TextView test18 = findViewById(R.id.test18);
        test18.setText("Preference测试");
        test18.setOnClickListener(v -> {
            startActivity(new Intent(context, PreferenceTestActivity.class));
        });

        TextView test19 = findViewById(R.id.test19);

        test19.setText("设置输入法");
        test19.setOnClickListener(v -> {
            //startActivity(new Intent(context, SettingsActivity.class));
//            Intent intent = new Intent();
//            ComponentName cn = ComponentName.unflattenFromString("com.ets100.secondary/.ui.main.MainActivity");
            //            intent.setComponent(cn);

            Intent intent = getPackageManager().getLaunchIntentForPackage("com.ets100.secondary");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        });

    }

    private void showPopupMenu(View view) {
//        // 创建 PopupMenu
//        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.END);
//        popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());
//        // 显示 PopupMenu
//        popupMenu.show();

        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }

        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 30, 30);

//        NewOptionsPopupWindow newOptionsPopupWindow = new NewOptionsPopupWindow(view, 0, 0);
//        newOptionsPopupWindow.showAtLocation(view, Gravity.CENTER, 150, 150);
    }

    public void showPopupChildMenu(View view) {
        if (popupChildWindow.isShowing()) {
            popupChildWindow.dismiss();
        }

//        popupChildWindow.showAtLocation(view, Gravity.NO_GRAVITY, 180, 200);
        popupChildWindow.showAsDropDown(view, 180, -30, Gravity.NO_GRAVITY);
    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getMenuInflater().inflate(R.menu.context_menu, menu);
//    }
//
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
////        switch (item.getItemId()) {
////            case R.id.menu_item_1_1:
////                // 处理子选项 1.1 的点击事件
////                return true;
////            case R.id.menu_item_1_2:
////                // 处理子选项 1.2 的点击事件
////                return true;
////            case R.id.menu_item_2_1:
////                // 处理子选项 2.1 的点击事件
////                return true;
////            case R.id.menu_item_2_2:
////                // 处理子选项 2.2 的点击事件
////                return true;
////            default:
////                return super.onContextItemSelected(item);
////        }
//        return super.onContextItemSelected(item);

//    }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        // 处理事件
        LogTool.i("onMessageEvent  " + event.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        observer.stopWatching();
        unregisterReceiver(wifiBroadcastReceiver);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            WindowInsetsController insetsController = getWindow().getInsetsController();
//            if (insetsController != null) {
//                insetsController.show(WindowInsets.Type.statusBars()); // 显示状态栏
//                insetsController.hide(WindowInsets.Type.navigationBars());
//
//            }
//        }
    }

//    private class MyNetdUnsolicitedEventListener extends INetdUnsolicitedEventListener.Stub {
//        @Override
//        public void onInterfaceChanged(String ifName, boolean isActive) {
//            Log.d(TAG, "Interface " + ifName + " changed, active: " + isActive);
//            // 处理接口状态变化
//        }
//
//        @Override
//        public void onInterfaceAddressUpdated(String addr, String ifName, int flags, int scope) {
//            Log.d(TAG, "Interface address updated: " + addr + " on " + ifName);
//            // 处理接口地址更新
//        }
//
//        @Override
//        public void onInterfaceAddressRemoved(String addr, String ifName, int flags, int scope) {
//            Log.d(TAG, "Interface address removed: " + addr + " from " + ifName);
//            // 处理接口地址移除
//        }
//
//        @Override
//        public void onInterfaceAdded(String ifName) {
//            Log.d(TAG, "Interface added: " + ifName);
//            // 处理新接口添加
//        }
//
//        @Override
//        public void onInterfaceRemoved(String ifName) {
//            Log.d(TAG, "Interface removed: " + ifName);
//            // 处理接口移除
//        }
//
//        // Android 14 新增方法
//        @Override
//        public void onQuotaLimitReached(String alertName, String ifName) {
//            Log.d(TAG, "Quota limit reached for " + ifName + ", alert: " + alertName);
//            // 处理配额限制达到
//        }
//
//        @Override
//        public void onInterfaceClassActivityChanged(boolean isActive, int timestamp, int uid,
//                                                    int trafficClass) {
//            Log.d(TAG, "Traffic class activity changed for UID " + uid +
//                    ", class: " + trafficClass + ", active: " + isActive);
//            // 处理流量类别活动变化
//        }
//    }


}