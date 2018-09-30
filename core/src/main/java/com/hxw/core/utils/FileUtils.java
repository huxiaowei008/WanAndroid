package com.hxw.core.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.core.content.FileProvider;

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
     * @param file 目标文件夹
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
     *
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

    /**
     * 获取文件Uri,适配7.0
     */
    public static Uri getUriFormFile(Context context, File file) {
        //7.0系统适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //转变成Content uri
            return FileProvider.getUriForFile(context,
                    context.getPackageName() + ".provider", file);
        } else {
            //file uri
            return Uri.fromFile(file);
        }
    }

    /**
     * 根据uri获取图片路径
     *
     * @param context 上下文
     * @param uri     uri
     * @return 路径path
     */
    public static String getUriImagePath(Context context, Uri uri) {
        String path = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                // 解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(context, contentUri, null);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        } else {
//            if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(context, uri, null);
        }
        return path;
    }

    /**
     * 通过uri查询路径,主要是图片查询
     *
     * @param context   上下文
     * @param uri       需要查询的uri
     * @param selection 过滤器
     * @return path路径
     */
    public static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
