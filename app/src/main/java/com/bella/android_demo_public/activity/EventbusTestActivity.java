package com.bella.android_demo_public.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class EventbusTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus_test);
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(view ->{
            EventBus.getDefault().post(new MessageEvent("Hello from EventBus!"));
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}