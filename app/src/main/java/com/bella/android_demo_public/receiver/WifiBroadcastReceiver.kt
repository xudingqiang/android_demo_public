package com.bella.android_demo_public.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.bella.android_demo_public.MainActivity
import com.bella.android_demo_public.utils.LogTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WifiBroadcastReceiver() : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        LogTool.w("33 WifiBroadcastReceiver  action: $action")

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        // 获取当前活动的网络信息
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo

        // 判断网络是否已连接
        val isConnected = (activeNetworkInfo != null) && activeNetworkInfo.isConnected
        if (isConnected) {
            // 执行网络连接时的操作，例如更新UI、重新发起请求
            Toast.makeText(context, "网络已连接", Toast.LENGTH_SHORT).show()
        } else {
            // 执行网络断开时的操作
            Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show()
        }

        GlobalScope.launch (Dispatchers.IO) {

            val inte = Intent(MainActivity.TEST_ACTION2)
            inte.putExtra("wifiStatus", 1)
            inte.setPackage(context.packageName)
            context.sendBroadcast(inte)

        }

//        if (WifiManager.WIFI_STATE_CHANGED_ACTION == action) {
//            // WiFi 状态变化（开启、关闭等）
//            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1)
//            when (wifiState) {
//                WifiManager.WIFI_STATE_DISABLED ->                     // WiFi 已关闭
//                    LogTool.w("wifi is close")
//
//                WifiManager.WIFI_STATE_ENABLED ->                     // WiFi 已开启
//                    LogTool.w("wifi is open")
//            }
//        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION == action) {
//            // WiFi 连接状态变化（连接、断开等）
//            val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
//            if (networkInfo != null && networkInfo.isConnected) {
//                // WiFi 已连接
//                LogTool.w("wifi is Connected")
//            } else {
//                // WiFi 已断开
//                LogTool.w("wifi is Not Connected")
//            }
//        }
    }
}
