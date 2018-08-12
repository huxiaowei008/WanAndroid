package com.hxw.core.utils;

import java.util.Random;

/**
 * 十六进制转化相关工具
 *
 * @author hxw on 2018/6/5.
 */
public final class HexUtils {
    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static byte[] crc16_tab_h = new byte[]{
            0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65,
            0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64,
            0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65,
            0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65,
            0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65,
            0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64,
            0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64,
            1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64,
            0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65,
            0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64,
            0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65,
            0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65,
            0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65,
            0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65,
            1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65,
            0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65,
            0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65,
            0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64,
            0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65,
            0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65,
            0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65,
            0, -63, -127, 64
    };
    private static byte[] crc16_tab_l = new byte[]{
            0, -64, -63, 1, -61, 3, 2, -62, -58, 6, 7, -57,
            5, -59, -60, 4, -52, 12, 13, -51, 15, -49, -50, 14,
            10, -54, -53, 11, -55, 9, 8, -56, -40, 24, 25, -39,
            27, -37, -38, 26, 30, -34, -33, 31, -35, 29, 28, -36,
            20, -44, -43, 21, -41, 23, 22, -42, -46, 18, 19, -45,
            17, -47, -48, 16, -16, 48, 49, -15, 51, -13, -14, 50,
            54, -10, -9, 55, -11, 53, 52, -12, 60, -4, -3, 61,
            -1, 63, 62, -2, -6, 58, 59, -5, 57, -7, -8, 56,
            40, -24, -23, 41, -21, 43, 42, -22, -18, 46, 47, -17,
            45, -19, -20, 44, -28, 36, 37, -27, 39, -25, -26, 38,
            34, -30, -29, 35, -31, 33, 32, -32, -96, 96, 97, -95,
            99, -93, -94, 98, 102, -90, -89, 103, -91, 101, 100, -92,
            108, -84, -83, 109, -81, 111, 110, -82, -86, 106, 107, -85,
            105, -87, -88, 104, 120, -72, -71, 121, -69, 123, 122, -70,
            -66, 126, 127, -65, 125, -67, -68, 124, -76, 116, 117, -75,
            119, -73, -74, 118, 114, -78, -77, 115, -79, 113, 112, -80,
            80, -112, -111, 81, -109, 83, 82, -110, -106, 86, 87, -105,
            85, -107, -108, 84, -100, 92, 93, -99, 95, -97, -98, 94,
            90, -102, -101, 91, -103, 89, 88, -104, -120, 72, 73, -119,
            75, -117, -118, 74, 78, -114, -113, 79, -115, 77, 76, -116,
            68, -124, -123, 69, -121, 71, 70, -122, -126, 66, 67, -125,
            65, -127, -128, 64};

    /**
     * 将十六进制字符数组转换为十进制字节数组
     * data长度需为偶数,例:
     * data = "12345a".toCharArray() = {'1','2','3','4','5','a'}
     * 结果是{18,52,90} 0x12->18  0x34->52  0x5a->90
     *
     * @param data 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] hexStr2Bytes(char[] data) {
        if (data == null) {
            return new byte[0];
        }
        int len = data.length;
        if (len <= 0) {
            return new byte[0];
        }

        //取最低位,判断是否为0,为0说明是偶数,为1说明是奇数
        if ((len & 0x01) != 0) {
            throw new RuntimeException("data 长度是奇数");
        }
        //len >> 1 右移一位，等价于/2
        byte[] out = new byte[len >> 1];

        // (0&0=0,0&1=0,1&1=1,0|0=0,0|1=1,1|1=1)
        for (int j = 0; j < len; j += 2) {
            //取到第一个值后左移4位变成高4位,空出低4位为0 取到第二个值后补到低4位上
            int f = (toDigit(data[j]) << 4 | toDigit(data[j + 1]));
            // & 0xff的作用是把更高的位清0
            out[j >> 1] = (byte) (f & 0xFF);
        }
        return out;
    }

    /**
     * 将十六进制字符数组转换为十进制字节数组
     * data长度需为偶数,例:
     * data="12345a"
     * 结果是{18,52,90} 0x12->18  0x34->52  0x5a->90
     *
     * @param data 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] hexStr2Bytes(String data) {
        if (data == null) {
            return new byte[0];
        }
        int len = data.length();

        if (len % 2 != 0) {
            throw new RuntimeException("data 长度是奇数");
        }

        int size = len / 2;
        byte[] out = new byte[size];

        for (int i = 0; i < size; i++) {
            int start = i * 2;
            out[i] = (byte) (Integer
                    .parseInt(data.substring(start, start + 2), 16) & 0xFF);
        }
        return out;
    }

    /**
     * 将十六进制字符转换成一个十进制整数
     * 'a'->10
     *
     * @param ch 十六进制char
     * @return 一个十进制整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    private static int toDigit(char ch) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("不合法的十六进制字符-> " + ch);
        }
        return digit;
    }

    /**
     * 将字节数组转换为十六进制字符数组
     * {18,52,90} -> {'1','2','3','4','5','a'}
     * 有符号的byte会被过滤成1个字节,变成无符号
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static char[] bytes2Hex(byte[] data) {
        if (data == null) {
            return new char[0];
        }
        int l = data.length;
        if (l <= 0) {
            return new char[0];
        }
        //l << 1 左移1位,相当于 *2
        char[] out = new char[l << 1];
        // (0&0=0,0&1=0,1&1=1,0|0=0,0|1=1,1|1=1)
        for (int i = 0, j = 0; i < l; i++) {
            //把低4位清0,然后无符号右移4位
            //或者先右移4位,在高4位清0
            out[j++] = DIGITS_LOWER[data[i] >>> 4 & 0x0F];
            //把高4位清0
            out[j++] = DIGITS_LOWER[data[i] & 0x0F];
        }
        return out;
    }

    /**
     * 将字节数组转换为十六进制字符数组
     * {18,52,90} -> "12345a"
     * 注:Integer.toHexString(-12)-> fffffff4
     * (item & 0xFF) 过滤掉负数的高位,只保留末位1个字节,等于把有符号转为无符号
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static String bytes2HexStr(byte[] data) {
        if (data == null || data.length <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (byte item : data) {
            String str = Integer.toHexString(item & 0xFF);
            if (str.length() == 1) {
                builder.append('0');
            }
            builder.append(str);
        }
        return builder.toString();
    }

    /**
     * 产生十六进制随机数
     *
     * @param len 随机数长度(字节)
     * @return 返回的字符串长度是随机数长度的2倍
     */
    public static String randomHex(int len) {
        //存放产生随机数的byte[]
        byte[] random = new byte[len];
        //此方法产生的随机byte是带有符号的,就是说会产生负数
        new Random().nextBytes(random);
        return bytes2HexStr(random);
    }

