package com.hxw.wanandroid;

import android.content.Context;
import android.widget.ImageView;

import com.hxw.core.glide.GlideApp;
import com.youth.banner.loader.ImageLoaderInterface;

/**
 * @author hxw on 2018/8/12
 */
public class GlideImageLoader implements ImageLoaderInterface<ImageView> {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideApp.with(context).load(path).into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        return new ImageView(context);
    }
}
