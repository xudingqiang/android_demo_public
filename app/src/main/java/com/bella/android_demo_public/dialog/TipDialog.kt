package com.bella.android_demo_public.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.NonNull
import com.bella.android_demo_public.R

class TipDialog(@NonNull context: Context, private val content: String) : Dialog(context) {
    private lateinit var txtContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_tip_view)
//        setLayout()
        txtContent = findViewById(R.id.txtContent)!!
        // txtContent.text = content
        txtContent.text = content
        // parseJson(content)
    }

    private fun setLayout() {
        window?.setGravity(Gravity.CENTER)
        val m: WindowManager = window?.windowManager!!
        val d = m.defaultDisplay
        val p = window?.attributes
        window?.setType(WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW + 24)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        p?.x = 0
        p?.y = 0
        p?.width = WindowManager.LayoutParams.WRAP_CONTENT
        p?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = p
    }
}