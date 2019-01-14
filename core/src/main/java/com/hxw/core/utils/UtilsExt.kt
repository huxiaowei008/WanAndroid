package com.hxw.core.utils

import android.content.Context
import java.util.*

/**
 * 工具类在kotlin中的拓展
 * @author hxw on 2018/12/18.
 *
 */
fun String.jsonFormat() = StringUtils.jsonFormat(this)

fun Date.toStr(pattern: String = "yyyy-MM-dd HH:mm:ss") = DateUtils.date2String(this, pattern)

fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss") = DateUtils.string2Date(this, pattern)

fun String?.encryptMD5ToString() = EncryptUtils.encryptMD5ToString(this)

fun String.encryptMD5() = EncryptUtils.encryptMD5(this.toByteArray())

fun String?.encryptSHA1ToString() = EncryptUtils.encryptSHA1ToString(this)

fun String.encryptSHA1() = EncryptUtils.encryptSHA1(this.toByteArray())

fun String.addCharToLeft(strLength: Int, c: Char = '0') = HexUtils.addCharToLeft(this, strLength, c)

fun String.addCharToRight(strLength: Int, c: Char = '0') = HexUtils.addCharToRight(this, strLength, c)

fun String.toSigned()=HexUtils.unSignedToSigned(this)