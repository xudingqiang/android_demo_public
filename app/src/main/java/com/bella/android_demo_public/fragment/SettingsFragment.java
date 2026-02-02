package com.bella.android_demo_public.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;

import com.bella.android_demo_public.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    protected RecyclerView.Adapter onCreateAdapter(@NonNull PreferenceScreen preferenceScreen) {
        return super.onCreateAdapter(preferenceScreen);
    }

    @NonNull
    @Override
    public RecyclerView onCreateRecyclerView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return super.onCreateRecyclerView(inflater, parent, savedInstanceState);
    }

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

        PreferenceCategory testList = findPreference("testList");
        Preference preference = new Preference(getActivity());
        preference.setTitle("test1");
        preference.setSummary("bella1");
        preference.setLayoutResource(R.layout.preference_item_corners);
        testList.addPreference(preference);

        preference = new Preference(getActivity());
        preference.setTitle("test2");
        preference.setSummary("bella2");
        preference.setLayoutResource(R.layout.preference_item_corners);
        testList.addPreference(preference);

    }

}