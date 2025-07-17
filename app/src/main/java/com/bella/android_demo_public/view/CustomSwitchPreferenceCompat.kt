package com.bella.android_demo_public.view

import android.content.Context
import android.util.AttributeSet
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import com.bella.android_demo_public.R


class CustomSwitchPreferenceCompat(context: Context, attrs: AttributeSet) :
    SwitchPreferenceCompat(context) {

    init {
        layoutResource = R.layout.preference_square_corners
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
    }
}