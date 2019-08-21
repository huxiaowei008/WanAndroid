package com.hxw.core.utils

import java.util.*

/**
 * 工具类在kotlin中的拓展
 * @author hxw
 * @date 2018/12/18
 */
fun String.jsonFormat() = StringUtils.jsonFormat(this)

fun Date.toStr(pattern: String = "yyyy-MM-dd HH:mm:ss") = date2String(this, pattern)

fun String.toDate(pattern: String = "yyyy-MM-dd HH:mm:ss") = string2Date(this, pattern)

fun String?.encryptMD5ToString() = EncryptUtils.encryptMD5ToString(this?.toByteArray())

fun String.encryptMD5() = EncryptUtils.encryptMD5(this.toByteArray())

fun String?.encryptSHA1ToString() = EncryptUtils.encryptSHA1ToString(this?.toByteArray())

fun String.encryptSHA1() = EncryptUtils.encryptSHA1(this.toByteArray())

fun String.addCharToLeft(strLength: Int, c: Char = '0') = HexUtils.addCharToLeft(this, strLength, c)

fun String.addCharToRight(strLength: Int, c: Char = '0') = HexUtils.addCharToRight(this, strLength, c)

fun String.toSigned() = HexUtils.unSignedToSigned(this)

fun String.hexToInt() = Integer.parseInt(this, 16)

fun ByteArray.toHexStr() = HexUtils.bytes2HexStr2(this)

fun String.hexToBytes() = HexUtils.hexStr2Bytes(this)

fun Throwable?.onError() = onError(this)