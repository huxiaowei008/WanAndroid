package com.hxw.wanandroid;

import com.hxw.core.utils.EncryptUtils;
import com.hxw.core.utils.HexUtils;



/**
 * @author hxw
 * @date 2018/6/27
 */
public class Test {
    public static void main(String[] args) {
//        String str = "appid=appid&noncestr=noncestr&sdk_ticket=-p3A5zVP95IuafPhzA6lRR95_F9nZEBfJ_n4E9t8ZFWKJTDPOwccVQhHCwDBmvLkayF_jh-m9HOExhumOziDWA&timestamp=1417508194";
//        String en = EncryptUtils.encryptSHA1ToString(str.getBytes());
//        System.out.print(HexUtils.calcCrc16("210000"));
//
//        String s = "21006b0d0a2b43434c4b3a31382f30382f3131" +
//                "2c30353a35343a34372b33320010fcacff7cfd" +
//                "22383638373434303337373435333635383938" +
//                "36313131383230373030333739313933350c31" +
//                "31372e36302e3135372e3133372c3536383300" +
//                "00193250783278783c2d0f00820096097e";
//        String crc = HexUtils.calcCrc16(s.substring(0, s.length() - 4));
//        System.out.println("crc->" + crc);
//        final byte[] data = {1, 0x12, 0x13};
//        final StringBuilder stringBuilder = new StringBuilder(data.length);
//        for (byte byteChar : data) {
//            stringBuilder.append(String.format("%02X ", byteChar));
//        }
//
//        System.out.println(new String(data) + "\n" + stringBuilder.toString());
//        byte[] f = {1};
//        System.out.println(HexUtils.bytes2HexStr1(f));
//
//        String d = "fe11000c";
//        byte[] e = HexUtils.hexStr2Bytes(d);
//        System.out.println(d);
//
//        String g = "20000101";
//        String crcrc = HexUtils.calcCrc16(g);
//        System.out.println("crc->" + crcrc);
//
//        String p=HexUtils.addCharToRight(HexUtils.bytes2HexStr1("1.4".getBytes()), 4, '0');
//        System.out.println("装好->"+p);
//
//        String i="AA013300000001efeb2b5d3e6bd641000000002017090510112520170906101125efeb2b5d3e6bd6410102030405060708010101010101010101010101010101010102030405060708091011121308DF";
//        String h=EncryptUtils.encryptAES2HexString(i.getBytes(),"11223344556677aa".getBytes(),"AES/CBC/NoPadding","mplock1234567890".getBytes());
//
//        String initVector="mplock1234567890";
//        String key="11223344556677aa";
//        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
//        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(),
//                "AES");
//
//        Cipher cipher = null;
//        try {
//            cipher = Cipher.getInstance("AES/CBC/NoPadding");
//        } catch (NoSuchAlgorithmException e1) {
//            e1.printStackTrace();
//        } catch (NoSuchPaddingException e1) {
//            e1.printStackTrace();
//        }
//        try {
//            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//        } catch (InvalidAlgorithmParameterException e1) {
//            e1.printStackTrace();
//        } catch (InvalidKeyException e1) {
//            e1.printStackTrace();
//        }
//
//        byte[] encrypted = new byte[0];
//        try {
//            encrypted = cipher.doFinal(i.getBytes());
//        } catch (BadPaddingException e1) {
//            e1.printStackTrace();
//        } catch (IllegalBlockSizeException e1) {
//            e1.printStackTrace();
//        }
//        System.out.println("aes->"+HexUtils.bytes2HexStr1(encrypted));

//
//        System.out.println("7月->"+60.03*100000000/35.32/(181.6*10000)+"公斤/头");
//        System.out.println("8月->"+59.12*100000000/34.47/(163.4*10000)+"公斤/头");
//        System.out.println("9月->"+57.54*100000000/32.12/(165*10000)+"公斤/头");

        byte[] data = HexUtils.hexStr2Bytes("1d68e651690000");
        int result = HexUtils.calcCrc16(data, 0, data.length, 0x1127);
        System.out.println(Integer.toHexString(result));
    }
}
