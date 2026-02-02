package com.fde.notepad.view

import android.graphics.Canvas
import android.view.MotionEvent

// The scrollbar implementation is referenced at https://ccc2.icu/archives/204/#anchor-18
interface IScrollBar {
    // 根据对应的值做出相应处理（这里为什么不用scrollY？因为还有横向）
    fun updateData(scrollLength: Int, width: Int, height: Int, allLength: Int)

    // 开始触摸，一般是down事件
    fun startTouch(event: MotionEvent?)

    // 停止触摸，一般是up或cancel事件
    fun endTouch(event: MotionEvent?)

    // 开始滚动，一般是在页面滚动回调里触发
    fun startScroll()

    // 是否可以拖拽，该逻辑封装在scrollbar里自行判断
    fun needDrag(event: MotionEvent?): Boolean

//     绘制自身（每次容器重绘都会触发）
//  fun onDraw(canvas: Canvas?)

    // 显示位置，给与更多显示的选择
    enum class Gravity(val value: Int) {
        LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);

        companion object {
            fun get(value: Int): Gravity {
                for (entry in entries) {
                    if (entry.value == value) {
                        return entry
                    }
                }
                return RIGHT
            }
        }
    }

    // 显示模式，始终不显示，始终显示，仅滚动时显示(涉及到动画处理)
    enum class ShowMode(val value: Int) {
        NONE(0), ALWAYS(1), SCROLLING(2);

        companion object {
            fun get(value: Int): ShowMode {
                for (entry in ShowMode.entries) {
                    if (entry.value == value) {
                        return entry
                    }
                }
                return NONE
            }
        }
    }
}