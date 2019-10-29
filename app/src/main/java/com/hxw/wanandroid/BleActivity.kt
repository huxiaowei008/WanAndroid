package com.hxw.wanandroid

import android.bluetooth.BluetoothGatt
import android.os.Bundle
import androidx.lifecycle.Observer
import com.hxw.core.base.AbstractActivity
import com.hxw.core.utils.HexUtils
import com.hxw.core.utils.hexToInt
import com.hxw.core.utils.toHexStr
import com.hxw.wanandroid.ble.BleConnectCallBack
import com.hxw.wanandroid.ble.BleTool
import kotlinx.android.synthetic.main.activity_ble.*
import timber.log.Timber
import java.util.*


/**
 * @author hxw
 * @date 2019/2/27
 */
class BleActivity : AbstractActivity() {
    override val layoutId: Int
        get() = R.layout.activity_ble
    private var mCmdSize = 0
    private val mCmdList = mutableListOf<String>()
    private val random = HexUtils.randomHex(12)


    override fun init(savedInstanceState: Bundle?) {
        BleTool.getInstance().init(this)
        btn_scan.setOnClickListener {
            BleTool.getInstance().startScan { device, _, _ ->
                if (device.name == "1600000310046235") {
                    BleTool.getInstance().stopScan()
                    BleTool.getInstance().connectDevice(this, device, object : BleConnectCallBack {
                        override fun onSuccess(gatt: BluetoothGatt) {
                            val writeCharacteristic = gatt.getService(
                                UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb")
                            ).getCharacteristic(
                                UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb")
                            )

                            BleTool.getInstance()
                                .setWriteCharacteristic(writeCharacteristic)
                            val characteristic = gatt.getService(
                                UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb")
                            )
                                .getCharacteristic(
                                    UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb")
                                )
                            if (BleTool.getInstance().setNotification(characteristic)) {
                                Timber.i("BleTool准备完成")
                            }
                        }

                        override fun onFail(reason: String) {

                        }
                    })
                }
            }
        }

        btn_write.setOnClickListener {
            val str = "11000c$random"
            val cmd = str + HexUtils.calcCrc16(str)
            BleTool.getInstance()
                .writeCharacteristic(HexUtils.hexStr2Bytes("fe$cmd"))
        }

        BleTool.getInstance().notify.observe(this, Observer {
            val value = it.toHexStr()
            if (value.substring(0, 2) == "fe") {
                mCmdList.clear()
                val l = value.substring(4, 8).hexToInt() + 6
                mCmdSize = if (l % 20 == 0) {
                    l / 20
                } else {
                    l / 20 + 1
                }
            }
            if (!mCmdList.contains(value)) {
                mCmdList.add(value)
            }
        })
    }
}