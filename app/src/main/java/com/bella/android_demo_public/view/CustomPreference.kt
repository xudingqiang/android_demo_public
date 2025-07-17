package com.bella.android_demo_public.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.bella.android_demo_public.R
import com.bella.android_demo_public.utils.LogTool

class CustomPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {
    private var key: String = ""


    init {
        key =
            attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "key").toString()
        LogTool.w("key " + key);
        layoutResource = R.layout.preference_square_corners
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        LogTool.w("onBindViewHolder .......")
        try {
            if (key != null) {
                LogTool.w("att " + key)
                LogTool.w("value " + key)
            } else {
                LogTool.e("attrs is null ")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            LogTool.e("e " + e.toString())
        }


    }
}