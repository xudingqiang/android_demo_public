package com.bella.android_demo_public.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomScrollBarView extends View {
    private Paint trackPaint;
    private Paint thumbPaint;

    private float thumbTop = 0;
    private float thumbHeight = 100;

    private float lastY;
    private OnScrollChangeListener listener;

    public interface OnScrollChangeListener {
        void onScroll(float progress);
    }

    public CustomScrollBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setColor(Color.parseColor("#33000000"));

        thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(Color.parseColor("#FF4285F4"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // track
        canvas.drawRoundRect(0, 0, getWidth(), getHeight(),
                getWidth() / 2f, getWidth() / 2f, trackPaint);

        // thumb
        canvas.drawRoundRect(0, thumbTop, getWidth(),
                thumbTop + thumbHeight,
                getWidth() / 2f, getWidth() / 2f, thumbPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                return true;

            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - lastY;
                lastY = event.getY();

                thumbTop += dy;
                limitThumb();

                invalidate();
                notifyProgress();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void limitThumb() {
        float max = getHeight() - thumbHeight;
        if (thumbTop < 0) thumbTop = 0;
        if (thumbTop > max) thumbTop = max;
    }

    private void notifyProgress() {
        if (listener != null) {
            float progress = thumbTop / (getHeight() - thumbHeight);
            listener.onScroll(progress);
        }
    }

    public void setThumbHeight(float height) {
        this.thumbHeight = height;
        invalidate();
    }

    public void setProgress(float progress) {
        thumbTop = progress * (getHeight() - thumbHeight);
        invalidate();
    }

    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        this.listener = l;
    }
}
