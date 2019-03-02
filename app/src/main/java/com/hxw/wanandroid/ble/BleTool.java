package com.hxw.wanandroid.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.hxw.core.utils.HexUtils;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.UUID;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import timber.log.Timber;

/**
 * Ble蓝牙工具,可以作为参考或简单使用,复杂还是用开源框架比较好,如FastBLE,要么再自定义
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
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    /**
     * 用于写入的通道
     */
    private BluetoothGattCharacteristic mCharacteristic;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private BleConnectCallBack mConnectCallBack;
    private Queue<byte[]> mDataQueue = new LinkedList<>();
    public final MutableLiveData<byte[]> notify = new MutableLiveData<>();

    private Handler mMainHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BleMsg.MSG_OUT_TIME:
                    close();
                    if (mConnectCallBack != null) {
                        mConnectCallBack.onFail("连接超时");
                    }
                    break;
                case BleMsg.MSG_DISCOVER_SERVICES:
                    if (mBluetoothGatt != null) {
                        mBluetoothGatt.discoverServices();
                    }
                    break;
                case BleMsg.MSG_CONNECT_S:
                    if (mConnectCallBack != null) {
                        mConnectCallBack.onSuccess(mBluetoothGatt);
                    }
                    break;
                case BleMsg.MSG_CONNECT_F:
                    if (mConnectCallBack != null) {
                        mConnectCallBack.onFail("断开连接");
                    }
                    break;
                case BleMsg.MSG_SERVICES_FAIL:
                    if (mConnectCallBack != null) {
                        mConnectCallBack.onFail("搜索服务失败");
                    }
                    break;
                case BleMsg.MSG_WRITE_S:
                    writeQueue();
                    break;
                default:
                    break;
            }
        }
    };

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            mMainHandler.removeMessages(BleMsg.MSG_OUT_TIME);

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Timber.i("连接成功");
                Message message = mMainHandler.obtainMessage();
                message.what = BleMsg.MSG_DISCOVER_SERVICES;
                mMainHandler.sendMessageDelayed(message, 500);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Timber.i("断开连接");
                Message message = mMainHandler.obtainMessage();
                message.what = BleMsg.MSG_CONNECT_F;
                mMainHandler.sendMessage(message);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.i("搜索服务成功");
                Message message = mMainHandler.obtainMessage();
                message.what = BleMsg.MSG_CONNECT_S;
                mMainHandler.sendMessage(message);
            } else {
                Timber.i("搜索服务失败");
                Message message = mMainHandler.obtainMessage();
                message.what = BleMsg.MSG_SERVICES_FAIL;
                mMainHandler.sendMessage(message);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.i("write->" + HexUtils.bytes2HexStr2(characteristic.getValue()));
                Message message = mMainHandler.obtainMessage();
                message.what = BleMsg.MSG_WRITE_S;
                mMainHandler.sendMessage(message);
            } else {
                Timber.i("写入失败");
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
                Timber.i("changed->" + stringBuilder.toString());
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
    public void connectDevice(Context context, BluetoothDevice device, BleConnectCallBack connectCallBack) {
        close();
        this.mConnectCallBack = connectCallBack;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBluetoothGatt = device.connectGatt(context, false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        }

        Message message = mMainHandler.obtainMessage();
        message.what = BleMsg.MSG_OUT_TIME;
        mMainHandler.sendMessageDelayed(message, 10000);
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
        //参考,这里给大于20的分包,如果设置过MTU,这里就不是20了
        if (value.length > 20) {
            mDataQueue = splitByte(value, 20);
            writeQueue();
        } else {
            write(value);
        }

    }

    private void writeQueue() {
        if (mDataQueue.peek() == null) {
            return;
        }
        byte[] data = mDataQueue.poll();
        write(data);
    }

    private void write(byte[] value) {
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

    private static Queue<byte[]> splitByte(byte[] data, int count) {
        Queue<byte[]> byteQueue = new LinkedList<>();
        int pkgCount;
        if (data.length % count == 0) {
            pkgCount = data.length / count;
        } else {
            pkgCount = data.length / count + 1;
        }
        for (int i = 0; i < pkgCount; i++) {
            byte[] dataPkg;
            if (pkgCount == 1 || i == pkgCount - 1) {
                int j = data.length - (i * count);
                System.arraycopy(data, i * count, dataPkg = new byte[j], 0, j);
            } else {
                System.arraycopy(data, i * count, dataPkg = new byte[count], 0, count);
            }
            byteQueue.offer(dataPkg);
        }
        return byteQueue;
    }
}
