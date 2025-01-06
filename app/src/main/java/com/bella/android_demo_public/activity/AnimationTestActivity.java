package com.bella.android_demo_public.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bella.android_demo_public.R;

public class AnimationTestActivity extends AppCompatActivity {
    Button btnStart ;
    Button btnEnd ;
    ImageView imgView;
    AnimationDrawable animationDrawable ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test);
        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        imgView = findViewById(R.id.imgView);

        imgView.setBackgroundResource(R.drawable.frame_animation);
        animationDrawable = (AnimationDrawable)imgView.getBackground();

        btnStart.setOnClickListener(view ->{
            animationDrawable.start();
        });


        btnEnd.setOnClickListener(view ->{
            animationDrawable.stop();
        });

    }
}