    /**
     * 为字符串左边补指定字符
     *
     * @param str       需要补0的字符串(通常是进制字符串)
     * @param strLength 完整字符串的长度(若以字节算,通常是 字节数*2)
     * @param c         需要补的字符
     * @return 补完整的字符串
     */
    public static String addCharToLeft(String str, int strLength, char c) {
        int strLen = str.length();
        if (strLen < strLength) {
            StringBuilder builder = new StringBuilder();
            builder.append(str);
            do {
                builder.insert(0, c);
            } while (builder.length() < strLength);
            return builder.toString();
        } else {
            return str.substring(strLen - strLength);
        }
    }

    /**
     * 为字符串右边补指定字符
     *
     * @param str       需要补0的字符串(通常是进制字符串)
     * @param strLength 完整字符串的长度(若以字节算,通常是 字节数*2)
     * @param c         需要补的字符
     * @return 补完整的字符串
     */
    public static String addCharToRight(String str, int strLength, char c) {
        int strLen = str.length();
        if (strLen < strLength) {
            StringBuilder builder = new StringBuilder();
            builder.append(str);
            do {
                builder.append(c);
            } while (builder.length() < strLength);
            return builder.toString();
        } else {
            return str.substring(strLen - strLength);
        }
    }

    /**
     * 把无符号的十六进制转化成有符号的整型
     * (1个字节是-128~127)
     * (2个字节是-32768~32767)
     *
     * @param value 无符号的十六进制值
     * @return 转化为有符号的值
     */
    public static int unSignedToSigned(String value) {
        int length = value.length();
        if (length <= 2) {
            //1个字节
            int result = Integer.parseInt(value, 16);
            if (result > 127) {
                return result - 256;
            }
            return result;
        } else if (length <= 4) {
            //2个字节
            int result = Integer.parseInt(value, 16);
            if (result > 32767) {
                return result - 65536;
            }
            return result;
        } else {
            //超过2个字节,确定这么大吗?要自己增加
            return Integer.parseInt(value, 16);
        }
    }

    /**
     * crc校验
     *
     * @param s 需要校验的字符
     * @return 校验生产的结果(2字节)
     */
    public static String calcCrc16(String s) {
        byte[] data = hexStr2Bytes(s);
        int result = calcCrc16(data, 0, data.length);
        return addCharToLeft(Integer.toHexString(result), 4, '0');
    }

    /**
     * crc校验
     *
     * @param data   需要校验的数据
     * @param offset 从数据的哪位开始校验
     * @param len    校验多少长度
     * @return 校验结果(2字节)
     */
    public static int calcCrc16(byte[] data, int offset, int len) {
        return calcCrc16(data, offset, len, 0xFFFF);
    }

    public static int calcCrc16(byte[] data, int offset, int len, int preval) {
        int ucCRCHi = (preval & 0xFF00) >> 8;
        int ucCRCLo = preval & 0xFF;

        for (int i = 0; i < len; i++) {
            int iIndex = (ucCRCLo ^ data[offset + i]) & 0xFF;
            ucCRCLo = ucCRCHi ^ crc16_tab_h[iIndex];
            ucCRCHi = crc16_tab_l[iIndex];
        }

        return (ucCRCHi & 0xFF) << 8 | (ucCRCLo & 0xFF) & 0xFFFF;
    }
}
