package com.hxw.wanandroid.mvp.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
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

        fa_btn.setOnClickListener {
            /**
             * 过渡动画需要在5.0版本以上
             * 对于transitionName,可以在xml中设置,注意版本在5.0以上
             * 或者定义成常量,在onCreate()中通过
             * ViewCompat.setTransitionName(mHeaderImageView, VIEW_NAME_HEADER_IMAGE);
             * 方法设置(或使用View.setTransitionName()方法5.0版本)
             */
            val options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions
                        .makeSceneTransitionAnimation(this, fa_btn, fa_btn.transitionName)
            } else {
                null
            }
            startActivity(Intent(this, RegisterActivity::class.java), options?.toBundle())
        }

        btn_login.setOnClickListener {
            Timber.i("登陆")
        }


    }
}