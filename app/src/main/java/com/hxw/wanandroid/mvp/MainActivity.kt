package com.hxw.wanandroid.mvp

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hxw.core.base.AbstractActivity
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_content_main.*

class MainActivity : AbstractActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
        setSupportActionBar(toolbar)
        //侧滑菜单在toolbar上的动画效果
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val controller = findNavController(R.id.host_nav_fragment)
        controller.addOnNavigatedListener { _, destination ->
            when (destination.id) {
                R.id.fragment_home -> {
                    toolbar.title = "首页"
                }
                R.id.fragment_system -> {
                    toolbar.title = "体系"
                }
                R.id.fragment_navigation -> {
                    toolbar.title = "导航"
                }
                R.id.fragment_project -> {
                    toolbar.title = "项目"
                }
                else -> {
                    toolbar.title = "WanAndroid"
                }
            }
        }
        bottom_navigation.setupWithNavController(controller)

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

    override fun onBackPressed() {
//        super.onBackPressed()
        finish()
        //这里禁掉返回按键的监听,简单处理navController对无法监听出栈的事件从而影响交互显示的问题
    }

}
