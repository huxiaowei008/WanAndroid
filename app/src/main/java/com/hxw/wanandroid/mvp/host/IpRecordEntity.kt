package com.hxw.wanandroid.mvp.host

/**
 * @author hxw on 2018/9/6.
 *
 */
data class IpRecordEntity(
        val description:String = "演示平台",
        val proxy:String = "http",
        val ip:String = "115.151.15.15",
        val port:String = "10002"
) {}