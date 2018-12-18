package com.hxw.core.utils

import android.content.Context
import java.util.*

/**
 * 工具类在kotlin中的拓展
 * @author hxw on 2018/12/18.
 *
 */
fun String.jsonFormat() = StringUtils.jsonFormat(this)

fun Float.dpToPx(context: Context) = AppUtils.dpToPx(context, this)

fun Float.spToPx(context: Context) = AppUtils.spToPx(context, this)

fun Date.date2String(pattern: String = "yyyy-MM-dd HH:mm:ss") = DateUtils.date2String(this, pattern)

fun String.string2Date(pattern: String = "yyyy-MM-dd HH:mm:ss") = DateUtils.string2Date(this, pattern)

fun String?.encryptMD5ToString() = EncryptUtils.encryptMD5ToString(this)

fun String.encryptMD5() = EncryptUtils.encryptMD5(this.toByteArray())

fun String?.encryptSHA1ToString() = EncryptUtils.encryptSHA1ToString(this)

fun String.encryptSHA1() = EncryptUtils.encryptSHA1(this.toByteArray())