package com.bella.android_demo_public;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bella.android_demo_public.activity.AnimationTestActivity;
import com.bella.android_demo_public.activity.EglTestActivity;
import com.bella.android_demo_public.activity.EventbusTestActivity;
import com.bella.android_demo_public.activity.GpsSetActivity;
import com.bella.android_demo_public.activity.LibpagDemoActivity;
import com.bella.android_demo_public.activity.NetTestActivity;
import com.bella.android_demo_public.activity.RecyclerviewSelectionTestActivity;
import com.bella.android_demo_public.activity.RoomTestActivity;
import com.bella.android_demo_public.bean.MessageEvent;
import com.bella.android_demo_public.utils.LogTool;
import com.bella.android_demo_public.utils.SPUtils;
import com.bella.android_demo_public.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
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

        EventBus.getDefault().register(this);
        // 设置Activity的窗口风格
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.white));

        setContentView(R.layout.activity_main);
        context = this;
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
        test1.setOnClickListener(view -> {
            startActivity(new Intent(context, LibpagDemoActivity.class));
        });

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
        });

        TextView test4 = findViewById(R.id.test4);
        test4.setText("RecyclerviewSelection测试");
        test4.setOnClickListener(view -> {
            startActivity(new Intent(context, RecyclerviewSelectionTestActivity.class));
        });

        TextView test5 = findViewById(R.id.test5);
        test5.setText("GPS设置");
        test5.setOnClickListener(view -> {
            startActivity(new Intent(context, GpsSetActivity.class));
        });

        TextView test6 = findViewById(R.id.test6);
        test6.setText("网络测试");
        test6.setOnClickListener(view -> {
            startActivity(new Intent(context, NetTestActivity.class));
        });

        TextView test7 = findViewById(R.id.test7);
        test7.setText("Kotlin测试");
        test7.setOnClickListener(view -> {
            LogTool.i("setOnClickListener............" + getPackageName());
//            startActivity(new Intent(context, TestKotlinActivity.class));
//            AppUtils.INSTANCE.uninstallApp(context,getPackageName());
//            AppUtils.INSTANCE.createShortcut(context,getPackageName(),"许什么心愿");
//            createShortcut(context);
            String t1 = Utils.getPackageNameByAppName(context, "设置");
            String t2 = Utils.getPackageNameByAppName(context, "settings");
            LogTool.i("test1............" + t1 + ", test2 " + t2);
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
        });

        TextView test9 = findViewById(R.id.test9);
        test9.setText("属性动画测试");
        test9.setOnClickListener(view -> {
            startActivity(new Intent(context, AnimationTestActivity.class));
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
}