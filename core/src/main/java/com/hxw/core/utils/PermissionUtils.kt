package com.hxw.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.annotation.Size
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.hxw.core.PermissionAspect

/**
 * 权限工具
 * @author hxw on 2018/8/4.
 *
 */
object PermissionUtils {

    private lateinit var resultAction: PermissionAction

    /**
     * 检查权限
     * 有权限: PackageManager.PERMISSION_GRANTED
     * 无权限: PackageManager.PERMISSION_DENIED
     *
     * @param context 上下文
     * @param perms   权限
     * @return 是否有权限 `true`: 有权限 `false`: 无权限
     */
    @JvmStatic
    fun hasPermissions(context: Context, @Size(min = 1) vararg perms: String): Boolean {
        //版本6.0以下不需要请求权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        //是否有权限
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 判断是否有必要向用户解释为什么要这项权限
     *
     * @param host activity或fragment
     * @param perms 权限
     * @return 是否需要解释 `true`: 需要 `false`: 不需要
     */
    @JvmStatic
    fun shouldShowRationale(host: Any, @Size(min = 1) @NonNull vararg perms: String): Boolean {
        when (host) {
            is Activity -> perms.forEach {
                if (ActivityCompat.shouldShowRequestPermissionRationale(host, it)) {
                    return true
                }
            }
            is Fragment -> perms.forEach {
                if (host.shouldShowRequestPermissionRationale(it)) {
                    return true
                }
            }
            else -> throw AssertionError("host 不是activity 或 fragment")
        }
        return false
    }

    /**
     * 请求权限的结果
     */
    @JvmStatic
    fun onRequestPermissionsResult(host: Any, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode != PermissionAspect.REQUEST_CODE) {
            return
        }
        permissions.forEachIndexed { index, s ->
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                //申请失败
                PermissionUtils.somePermissionPermanentlyDenied(host, s)
                return
            }
        }
        resultAction.doAction()
    }

    /**
     * 检查被拒绝权限列表中的至少一个权限是否已被永久拒绝（用户点击“永不再询问”）
     *
     * @param host activity或fragment
     * @param perms 权限
     */
    @JvmStatic
    fun somePermissionPermanentlyDenied(host: Any, perms: String) {
        if (host is Activity) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(host, perms)) {
                showSettingDialog(host)
            }
        } else if (host is Fragment) {
            if (!host.shouldShowRequestPermissionRationale(perms)) {
                showSettingDialog(host.activity!!)
            }
        } else {
            throw AssertionError("host 不是activity 或 fragment")
        }
    }

    /**
     * 显示跳转设置的窗口
     */
    @JvmStatic
    fun showSettingDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("温馨提示")
        builder.setMessage("如果没有请求的权限，这个应用程序可能无法正常工作。打开app设置界面，修改app权限。")
        builder.setPositiveButton("确定") { dialog, _ ->
            context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.fromParts("package", context.packageName, null)))
            dialog.dismiss()
        }
        builder.setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    /**
     * 权限请求一条龙
     */
    @JvmStatic
    fun checkPermissions(host: Activity, action: PermissionAction, @IntRange(from = 0) requestCode: Int,
                         @Size(min = 1) vararg perms: String) {
        //第一步:先判断是否有权限
        if (hasPermissions(host, *perms)) {
            //有权限
            action.doAction()
        } else {
            //第二步:判断是否需要解释(似乎可以不用这步)
            if (shouldShowRationale(host, *perms)) {
                val builder = AlertDialog.Builder(host)
                builder.setTitle("温馨提示")
                builder.setMessage("没有此权限会导致某些功能无法使用或崩溃")
                builder.setPositiveButton("申请权限") { dialog, _ ->
                    //申请权限
                    resultAction = action
                    ActivityCompat.requestPermissions(host, perms, requestCode)
                    dialog.dismiss()
                }
                builder.setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                builder.show()
            } else {
                //第三步:不需要就申请
                resultAction = action
                ActivityCompat.requestPermissions(host, perms, requestCode)
            }
        }
    }

    @JvmStatic
    fun checkPermissions(host: Fragment, action: PermissionAction, @IntRange(from = 0) requestCode: Int,
                         @Size(min = 1) vararg perms: String) {
        //第一步:先判断是否有权限
        if (hasPermissions(host.activity!!, *perms)) {
            //有权限
            action.doAction()
        } else {
            //第二步:判断是否需要解释(似乎可以不用这步)
            if (shouldShowRationale(host, *perms)) {
                val builder = AlertDialog.Builder(host.activity!!)
                builder.setTitle("温馨提示")
                builder.setMessage("没有此权限会导致某些功能无法使用或崩溃")
                builder.setPositiveButton("申请权限") { dialog, _ ->
                    //申请权限
                    resultAction = action
                    host.requestPermissions(perms, requestCode)
                    dialog.dismiss()
                }
                builder.setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                builder.show()
            } else {
                //第三步:不需要就申请
                resultAction = action
                host.requestPermissions(perms, requestCode)
            }
        }
    }


    interface PermissionAction {
        fun doAction()
    }
}