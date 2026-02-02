package com.bella.android_demo_public.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.bella.android_demo_public.R
import com.bella.android_demo_public.utils.LogTool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TestKotlinActivity : AppCompatActivity() {
    private var windowContentView: View? = null
    private var txtTestKotlin: TextView? = null;
    var context :Context? = null ;
    var imgView: ImageView? = null
    var animationDrawable: AnimationDrawable? = null

    var btnStart: Button? = null
    var btnEnd: Button? = null
    var btnTest1: Button? = null
    var btnTest2: Button? = null
    var btnTest3: Button? = null

    private val arrayTop =
        arrayOf("dark_ui_mode", "toggle_keyboard_sticky_keys", "toggle_audio_description")
    private val arrayBottom = arrayOf("screen_timeout", "toggle_keyboard_bounce_keys")
    private val arraySquare = arrayOf("reduce_bright_colors_preference")

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
        btnStart = findViewById(R.id.btnStart)
        btnEnd = findViewById(R.id.btnEnd)
        btnTest1 = findViewById(R.id.btnTest1)
        btnTest2 = findViewById(R.id.btnTest2)
        btnTest3 = findViewById(R.id.btnTest3)
        imgView = findViewById(R.id.imageView)


        val imageUri = intent.data


        val redId = resources.getIdentifier("status_bar_height","dimen","android");
        val height = resources.getDimensionPixelSize(redId)

        val wi = "am-guest:44::";
        val arrInfo = wi.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        LogTool.w("arrInfo "+arrInfo.size)
//        imgView?.setBackgroundResource(R.drawable.frame_animation)
        animationDrawable = imgView!!.background as AnimationDrawable

        btnTest1?.setOnClickListener({
//            val intent = Intent()
//            val cn: ComponentName? = ComponentName.unflattenFromString("com.android.settings/.Settings\$PrivacyDashboardActivity")
//            intent.component = cn;
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                // 传入当前应用的包名（必须，否则无法定位到具体应用的权限）
                intent.data = Uri.parse("package:${packageName}")
                startActivity(intent)
            } else {
                // 旧版本（Android 7.0及以下）：打开应用详情页面（需用户手动操作）
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${packageName}")
                startActivity(intent)
            }
        })

        btnTest2?.setOnClickListener({
            val intent = Intent()
            val cn: ComponentName? = ComponentName.unflattenFromString("com.android.settings/.Settings\$PrivacyDashboardActivity")
            intent.component = cn;
            intent.putExtra("packageName",packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        })

        btnTest3?.setOnClickListener({
            val intent = Intent()
            val cn: ComponentName? = ComponentName.unflattenFromString("com.android.settings/.Settings\$PrivacyDashboardActivity")
            intent.component = cn;
            intent.putExtra("keyCode","isShiftContentBelowCaption")
            intent.putExtra("activityName","TestKotlinActivity")
//            intent.putExtra("packageName",packageName)
            intent.putExtra("packageName","com.tencent.mm")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        })

        btnStart!!.setOnClickListener { view: View? ->
            animationDrawable!!.start()
        }


        btnEnd!!.setOnClickListener { view: View? ->
            animationDrawable!!.stop()
        }


        val job = GlobalScope.launch {
            repeat(1) { i ->
                animationDrawable!!.start()


            }
        }



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