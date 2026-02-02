package com.bella.android_demo_public.fragment;

import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bella.android_demo_public.R;

public class LoginFragment extends Fragment {
    private static final String ARG_TITLE = "login";


    public static LoginFragment newInstance(String title) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_test_touch, container, false);
//        TextView textView = view.findViewById(R.id.text_view);
//        View rootLayout = view.findViewById(R.id.root_layout);

        String title = getArguments() != null ? getArguments().getString(ARG_TITLE, "Home") : "Home";
//        textView.setText(title);
//        rootLayout.setBackgroundColor(Color.parseColor("#FFB6C1")); // 浅粉色

        return view;
    }
}
