package com.hxw.wanandroid.permission

import android.Manifest
import android.os.Bundle
import com.hxw.core.base.AbstractFragment
import com.hxw.core.utils.PermissionUtils
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.fragment_permission.*
import org.jetbrains.anko.support.v4.longToast


/**
 * @author hxw
 * @date 2018/8/4
 */
class PermissionFragment : AbstractFragment() {

    private val RC_SMS_PERM = 122

    override val layoutId: Int
        get() = R.layout.fragment_permission

    override fun init(savedInstanceState: Bundle?) {
        button_sms.setOnClickListener {
            smsTask()
//            startActivityForResult(Intent(activity, TakePhotoActivity::class.java),2000)
        }
    }


    private fun smsTask() {
        PermissionUtils.checkPermissions(this, arrayOf(Manifest.permission.READ_SMS)) {
            longToast("TODO: SMS things")
        }
    }

}