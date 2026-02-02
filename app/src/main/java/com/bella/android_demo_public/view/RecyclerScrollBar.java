package com.bella.android_demo_public.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bella.android_demo_public.utils.LogTool;

public class RecyclerScrollBar extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float barTop;
    private float barHeight;

    private RecyclerView recyclerView;
    private boolean dragging = false;

    public RecyclerScrollBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(0x88000000);
    }

    public void attachRecyclerView(RecyclerView rv) {
        this.recyclerView = rv;
    }

    public void updateFromRecyclerView() {
        if (recyclerView == null) return;

        int range = recyclerView.computeVerticalScrollRange();
        int offset = recyclerView.computeVerticalScrollOffset();
        int extent = recyclerView.computeVerticalScrollExtent();

        if (range <= extent) {
            setVisibility(INVISIBLE);
            return;
        }

        setVisibility(VISIBLE);

        float height = getHeight();
        barHeight = (float) extent / range * height;
        barTop = (float) offset / range * height;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(
                0,
                barTop,
                getWidth(),
                barTop + barHeight,
                8,
                8,
                paint
        );
    }

    // ===== 鼠标左键按下 / 拖动 =====
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (recyclerView == null || getVisibility() != VISIBLE) return false;

        int action = event.getActionMasked();

        LogTool.w("onTouchEvent  "+action);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 鼠标左键
                if ((event.getButtonState() & MotionEvent.BUTTON_PRIMARY) != 0) {
                    if (isHitBar(event.getY())) {
                        dragging = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (dragging) {
                    scrollRecyclerByY(event.getY());
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                dragging = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isHitBar(float y) {
        return y >= barTop && y <= barTop + barHeight;
    }

    private void scrollRecyclerByY(float y) {
        if (recyclerView == null) return;

        float percent = y / getHeight();
        percent = Math.max(0f, Math.min(1f, percent));

        int totalScrollRange =
                recyclerView.computeVerticalScrollRange()
                        - recyclerView.computeVerticalScrollExtent();

        int targetOffset = (int) (totalScrollRange * percent);
        int currentOffset = recyclerView.computeVerticalScrollOffset();

        recyclerView.scrollBy(0, targetOffset - currentOffset);
    }
}
