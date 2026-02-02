package com.bella.android_demo_public.utils;

public class JniUtils {
    static {
        System.loadLibrary("jnidemo"); // 加载动态库，名称与 CMakeLists.txt 中定义的一致
    }

    // 声明 native 方法
    public native String stringFromJNI();
    public native int add(int a, int b);
    public native void printHelloWorld();
}
