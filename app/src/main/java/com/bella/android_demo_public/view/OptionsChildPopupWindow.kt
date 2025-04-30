package com.bella.android_demo_public.view

import android.app.UiModeManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.SimpleAdapter
import androidx.core.content.ContextCompat.getSystemService
import com.bella.android_demo_public.MainActivity
import com.bella.android_demo_public.R
import com.bella.android_demo_public.utils.LogTool


class OptionsChildPopupWindow(
    contentView: View,
    width: Int,
    height: Int,
    val redId:Int,
    val mainActivity: MainActivity
) : PopupWindow(contentView, width, height,false) {

    init {
        initView()
    }

    fun initView(){
        var listView = contentView.findViewById<ListView>(R.id.listView)

        val items = mainActivity.resources.getStringArray(redId)
        val data = mutableListOf<Map<String, String>>()

        for (item in items) {
            val map = mutableMapOf<String, String>()
            map["name"] = item // 键名 "name" 用于标识数据
            data.add(map)
        }

        val adapter = SimpleAdapter(
            mainActivity,  // 当前上下文
            data,  // 数据源
            R.layout.item_child,  // 使用系统提供的布局
            arrayOf<String>("name"),  // 数据源中的键名数组
            intArrayOf(R.id.txtTitle) // 布局中的视图 ID 数组
        )

//        val adapter = ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, items.toList())

        listView?.adapter = adapter


//        listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            // 获取被点击的条目
//            val uiModeManager: UiModeManager = mainActivity?.getSystemService(
//                UiModeManager::class.java
//            )
//            val  mp = parent.getItemAtPosition(position) as Map<String, String>;
//             val name = mp.get("name");
//             val  res = mainActivity.resources ;
//             LogTool.i("Clicked---name : ${name}")
//             if(res.getStringArray(R.array.theme_options).get(0).equals(name)){
//                 LogTool.i("Clicked--1-name : ${name}")
//                 // 切换到深色模式
//                 uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
//
////                 uiModeManager.setNightModeActivated(true);
//             }else if(res.getStringArray(R.array.theme_options).get(1).equals(name)){
//                 LogTool.i("Clicked--2-name : ${name}")
//                 uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
//             }
//            dismiss()
//        }


    }


}