package com.hxw.wanandroid;

import com.hxw.core.utils.EncryptUtils;
import com.hxw.core.utils.HexUtils;

/**
 * @author hxw
 * @date 2018/6/27
 */
public class Test {
    public static void main(String args[]) {
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
        System.out.print("crc->" + crc);
        String[] permissions=new String[]{"dd","dd"};
    }
}
