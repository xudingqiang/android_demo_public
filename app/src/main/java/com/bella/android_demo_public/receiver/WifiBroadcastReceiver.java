package com.bella.android_demo_public.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.bella.android_demo_public.utils.LogTool;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
            // WiFi 状态变化（开启、关闭等）
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    // WiFi 已关闭
                    LogTool.w("wifi is close");
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    // WiFi 已开启
                    LogTool.w("wifi is open");
                    break;
            }
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            // WiFi 连接状态变化（连接、断开等）
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.isConnected()) {
                // WiFi 已连接
                LogTool.w("wifi is Connected");
            } else {
                // WiFi 已断开
                LogTool.w("wifi is Not Connected");
            }
        }
    }
}
