package com.bella.android_demo_public.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.SeekBar;

import com.bella.android_demo_public.utils.LogTool;

public class FdeSeekBar extends SeekBar {
    private Paint paint;
    private int progress = 0 ;
    private String strContent = "";


    public FdeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public FdeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FdeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FdeSeekBar(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD ));

//        paint.setColor(Color.BLACK);
    }

    public void updatePro(int progress ){
        this.progress = progress ;
        invalidate();
    }

    public void updateContent(String strContent){
        this.strContent = strContent;
        invalidate();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(strContent,progress,30,paint);
    }

}
