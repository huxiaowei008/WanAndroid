package com.hxw.core.utils

/**
 * 工具类在kotlin中的拓展
 * @author hxw
 * @date 2018/12/18
 */
fun String.jsonFormat() = StringUtils.jsonFormat(this)

fun String.addCharToLeft(strLength: Int, c: Char = '0') =
    HexUtils.addCharToLeft(this, strLength, c)

fun String.addCharToRight(strLength: Int, c: Char = '0') =
    HexUtils.addCharToRight(this, strLength, c)

fun String.toSigned() = HexUtils.unSignedToSigned2(this)

fun String.hexToInt() = Integer.parseInt(this, 16)

fun ByteArray.toHexStr(): String = HexUtils.bytes2HexStr2(this)

fun String.hexToBytes(): ByteArray = HexUtils.hexStr2Bytes(this)