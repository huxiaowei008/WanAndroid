package com.hxw.wanandroid;

import com.hxw.core.utils.EncryptUtils;

/**
 * @author hxw on 2018/6/27.
 */
public class Test {
    public static void main(String args[]) {
        String str = "appid=appid&noncestr=noncestr&sdk_ticket=-p3A5zVP95IuafPhzA6lRR95_F9nZEBfJ_n4E9t8ZFWKJTDPOwccVQhHCwDBmvLkayF_jh-m9HOExhumOziDWA&timestamp=1417508194";
        String en = EncryptUtils.encryptSHA1ToString(str);
        System.out.print("结果->" + en);
    }
}
