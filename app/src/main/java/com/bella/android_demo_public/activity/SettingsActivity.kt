package com.bella.android_demo_public.activity

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.preference.PreferenceManager
import com.bella.android_demo_public.MyInputMethodService
import com.bella.android_demo_public.R


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // 启用输入法按钮
        findViewById<Button>(R.id.btn_enable_ime)?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))

            val uri = Uri.parse("com.android.locationprovider" + "/LOCATION_VALUE")
            var cursor: Cursor? = null
            var result: String? = null
            val selection = "PACKAGE_NAME = ? AND KEY_CODE = ? AND IS_DEL != 1"
            val selectionArgs = null
            try {
                val contentResolver: ContentResolver = getContentResolver()
                cursor = contentResolver.query(uri, null, selection, selectionArgs, null)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }

        // 选择输入法按钮
        findViewById<Button>(R.id.btn_select_ime)?.setOnClickListener {
            val imeId = ComponentName(this, MyInputMethodService::class.java).flattenToShortString()
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS)
            intent.putExtra(Settings.EXTRA_INPUT_METHOD_ID, imeId)
            startActivity(intent)
        }

        // 振动反馈开关
        val switchVibrate = findViewById<SwitchCompat>(R.id.switch_vibrate)
        switchVibrate?.isChecked = PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("pref_vibrate", true)
        switchVibrate?.setOnCheckedChangeListener { _, isChecked ->
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("pref_vibrate", isChecked)
                .apply()
        }
    }

}