package com.hxw.wanandroid.permission

import android.Manifest
import android.os.Bundle
import com.hxw.core.annotation.CheckPermission
import com.hxw.core.base.AbstractActivity
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
        button_camera.setOnClickListener {
            cameraTask()
//            startActivityForResult(Intent(this@PermissionActivity,TakePhotoActivity::class.java),1000)
        }
        button_location_and_contacts.setOnClickListener { locationAndContactsTask("多任务") }
    }

    @CheckPermission(permissions = [Manifest.permission.CAMERA])
    private fun cameraTask() {
        longToast("TODO: Camera things")
    }

    @CheckPermission(permissions = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS])
    private fun locationAndContactsTask(msg: String) {
        longToast("TODO: Location and Contacts things")
        toast(msg)
    }


}