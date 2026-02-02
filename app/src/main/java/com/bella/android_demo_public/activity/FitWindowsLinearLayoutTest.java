package com.bella.android_demo_public.activity;



import com.bella.android_demo_public.R;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.FitWindowsLinearLayout;
import com.google.android.material.button.MaterialButton;

public class FitWindowsLinearLayoutTest extends AppCompatActivity {
    private FitWindowsLinearLayout rootLayout;
    private TextView insetsInfoView;
    private MaterialButton btnToggleImmersive;
    private MaterialButton btnShowSystemBars;
    private MaterialButton btnHideSystemBars;

    private boolean isImmersiveMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_windows_linear_layout_test);

        initializeViews();
        setupClickListeners();
        setupWindowInsetsListener();
    }

    private void initializeViews() {
        rootLayout = findViewById(R.id.root_layout);
        insetsInfoView = findViewById(R.id.tv_insets_info);
        btnToggleImmersive = findViewById(R.id.btn_toggle_immersive);
        btnShowSystemBars = findViewById(R.id.btn_show_system_bars);
        btnHideSystemBars = findViewById(R.id.btn_hide_system_bars);
    }

    private void setupClickListeners() {
        btnToggleImmersive.setOnClickListener(v -> toggleImmersiveMode());
        btnShowSystemBars.setOnClickListener(v -> showSystemBars());
        btnHideSystemBars.setOnClickListener(v -> hideSystemBars());
    }

    private void setupWindowInsetsListener() {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            updateInsetsInfo();
                        }
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void toggleImmersiveMode() {
        isImmersiveMode = !isImmersiveMode;

        if (isImmersiveMode) {
            enterImmersiveMode();
            btnToggleImmersive.setText("退出沉浸式模式");
        } else {
            exitImmersiveMode();
            btnToggleImmersive.setText("进入沉浸式模式");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void enterImmersiveMode() {
        hideSystemBars();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void exitImmersiveMode() {
        showSystemBars();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideSystemBars() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showSystemBars() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void updateInsetsInfo() {
        String info = String.format(
                "Padding Values:\n" +
                        "Top: %dpx (状态栏区域)\n" +
                        "Bottom: %dpx (导航栏区域)\n" +
                        "Left: %dpx\n" +
                        "Right: %dpx\n\n" +
                        "FitWindowsLinearLayout 自动处理了系统窗口插入区域，确保内容不会被系统UI遮挡。",
                rootLayout.getPaddingTop(),
                rootLayout.getPaddingBottom(),
                rootLayout.getPaddingLeft(),
                rootLayout.getPaddingRight()
        );

        insetsInfoView.setText(info);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            updateInsetsInfo();
        }
    }
}