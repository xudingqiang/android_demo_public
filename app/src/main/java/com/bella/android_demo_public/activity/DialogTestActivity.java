package com.bella.android_demo_public.activity;

import android.annotation.Nullable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.internal.app.AlertActivity;
import com.bella.android_demo_public.R;
import com.bella.android_demo_public.utils.LogTool;

public class DialogTestActivity extends AlertActivity {
    boolean isTransparent;
    EditText editView ;

    private Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_test);

        editView = (EditText) findViewById(R.id.editView);


        editView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LogTool.w("onEditorAction actionId: "+actionId );
                return false;
            }
        });


//        Button toggleBtn = findViewById(R.id.toggle_button);
//        toggleBtn.setOnClickListener(v -> {
//            if (isTransparent) {
//                setActivityOpaque();
//            } else {
//                setActivityTransparent();
//            }
//            isTransparent = !isTransparent;
//        });

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_layout, null);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_input_dialog_layout, null);
        EditText editText = customView.findViewById(R.id.editText);


        AlertDialog alertDialog = new AlertDialog.Builder(this,R.style.RoundedAlertDialog)
                .setTitle("Rename")
//                .setMessage("This is an opaque dialog")
                .setView(customView)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK", null).create();
//                .show();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
        alertDialog.show();

        editText.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(
                            TextView view, int actionId, @Nullable KeyEvent event) {
                        LogTool.w("actionId "+actionId  +",event.getKeyCode() "+event.getKeyCode());
                        if ((actionId == EditorInfo.IME_ACTION_DONE) || (event != null
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                && event.hasNoModifiers())) {
                            alertDialog.dismiss();
                        }
                        return false;
                    }
                });


        Window window = alertDialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//             window.setLayout(160, 80);
             params.width = 400;
             params.height = 200;
             params.x = (int) (1900 - d.getWidth()/2);
             params.y = (int ) (1000 - d.getHeight()/2);

            window.setAttributes(params);
        }
//        alertDialog.setw


    }


    // 恢复非透明背景
    private void setActivityOpaque() {
        // 恢复默认窗口背景（或指定自定义背景）
//        getWindow().setBackgroundDrawableResource(R.drawable.your_opaque_background);

        // 恢复默认遮罩
        getWindow().setDimAmount(0.5f);

        // 清除透明标志位
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // 恢复状态栏/导航栏颜色（示例为黑色）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
            getWindow().setNavigationBarColor(Color.BLACK);
        }
    }


    // 设置透明背景（等效于 XML 的 android:windowIsTranslucent=true）
    private void setActivityTransparent() {
        // 关键步骤：设置窗口背景为透明
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 移除系统自带的半透明遮罩（防止灰蒙效果）
        getWindow().setDimAmount(0f);

        // 可选：设置状态栏和导航栏透明（API 19+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        // 如果是 Android 5.0+，可以更精细控制
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}