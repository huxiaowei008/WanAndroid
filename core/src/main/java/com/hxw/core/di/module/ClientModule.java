package com.hxw.core.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hxw.core.utils.StringUtils;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * 提供一些三方库客户端实例
 *
 * @author hxw on 2018/5/3.
 */
@Module
public class ClientModule {

    /**
     * 提供{@link Gson}
     */
    @Singleton
    @Provides
    static Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder()
                .serializeNulls();//值为null时会输出null
        if (configuration != null) {
            configuration.configGson(application, builder);
        }
        return builder.create();
    }

    /**
     * 提供 {@link Retrofit}
     */
    @Singleton
    @Provides
    static Retrofit provideRetrofit(Application application, OkHttpClient client, HttpUrl httpUrl,
                                    Gson gson, @Nullable RetrofitConfiguration configuration) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        if (configuration != null) {
            configuration.configRetrofit(application, builder);
        }
        return builder.build();
    }

    /**
     * 提供{@link OkHttpClient}
     */
    @Singleton
    @Provides
    static OkHttpClient provideClient(Application application, @Nullable OkHttpConfiguration configuration) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                String str = message;
                if (message.startsWith("{") || message.startsWith("[")) {
                    str = StringUtils.jsonFormat(message);
                }
                Timber.tag("OkHttp").d(str);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(logging);
        if (configuration != null) {
            configuration.configOkHttp(application, builder);
        }
        return builder.build();
    }

    /**
     * Gson配置接口
     */
    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }

    /**
     * Retrofit配置接口
     */
    public interface RetrofitConfiguration {
        void configRetrofit(Context context, Retrofit.Builder builder);
    }

    /**
     * OkHttp配置接口
     */
    public interface OkHttpConfiguration {
        void configOkHttp(Context context, OkHttpClient.Builder builder);
    }
}
