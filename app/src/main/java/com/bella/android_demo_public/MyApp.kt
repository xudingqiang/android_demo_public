package com.bella.android_demo_public

import android.app.Application
import com.squareup.leakcanary.core.BuildConfig
import leakcanary.LeakCanary
import shark.AndroidReferenceMatchers
import shark.IgnoredReferenceMatcher

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
}