package com.bella.android_demo_public;

import android.os.FileObserver;
import android.util.Log;

public class MyFileObserver extends FileObserver {
    private static final String TAG = "MyFileObserver";

    public MyFileObserver(String path) {
        super(path, FileObserver.ALL_EVENTS); // 监听所有事件
    }

    @Override
    public void onEvent(int event, String path) {
        if (path == null) {
            return;
        }

        switch (event) {
            case FileObserver.ACCESS:
                Log.w(TAG, "File accessed: " + path);
                break;
            case FileObserver.MODIFY:
                Log.w(TAG, "File modified: " + path);
                break;
            case FileObserver.ATTRIB:
                Log.w(TAG, "File attributes changed: " + path);
                break;
            case FileObserver.CLOSE_WRITE:
                Log.w(TAG, "File closed after writing: " + path);
                break;
            case FileObserver.CLOSE_NOWRITE:
                Log.w(TAG, "File closed without writing: " + path);
                break;
            case FileObserver.OPEN:
                Log.w(TAG, "File opened: " + path);
                break;
            case FileObserver.MOVED_FROM:
                Log.w(TAG, "File moved from: " + path);
                break;
            case FileObserver.MOVED_TO:
                Log.w(TAG, "File moved to: " + path);
                break;
            case FileObserver.CREATE:
                Log.w(TAG, "File created: " + path);
                break;
            case FileObserver.DELETE:
                Log.w(TAG, "File deleted: " + path);
                break;
            case FileObserver.DELETE_SELF:
                Log.w(TAG, "Directory deleted: " + path);
                break;
            case FileObserver.MOVE_SELF:
                Log.w(TAG, "Directory moved: " + path);
                break;
            default:
                Log.w(TAG, "Unknown event on file: " + path);
                break;
        }
    }
}
