package com.bella.android_demo_public

import android.app.Application
import leakcanary.LeakCanary
import shark.AndroidReferenceMatchers

class MyApp : Application() {




    override fun onCreate() {
        super.onCreate()
        val config = LeakCanary.config.copy(
            dumpHeap = true, // 只在debug模式下dump堆
            retainedVisibleThreshold = 3, // 当泄漏对象数达到3个时才显示通知
            referenceMatchers = AndroidReferenceMatchers.appDefaults +
                    AndroidReferenceMatchers.staticFieldLeak(
                        className = "com.bella.android_demo_public",
                        fieldName = "sInstance"
                    ) // 添加自定义的引用匹配器
        )
        LeakCanary.config = config
    }

    companion object {
        const val APPID: String = "5d1329a5"
        const val APIKEY: String = "4c02ca28595fa5c15070bdb9a55e98b5"
        const val APISECRET: String = "68c81c30bb454ece0db99769c43b5837"
        const val WORK_DIR: String = "/sdcard/iflytek/"
    }
}