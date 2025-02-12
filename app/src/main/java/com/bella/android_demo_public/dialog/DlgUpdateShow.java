package com.bella.android_demo_public.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bella.android_demo_public.R;


public class DlgUpdateShow extends Dialog implements View.OnClickListener {
    String packageUrl;
    String packageDesc;
    Activity context;
    TextView txtDesc;
    TextView txtCancel;
    TextView txtUpdate;

    public DlgUpdateShow(@NonNull Activity context, String packageDesc, String packageUrl) {
        super(context);
        this.packageUrl = packageUrl;
        this.packageDesc = packageDesc;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_show);
        txtDesc = (TextView)findViewById(R.id.txtDesc);
        txtCancel = (TextView)findViewById(R.id.txtCancel);
        txtUpdate = (TextView)findViewById(R.id.txtUpdate);

        txtCancel.setOnClickListener(this);
        txtUpdate.setOnClickListener(this);
        txtDesc.setText(packageDesc);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txtCancel){
            dismiss();
        }else {
            DlgUpdate dlgUpdate = new DlgUpdate(context, packageUrl);
            if (!dlgUpdate.isShowing()) {
                dlgUpdate.show();
            }
            dismiss();
        }
    }
}
