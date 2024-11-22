package com.bella.android_demo_public.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.bella.android_demo_public.R;

import java.util.List;
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final List<String> items;
    private SelectionTracker<Long> selectionTracker;

    public MyAdapter(List<String> items) {
        this.items = items;
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String item = items.get(position);
//        holder.textView.setText(item);

        holder.bind(item, selectionTracker.isSelected(Long.valueOf(String.valueOf(position))));

//        // 更新 UI 状态
//        boolean isSelected = selectionTracker != null && selectionTracker.isSelected((long) position);
//        holder.itemView.setActivated(isSelected);
//        holder.textView.setOnClickListener(view -> {
//            boolean isSel = holder.textView.isSelected();
//            holder.textView.setSelected(!isSel);
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }

        public void bind(String content, boolean isSelected) {
            textView.setText(content);
            textView.setBackgroundColor(isSelected ? Color.LTGRAY : Color.WHITE);
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Override
                public Long getSelectionKey() {
                    return (long) getAdapterPosition();
                }
            };
        }
    }
}
