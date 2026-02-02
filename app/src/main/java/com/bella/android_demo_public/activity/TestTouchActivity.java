package com.bella.android_demo_public.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.adapter.FragmentPagerAdapterDemo;
import com.bella.android_demo_public.fragment.LoginFragment;

public class TestTouchActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.act_login_register);

        viewPager = findViewById(R.id.view_pager);
        setupViewPager();

    }


    private void setupViewPager() {
        // 使用 FragmentPagerAdapter（适合少量固定页面）
        FragmentPagerAdapterDemo adapter = new FragmentPagerAdapterDemo(getSupportFragmentManager());

        // 添加 Fragment
        adapter.addFragment(LoginFragment.newInstance("login Page"), "login");

        viewPager.setAdapter(adapter);


    }
}