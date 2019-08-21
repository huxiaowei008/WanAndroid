package com.hxw.core.utils


import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.AlgorithmParameterSpec

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 加密相关工具类
 *
 * @author hxw
 * @date 2018/5/7
 */

//    private static final char[] HEX_DIGITS =
//            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


/**
 * Return the hex string of MD5 encryption.
 *
 * @param data The data.
 * @return the hex string of MD5 encryption
 */
fun encryptMD5ToString(data: ByteArray?): String {
    return if (data == null || data.isEmpty()) {
        ""
    } else HexUtils.bytes2HexStr2(encryptMD5(data))
}

/**
 * Return the bytes of MD5 encryption.
 *
 * @param data The data.
 * @return the bytes of MD5 encryption
 */
fun encryptMD5(data: ByteArray): ByteArray {
    return hashTemplate(data, "MD5")
}

/**
 * Return the hex string of SHA1 encryption.
 *
 * @param data The data.
 * @return the hex string of SHA1 encryption
 */
fun encryptSHA1ToString(data: ByteArray?): String {
    return if (data == null || data.isEmpty()) {
        ""
    } else HexUtils.bytes2HexStr2(encryptSHA1(data))
}

/**
 * Return the bytes of SHA1 encryption.
 *
 * @param data The data.
 * @return the bytes of SHA1 encryption
 */
fun encryptSHA1(data: ByteArray): ByteArray {
    return hashTemplate(data, "SHA1")
}

/**
 * Return the bytes of hash encryption.
 *
 * @param data      The data.
 * @param algorithm The name of hash encryption.
 * @return the bytes of hash encryption
 */
private fun hashTemplate(data: ByteArray, algorithm: String): ByteArray {
    return try {
        val md = MessageDigest.getInstance(algorithm)
        md.update(data)
        md.digest()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        ByteArray(0)
    }
}

/**
 * Return the hex string of AES encryption.
 *
 * @param data           The data.
 * @param key            The key.
 * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
 * @param initVector     The buffer with the IV. The contents of the
 * buffer are copied to protect against subsequent modification.
 * @return the hex string of AES encryption
 */
fun encryptAES2HexString(
    data: ByteArray,
    key: ByteArray,
    transformation: String,
    initVector: ByteArray
): String {
    return HexUtils.bytes2HexStr1(encryptAES(data, key, transformation, initVector))
}

/**
 * Return the bytes of AES encryption.
 *
 * @param data           The data.
 * @param key            The key.
 * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
 * @param initVector     The buffer with the IV. The contents of the
 * buffer are copied to protect against subsequent modification.
 * @return the bytes of AES encryption
 */
fun encryptAES(
    data: ByteArray,
    key: ByteArray,
    transformation: String,
    initVector: ByteArray
): ByteArray? {
    return symmetricTemplate(data, key, "AES", transformation, initVector, true)
}

/**
 * Return the bytes of symmetric encryption or decryption.
 *
 * @param data           The data.
 * @param key            The key.
 * @param algorithm      The name of algorithm.
 * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
 * @param isEncrypt      True to encrypt, false otherwise.
 * @return the bytes of symmetric encryption or decryption
 */
private fun symmetricTemplate(
    data: ByteArray?,
    key: ByteArray?,
    algorithm: String,
    transformation: String,
    initVector: ByteArray?,
    isEncrypt: Boolean
): ByteArray? {
    if (data == null || data.isEmpty() || key == null || key.isEmpty()) {
        return null
    }
    try {
        val secretKey: SecretKey
        if ("DES" == algorithm) {
            val desKey = DESKeySpec(key)
            val keyFactory = SecretKeyFactory.getInstance(algorithm)
            secretKey = keyFactory.generateSecret(desKey)
        } else {
            secretKey = SecretKeySpec(key, algorithm)
        }
        val cipher = Cipher.getInstance(transformation)
        if (initVector == null || initVector.isEmpty()) {
            cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, secretKey)
        } else {
            val params = IvParameterSpec(initVector)
            cipher.init(
                if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE,
                secretKey,
                params
            )
        }
        return cipher.doFinal(data)
    } catch (e: Throwable) {
        e.printStackTrace()
        return null
    }
}
