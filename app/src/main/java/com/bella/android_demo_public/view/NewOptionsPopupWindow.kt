package com.bella.android_demo_public.view

import android.graphics.RenderEffect
import android.graphics.Shader
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bella.android_demo_public.MainActivity
import com.bella.android_demo_public.R
import com.bella.android_demo_public.utils.LogTool


class NewOptionsPopupWindow(
    contentView: View,
    width: Int,
    height: Int,
    val mainActivity: MainActivity
) :
    PopupWindow(contentView, width, height, true) {

    var popupChildWindow: OptionsChildPopupWindow? = null

    init {
        initView();
    }

    fun initView() {


//        contentView.findViewById<TextView>(R.id.text_new_directory).setOnClickListener({
//
//            dismiss()
//        })

        contentView.findViewById<TextView>(R.id.text_display_properties)?.setOnClickListener({

            dismiss()
        })



        contentView.findViewById<TextView>(R.id.text_refresh)?.setOnClickListener({

            dismiss()
        })

        contentView.findViewById<TextView>(R.id.text_change_wallpaper)?.setOnClickListener({

            dismiss()
        })

        contentView.findViewById<TextView>(R.id.text_display_settings)?.setOnClickListener({

            dismiss()
        })

//        contentView.findViewById<TextView>(R.id.text_refresh)?.setOnHoverListener { v: View, event: MotionEvent ->
//            false
//        }

        contentView.findViewById<TextView>(R.id.text_display_settings)?.setOnHoverListener { v: View, event: MotionEvent ->
            if (popupChildWindow != null && popupChildWindow!!.isShowing) {
                popupChildWindow!!.dismiss()
            }
            LogTool.i("ACTION_HOVER_MOVE text_display_settings ")
            false
        }

        contentView.findViewById<TextView>(R.id.txtSystemTheme)?.setOnClickListener({
            false
        })

        contentView.findViewById<TextView>(R.id.txtSort)?.setOnClickListener({
            true
        })

        contentView.findViewById<RelativeLayout>(R.id.layout_system_theme)?.setOnHoverListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_HOVER_ENTER -> {
                    // 鼠标悬停进入
                    if (popupChildWindow != null && popupChildWindow!!.isShowing) {
                        popupChildWindow!!.dismiss()
                    }
                    popupChildWindow = OptionsChildPopupWindow(
                        LayoutInflater.from(mainActivity).inflate(R.layout.popup_child_layout, null),
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        R.array.theme_options,
                        mainActivity
                    )

                    if (v != null && !mainActivity.isDestroyed && !mainActivity.isFinishing) {
                        popupChildWindow?.showAsDropDown(v, 164, -30, Gravity.NO_GRAVITY)
                    }
                    true
                }
                MotionEvent.ACTION_HOVER_EXIT -> {
                    // 鼠标悬停离开
                    val  x = event.x;
                    val y = event.y ;
                    LogTool.i("ACTION_HOVER_EXIT x "+x + ", y "+y)
//                    if (popupChildWindow != null && popupChildWindow!!.isShowing) {
//                        popupChildWindow!!.dismiss()
//                    }
                    true
                }
                else -> {
                    val  x = event.x;
                    val y = event.y ;

                    LogTool.i("ACTION_HOVER" +event.action+ ", x "+x + ", y "+y)
                    if (popupChildWindow != null && popupChildWindow!!.isShowing) {
                        popupChildWindow!!.dismiss()
                    }
                    popupChildWindow = OptionsChildPopupWindow(
                        LayoutInflater.from(mainActivity).inflate(R.layout.popup_child_layout, null),
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        R.array.theme_options,
                        mainActivity
                    )

                    if (v != null && !mainActivity.isDestroyed && !mainActivity.isFinishing) {
                        popupChildWindow?.elevation = 6f;
                        popupChildWindow?.showAsDropDown(v, 164, -30, Gravity.NO_GRAVITY)
                    }
                    true
                }
            }
        }



        contentView.findViewById<RelativeLayout>(R.id.layout_view_type)?.setOnHoverListener { v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_HOVER_ENTER -> {
                    // 鼠标悬停进入
                    if (popupChildWindow != null && popupChildWindow!!.isShowing) {
                        popupChildWindow!!.dismiss()
                    }
                    popupChildWindow = OptionsChildPopupWindow(
                        LayoutInflater.from(mainActivity).inflate(R.layout.popup_child_layout, null),
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        R.array.sort_options,
                        mainActivity
                    )

                    if (v != null && !mainActivity.isDestroyed && !mainActivity.isFinishing) {
                        popupChildWindow?.showAsDropDown(v, 164, -30, Gravity.NO_GRAVITY)
                    }
                    true
                }
                MotionEvent.ACTION_HOVER_MOVE ->{
                    val  x = event.x;
                    val y = event.y ;
                    LogTool.i("ACTION_HOVER_MOVE x "+x + ", y "+y)
                    true
                }
                MotionEvent.ACTION_HOVER_EXIT -> {
                    // 鼠标悬停离开
                    val  x = event.x;
                    val y = event.y ;
                    LogTool.i("ACTION_HOVER_EXIT x "+x + ", y "+y)
//                    if (popupChildWindow != null && popupChildWindow!!.isShowing) {
//                        popupChildWindow!!.dismiss()
//                    }
                    true
                }
                else -> false
            }
        }


//        val cardView =  contentView?.findViewById<CardView>(R.id.rootView)
//        val blurEffect = RenderEffect.createBlurEffect(6f, 6f, Shader.TileMode.CLAMP)
//        cardView?.setRenderEffect(blurEffect)

        contentView.findViewById<RelativeLayout>(R.id.layout_sort)?.setOnClickListener({

            dismiss()
        })

        contentView.findViewById<TextView>(R.id.text_open_the_terminal)?.setOnClickListener({

            dismiss()
        })

    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)

    }

    override fun dismiss() {
        super.dismiss()
    }
}
