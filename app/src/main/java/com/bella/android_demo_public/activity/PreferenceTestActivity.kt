package com.bella.android_demo_public.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bella.android_demo_public.R
import com.bella.android_demo_public.fragment.SettingsFragment



class PreferenceTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_test)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
    }
}