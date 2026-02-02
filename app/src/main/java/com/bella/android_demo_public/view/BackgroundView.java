package com.bella.android_demo_public.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class BackgroundView extends View {
    private final Path path;
    private final Paint paint;

    public BackgroundView(Context context) {
        super(context);
        path = new Path();
        paint = new Paint();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        int width = getWidth();
        int height = getHeight();
        int tileSize = (int)(8 *  getContext().getResources().getDisplayMetrics().density);
        for (int y = 0; y < height; y += tileSize) {
            boolean draw = (y / tileSize) % 2 == 1;
            for (int x = 0; x < width; x += tileSize) {
                if (draw) {
                    path.addRect(x, y, x + tileSize, y + tileSize, Path.Direction.CCW);
                }
                draw = !draw;
            }
        }
        paint.setColor(Color.argb(255, 204, 204, 204));
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
