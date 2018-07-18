package com.hxw.wanandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hxw.core.utils.AppUtils
import com.hxw.wanandroid.mvp.login.LoginPresenter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.jxinject.jx

/**
 * @author hxw on 2018/7/18.
 *
 */
class TestActivity : AppCompatActivity(), KodeinAware {

    override val kodein:Kodein by closestKodein()

    private val presenter:LoginPresenter by lazy { kodein.jx.newInstance<LoginPresenter>() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        presenter.getHotKey()
    }
}