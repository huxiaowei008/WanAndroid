package com.hxw.wanandroid.base;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.google.gson.GsonBuilder;
import com.hxw.core.base.ConfigModule;
import com.hxw.wanandroid.WanApi;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author hxw on 2018/7/18.
 */
public class GlobalConfigModule implements ConfigModule {
    @Override
    public void configGson(@NotNull Context context, @NotNull GsonBuilder builder) {
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void configOkHttp(@NotNull Context context, @NotNull OkHttpClient.Builder builder) {

    }

    @Override
    public void configRetrofit(@NotNull Context context, @NotNull Retrofit.Builder builder) {
        builder.baseUrl(WanApi.BASEURL);
    }

    @Override
    public void applyGlideOptions(@NotNull Context context, @NotNull GlideBuilder builder) {

    }
}
