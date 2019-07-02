package com.hxw.wanandroid.permission

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.hxw.core.annotation.CheckPermission
import com.hxw.core.base.AbstractFragment
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.fragment_permission.*
import org.jetbrains.anko.support.v4.longToast
import timber.log.Timber

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

    @CheckPermission(permissions = [Manifest.permission.READ_SMS])
    private fun smsTask() {
        longToast("TODO: SMS things")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.tag("result").i("fragmentResult->requestCode==$requestCode,resultCode==$resultCode")
    }
}