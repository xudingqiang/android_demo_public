package com.bella.android_demo_public.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;

public class ScreenUtils {
    // 现代方式获取屏幕尺寸
    public static void getScreenSizeWithPoint(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ 推荐方式
            WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();

            Rect bounds = windowMetrics.getBounds();
            int width = bounds.width();
            int height = bounds.height();
            LogTool.w("Screen Android 11+: " + width + "x" + height);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Android 4.2+ 方式
            windowManager.getDefaultDisplay().getRealSize(size);
            LogTool.w("Screen Real size: " + size.x + "x" + size.y);
        } else {
            // 传统方式（已过时，但兼容旧版本）
            windowManager.getDefaultDisplay().getSize(size);
            LogTool.w("Screen Size: " + size.x + "x" + size.y);
        }
    }


    // 获取整个屏幕的物理宽高
    public static void getScreenDimensions(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        // 方法1：通过 WindowManager
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        // 方法2：通过 Resources
        // DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int screenWidth = displayMetrics.widthPixels;   // 屏幕宽度（像素）
        int screenHeight = displayMetrics.heightPixels;  // 屏幕高度（像素）
        float density = displayMetrics.density;          // 屏幕密度
        int densityDpi = displayMetrics.densityDpi;      // DPI

        LogTool.w("Screen 物理屏幕  displayMetrics.widthPixels : " + screenWidth + ",displayMetrics.heightPixels: " + screenHeight);
        LogTool.w("Screen 密度: " + density + ", DPI: " + densityDpi);

        // 转换为dp
        int widthDp = (int) (screenWidth / density);
        int heightDp = (int) (screenHeight / density);
        LogTool.w("Screen 屏幕DP: " + widthDp + "x" + heightDp + "dp");


        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        LogTool.w("Screen metrics.widthPixels : " + metrics.widthPixels + ",metrics.heightPixels " + metrics.heightPixels + "");
        int screenWidthDp = context.getResources().getConfiguration().screenWidthDp;
        int screenHeightDp = context.getResources().getConfiguration().screenHeightDp;
        LogTool.w("Screen screenWidthDp: " + screenWidthDp + ",screenHeightDp: " + screenHeightDp + "");

    }


    // 获取应用可用的显示区域
    public static void getUsableScreenArea(Activity activity) {
        // 方法1：使用 DisplayMetrics（包含状态栏和导航栏）
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int fullHeight = metrics.heightPixels;

        // 方法2：获取可见显示框架（扣除状态栏）
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int usableHeight = rect.height();    // 扣除状态栏的高度
        int statusBarHeight = rect.top;      // 状态栏高度

        // 方法3：获取实际可绘制区域（进一步扣除导航栏）
        View decorView = activity.getWindow().getDecorView();
        WindowInsets insets = decorView.getRootWindowInsets();
        if (insets != null) {
            int navigationBarHeight = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                navigationBarHeight = insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
            } else {
                navigationBarHeight = insets.getStableInsetBottom();
            }

            int trulyUsableHeight = usableHeight - navigationBarHeight;

            LogTool.w("Screen Usable 完整高度: " + fullHeight);
            LogTool.w("Screen  Usable 可用高度: " + usableHeight);
            LogTool.w("Screen  真正可用: " + trulyUsableHeight);
            LogTool.w("Screen  状态栏: " + statusBarHeight);
            LogTool.w("Screen  导航栏: " + navigationBarHeight);
        }
    }

}
