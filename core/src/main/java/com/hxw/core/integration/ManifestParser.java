package com.hxw.lol.integration;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * 用于解析 AndroidManifest 中的 Meta 属性
 *
 * @author hxw on 2018/5/4.
 */
public final class ManifestParser {
    private static final String TAG = "ManifestParser";
    private static final String MODULE_VALUE = "ConfigModule";

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    private static ConfigModule parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find ConfigModule implementation", e);
        }

        Object module = null;
        try {
            module = clazz.getDeclaredConstructor().newInstance();
            // These can't be combined until API minimum is 19.
        } catch (InstantiationException e) {
            throwInstantiateGlideModuleException(clazz, e);
        } catch (IllegalAccessException e) {
            throwInstantiateGlideModuleException(clazz, e);
        } catch (NoSuchMethodException e) {
            throwInstantiateGlideModuleException(clazz, e);
        } catch (InvocationTargetException e) {
            throwInstantiateGlideModuleException(clazz, e);
        }

        if (!(module instanceof ConfigModule)) {
            throw new RuntimeException("Expected instanceof ConfigModule, but found: " + module);
        }
        return (ConfigModule) module;
    }

    private static void throwInstantiateGlideModuleException(Class<?> clazz, Exception e) {
        throw new RuntimeException("Unable to instantiate ConfigModule implementation for " + clazz, e);
    }

    public List<ConfigModule> parse() {
        List<ConfigModule> modules = new ArrayList<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData == null) {
                Timber.tag(TAG).d("Got null app info metadata");
                return modules;
            }

            Timber.tag(TAG).v("Got app info metadata: %s", appInfo.metaData);

            for (String key : appInfo.metaData.keySet()) {
                if (MODULE_VALUE.equals(appInfo.metaData.get(key))) {
                    modules.add(parseModule(key));
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
                        Timber.tag(TAG).d("Loaded Config module: %s", key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse ConfigModules", e);
        }

        Timber.tag(TAG).d("Finished loading Config modules");

        return modules;
    }
}
