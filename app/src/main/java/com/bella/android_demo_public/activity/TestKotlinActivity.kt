package com.bella.android_demo_public.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.bella.android_demo_public.R
import com.bella.android_demo_public.utils.LogTool

class TestKotlinActivity : AppCompatActivity() {
    private var windowContentView: View? = null
    private var txtTestKotlin: TextView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_test_kotlin)

        txtTestKotlin = findViewById<TextView>(R.id.txtTestKotlin)

//        windowContentView = LayoutInflater.from(this).inflate(R.layout.layout_all_apps, null)

//        val testLayout = windowContentView?.findViewById<TestLayout>(R.id.testLayout)
//        if(testLayout is TestLayout){
//            LogTool.i("111111111111")
//        }else{
//            LogTool.i("0000000000")
//        }

        txtTestKotlin?.setOnClickListener {
            createPinnedShortcut(this);
        }

    }

    fun createPinnedShortcut(context: Context) {
        LogTool.i("createPinnedShortcut.........")
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            val shortcutInfo = ShortcutInfoCompat.Builder(context, "my-shortcut-id")
                .setShortLabel("My Shortcut")
                .setLongLabel("Open My App Feature")
                .setIcon(
                    IconCompat.createWithResource(
                        context,
                        R.drawable.icon_down
                    )
                ) // Replace with your icon
                .setIntent(
                    Intent(context, LibpagDemoActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                    }
                )
                .build()

            // Request the shortcut to be pinned
            ShortcutManagerCompat.requestPinShortcut(context, shortcutInfo, null)
        } else {
            LogTool.e("isRequestPinShortcutSupported error.........")
        }
    }
}