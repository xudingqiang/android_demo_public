package com.bella.android_demo_public.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class TestLayout extends RecyclerView {
    public TestLayout(@NonNull Context context) {
        super(context);
    }

    public TestLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
