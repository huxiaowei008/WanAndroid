package com.hxw.wanandroid.mvp.register

import android.os.Bundle
import com.hxw.core.base.AbstractActivity
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @author hxw on 2018/6/6.
 *
 */
class RegisterActivity : AbstractActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun init(savedInstanceState: Bundle?) {

        fa_btn.setOnClickListener { finish() }
    }
}