package com.hxw.wanandroid.mvp.login

import android.os.Bundle
import com.hxw.core.base.AbstractActivity
import com.hxw.core.utils.HexUtils
import com.hxw.wanandroid.R
import kotlinx.android.synthetic.main.activity_login.*
import java.math.BigInteger

/**
 * @author hxw on 2018/6/2.
 *
 */
class LoginActivity : AbstractActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun init(savedInstanceState: Bundle?) {

        fa_btn.setOnClickListener { }

        btn_login.setOnClickListener { }

        val a = "12345a"
        val b = HexUtils.unSignedToSigned(Integer.parseInt("00", 16))
        val c = HexUtils.unSignedToSigned(Integer.parseInt("ff", 16))
        val d = HexUtils.unSignedToSigned(Integer.parseInt("f4", 16))
        val e = HexUtils.unSignedToSigned(Integer.parseInt("56", 16))
        val f = HexUtils.unSignedToSigned(Integer.parseInt("7f", 16))
        val g = HexUtils.unSignedToSigned(Integer.parseInt("03e8", 16))
        val h = HexUtils.unSignedToSigned(Integer.parseInt("fc18", 16))
        val i = HexUtils.unSignedToSigned(Integer.parseInt("ffff", 16))
        val j = HexUtils.unSignedToSigned(Integer.parseInt("0000", 16))
        val k = HexUtils.unSignedToSigned(Integer.parseInt("0056", 16))
        val l = HexUtils.unSignedToSigned(Integer.parseInt("00f4", 16))
        val m = HexUtils.unSignedToSigned(Integer.parseInt("f4", 16))
        val n = BigInteger("300").toByte()
        val o = 0
    }
}