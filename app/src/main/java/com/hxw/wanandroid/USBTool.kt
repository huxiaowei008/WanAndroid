package com.hxw.wanandroid


import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.hxw.core.utils.showToast
import timber.log.Timber
import java.nio.ByteBuffer

/**
 * USB连接器
 * @author hxw
 * @date 2018/8/13
 */
object USBTool : LifecycleObserver, DefaultLifecycleObserver {
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    private lateinit var mUsbManager: UsbManager
    private var mInterface: UsbInterface? = null
    private var mDeviceConnection: UsbDeviceConnection? = null
    private var usbEpIn: UsbEndpoint? = null  //代表一个接口的某个节点的类:读数据节点
    private var usbEpOut: UsbEndpoint? = null  //代表一个接口的某个节点的类:写数据节点
    //返回true的话就不会执行内部的逻辑了
    private lateinit var mAction: ((deviceConnection: UsbDeviceConnection) -> Boolean)
    private val mUsbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when {
                ACTION_USB_PERMISSION == action -> synchronized(this) {
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.let {
                            prepareCommunication(device)
                        }
                    } else {
                        Timber.i("设备 $device 权限请求被拒绝")
                    }
                }
                UsbManager.ACTION_USB_DEVICE_DETACHED == action -> Timber.i("USB拔出")
                UsbManager.ACTION_USB_DEVICE_ATTACHED == action -> Timber.i("USB插入")
            }
        }
    }

    fun onCreate(activity: AppCompatActivity) {
        activity.lifecycle.addObserver(this)
        mUsbManager = activity.getSystemService(Context.USB_SERVICE) as UsbManager
        //注册USB设备权限管理广播
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        activity.registerReceiver(mUsbReceiver, filter)
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (owner is Context) {
            owner.unregisterReceiver(mUsbReceiver)
        }
        owner.lifecycle.removeObserver(this)
        closeDevice()
    }

    /**
     * 打开设备
     */
    fun openDevice(
        context: Context,
        device: UsbDevice,
        action: ((deviceConnection: UsbDeviceConnection) -> Boolean)
    ) {
        mAction = action
        // 判断系统是否支持USB HOST
        if (!context.packageManager.hasSystemFeature("android.hardware.usb.host")) {
            val dialog = AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                .setPositiveButton("确认") { arg0, arg1 -> System.exit(0) }.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
        // 向用户请求连接设备权限的对话框
        if (mUsbManager.hasPermission(device)) {
            prepareCommunication(device)
        } else {
            //权限判断
            val mPermissionIntent = PendingIntent
                .getBroadcast(
                    context, 0,
                    Intent(ACTION_USB_PERMISSION), 0
                )
            mUsbManager.requestPermission(device, mPermissionIntent)
        }
    }

    private fun prepareCommunication(device: UsbDevice) {
        //先关闭之前的设备
        closeDevice()
        mDeviceConnection = mUsbManager.openDevice(device)
        if (mDeviceConnection == null) {
            throw NullPointerException("mDeviceConnection==null,请检查是否申请权限")
        }
        if (!mAction.invoke(mDeviceConnection!!)) {
            if (!setInterface(device.getInterface(0))) {
                Timber.i("没有找到 USB 设备接口")
            }
        }
    }

    fun setInterface(usbInterface: UsbInterface): Boolean {
        mInterface = usbInterface
        for (i in 0 until usbInterface.endpointCount) {
            val ep = usbInterface.getEndpoint(i)
            if (ep.type == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                when (ep.direction) {
                    UsbConstants.USB_DIR_OUT -> usbEpOut = ep
                    UsbConstants.USB_DIR_IN -> usbEpIn = ep
                    else -> {

                    }
                }
            }
        }
        return mDeviceConnection?.claimInterface(usbInterface, true) ?: false
    }

    fun closeDevice() {
        usbEpOut = null
        usbEpIn = null
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
            showToast("usbEpIn or mDeviceConnection == null")
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
            showToast("mDeviceConnection == null")
            false
        }
    }

    /**
     * 异步读取
     */
    fun readDataAsync(): ByteArray {
        if (usbEpIn == null || mDeviceConnection == null) {
            showToast("usbEpIn or mDeviceConnection == null")
            return ByteArray(0)
        }
        val inMax = usbEpIn!!.maxPacketSize
        val byteArray = ByteArray(inMax)
        val byteBuffer = ByteBuffer.wrap(byteArray)
        val usbRequest = UsbRequest()
        return if (usbRequest.initialize(mDeviceConnection, usbEpIn)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                usbRequest.queue(byteBuffer)
            } else {
                usbRequest.queue(byteBuffer, inMax)
            }
            if (mDeviceConnection!!.requestWait() == usbRequest) {
                usbRequest.close()
                byteArray
            } else {
                ByteArray(0)
            }
        } else {
            showToast("异步读取 usbRequest 初始化失败")
            ByteArray(0)
        }
    }

    /**
     * 异步写入
     */
    fun writeDataAsync(data: ByteArray): Boolean {
        if (usbEpOut == null || mDeviceConnection == null) {
            showToast("usbEpOut or mDeviceConnection == null")
            return false
        }
        val byteBuffer = ByteBuffer.wrap(data)
        val usbRequest = UsbRequest()
        return if (usbRequest.initialize(mDeviceConnection, usbEpOut)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                usbRequest.queue(byteBuffer)
            } else {
                usbRequest.queue(byteBuffer, data.size)
            }
            if (mDeviceConnection!!.requestWait() == usbRequest) {
                usbRequest.close()
                true
            } else {
                false
            }
        } else {
            showToast("异步写入 usbRequest 初始化失败")
            false
        }
    }

}