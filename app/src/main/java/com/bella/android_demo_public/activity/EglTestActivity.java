package com.bella.android_demo_public.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.view.MyGLSurfaceView;

public class EglTestActivity extends AppCompatActivity {
    MyGLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egl_test);
        glSurfaceView = findViewById(R.id.gl_surface_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();

    }
}