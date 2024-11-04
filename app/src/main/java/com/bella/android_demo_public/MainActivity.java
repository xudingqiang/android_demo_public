package com.bella.android_demo_public;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bella.android_demo_public.activity.EglTestActivity;
import com.bella.android_demo_public.activity.LibpagDemoActivity;
import com.bella.android_demo_public.activity.RoomTestActivity;

public class MainActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();

    }

    private void initView(){
        TextView test1 = findViewById(R.id.test1);
        test1.setText("libpag动画测试");
        test1.setOnClickListener(view ->{
            startActivity(new Intent(context, LibpagDemoActivity.class));
        });

        TextView test2 = findViewById(R.id.test2);
        test2.setText("EGL测试");
        test2.setOnClickListener(view ->{
            startActivity(new Intent(context, EglTestActivity.class));
        });

        TextView test3 = findViewById(R.id.test3);
        test3.setText("Room测试");
        test3.setOnClickListener(view ->{
            startActivity(new Intent(context, RoomTestActivity.class));
        });


    }

}