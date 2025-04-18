package com.bella.android_demo_public.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;

public class FreeformWindowDragger implements View.OnTouchListener {
    private final Activity mActivity;
    private final WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private int mInitialX;
    private int mInitialY;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private final View mDragHandle;
    private int mScreenWidth;
    private int mScreenHeight;

    public FreeformWindowDragger(Activity activity, View dragHandle) {
        this.mActivity = activity;
        this.mDragHandle = dragHandle;
        this.mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);

        // 初始化窗口参数
        mLayoutParams = (WindowManager.LayoutParams) activity.getWindow().getAttributes();

        // 获取屏幕尺寸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = mWindowManager.getCurrentWindowMetrics();
            mScreenWidth = windowMetrics.getBounds().width();
            mScreenHeight = windowMetrics.getBounds().height();
        } else {
            Display display = mWindowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            mScreenWidth = size.x;
            mScreenHeight = size.y;
        }

        // 设置拖拽监听
        mDragHandle.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录初始位置
                mInitialX = mLayoutParams.x;
                mInitialY = mLayoutParams.y;
                mInitialTouchX = event.getRawX();
                mInitialTouchY = event.getRawY();
                return true;

            case MotionEvent.ACTION_MOVE:
                // 计算位移
                int deltaX = (int) (event.getRawX() - mInitialTouchX);
                int deltaY = (int) (event.getRawY() - mInitialTouchY);

                // 更新窗口位置
                mLayoutParams.x = mInitialX + deltaX;
                mLayoutParams.y = mInitialY + deltaY;

                // 限制窗口不超出屏幕边界
                constrainWindowPosition();

                // 应用新位置
                mWindowManager.updateViewLayout(mActivity.getWindow().getDecorView(), mLayoutParams);
                return true;
        }
        return false;
    }

    private void constrainWindowPosition() {
        // 获取窗口尺寸
        int windowWidth = mActivity.getWindow().getDecorView().getWidth();
        int windowHeight = mActivity.getWindow().getDecorView().getHeight();

        // 限制X轴位置
        mLayoutParams.x = Math.max(-windowWidth / 2,
                Math.min(mLayoutParams.x, mScreenWidth - windowWidth / 2));

        // 限制Y轴位置
        mLayoutParams.y = Math.max(0,
                Math.min(mLayoutParams.y, mScreenHeight - windowHeight / 2));
    }
}
