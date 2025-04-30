package com.bella.android_demo_public.activity

import android.app.ActivityManager
import android.app.ActivityManager.AppTask
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowMetricsCalculator
import com.bella.android_demo_public.R
import com.bella.android_demo_public.utils.LogTool


class SplitScreenChildActivity : AppCompatActivity() {
    var context: Context? = null;
    private lateinit var windowInfoTracker: WindowInfoTracker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_split_screen_child)
        windowInfoTracker = WindowInfoTracker.getOrCreate(this)


        val btnFinish = findViewById<Button>(R.id.btnFinish)
        btnFinish?.setOnClickListener {
            forceReLayout()
//            finish()
        }

//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                windowInfoTracker.windowLayoutInfo(this@SplitScreenChildActivity).collect { layoutInfo ->
//                    val windowMetrics = layoutInfo.windowMetrics
//                    val bounds = windowMetrics.bounds
//                    // 根据 bounds 调整 UI
//                }
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LogTool.i("child--onDestroy ...........")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogTool.i("child--onConfigurationChanged ..........." + newConfig.screenWidthDp + " : ")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogTool.i("child--onTouchEvent x: " + event?.x + " ,y:  " + event?.y)
        return super.onTouchEvent(event)
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

        val location = IntArray(2)
        rootView?.getLocationInWindow(location)
        val xInWindow = location[0] // 视图相对于窗口的 X 坐标
        val yInWindow = location[1]

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
            LogTool.i("child rootView windowWidth: " + windowWidth + ",windowHeight: " + windowHeight)

        })

        rootView?.viewTreeObserver?.addOnGlobalLayoutListener {
            LogTool.i("child viewTreeObserver.....: ")
            val rootView = findViewById<LinearLayout>(R.id.rootView);

//            rootView.requestLayout()
//            rootView?.invalidate()
//            rootView.layoutTransition = LayoutTransition().apply {
//                enableTransitionType(LayoutTransition.CHANGING)
//            }
        }


//        forceReLayout()
        LogTool.i("child windowWidth: " + windowWidth + ",windowHeight: " + windowHeight)
        LogTool.i("child widthPixels: " + widthPixels + ",heightPixels: " + heightPixels + ", xInWindow " + xInWindow + ",yInWindow " + yInWindow)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val rootView = findViewById<LinearLayout>(R.id.rootView);
        val height = rootView?.height
        val width = rootView?.width
        LogTool.i("child width " + width + ",height " + height)
    }

    private fun forceReLayout() {
//          recreate()
        val rootView = findViewById<LinearLayout>(R.id.rootView);
//        rootView?.removeAllViews()

//        rootView?.requestLayout()
//        rootView?.requestFitSystemWindows()
//        rootView?.post({
//            rootView?.requestLayout()
//            rootView?.invalidate()
//        })
        val activityManager = context!!.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
        val tasks: MutableList<AppTask>? = activityManager?.getAppTasks()
        //        tasks.get(0).finishAndRemoveTask();
        val curTaskId = tasks!![0].taskInfo.id

//        activityManager.moveTaskToBack(true, curTaskId)
        activityManager.moveTaskToFront(curTaskId, ActivityManager.MOVE_TASK_NO_USER_ACTION)


//        window.decorView.requestLayout()
//        rootView?.requestLayout();

        LogTool.i(
            "child curTaskId " + curTaskId + ",className " + tasks[0].taskInfo.topActivity?.className
        )
//        activityManager.for

    }

    override fun onStart() {
        super.onStart()
//        forceReLayout()
    }

}