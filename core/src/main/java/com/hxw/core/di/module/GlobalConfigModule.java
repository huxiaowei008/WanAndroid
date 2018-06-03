package com.hxw.lol.di.module;

import android.app.Application;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hxw.lol.imageloader.GlideAppliesOptions;
import com.hxw.lol.utils.FileUtils;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;

/**
 * 向框架中注入外部配置的自定义参数
 *
 * @author hxw on 2018/5/3.
 */
@Module
public class GlobalConfigModule {
    private HttpUrl mApiUrl;
    private File mCacheFile;
    private ClientModule.GsonConfiguration mGsonConfiguration;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkHttpConfiguration mOkhttpConfiguration;
    private GlideAppliesOptions mGlideAppliesOptions;


    private GlobalConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mCacheFile = builder.cacheFile;
        this.mGsonConfiguration = builder.gsonConfiguration;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkhttpConfiguration = builder.okHttpConfiguration;
        this.mGlideAppliesOptions = builder.glideAppliesOptions;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        return mApiUrl == null ? HttpUrl.parse("https://api.github.com/") : mApiUrl;
    }

    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return mCacheFile == null ? FileUtils.getCacheFile(application) : mCacheFile;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    GlideAppliesOptions provieGlideAppliesOptions() {
        return mGlideAppliesOptions;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkHttpConfiguration provideOkHttpConfiguration() {
        return mOkhttpConfiguration;
    }


    public static final class Builder {
        private HttpUrl apiUrl;
        private File cacheFile;
        private ClientModule.GsonConfiguration gsonConfiguration;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkHttpConfiguration okHttpConfiguration;
        private GlideAppliesOptions glideAppliesOptions;

        private Builder() {
        }

        public Builder baseurl(String baseUrl) {//基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw new NullPointerException("BaseUrl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder gsonConfiguration(ClientModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okhttpConfiguration(ClientModule.OkHttpConfiguration okHttpConfiguration) {
            this.okHttpConfiguration = okHttpConfiguration;
            return this;
        }

        public Builder glideAppliesOptions(GlideAppliesOptions glideAppliesOptions) {
            this.glideAppliesOptions = glideAppliesOptions;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }
    }
}
