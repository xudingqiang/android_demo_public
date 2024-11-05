package com.bella.android_demo_public.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @desc: 打印类
 */
public class LogTool {
    private final static boolean isLog = true;

    public static String TAR = "bellaTest";

    /**
     * debug消息
     * 默认标识
     *
     * @param msg
     */
    public static void d(String msg) {
        d(TAR, msg);
    }

    /**
     * debug消息
     *
     * @param tar
     * @param msg
     */
    public static void d(String tar, String msg) {
        if (isLog) {
            Log.d(tar, msg);
        }
    }

    /**
     * error消息
     * 默认标识
     *
     * @param msg
     */
    public static void e(String msg) {
        e(TAR, msg);
    }

    /**
     * error消息
     *
     * @param tar
     * @param msg
     */
    public static void e(String tar, String msg) {
        if (isLog) {
            Log.e(tar, msg);
        }
    }

    /**
     * warn消息
     * 默认标识
     *
     * @param msg
     */
    public static void w(String msg) {
        w(TAR, msg);
    }

    /**
     * warn消息
     *
     * @param tar
     * @param msg
     */
    public static void w(String tar, String msg) {
        if (isLog) {
            Log.w(tar, msg);
        }
    }

    /**
     * info消息
     * 默认标识
     *
     * @param msg
     */
    public static void i(String msg) {
        i(TAR, msg);
    }

    /**
     * info消息
     *
     * @param tar
     * @param msg
     */
    public static void i(String tar, String msg) {
        if (isLog) {
            Log.i(tar, msg);
        }
    }

    /**
     * 日志写成文件
     *
     * @param log
     */
    private static void saveLog2File(final String log) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String d = sdf.format(new Date());
                String result = d + ": " + log;
                StringBuffer sb = new StringBuffer();
                sb.append(result);
                try {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    long timetamp = System.currentTimeMillis();
                    String time = formatter.format(new Date());
                    String fileName = time + ".log";
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        String path = Environment.getExternalStorageDirectory().toString() + File.separator + "GCS" + File.separator + "LOG";
                        File dir = new File(path);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        FileOutputStream fos = new FileOutputStream(new File(path + File.separator + fileName), true);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                        osw.write(sb.toString());
                        osw.write("\n");
                        osw.flush();
                        osw.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogTool.e("an error occured while writing file ...");
                }
                return null;
            }
        }.execute();
    }
}
