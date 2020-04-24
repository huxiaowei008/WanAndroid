package com.hxw.wanandroid.mvp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hxw.core.base.AbstractActivity
import com.hxw.core.base.exceptionHandler
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.mvp.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_content_main.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import retrofit2.await

/**
 * @author hxw
 * @date 2018/7/17
 */
class MainActivity : AbstractActivity() {
    override val layoutId: Int = R.layout.activity_main
    private val api: WanApi by inject()
    private val sp: SharedPreferences by inject()

    override fun init(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
        setSupportActionBar(toolbar)
        //侧滑菜单在toolbar上的动画效果
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val controller = findNavController(R.id.host_nav_fragment)
        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragment_home -> toolbar.title = "WanAndroid"

                R.id.fragment_system -> toolbar.title = "体系"

                R.id.fragment_navigation -> toolbar.title = "导航"

                R.id.fragment_project -> toolbar.title = "项目"

                R.id.fragment_wx_article -> toolbar.title = "公众号"
                else -> toolbar.title = "WanAndroid"

            }
        }
        bottom_navigation.setupWithNavController(controller)

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_out -> {
                    lifecycle.coroutineScope.launch(exceptionHandler) {
                        val result = api.loginOut()
                        if (result.errorCode == Constant.NET_SUCCESS) {
                            toast("登出成功")
                        } else {
                            toast(result.errorMsg)
                        }
                    }
                }
                else -> {
                    val intent= Intent("xixun.intent.action.TEMPERATURE_HUMIDITY")
                    intent.setPackage("add.xixun.www.testcard")
                    sendBroadcast(intent)
                }
            }
            return@setNavigationItemSelectedListener true
        }

        nav_view.getHeaderView(0).let {
            it.findViewById<TextView>(R.id.tv_username)
                .text = sp.getString(Constant.USERNAME, "Android Studio")
            it.findViewById<ImageView>(R.id.iv_head).setOnClickListener {
                startActivity<LoginActivity>()
            }
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
