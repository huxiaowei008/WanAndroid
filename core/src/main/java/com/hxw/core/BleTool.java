package com.hxw.core;

import android.app.Activity;
import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import com.hxw.core.utils.HexUtils;
import timber.log.Timber;

import java.util.Locale;
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
 * @author hxw
 * @date 2018/5/9
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public final class BleTool {
    private static final int REQUEST_ENABLE_BT = 1066;
    private static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    private static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    private static volatile BleTool INSTANCE;
    public static final String CONNECT_S = "CONNECT_S";
    public static final String CONNECT_F = "CONNECT_F";
    public static final String SERVICES_S = "SERVICES_S";
    public static final String SERVICES_F = "SERVICES_F";
    public static final String WRITE_S = "WRITE_S";
    public static final String WRITE_F = "WRITE_F";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    /**
     * 用于写入的通道
     */
    private BluetoothGattCharacteristic mCharacteristic;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    final MutableLiveData<byte[]> notify = new MutableLiveData<>();
    final MutableLiveData<String> result = new MutableLiveData<>();
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Timber.i("连接成功");
                result.postValue(CONNECT_S);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Timber.i("连接失败");
                result.postValue(CONNECT_F);
            } else {
                Timber.i("还有其他连接状态?->" + newState);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.i("搜索服务成功");
                result.postValue(SERVICES_S);
            } else {
                Timber.i("搜索服务失败");
                result.postValue(SERVICES_F);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.i("write->" + HexUtils.bytes2HexStr1(characteristic.getValue()));
                result.postValue(WRITE_S);
            } else {
                Timber.i("写入失败");
                result.postValue(WRITE_F);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.i("read->" + HexUtils.bytes2HexStr1(characteristic.getValue()));
            } else {
                Timber.i("读取失败");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
                int flag = characteristic.getProperties();
                int format;
                if ((flag & 0x01) != 0) {
                    format = BluetoothGattCharacteristic.FORMAT_UINT16;
                    Timber.i("Heart rate format UINT16.");
                } else {
                    format = BluetoothGattCharacteristic.FORMAT_UINT8;
                    Timber.i("Heart rate format UINT8.");
                }
                final int heartRate = characteristic.getIntValue(format, 1);
                Timber.i(String.format(Locale.CHINA, "Received heart rate: %d", heartRate));
                return;
            }
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data) {
                    stringBuilder.append(String.format("%02X ", byteChar));
                }
                Timber.i(stringBuilder.toString());
                notify.postValue(data);
            }

        }
    };

    private BleTool() {
    }

    public static BleTool getInstance() {
        if (INSTANCE == null) {
            synchronized (BleTool.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BleTool();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化蓝牙,并开启蓝牙,调用一次就够了
     *
     * @param activity 使用蓝牙的Activity
     */
    public void init(Activity activity) {
        if (mBluetoothAdapter != null) {
            return;
        }
        Context mContext = activity.getApplicationContext();
        //使用此检查来确定设备是否支持BLE,然后您可以选择性地禁用BLE相关功能
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            throw new RuntimeException("手机不支持ble");
        }

        //初始化蓝牙适配器
        BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //确保蓝牙在设备上可用并且已启用。如果没有显示一个对话框，请求用户启用蓝牙的权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            mBluetoothAdapter.enable();
        }
    }

    /**
     * 蓝牙设备关闭
     */
    public void close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
        if (mLeScanCallback != null) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mLeScanCallback = null;
        }
        mCharacteristic = null;

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
     * 2、切勿扫描循环，应该在扫描上设置时间限制。之前可用的设备可能已移出范围，继续扫描将耗尽电池电量。
     *
     * @param callback 扫描的回调监听
     */
    public void startScan(BluetoothAdapter.LeScanCallback callback) {
        stopScan();
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
     * @param context 上下文
     * @param device  需要连接的设备
     */
    public void connectDevice(Context context, BluetoothDevice device) {
        disAndClose();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBluetoothGatt = device.connectGatt(context, false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
    }

    /**
     * 重新连接
     */
    public void reconnection() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.connect();
        }
    }

    /**
     * 断开连接并关闭客户端
     */
    public void disAndClose() {
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
                .getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
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
    public void setWriteCharacteristic(BluetoothGattCharacteristic characteristic) {
        mCharacteristic = characteristic;
    }

    /**
     * 设置通知通道
     *
     * @param characteristic 仅能接受通知的通道
     */
    public boolean setNotification(BluetoothGattCharacteristic characteristic) {
        BluetoothGattDescriptor descriptor = characteristic
                .getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
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
    public void writeCharacteristic(byte[] value) {
        if (mCharacteristic != null && mBluetoothGatt != null) {
            mCharacteristic.setValue(value);
            mBluetoothGatt.writeCharacteristic(mCharacteristic);
        }
    }

    /**
     * 判断是否已关闭Gatt
     *
     * @return true 未关闭
     */
    public boolean isCloseGatt() {
        return mBluetoothGatt != null;
    }

}
