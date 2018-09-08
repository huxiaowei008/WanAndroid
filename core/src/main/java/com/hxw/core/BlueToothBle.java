package com.hxw.core;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.UUID;

/**
 * Ble蓝牙工具
 * 需要权限蓝牙权限
 * <uses-permission android:name="android.permission.BLUETOOTH"/>
 * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
 * 如果您想声明您的应用仅适用于支持BLE的设备
 * <uses-feature android:name="android.hardware.bluetooth_le" android:required="true"/>
 * 如果您希望将您的应用程序提供给不支持BLE的设备
 * required="false"
 * 注意:LE信标往往与位置有关,所以需要ACCESS_COARSE_LOCATION或 ACCESS_FINE_LOCATION权限来请求用户的许可
 * 没有这些权限，扫描将不会返回任何结果
 * <p>
 * 蓝牙操作和操作间需要时间,注意延迟操作
 *
 * @author hxw on 2018/5/9.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BlueToothBle {
    private static final int REQUEST_ENABLE_BT = 1066;
    private static final String UUID_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";
    private static volatile BlueToothBle INSTANCE;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    /**
     * 用于写入的通道
     */
    private BluetoothGattCharacteristic mCharacteristic;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    private BlueToothBle() {
    }

    public static BlueToothBle getInstance() {
        if (INSTANCE == null) {
            synchronized (BlueToothBle.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BlueToothBle();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化蓝牙,并开启蓝牙
     *
     * @param activity 启动时的活动
     */
    public void init(Activity activity) {
        //使用此检查来确定设备是否支持BLE,然后您可以选择性地禁用BLE相关功能
        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            throw new RuntimeException("手机不支持ble");
        }
        Context mContext = activity.getApplicationContext();
        //初始化蓝牙适配器
        bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            throw new NullPointerException("该手机不支持蓝牙");
        }
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //确保蓝牙在设备上可用并且已启用。如果没有显示一个对话框，请求用户启用蓝牙的权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            mBluetoothAdapter.enable();
        }
    }

    /**
     * 蓝牙结束
     */
    public void closeBluetooth() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
            mBluetoothAdapter = null;
        }
        bluetoothManager = null;
        mCharacteristic = null;
        mLeScanCallback = null;
    }

    /**
     * 请求用于给定连接的MTU大小
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean requestMtu(int mtu) {
        return mBluetoothGatt != null && mBluetoothGatt.requestMtu(mtu);
    }

    /**
     * 开始扫描,由于扫描耗电量大，您应遵守以下准则：
     * 1、一旦找到所需的设备，请停止扫描。
     * 2、切勿扫描循环，并在扫描上设置时间限制。之前可用的设备可能已移出范围，并继续扫描电池电量。
     *
     * @param callback 扫描的回调监听
     */
    public void startScan(BluetoothAdapter.LeScanCallback callback) {
        mLeScanCallback = callback;
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        if (mLeScanCallback != null) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mLeScanCallback = null;
        }
    }


    /**
     * 连接设备
     *
     * @param context  上下文
     * @param device   需要连接的设备
     * @param callback 监听设备和设备通信的回调
     */
    public void connectDevice(Context context, BluetoothDevice device, BluetoothGattCallback callback) {
        this.mBluetoothGatt = device.connectGatt(context, false, callback);
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    /**
     * 连接可读写服务通道(可接受通知)
     *
     * @param characteristic 用于读写的通道
     */
    public boolean connectCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.mCharacteristic = characteristic;
        BluetoothGattDescriptor descriptor = characteristic
                .getDescriptor(UUID.fromString(UUID_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR));
        if (mBluetoothGatt != null) {
            boolean result = mBluetoothGatt.setCharacteristicNotification(characteristic, true);
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            }
            return result;
        } else {
            throw new NullPointerException("mBluetoothGatt is null.");
        }
    }

    /**
     * 设置可写通道
     *
     * @param characteristic 仅能用于写的通道
     */
    public void connectWriteCharacteristic(BluetoothGattCharacteristic characteristic) {
        mCharacteristic = characteristic;
    }

    /**
     * 设置通知通道
     *
     * @param characteristic 仅能接受通知的通道
     */
    public boolean setNotification(BluetoothGattCharacteristic characteristic) {
        BluetoothGattDescriptor descriptor = characteristic
                .getDescriptor(UUID.fromString(UUID_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR));
        if (mBluetoothGatt != null) {
            boolean result = mBluetoothGatt.setCharacteristicNotification(characteristic, true);
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            }
            return result;
        } else {
            throw new NullPointerException("mBluetoothGatt or descriptor is null.");
        }
    }

    /**
     * 写操作
     * 注意写操作,操作和操作之间需要时间间隔,在setNotification后直接写无法成功是因为writeDescriptor的原因
     *
     * @param value string.getBytes()
     */
    public boolean writeCharacteristic(byte[] value) {
        if (mCharacteristic != null && mBluetoothGatt != null) {
            mCharacteristic.setValue(value);
            return mBluetoothGatt.writeCharacteristic(mCharacteristic);
        }
        return false;
    }

    /**
     * 判断是否已连接
     *
     * @return true 已连接
     */
    public boolean isConnecting() {
        return mBluetoothGatt != null;
    }

}
