package com.bella.android_demo_public.bean

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat

data class OptionItem(
    val label: CharSequence = "",
    val icon: Drawable?,
    var hasChild: Boolean = false,
    var options: ArrayList<OptionItem>? = null,
    val clickListener: View.OnLongClickListener?
) {
    constructor(
        context: Context,
        labelRes: Int,
        iconRes: Int,
        clickListener: View.OnLongClickListener?
    ) : this(
        label = context.getText(labelRes),
        icon = ContextCompat.getDrawable(context, iconRes),
        clickListener = clickListener
    )

    constructor(
        context: Context,
        labelRes: Int,
        iconRes: Int,
        hasChild: Boolean,
        clickListener: View.OnLongClickListener?,
        options: ArrayList<OptionItem>?
    ) : this(
        label = context.getText(labelRes),
        icon = ContextCompat.getDrawable(context, iconRes),
        hasChild = hasChild,
        options = options,
        clickListener = clickListener
    )

}