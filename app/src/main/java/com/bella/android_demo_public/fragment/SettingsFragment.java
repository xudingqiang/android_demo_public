package com.bella.android_demo_public.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import com.bella.android_demo_public.R;


public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // 加载 preferences.xml 文件
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // 监听开关设置项的变化
        Preference switchPreference = findPreference("pref_key_switch");
        if (switchPreference != null) {
            switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isEnabled = (boolean) newValue;
                    // 处理开关状态变化
                    return true; // 返回 true 表示允许修改
                }
            });
        }

        // 监听输入框设置项的变化
        Preference editTextPreference = findPreference("pref_key_edit_text");
        if (editTextPreference != null) {
            editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String newText = (String) newValue;
                    // 处理输入框内容变化
                    return true; // 返回 true 表示允许修改
                }
            });
        }

        // 监听列表选择设置项的变化
        Preference listPreference = findPreference("pref_key_list");
        if (listPreference != null) {
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String newEntry = (String) newValue;
                    // 处理列表选择变化
                    return true; // 返回 true 表示允许修改
                }
            });
        }
    }

}