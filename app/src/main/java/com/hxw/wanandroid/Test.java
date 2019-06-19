package com.hxw.wanandroid;

import com.hxw.core.utils.EncryptUtils;
import com.hxw.core.utils.HexUtils;

import java.util.Random;

/**
 * @author hxw
 * @date 2018/6/27
 */
public class Test {
    public static void main(String[] args) {
        String str = "appid=appid&noncestr=noncestr&sdk_ticket=-p3A5zVP95IuafPhzA6lRR95_F9nZEBfJ_n4E9t8ZFWKJTDPOwccVQhHCwDBmvLkayF_jh-m9HOExhumOziDWA&timestamp=1417508194";
        String en = EncryptUtils.encryptSHA1ToString(str);
        System.out.print("结果->" + en);

        String s = "21006b0d0a2b43434c4b3a31382f30382f3131" +
                "2c30353a35343a34372b33320010fcacff7cfd" +
                "22383638373434303337373435333635383938" +
                "36313131383230373030333739313933350c31" +
                "31372e36302e3135372e3133372c3536383300" +
                "00193250783278783c2d0f00820096097e";
        String crc=HexUtils.calcCrc16(s.substring(0,s.length()-4));
        System.out.println("crc->" + crc);
        final byte[] data={1,0x12,0x13};
        final StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data) {
            stringBuilder.append(String.format("%02X ", byteChar));
        }

        System.out.println(new String(data) + "\n" + stringBuilder.toString());
        byte[] f={1};
        System.out.println(HexUtils.bytes2HexStr1(f));

        String d="fe11000c";
        byte[] e=HexUtils.hexStr2Bytes(d);
        System.out.println(d);

        String g="FE1100107666151A6F2B0C6608B042E3DC663AEDEB88";
        String crcrc=HexUtils.calcCrc16(g);
        System.out.println("crc->"+crcrc);
    }
}
