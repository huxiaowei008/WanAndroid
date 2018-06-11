package com.hxw.wanandroid.mvp.login

import android.content.Intent
import android.os.Bundle
import com.hxw.core.base.AbstractActivity
import com.hxw.wanandroid.R
import com.hxw.wanandroid.mvp.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

/**
 * @author hxw on 2018/6/2.
 *
 */
class LoginActivity : AbstractActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun init(savedInstanceState: Bundle?) {

        fa_btn.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }

        btn_login.setOnClickListener {
            Timber.i("登陆")
        }


    }
}