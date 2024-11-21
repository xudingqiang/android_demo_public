package com.bella.android_demo_public;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bella.android_demo_public.activity.EglTestActivity;
import com.bella.android_demo_public.activity.LibpagDemoActivity;
import com.bella.android_demo_public.activity.RoomTestActivity;
import com.bella.android_demo_public.utils.LogTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context;
    LinearLayout layoutParent ;

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        // 循环遍历父类，获取所有声明的字段
        while (clazz != null) {
            // 获取当前类声明的所有字段
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                fields.add(field);
            }
            // 继续获取父类的字段
            clazz = clazz.getSuperclass();
        }

        return fields;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        // 设置Activity的窗口风格
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.white));

        setContentView(R.layout.activity_main);
        context = this;
        initView();

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide(); // 隐藏 ActionBar
//            // getSupportActionBar().show(); // 显示 ActionBar
//        }

//        WindowInsetsController controller = getWindow().getInsetsController();
//        if (controller != null) {
//            controller.hide(WindowInsets.Type.systemBars());
//            controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
//        }
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



        LinearLayout layoutParent = new LinearLayout(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_file_controller, layoutParent, false);
        layoutParent.addView(view);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParent.setTag("DecorCaptionView");
        getWindow().addContentView(layoutParent,params);



    }

}