package com.bella.android_demo_public.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.adapter.MyAdapter;
import com.bella.android_demo_public.commom.MyItemDetailsLookup;
import com.bella.android_demo_public.commom.MyItemKeyProvider;
import com.bella.android_demo_public.utils.LogTool;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewSelectionTestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private MyAdapter adapter;
    private SelectionTracker<Long> selectionTracker;
    private static long lastClickTime = 0;
    private static final long DOUBLE_CLICK_TIME_DELTA = 600; // m

    Button btnClick;

    private static long  selectPos = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_selection_test);
        recyclerView = findViewById(R.id.recyclerView);
        btnClick = findViewById(R.id.btnClick);

        btnClick.setOnClickListener(view ->{
            LogTool.i("selectPos "+selectPos);
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 数据初始化
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            items.add("Item " + i);
        }

        adapter = new MyAdapter(items);
        recyclerView.setAdapter(adapter);

        // 初始化 SelectionTracker
        selectionTracker = new SelectionTracker.Builder<>(
                "selection-id",
                recyclerView,
                new MyItemKeyProvider(recyclerView),
                new MyItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage())
                .withOnItemActivatedListener(this::onItemActivated)
                .build();

        // 将 SelectionTracker 传递给适配器
        adapter.setSelectionTracker(selectionTracker);
    }

    private boolean onItemActivated(ItemDetailsLookup.ItemDetails<Long> item, MotionEvent e) {
        long currentTime = System.currentTimeMillis();
        long subTime = currentTime - lastClickTime;
        selectPos = item.getPosition();
        boolean  isSel = selectionTracker.isSelected(selectPos);
        LogTool.i("bella subTime:  " + subTime + " ,isSel:" + isSel + "  ,selectPos:" + selectPos);
        if(isSel){
            selectionTracker.deselect(selectPos);
        }else {
            selectionTracker.select(selectPos);
        }


        if (subTime < DOUBLE_CLICK_TIME_DELTA) {
            //double click
        } else {
            lastClickTime = currentTime;
            return true;
        }
        return  false ;
    }

}
