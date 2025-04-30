package com.bella.android_demo_public.activity;

import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bella.android_demo_public.R;

import java.util.concurrent.ConcurrentHashMap;

public class GaussianBlurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaussian_blur);

        // 获取背景图片视图
        View backgroundImage = findViewById(R.id.backgroundImage);

        // 创建高斯模糊效果
        RenderEffect blurEffect = RenderEffect.createBlurEffect(
                25f, // 模糊半径 X
                25f, // 模糊半径 Y
                Shader.TileMode.CLAMP // 边缘处理模式
        );

        // 将模糊效果应用到背景图片上
        backgroundImage.setRenderEffect(blurEffect);

    }
}