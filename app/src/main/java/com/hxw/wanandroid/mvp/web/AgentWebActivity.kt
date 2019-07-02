package com.hxw.wanandroid.mvp.web

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.hxw.core.base.AbstractActivity
import com.hxw.wanandroid.Constant
import com.hxw.wanandroid.R
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebViewClientDelegate
import kotlinx.android.synthetic.main.activity_web.*

/**
 * Web界面,展示详情
 * @author hxw
 * @date 2019/2/2
 */
class AgentWebActivity : AbstractActivity() {

    private lateinit var mAgent: AgentWeb
    private val link: String? by lazy { intent.getStringExtra(Constant.WEB_URL) }
    override val layoutId: Int
        get() = R.layout.activity_web

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(tool_title)
        tool_title.setNavigationOnClickListener { finish() }

        mAgent = AgentWeb.with(this)
                .setAgentWebParent(ll_web, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(link)


    }

    override fun onResume() {
        mAgent.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgent.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgent.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}