package com.hxw.core.utils;

import android.content.Context;
import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author hxw on 2018/5/3.
 */
public final class FileUtils {

    /**
     * 返回缓存文件夹
     */
    public static File getCacheFile(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = context.getExternalCacheDir();
            if (file == null) {
                file = new File(getCacheFilePath(context));
                try {
                    makeDirs(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }

    /**
     * 获取自定义缓存文件地址
     */
    public static String getCacheFilePath(Context context) {
        String packageName = context.getPackageName();
        return Environment.getExternalStorageDirectory().getPath() + packageName;
    }

    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    public static void makeDirs(File file) throws FileNotFoundException {
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new FileNotFoundException("文件创建失败!");
            }
        }
    }

    /**
     * 关闭io流
     * @param closeables 被关闭的对象
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
