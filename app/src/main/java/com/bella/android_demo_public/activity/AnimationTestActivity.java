package com.bella.android_demo_public.activity;

import android.content.Context;
import android.graphics.Insets;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.utils.LogTool;

public class AnimationTestActivity extends AppCompatActivity {
    Button btnStart ;
    Button btnEnd ;
    ImageView imgView;
    AnimationDrawable animationDrawable ;
    private static Context leakContext;
    private static AnimationTestActivity animationTestActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_animation_test);
        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        imgView = findViewById(R.id.imgView);

        // 故意制造泄漏
        leakContext = this;
        animationTestActivity =  this ;

        imgView.setBackgroundResource(R.drawable.frame_animation);
        animationDrawable = (AnimationDrawable)imgView.getBackground();

        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        long usedMemory = memoryInfo.getTotalPss() * 1024L;
        LogTool.w("usedMemory "+usedMemory);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                // 获取标题栏的内边距
                Insets captionBarInsets = insets.getInsets(WindowInsets.Type.captionBar());
                Log.d("bellaTest", "WindowInsets Caption Bar Insets: " + captionBarInsets);


                // 根据标题栏的内边距调整视图布局
                // 例如，为视图添加内边距或外边距
                v.setPadding(
                        0 , captionBarInsets.top , 0 , 0
                );


//                v.setPaddingRelative(0,-30,0,0);


                // 返回处理后的内边距
                return insets;
            });
        }

        btnStart.setOnClickListener(view ->{
            animationDrawable.start();
        });


        btnEnd.setOnClickListener(view ->{
            animationDrawable.stop();
        });

    }


//    private void checkForMemoryLeaks() {
//        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
//        Debug.getMemoryInfo(memoryInfo);
//
//        // 检查Native内存异常增长
//        if (memoryInfo.nativePss > NATIVE_LEAK_THRESHOLD) {
//            Log.w("MemoryMonitor", "Possible native memory leak detected");
//            analyzeNativeMemory();
//        }
//
//        // 检查Dalvik内存异常增长
//        if (memoryInfo.dalvikPss > DALVIK_LEAK_THRESHOLD) {
//            Log.w("MemoryMonitor", "Possible Java memory leak detected");
//            analyzeJavaMemory();
//        }
//    }
}