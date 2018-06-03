package com.hxw.lol.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.hxw.lol.delegate.AppDelegate;

/**
 * App相关工具类
 *
 * @author hxw on 2018/5/5.
 */
public class AppUtils {
    private static Toast mToast;
    public static void showSnackBar(String message) {
        Activity currentActivity = AppDelegate
                .getAppComponent()
                .appManager()
                .getCurrentActivity();
        if (currentActivity != null) {
            View view = currentActivity
                    .getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ShowToast")
    public static void showToast(String message){
        if (mToast==null){
            mToast=Toast.makeText(AppDelegate.getAppComponent().application(),
                    "",Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.show();
    }
    public static int getVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 检查权限
     * 有权限: PackageManager.PERMISSION_GRANTED
     * 无权限: PackageManager.PERMISSION_DENIED
     *
     * @param context 上下文
     * @param perms   权限
     * @return 是否有权限 {@code true} 有权限 {@code false} 无权限
     */
    public static boolean hasPermissions(@NonNull Context context,
                                         @Size(min = 1) @NonNull String... perms) {
        //版本6.0以下不需要请求权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        //是否有权限
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dpToPx(Context context,float dpValue){
        float scale= context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int spToPx(Context context,float spValue){
        float scale= context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
