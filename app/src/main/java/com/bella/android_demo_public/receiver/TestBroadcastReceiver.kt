package com.bella.android_demo_public.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bella.android_demo_public.utils.LogTool

class TestBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        LogTool.w("88 TestBroadcastReceiver  action: $action")
    }
}