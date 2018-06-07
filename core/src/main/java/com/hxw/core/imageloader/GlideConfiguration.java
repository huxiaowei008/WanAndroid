package com.hxw.core.imageloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.Excludes;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.hxw.core.utils.AppUtils;

import java.io.InputStream;

import javax.inject.Inject;

/**
 * {@link Glide}的配置类
 *
 * @author hxw on 2018/5/4.
 */
@Excludes(OkHttpLibraryGlideModule.class)
@GlideModule
public class GlideConfiguration extends AppGlideModule {

    @Nullable
    @Inject
    GlideAppliesOptions options;

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //图片缓存文件最大值为100Mb
        long maxSize = 100 * 1024 * 1024;
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2f)
                .setBitmapPoolScreens(3f)
                .build();

        builder //内存缓存
                .setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()))
                //Bitmap 池
                .setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()))
                //磁盘缓存
                .setDiskCache(new DiskLruCacheFactory(AppUtils.getAppComponent()
                        .cacheFile().getParent(), "Glide", maxSize))
                .setDiskCacheExecutor(GlideExecutor
                        .newDiskCacheExecutor(GlideExecutor.UncaughtThrowableStrategy.THROW))
                .setSourceExecutor(GlideExecutor
                        .newSourceExecutor(GlideExecutor.UncaughtThrowableStrategy.THROW));
        if (options != null) {
            options.applyGlideOptions(context, builder);
        }
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //Glide默认使用HttpURLConnection做网络请求,
        //用了OkHttpUrlLoader.Factory()后会换成OKHttp请求，在这放入我们自己创建的OkHttp
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader
                .Factory(AppUtils.getAppComponent().okHttpClient()));
    }
}
