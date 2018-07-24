package com.hxw.wanandroid.mvp

import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hxw.core.base.AbstractActivity
import com.hxw.core.utils.StatusBarUtils
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_content_main.*

class MainActivity : AbstractActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        //侧滑菜单在toolbar上的动画效果
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        bottom_navigation.setupWithNavController(findNavController(R.id.host_nav_fragment))



        nav_view.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_camera -> {
                }
                else -> {

                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.host_nav_fragment).navigateUp()
    }

    override fun useFragment(): Boolean {
        return true
    }

}
