package com.hxw.wanandroid.mvp

import android.os.Bundle
import com.hxw.core.base.AbstractActivity
import com.hxw.core.utils.StatusBarUtils
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbstractActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        StatusBarUtils.noStatusBar(this)

        nav_view.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_camera->{}
                else -> {

                }
            }


            return@setNavigationItemSelectedListener true
        }
    }

}
