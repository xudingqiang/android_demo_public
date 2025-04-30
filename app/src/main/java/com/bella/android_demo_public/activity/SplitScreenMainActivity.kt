package com.bella.android_demo_public.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowMetrics
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.window.layout.WindowMetricsCalculator
import com.bella.android_demo_public.R
import com.bella.android_demo_public.utils.LogTool


class SplitScreenMainActivity : AppCompatActivity() {
    var context: Context ? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this ;
        setContentView(R.layout.activity_split_screen_main)
        enableSplitScreen()

        val startDetailActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {}

        startChildPage()

        val startButton = findViewById<Button>(R.id.startDetailActivity)
        startButton?.setOnClickListener {
//            val intent = Intent(this, SplitScreenChildActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startDetailActivityLauncher.launch(intent)
//            overridePendingTransition(0, 0)
            startChildPage()
        }


        val btnFinish = findViewById<Button>(R.id.btnFinish)
        btnFinish?.setOnClickListener {
            recreate()
//            finish()
        }


    }

    fun startChildPage(){
        val intent = Intent(context,SplitScreenChildActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogTool.i("main--onTouchEvent x: " + event?.x + " ,y:  " + event?.y)
        return super.onTouchEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogTool.i("main--onDestroy ..........." )

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogTool.i("main--onConfigurationChanged ..........."+newConfig.screenWidthDp  +" : ")
        val rect = Rect();
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth: Int = displayMetrics.widthPixels
        val screenHeight: Int = displayMetrics.heightPixels


        val rootView = findViewById<LinearLayout>(R.id.rootView);
        val height = rootView?.height
        val width = rootView?.width
        val metrics: DisplayMetrics = getResources().getDisplayMetrics()
        val density = metrics.density
        val heightPixels = metrics.heightPixels
        val widthPixels = metrics.widthPixels


        // 获取当前窗口的尺寸
        val windowMetrics: androidx.window.layout.WindowMetrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(
                this
            )
        val windowWidth: Int = windowMetrics.bounds.width()
        val windowHeight: Int = windowMetrics.bounds.height()

//        val params = window.attributes
//        val windowWidth = params.width
//        val windowHeight = params.height


        rootView?.post(Runnable {
            val windowMetrics: androidx.window.layout.WindowMetrics? =
                context?.let {
                    WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(
                        it
                    )
                }
            val windowWidth = windowMetrics?.bounds?.width()
            val windowHeight = windowMetrics?.bounds?.height()
            LogTool.i("main rootView windowWidth: " + windowWidth + ",windowHeight: " + windowHeight)

        })

        LogTool.i("main windowWidth: " + windowWidth + ",windowHeight: " + windowHeight)
        LogTool.i("main screenWidth: " + screenWidth + ",screenHeight: " + screenHeight + ", width " + width + ",height " + height)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val rootView = findViewById<LinearLayout>(R.id.rootView);
        val height = rootView?.height
        val width = rootView?.width
        LogTool.i("main width " + width + ",height " + height)
    }

    private fun enableSplitScreen() {

    }
}