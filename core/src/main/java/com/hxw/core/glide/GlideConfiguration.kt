package com.hxw.core.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.hxw.core.base.ConfigModule
import okhttp3.OkHttpClient
import org.koin.core.KoinComponent
import org.koin.core.get

import java.io.InputStream

/**
 * [Glide]的配置类
 *
 * @author hxw
 * @date 2018/5/4
 */
@Excludes(OkHttpLibraryGlideModule::class)
@GlideModule
class GlideConfiguration : AppGlideModule(), KoinComponent {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val config: ConfigModule = get()
        //图片缓存文件最大值为100Mb
        val maxSize = (100 * 1024 * 1024).toLong()
        val calculator = MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2f)
                .setBitmapPoolScreens(3f)
                .build()

        builder //内存缓存
                .setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
                //Bitmap 池
                .setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))
                //磁盘缓存
                .setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, "Glide", maxSize))
                .setDiskCacheExecutor(GlideExecutor
                        .newDiskCacheExecutor(GlideExecutor.UncaughtThrowableStrategy.THROW))
                .setSourceExecutor(GlideExecutor
                        .newSourceExecutor(GlideExecutor.UncaughtThrowableStrategy.THROW))
        config.applyGlideOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        //Glide默认使用HttpURLConnection做网络请求,
        //用了OkHttpUrlLoader.Factory()后会换成OKHttp请求，在这放入我们自己创建的OkHttp
        val ok: OkHttpClient = get()
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(ok))
    }
}
