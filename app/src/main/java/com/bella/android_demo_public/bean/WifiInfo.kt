package com.bella.android_demo_public.bean

data class WifiInfo(
    var id: Int = 0,
    var wifiName: String = "",
    var signal: Int = 0,
    var wifiType: String = "",
    var encryption: String = "",
    var hasPwd: Boolean = false,
    var isSaved: Boolean = false
)
