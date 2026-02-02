package com.bella.android_demo_public.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.bean.ImageItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter
        extends RecyclerView.Adapter<ImageAdapter.VH> {

    private static final int PAYLOAD_SIZE = 1;

    private final List<ImageItem> data;
    private int itemSizePx;

    public ImageAdapter(List<ImageItem> data) {
        this.data = data;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).id;
    }

    public void updateItemSize(int sizePx) {
        if (itemSizePx != sizePx) {
            itemSizePx = sizePx;
            notifyItemRangeChanged(0, getItemCount(), PAYLOAD_SIZE);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(data.get(position), itemSizePx);
    }

    @Override
    public void onBindViewHolder(
            @NonNull VH holder,
            int position,
            @NonNull List<Object> payloads) {

        if (!payloads.isEmpty() && payloads.contains(PAYLOAD_SIZE)) {
            holder.updateSize(itemSizePx);
            return;
        }
        onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        ImageView image;
        Uri boundUri;

        VH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }

        void bind(ImageItem item, int sizePx) {
            updateSize(sizePx);

            if (!item.uri.equals(boundUri)) {
                boundUri = item.uri;

                Glide.with(image)
                        .load(item.uri)
                        .thumbnail(0.1f) // ⭐ 首帧快
                        .dontAnimate()
                        .centerCrop()
                        .into(image);
            }
        }

        void updateSize(int sizePx) {
            ViewGroup.LayoutParams lp = image.getLayoutParams();
            if (lp.width != sizePx) {
                lp.width = sizePx;
                lp.height = sizePx;
                image.setLayoutParams(lp);
            }
        }
    }
}
