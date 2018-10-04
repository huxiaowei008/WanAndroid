package com.hxw.core

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.*
import android.os.Parcelable
import com.hxw.core.utils.AppUtils
import timber.log.Timber
import java.nio.ByteBuffer

/**
 * USB连接器
 * @author hxw on 2018/8/13.
 *
 */
object USBConnector {
    val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    val ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED"
    private var mUsbManager: UsbManager? = null
    private var mInterface: UsbInterface? = null
    private var mDeviceConnection: UsbDeviceConnection? = null
    private var usbEpIn: UsbEndpoint? = null  //代表一个接口的某个节点的类:读数据节点
    private var usbEpOut: UsbEndpoint? = null  //代表一个接口的某个节点的类:写数据节点
    private var onInterface: OnInterface? = null
    private val mUsbReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ACTION_USB_PERMISSION == action) {
                synchronized(this) {
                    val device = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice?
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                            prepareCommunication(device)
                        }
                    } else {
                        Timber.i("设备 $device 权限请求被拒绝")
                    }
                }
            } else if (ACTION_USB_DETACHED == action) {
                AppUtils.showToast("USB拔出")
            }
        }
    }

    /**
     * 先初始话
     */
    fun init(usbManager: UsbManager, context: Context) {
        this.mUsbManager = usbManager
        //注册USB设备权限管理广播
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        filter.addAction(ACTION_USB_DETACHED)
        context.registerReceiver(mUsbReceiver, filter)
    }

    fun connection(context: Context, productId: Int, vendorId: Int) {
        val deviceList = mUsbManager!!.deviceList
        var device: UsbDevice? = null
        val deviceIterator = deviceList.values.iterator()
        while (deviceIterator.hasNext()) {
            val dd = deviceIterator.next()
            if (dd.productId == productId && dd.vendorId == vendorId) {
                device = dd
            }
        }
        if (device != null) {
            openDevice(context, device)
        } else {
            AppUtils.showToast("没有设备或设备不匹配")
        }
    }

    /**
     * 打开设备
     */
    fun openDevice(context: Context, device: UsbDevice) {
        // 向用户请求连接设备权限的对话框
        if (!mUsbManager!!.hasPermission(device)) {
            //权限判断
            val mPermissionIntent = PendingIntent
                    .getBroadcast(context, 0,
                            Intent(ACTION_USB_PERMISSION), 0)
            mUsbManager!!.requestPermission(device, mPermissionIntent)
        } else {
            prepareCommunication(device)
        }
    }

    private fun prepareCommunication(device: UsbDevice) {
        if (mDeviceConnection != null) {
            if (mInterface != null) {
                mDeviceConnection?.releaseInterface(mInterface)
            }
            mDeviceConnection?.close()
            mDeviceConnection = null
            mInterface = null
        }
        mDeviceConnection = mUsbManager!!.openDevice(device)
        if (mDeviceConnection == null) {
            AppUtils.showToast("mDeviceConnection ->null")
            return
        }
        if (onInterface != null) {
            onInterface!!.chooseInterface(device)
        }
    }

    fun setOnInterface(onInterface: OnInterface) {
        this.onInterface = onInterface
    }

    fun setInterface(usbInterface: UsbInterface): Boolean {
        mInterface = usbInterface
        for (i in 0 until mInterface!!.endpointCount) {
            val ep = mInterface!!.getEndpoint(i)
            if (ep.type == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.direction == UsbConstants.USB_DIR_OUT) {
                    usbEpOut = ep
                } else {
                    usbEpIn = ep
                }
            }
        }
        return mDeviceConnection!!.claimInterface(mInterface, true)
    }

    fun closeDevice(context: Context) {
        context.unregisterReceiver(mUsbReceiver)
        usbEpOut = null
        usbEpIn = null
        onInterface = null
        mUsbManager = null
        if (mInterface != null) {
            mDeviceConnection?.releaseInterface(mInterface)
        }
        mDeviceConnection?.close()
        mDeviceConnection = null
        mInterface = null

    }

    /**
     * 同步读取
     */
    fun readData(): ByteArray {
        if (usbEpIn == null || mDeviceConnection == null) {
            AppUtils.showToast("usbEpIn or mDeviceConnection == null")
            return ByteArray(0)
        }
        val inMax = usbEpIn!!.maxPacketSize
        val byteArray = ByteArray(inMax)
        val ret = mDeviceConnection!!.bulkTransfer(usbEpIn, byteArray, byteArray.size, 3000)
        return if (ret >= 0) {
            byteArray
        } else {
            ByteArray(0)
        }
    }

    /**
     * 同步写入
     */
    fun writeData(data: ByteArray): Boolean {
        return if (mDeviceConnection != null) {
            val ret = mDeviceConnection!!.bulkTransfer(usbEpOut, data, data.size, 3000)
            ret >= 0
        } else {
            AppUtils.showToast("mDeviceConnection == null")
            false
        }
    }

    /**
     * 异步读取
     */
    fun readDataAsync(): ByteArray {
        if (usbEpIn == null || mDeviceConnection == null) {
            AppUtils.showToast("usbEpIn or mDeviceConnection == null")
            return ByteArray(0)
        }
        val inMax = usbEpIn!!.maxPacketSize
        val byteArray = ByteArray(inMax)
        val byteBuffer = ByteBuffer.wrap(byteArray)
        val usbRequest = UsbRequest()
        return if (usbRequest.initialize(mDeviceConnection, usbEpIn)) {
            usbRequest.queue(byteBuffer, inMax)
            if (mDeviceConnection!!.requestWait() == usbRequest) {
                usbRequest.close()
                byteArray
            } else {
                ByteArray(0)
            }
        } else {
            AppUtils.showToast("异步读取 usbRequest 初始化失败")
            ByteArray(0)
        }
    }

    /**
     * 异步写入
     */
    fun writeDataAsync(data: ByteArray): Boolean {
        if (usbEpOut == null || mDeviceConnection == null) {
            AppUtils.showToast("usbEpOut or mDeviceConnection == null")
            return false
        }
        val byteBuffer = ByteBuffer.wrap(data)
        val usbRequest = UsbRequest()
        return if (usbRequest.initialize(mDeviceConnection, usbEpOut)) {
            usbRequest.queue(byteBuffer, data.size)
            if (mDeviceConnection!!.requestWait() == usbRequest) {
                usbRequest.close()
                true
            } else {
                false
            }
        } else {
            AppUtils.showToast("异步写入 usbRequest 初始化失败")
            false
        }
    }

    interface OnInterface {
        fun chooseInterface(device: UsbDevice)
    }
}