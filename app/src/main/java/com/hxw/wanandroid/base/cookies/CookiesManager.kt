package com.hxw.wanandroid.base.cookies

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


/**
 * Cookie 处理
 * @author hxw
 * @date 2019/2/12
 */
class CookiesManager(context: Context) : CookieJar {

    private val cookieStore = PersistentCookieStore(context.applicationContext)

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        for (item in cookies) {
            cookieStore.add(url, item)
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return cookieStore.get(url)
    }

}