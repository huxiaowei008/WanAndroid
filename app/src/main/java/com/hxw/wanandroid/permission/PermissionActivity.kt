package com.hxw.wanandroid.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import com.hxw.core.base.AbstractActivity
import com.hxw.core.utils.PermissionUtils
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.activity_permission.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

/**
 * @author hxw on 2018/8/4.
 *
 */
class PermissionActivity : AbstractActivity() {

    private val LOCATION_AND_CONTACTS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)
    private val RC_CAMERA_PERM = 123
    private val RC_LOCATION_CONTACTS_PERM = 124
    override fun getLayoutId(): Int {
        return R.layout.activity_permission
    }

    override fun init(savedInstanceState: Bundle?) {
        button_camera.setOnClickListener { cameraTask() }
        button_location_and_contacts.setOnClickListener { locationAndContactsTask() }
    }

    private fun cameraTask() {
        PermissionUtils.checkPermissions(this@PermissionActivity, object : PermissionUtils.PermissionAction {
            override fun doAction() {
                longToast("TODO: Camera things")
            }
        }, RC_CAMERA_PERM, Manifest.permission.CAMERA)

    }

    private fun locationAndContactsTask() {
        PermissionUtils.checkPermissions(this@PermissionActivity, object : PermissionUtils.PermissionAction {
            override fun doAction() {
                longToast("TODO: Location and Contacts things")
            }
        }, RC_LOCATION_CONTACTS_PERM, *LOCATION_AND_CONTACTS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.forEachIndexed { index, s ->
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                //申请成功
                toast("申请成功 requestCode:$requestCode")
            } else {
                toast("申请失败 requestCode:$requestCode")
                PermissionUtils.somePermissionPermanentlyDenied(this@PermissionActivity, s)
            }
        }
    }
}