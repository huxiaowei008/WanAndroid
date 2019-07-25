package com.hxw.core.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hxw.core.utils.PermissionUtils


/**
 * [Activity] 基类
 * 如果继承这类使用Presenter,记得添加生命周期订阅  getLifecycle().addObserver(mPresenter);
 * (现在已经在[com.hxw.core.mvp.BasePresenter]中做好了,记得调用mPresenter.takeView())
 *
 * @author hxw
 * @date 2018/5/5
 */
abstract class AbstractActivity : AppCompatActivity(), IActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutId != 0) {
            setContentView(layoutId)
        }
        init(savedInstanceState)
    }

    override fun useFragment(): Boolean {
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }
}
