package com.hxw.core.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 加密相关工具类
 *
 * @author hxw
 * @date 2018/5/7
 */
public final class EncryptUtils {
//    private static final char[] HEX_DIGITS =
//            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    public static String encryptMD5ToString(@Nullable byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return HexUtils.bytes2HexStr2(encryptMD5(data));
    }

    /**
     * Return the bytes of MD5 encryption.
     *
     * @param data The data.
     * @return the bytes of MD5 encryption
     */
    public static byte[] encryptMD5(@NonNull byte[] data) {
        return hashTemplate(data, "MD5");
    }

    /**
     * Return the hex string of SHA1 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA1 encryption
     */
    public static String encryptSHA1ToString(@Nullable byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return HexUtils.bytes2HexStr2(encryptSHA1(data));
    }

    /**
     * Return the bytes of SHA1 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA1 encryption
     */
    public static byte[] encryptSHA1(@NonNull byte[] data) {
        return hashTemplate(data, "SHA1");
    }

    /**
     * Return the bytes of hash encryption.
     *
     * @param data      The data.
     * @param algorithm The name of hash encryption.
     * @return the bytes of hash encryption
     */
    private static byte[] hashTemplate(@NonNull byte[] data, final String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * Return the hex string of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param initVector     The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the hex string of AES encryption
     */
    public static String encryptAES2HexString(final byte[] data,
                                              final byte[] key,
                                              final String transformation,
                                              final byte[] initVector) {
        return HexUtils.bytes2HexStr1(encryptAES(data, key, transformation, initVector));
    }

    /**
     * Return the bytes of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param initVector     The buffer with the IV. The contents of the
     *                       buffer are copied to protect against subsequent modification.
     * @return the bytes of AES encryption
     */
    public static byte[] encryptAES(final byte[] data,
                                    final byte[] key,
                                    final String transformation,
                                    final byte[] initVector) {
        return symmetricTemplate(data, key, "AES", transformation, initVector, true);
    }

    /**
     * Return the bytes of symmetric encryption or decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param algorithm      The name of algorithm.
     * @param transformation The name of the transformation, e.g., <i>DES/CBC/PKCS5Padding</i>.
     * @param isEncrypt      True to encrypt, false otherwise.
     * @return the bytes of symmetric encryption or decryption
     */
    private static byte[] symmetricTemplate(final byte[] data,
                                            final byte[] key,
                                            final String algorithm,
                                            final String transformation,
                                            final byte[] initVector,
                                            final boolean isEncrypt) {
        if (data == null || data.length == 0 || key == null || key.length == 0) {
            return null;
        }
        try {
            SecretKey secretKey;
            if ("DES".equals(algorithm)) {
                DESKeySpec desKey = new DESKeySpec(key);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
                secretKey = keyFactory.generateSecret(desKey);
            } else {
                secretKey = new SecretKeySpec(key, algorithm);
            }
            Cipher cipher = Cipher.getInstance(transformation);
            if (initVector == null || initVector.length == 0) {
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey);
            } else {
                AlgorithmParameterSpec params = new IvParameterSpec(initVector);
                cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey, params);
            }
            return cipher.doFinal(data);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
