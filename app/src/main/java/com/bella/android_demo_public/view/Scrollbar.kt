package com.fde.notepad.view

//import android.content.Context
//import android.graphics.Canvas
//import android.util.AttributeSet
//import android.view.View
//
//class Scrollbar @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : View(context, attrs, defStyleAttr) {
//    init {
//        /* *
//            // 高度的比例关系及求值公式
//            scrollbar.height / screen.height = screen.height / child.height
//            scrollbar.heigth = screen.height^2 / child.height
//            // 位移的比例关系
//            scrollbar.y / screen.height = child.scrollY / child.height
//            scrollbar.y = screen.height * child.scrollY / child.height
//        * */
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//    }
//
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//    }
//
//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//    }
//}