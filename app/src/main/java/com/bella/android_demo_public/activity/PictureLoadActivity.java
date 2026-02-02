package com.bella.android_demo_public.activity;

import android.content.ContentUris;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.adapter.ImageAdapter;
import com.bella.android_demo_public.bean.ImageItem;
import com.bella.android_demo_public.utils.LogTool;
import com.bella.android_demo_public.view.CustomScrollBarView;
import com.bella.android_demo_public.view.RecyclerScrollBinder;
import com.bella.android_demo_public.view.VerticalDragScrollBar;

import java.util.ArrayList;
import java.util.List;

public class PictureLoadActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private ImageAdapter adapter;
    //    RecyclerScrollBar scrollBar;
    private VerticalDragScrollBar mVerticalDragScrollBar;
    private int scrollProcess = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_load);
        recyclerView = findViewById(R.id.recycler);
//        scrollBar = findViewById(R.id.scrollBar);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null); // ⭐ 防抖
        recyclerView.setNestedScrollingEnabled(false);
        List<ImageItem> images = loadImages();

        adapter = new ImageAdapter(images);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        mVerticalDragScrollBar = findViewById(R.id.verticalTouchableScrollBar);
        mVerticalDragScrollBar.setScrollListener(scrollListener);

        CustomScrollBarView bar = findViewById(R.id.scrollBar);
        RecyclerScrollBinder.bind(recyclerView, bar);


//        mVerticalDragScrollBar.setOnGenericMotionListener((v, event) -> {
//
//            if (event.getAction() == MotionEvent.ACTION_SCROLL
//                    && (event.getSource() & InputDevice.SOURCE_MOUSE) != 0) {
//
//                float delta = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
//
//                // 桌面滚动速度
//                int scrollY = (int) (-delta * 120);
//
//                nestedScrollView.scrollBy(0, scrollY);
//                return true;
//            }
//            return false;
//        });

//        scrollBar.attachRecyclerView(recyclerView);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
//                int offset = rv.computeVerticalScrollOffset();
//                int range = rv.computeVerticalScrollRange();
//                int extent = rv.computeVerticalScrollExtent();
//
//                LogTool.w("onScrolled dx "+dx +",dy "+dy + " ,recyclerView.getHeight() "+recyclerView.getHeight() + ",range "+range +",extent "+extent + ",recyclerView.getScrollY() "+recyclerView.getScrollY());
//                mVerticalDragScrollBar.updateData(
//                        200,
//                        0,
//                        dy,
//                        recyclerView.getHeight()
//                );
//                mVerticalDragScrollBar.invalidate();
//            }
//        });


        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LogTool.w("onScrollChange  scrollY " + scrollY + " ,oldScrollY " + oldScrollY + "  " + recyclerView.getHeight() + ", scrollProcess " + scrollProcess + " " + v.getScrollY());
//                mVerticalDragScrollBar.scrollBy(scrollX,scrollY);
//                scrollProcess -= oldScrollY ;
//                mVerticalDragScrollBar.updateData(
//                        scrollY,
//                        recyclerView.getWidth(),
//                        scrollProcess,
//                        recyclerView.getHeight()
//                );
//                mVerticalDragScrollBar.invalidate();
            }
        });

        listenWindowResize();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recyclerView.post(() -> {
            mVerticalDragScrollBar.updateData(
                    recyclerView.getScrollY(),
                    recyclerView.getWidth(),
                    200,
                    500
            );
            mVerticalDragScrollBar.invalidate();
        });
    }


    private void listenWindowResize() {
        View root = getWindow().getDecorView();
        root.addOnLayoutChangeListener(
                (v, l, t, r, b, ol, ot, orr, ob) -> {
                    int newW = r - l;
                    int oldW = orr - ol;
                    if (newW != oldW) {
                        onWindowWidthChanged(newW);
                    }
                });
    }

    private void onWindowWidthChanged(int widthPx) {
        int minItemPx = dpToPx(100);
        int span = Math.max(1, widthPx / minItemPx);

        layoutManager.setSpanCount(span);

        int itemSize = widthPx / span;
        adapter.updateItemSize(itemSize);
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    // MediaStore 查询
    private List<ImageItem> loadImages() {
        List<ImageItem> list = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_MODIFIED
        };

        Cursor c = getContentResolver().query(
                uri,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        );

        if (c != null) {
            int idIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            while (c.moveToNext()) {
                long id = c.getLong(idIndex);
                Uri contentUri = ContentUris.withAppendedId(uri, id);
                list.add(new ImageItem(id, contentUri));
            }
            c.close();
        }
        return list;
    }

    private ScrollListener scrollListener = new ScrollListener() {
        @Override
        public void scrollYBy(int y, float dy) {
            LogTool.w("scrollYBy " + y + ",dy " + dy);
//            int currentScrollY = mContentTxt.getScrollY();
//            int maxScrollY = mContentTxt.getLayout().getHeight() - mContentTxt.getHeight();
//            if (maxScrollY <= 0) return;
//            int newScrollY = Math.max(0, Math.min(currentScrollY + y, maxScrollY));
//            mContentTxt.scrollBy(0, newScrollY - currentScrollY);
//            recyclerView.scrollToPosition(y);


//            recyclerView.scrollBy(recyclerView.getScrollX(),(int)dy);

        }

        @Override
        public void scrollXBy(int x) {
            // Horizontal scrolling implementation if needed
        }
    };

    public interface ScrollListener {
        void scrollXBy(int x);

        void scrollYBy(int y, float dy);
    }
}