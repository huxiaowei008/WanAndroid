package com.hxw.lol.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

/**
 * 让外部可以配置{@link Glide}
 *
 * @author hxw on 2018/5/4.
 */
public interface GlideAppliesOptions {
    /**
     * 配置 @{@link Glide} 的自定义参数,此方法在 @{@link Glide} 初始化时执行(@{@link Glide}
     * 在第一次被调用时初始化),只会执行一次
     *
     * @param context 上下文
     * @param builder {@link GlideBuilder} 此类被用来创建 Glide
     */
    void applyGlideOptions(Context context, GlideBuilder builder);
}
