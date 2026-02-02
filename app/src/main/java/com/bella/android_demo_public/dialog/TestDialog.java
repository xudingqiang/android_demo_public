package com.bella.android_demo_public.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.backup.BackupAgent;
import android.content.ContextWrapper;
import android.content.MutableContextWrapper;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.window.WindowContext;
import com.android.internal.policy.DecorContext;
import android.app.Application;

import android.content.Context;

import android.widget.TextView;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.utils.AdvancedClassPrinter;
import com.bella.android_demo_public.utils.LogTool;

public class TestDialog extends Dialog {
    Context context;
    TextView txtAppName;

    public TestDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_layout);
        txtAppName = findViewById(R.id.txtAppName);

        LogTool.w("TestDialog....onCreate............");

        AdvancedClassPrinter.printFullTree(Context.class);

        if (txtAppName.getContext() instanceof Activity) {
            LogTool.w("Activity................");
        } else if (txtAppName.getContext() instanceof ContextThemeWrapper) {
            LogTool.w("ContextThemeWrapper................");
        } else if (txtAppName.getContext() instanceof DecorContext) {
            LogTool.w("ContextWrapper ................");
        }else if (txtAppName.getContext() instanceof WindowContext) {
            LogTool.w("ContextWrapper ................");
        }else if (txtAppName.getContext() instanceof Application) {
            LogTool.w("ContextWrapper ................");
//        }else if (txtAppName.getContext() instanceof RemoteViews) {
//            LogTool.w("ContextWrapper ................");
        }else if (txtAppName.getContext() instanceof MutableContextWrapper) {
            LogTool.w("ContextWrapper ................");
        }else if (txtAppName.getContext() instanceof BackupAgent) {
            LogTool.w("ContextWrapper ................");
        }else if (txtAppName.getContext() instanceof ContextWrapper) {
            LogTool.w("ContextWrapper ................");
        }



    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            // 关键判断：Dialog 的窗口类型通常大于 TYPE_APPLICATION (2)
            LogTool.w("TestDialog...........params.type  "+params.type  + ",params.flags "+params.flags);
            params.width = 120;
            window.setAttributes(params);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogTool.w("TestDialog....onStart............");


    }
}
