package com.fde.baselib.Animation

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView

class AnimDrawablePlayer(private val context: Context) {

    private var animation: AnimationDrawable? = null

    fun attach(imageView: ImageView, drawableRes: Int) : AnimDrawablePlayer {
        imageView.setImageResource(drawableRes)
        animation = imageView.drawable as? AnimationDrawable
        return this
    }

    fun start() {
        animation?.start()
    }

    fun stop() {
        animation?.stop()
    }
}