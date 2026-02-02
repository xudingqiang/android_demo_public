package com.bella.android_demo_public.view;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerScrollBinder {
    public static void bind(RecyclerView rv, CustomScrollBarView bar) {

        LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int range = recyclerView.computeVerticalScrollRange();
                int offset = recyclerView.computeVerticalScrollOffset();
                int extent = recyclerView.computeVerticalScrollExtent();

                float progress = offset * 1f / (range - extent);
                bar.setThumbHeight(extent * 1f / range * bar.getHeight());
                bar.setProgress(progress);
            }
        });

        bar.setOnScrollChangeListener(progress -> {
            int range = rv.computeVerticalScrollRange();
            int extent = rv.computeVerticalScrollExtent();

            int targetOffset = (int) ((range - extent) * progress);
            rv.scrollBy(0, targetOffset - rv.computeVerticalScrollOffset());
        });
    }
}
