package com.hxw.core.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author hxw on 2018/5/7.
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
    public static String encryptMD5ToString(@Nullable String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptMD5ToString(data.getBytes());
    }

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
    public static String encryptSHA1ToString(@Nullable String data) {
        if (data == null || data.length() == 0) {
            return "";
        }
        return encryptSHA1ToString(data.getBytes());
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
            return null;
        }
    }
}
