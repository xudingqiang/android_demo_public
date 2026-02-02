package com.bella.android_demo_public.bean;

import android.net.Uri;

public class ImageItem {
    public long id;
    public Uri uri;

    public ImageItem(long id, Uri uri) {
        this.id = id;
        this.uri = uri;
    }
}
