package com.hxw.wanandroid.base.cookies

import android.content.Context
import android.text.TextUtils
import androidx.core.content.edit
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.concurrent.ConcurrentHashMap


/**
 * Cookie 处理,自己写的
 * @author hxw
 * @date 2019/2/12
 */
class CookiesManager(context: Context) : CookieJar {

    private val cookiePrefs = context.getSharedPreferences("Cookies_Prefs", Context.MODE_PRIVATE)
    private val cookiesMap: HashMap<String, ConcurrentHashMap<String, Cookie>> = hashMapOf()

    //    private val cookieStore = PersistentCookieStore(context.applicationContext)
    init {
        val prefsMap = cookiePrefs.all
        prefsMap.entries.forEach { entry ->
            val cookieNames = TextUtils.split(entry.value as String, ",")
            cookieNames.forEach { name ->
                val encodedCookie = cookiePrefs.getString(name, null)
                if (encodedCookie != null) {
                    val decodedCookie = SerializableOkHttpCookies.decodeCookie(encodedCookie)
                    if (decodedCookie != null) {
                        if (!cookiesMap.containsKey(entry.key)) {
                            cookiesMap[entry.key] = ConcurrentHashMap()
                        }
                        cookiesMap[entry.key]!![name] = decodedCookie
                    }
                }
            }

        }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
//        for (item in cookiesMap) {
//            cookieStore.add(url, item)
//        }
        cookies.forEach {
            val name = getCookieToken(it)
            if (!cookiesMap.containsKey(url.host())) {
                cookiesMap[url.host()] = ConcurrentHashMap()
            }
            cookiesMap[url.host()]!![name] = it
            cookiePrefs.edit {
                putString(url.host(), TextUtils.join(",", cookiesMap[url.host()]!!.keys))
                putString(name, SerializableOkHttpCookies.encodeCookie(SerializableOkHttpCookies(it)))
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
//        return cookieStore.get(url)
        val ret = mutableListOf<Cookie>()
        if (cookiesMap.containsKey(url.host())) {
            ret.addAll(cookiesMap[url.host()]!!.values)
        }
        return ret
    }

    private fun getCookieToken(cookie: Cookie): String {
        return cookie.domain() + "@" + cookie.name()
    }

}