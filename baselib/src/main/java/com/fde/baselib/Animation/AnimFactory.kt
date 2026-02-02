package com.fde.baselib.Animation

import android.content.Context
import android.widget.ImageView
import com.fde.baselib.R

object AnimFactory {

    /**
     * 获取加载动画
     */
    fun loading(context: Context, imageView: ImageView): AnimDrawablePlayer {
        return AnimDrawablePlayer(context)
            .attach(imageView, R.drawable.frame_animation)
    }
}