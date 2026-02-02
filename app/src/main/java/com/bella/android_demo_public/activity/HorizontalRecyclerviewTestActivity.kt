package com.bella.android_demo_public.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bella.android_demo_public.R
import com.bella.android_demo_public.adapter.HorizontalAdapter
import com.bella.android_demo_public.utils.LogTool

class HorizontalRecyclerviewTestActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_recyclerview_test)
        val config = Configuration(getResources().getConfiguration())

        LogTool.w("screenWidthDp " + config.screenWidthDp + " ,smallestScreenWidthDp: " + config.smallestScreenWidthDp)

        // 1. 初始化 RecyclerView
        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHorizontal)!!

        // 2. 关键步骤：设置布局管理器为横向线性布局
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // 3. 准备模拟数据
        val itemList = listOf(
            HorizontalAdapter.DemoItem("项目一", "描述一", R.drawable.ic_settings_location),
            HorizontalAdapter.DemoItem("项目二", "描述二", R.drawable.ic_settings_about_device),
            HorizontalAdapter.DemoItem("项目三", "描述三", R.drawable.ic_settings_wireless),
            HorizontalAdapter.DemoItem("项目四", "描述四", R.drawable.ic_settings_safety_center),
            // ... 可以添加更多数据
        )

        // 4. 创建适配器并设置
        val adapter = HorizontalAdapter(itemList)
        recyclerView.adapter = adapter

        recyclerView.setClipToPadding(false)
        recyclerView.setClipChildren(false)

        // 5. 可选：添加 item 装饰（例如，给最后一项右边也添加间距）
//        recyclerView.addItemDecoration(
//            HorizontalItemDecorati(
//                itemMargin = resources.getDimensionPixelSize(R.dimen.item_margin)
//            )
//        )
    }
}