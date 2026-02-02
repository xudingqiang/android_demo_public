# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 1. 保留整个 openfde 包下的所有类及其成员
# 确保 SDK 开放接口的类名、方法名和变量名不被混淆
-keep class android.openfde.** {
    *;
}

# 2. 特别保留内部接口及常量
# 防止 AppTaskStatusListener 和 WmShellCaller 中的 OPERATION_ 常量被内联或删除
-keepnames interface android.openfde.AppTaskStatusListener {
    static final int *;
}

-keepnames interface android.openfde.WmShellCaller {
    static final int *;
}

# 3. 保留特定的回调方法
# 确保系统底层在调用这些回调时能通过名称找到对应方法
-keepclassmembers interface * extends android.openfde.AppTaskStatusListener {
    void onStatusChanged(int, boolean);
}

# 4. 忽略与系统库相关的警告（可选）
# 如果你的 SDK 引用了某些隐藏的系统类 (@hide)，可以添加此项以防止编译警告导致失败
-dontwarn android.openfde.**