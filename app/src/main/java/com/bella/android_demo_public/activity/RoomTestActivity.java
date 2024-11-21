package com.bella.android_demo_public.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bella.android_demo_public.BellaDataBase;
import com.bella.android_demo_public.R;
import com.bella.android_demo_public.bean.User;

import java.util.List;

public class RoomTestActivity extends AppCompatActivity implements View.OnClickListener {
    TextView test, test1, test2, test3, test4;
    Context context;

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

        test = findViewById(R.id.test);
        test1 = findViewById(R.id.test1);
        test2 = findViewById(R.id.test2);
        test3 = findViewById(R.id.test3);
        test4 = findViewById(R.id.test4);

        test1.setOnClickListener(this);
        test2.setOnClickListener(this);
        test3.setOnClickListener(this);
        test4.setOnClickListener(this);

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
        switch (view.getId()) {
            case R.id.test1:
                queryList();
                break;

            case R.id.test2:
                insertUser();
                break;

            case R.id.test3:
                updateUser();
                break;

            case R.id.test4:
                deleteUser();
                break;
        }
    }
}