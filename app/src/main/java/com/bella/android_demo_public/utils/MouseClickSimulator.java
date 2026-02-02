package com.bella.android_demo_public.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

public class MouseClickSimulator {

    public enum ClickType {
        SINGLE, DOUBLE, TRIPLE ,FOUR,FIVE
    }

    public static void simulateClick(View view, ClickType type) {
        simulateClick(view, type, view.getWidth() / 2f, view.getHeight() / 2f);
    }

    public static void simulateClick(final View view, final ClickType type, final float x, final float y) {
        final int clickCount;
        switch (type) {
            case DOUBLE: clickCount = 2; break;
            case TRIPLE: clickCount = 3; break;
            case FOUR: clickCount = 4; break;
            case FIVE: clickCount = 5; break;
            default: clickCount = 1; break;
        }

        final long baseTime = SystemClock.uptimeMillis();
        final Handler handler = new Handler(Looper.getMainLooper());

        for (int clickIndex = 0; clickIndex < clickCount; clickIndex++) {
            // 每次点击的 DOWN 事件
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dispatchCompatMotionEvent(view, baseTime, SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN, x, y);
                }
            }, clickIndex * 10);

            // 每次点击的 UP 事件（50ms后）
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dispatchCompatMotionEvent(view, baseTime, SystemClock.uptimeMillis() + 50,
                            MotionEvent.ACTION_UP, x, y);
                }
            }, clickIndex * 10 + 5);
        }
    }

    private static void dispatchCompatMotionEvent(View view, long downTime, long eventTime,
                                                  int action, float x, float y) {
        try {
            MotionEvent event;

            // 根据 API 级别选择合适的方法
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                event = MotionEvent.obtain(downTime, eventTime, action, x, y, 0, 0, 0, 0, 0, 0, 0);
            } else {
                // 使用最基础的 obtain 方法
                event = MotionEvent.obtain(downTime, eventTime, action, x, y, 0);
            }

            if (event != null) {
                view.dispatchTouchEvent(event);
                event.recycle();
            }
            LogTool.w( "MotionEvent.obtain success, using performClick");
        } catch (Exception e) {
            LogTool.e( "MotionEvent.obtain failed, using performClick");
            if (action == MotionEvent.ACTION_UP) {
                view.performClick();
            }
        }
    }
}
