package com.hxw.wanandroid.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import com.hxw.core.base.AbstractFragment
import com.hxw.core.utils.PermissionUtils
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.fragment_permission.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast

/**
 * @author hxw on 2018/8/4.
 *
 */
class PermissionFragment : AbstractFragment() {

    private val RC_SMS_PERM = 122

    override fun getLayoutId(): Int {
        return R.layout.fragment_permission
    }

    override fun init(savedInstanceState: Bundle?) {
        button_sms.setOnClickListener { smsTask() }
    }

    private fun smsTask() {
        PermissionUtils.checkPermissions(this@PermissionFragment, object : PermissionUtils.PermissionAction {
            override fun doAction() {
                longToast("TODO: SMS things")
            }

        }, RC_SMS_PERM, Manifest.permission.READ_SMS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.forEachIndexed { index, s ->
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                //申请成功
                toast("申请成功 requestCode:$requestCode")
            } else {
                toast("申请失败 requestCode:$requestCode")
                PermissionUtils.somePermissionPermanentlyDenied(this@PermissionFragment, s)
            }
        }
    }
}