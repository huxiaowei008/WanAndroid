package com.hxw.wanandroid.ble;

import android.bluetooth.BluetoothGatt;

/**
 * 蓝牙连接回调
 *
 * @author hxw
 * @date 2019/2/27
 */
public interface BleConnectCallBack {

    /**
     * 成功连接并搜索到服务
     *
     * @param gatt {@link BluetoothGatt}
     */
    void onSuccess(BluetoothGatt gatt);

    /**
     * 连接失败或断开
     *
     * @param reason 失败的原因
     */
    void onFail(String reason);
}
