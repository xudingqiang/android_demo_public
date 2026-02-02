package com.bella.android_demo_public.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bella.android_demo_public.activity.PictureLoadActivity;
import com.bella.android_demo_public.utils.LogTool;
import com.fde.notepad.view.IScrollBar;
import com.bella.android_demo_public.R;

public class VerticalDragScrollBar extends View implements IScrollBar {
    private static final String TAG = "VerticalDragScrollBar";
    private boolean isHoverScrollBar = false;
    private boolean isDragScrollBar = false;
    private boolean isFull = false;
    private int allLength = 0;
    private final Rect rect = new Rect();
    private final Paint paint = new Paint();
    private float lastRawY = 0f;
    private PictureLoadActivity.ScrollListener scrollListener;

    public VerticalDragScrollBar(Context context) {
        super(context);
        init();
    }

    public VerticalDragScrollBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalDragScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setColor(ContextCompat.getColor(getContext(), R.color.scrollbar_thumb));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    public void updateData(int scrollLength, int width, int height, int allLength) {
//        int scrollBarY = height * scrollLength / allLength;
//        int scrollBarLength = height * height / allLength;
//        this.allLength = allLength;
//
//        isFull = height >= allLength;
//        rect.set(0, scrollBarY, getMeasuredWidth(), scrollBarY + scrollBarLength + 1);

        rect.set(0, height, getMeasuredWidth(), height + 200 + 1);
    }

    @Override
    public void startTouch(@Nullable MotionEvent event) {
        if (event == null) return;
        isDragScrollBar = rect.contains((int) event.getX(), (int) event.getY());
        invalidate();
    }

    @Override
    public void endTouch(@Nullable MotionEvent event) {
        if (event == null) return;
        isDragScrollBar = false;
        isHoverScrollBar = rect.contains((int) event.getX(), (int) event.getY());
        invalidate();
    }

    @Override
    public void startScroll() {
        // TODO: Implement if needed
    }

    @Override
    public boolean needDrag(@Nullable MotionEvent event) {
        return isDragScrollBar;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
//        LogTool.w("onTouchEvent  "+event.getAction() +" ,y : "+event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(event);
                lastRawY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isDragScrollBar) {
                    int scrollAmount = (int) ((event.getRawY() - lastRawY) * this.allLength / getMeasuredHeight());
                    if (scrollListener != null) {
                        scrollListener.scrollYBy(scrollAmount,event.getY());
                    }
                    endTouch(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDragScrollBar) {
                    int scrollAmount = (int) ((event.getRawY() - lastRawY) * this.allLength / getMeasuredHeight());
                    if (scrollListener != null) {
                        scrollListener.scrollYBy(scrollAmount,event.getY());
                    }
                    lastRawY = event.getRawY();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onHoverEvent(@NonNull MotionEvent event) {
//        LogTool.w("onHoverEvent  "+event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
            case MotionEvent.ACTION_HOVER_MOVE:
            case MotionEvent.ACTION_HOVER_EXIT:
                isHoverScrollBar = rect.contains((int) event.getX(), (int) event.getY());
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Set color based on state
        if (isDragScrollBar) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.scrollbar_thumb_selected));
        } else if (isHoverScrollBar) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.scrollbar_thumb_hovered));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.scrollbar_thumb));
        }

        // Only draw if not full
        if (!isFull) {
            canvas.drawRect(rect, paint);
        }
    }

    // Setter for scroll listener
    public void setScrollListener(PictureLoadActivity.ScrollListener listener) {
        this.scrollListener = listener;
    }
}