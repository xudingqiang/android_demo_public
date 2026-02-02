package com.bella.android_demo_public.utils;

public class AdvancedClassPrinter {
    private static final String TAG = "ClassAnalyzer";

    public static void printFullTree(Class<?> clazz) {
        printFullTree(clazz, 0);
    }

    private static void printFullTree(Class<?> clazz, int depth) {
        if (clazz == null) return;

        // 格式化输出
        String indent = "  ".repeat(depth);
        String className = clazz.getName();

        // 判断是否Android系统类
        String prefix = isAndroidClass(clazz) ? "[A] " : "[U] ";

        LogTool.w(indent + prefix + className);

        // 递归打印父类
        printFullTree(clazz.getSuperclass(), depth + 1);

        // 打印接口
        if (depth == 0) {  // 只在顶级类打印接口
            for (Class<?> iface : clazz.getInterfaces()) {
                LogTool.w( indent + "  Implements: " + iface.getName());
            }
        }
    }

    private static boolean isAndroidClass(Class<?> clazz) {
        return clazz.getName().startsWith("android.") ||
                clazz.getName().startsWith("androidx.");
    }
}
