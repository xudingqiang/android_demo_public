package com.bella.android_demo_public.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bella.android_demo_public.BellaDataBase;
import com.bella.android_demo_public.R;
import com.bella.android_demo_public.bean.User;
import com.bella.android_demo_public.utils.LogTool;
import com.bella.android_demo_public.utils.Utils;

import java.util.List;

public class RoomTestActivity extends AppCompatActivity implements View.OnClickListener {
    TextView test, test1, test2, test3, test4;
    TextView test5, test6;
    Context context;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private float mTouchX, mTouchY;

    Handler mHander = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                String list = msg.obj.toString();
                test.setText(list);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_test);
        context = this;


        mWindowManager = getWindowManager();
        mLayoutParams = getWindow().getAttributes();

        test = findViewById(R.id.test);
        test1 = findViewById(R.id.test1);
        test2 = findViewById(R.id.test2);
        test3 = findViewById(R.id.test3);
        test4 = findViewById(R.id.test4);
        test5 = findViewById(R.id.test5);
        test6 = findViewById(R.id.test6);

        test1.setOnClickListener(this);
        test2.setOnClickListener(this);
        test3.setOnClickListener(this);
        test4.setOnClickListener(this);
        test5.setOnClickListener(this);
        test6.setOnClickListener(this);
//
//        // 设置窗口大小和位置
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.x = 0;
//        params.y = 0;
//        getWindow().setAttributes(params);
//
//        // 初始化拖拽控制器
//        View dragHandle = findViewById(R.id.test);
//        new FreeformWindowDragger(this, dragHandle);

    }



    private void queryList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> list = BellaDataBase.getInstance(context).userDao().getAllUserList();
                Message msg = new Message();
                msg.what = 1;
                msg.obj = list;
                mHander.sendMessage(msg);
            }
        }).start();
    }

    private void insertUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                List<User> list = BellaDataBase.getInstance(context).userDao().getAllUserList();
                int size = list.size()+1;
                user.setId(size);
                user.setUserName("bella_"+size);
                user.setUserAge(28);
                user.setAddress("湖南省衡阳市耒阳市");
                user.setNickName("衬衬");
                BellaDataBase.getInstance(context).userDao().insert(user);
                queryList();
            }
        }).start();
    }


    private void updateUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setId(1);
                user.setUserName("bella");
                user.setUserAge(28);
                user.setAddress("湖南省衡阳市耒阳市");
                user.setNickName("颖子");
                BellaDataBase.getInstance(context).userDao().update(user);
                queryList();
            }
        }).start();
    }


    private void deleteUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> list = BellaDataBase.getInstance(context).userDao().getAllUserList();
                int size = list.size();
                User user = new User();
                user.setId(size);
                BellaDataBase.getInstance(context).userDao().delete(user);
                queryList();
            }
        }).start();
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.test1){
            queryList();
            LogTool.w("this.getLocalClassName() "+this.getLocalClassName());
        }else if(view.getId() == R.id.test2){
            insertUser();
        }else if(view.getId() == R.id.test3){
            updateUser();
        }else if(view.getId() == R.id.test4){
            deleteUser();
        }else if(view.getId() == R.id.test5){
//            CompatibleConfig.parseValueXML(RoomTestActivity.this,"");
            Utils.parseValue(RoomTestActivity.this,context.getResources().openRawResource(R.raw.comp_config_value));
        }else if(view.getId() == R.id.test6){

        }

    }
}