package com.hxw.wanandroid.mvp.host

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hxw.core.base.AbstractActivity
import com.hxw.core.integration.AppManager
import com.hxw.wanandroid.R
import com.hxw.wanandroid.WanApi
import com.hxw.wanandroid.mvp.login.LoginActivity
import kotlinx.android.synthetic.main.activity_host_setting.*
import org.jetbrains.anko.*
import org.koin.android.ext.android.inject


class HostSettingActivity : AbstractActivity() {
    companion object {
        val IPDATALIST = "ip_data_list"
        val IPUSE = "ip_use"
    }

    private val ipData = mutableListOf<IpRecordEntity>()
    private val sp: SharedPreferences by inject()
    private val gson: Gson by inject()
    private val alarmManager: AlarmManager by lazy { getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    private lateinit var mAdapter: IpRecordAdapter
    override fun getLayoutId(): Int {
        return R.layout.activity_host_setting
    }

    override fun init(savedInstanceState: Bundle?) {
        //获取解析记录数据
        val dataListStr = sp.getString(IPDATALIST, "")
        if (!dataListStr.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<IpRecordEntity>>() {}.type
            ipData.addAll(gson.fromJson<MutableList<IpRecordEntity>>(dataListStr, type))
        }
        //获取解析使用中Ip
        val dataStr = sp.getString(IPUSE, WanApi.BASEURL)!!
        val strArray = dataStr.split(":")
        if (strArray.size == 3) {
            if (strArray[0] == "http") {
                rb_host_http.isChecked = true
            } else {
                rb_host_https.isChecked = true
            }
            et_host_ip.setText(strArray[1].replace("/", ""))
            et_host_port.setText(strArray[2].replace("/", ""))
        }
        iv_host_back.setOnClickListener {
            finish()
        }

        btn_host_save.setOnClickListener { _ ->
            val proxy = if (rb_host_http.isChecked) {
                "http"
            } else {
                "https"
            }
            val ip = et_host_ip.text.toString()
            val port = et_host_port.text.toString()
            if (proxy.isEmpty() || ip.isEmpty() || port.isEmpty()) {
                toast("请先填写完整地址")
            } else {
                alert {
                    title = "请填写保存名称"
                    customView {
                        val et = editText()
                        yesButton {
                            val des = et.text.toString()
                            ipData.add(0, IpRecordEntity(des, proxy, ip, port))
                            mAdapter.notifyItemInserted(0)
                            rv_host_record.scrollToPosition(0)
                            mAdapter.notifyItemRangeChanged(0, ipData.size)
                            it.dismiss()
                        }
                        noButton {
                            it.dismiss()
                        }
                        isCancelable = false
                    }

                }.show()
            }
        }

        btn_host_sure.setOnClickListener { _ ->
            val proxy = if (rb_host_http.isChecked) {
                "http"
            } else {
                "https"
            }
            val ip = et_host_ip.text.toString()
            val port = et_host_port.text.toString()
            if (proxy.isEmpty() || ip.isEmpty() || port.isEmpty()) {
                toast("请先填写完整地址")
            } else {
                //保存使用ip
                val str = "$proxy://$ip:$port/"
                sp.edit {
                    putString(IPUSE, str)
                }
                saveListData()
                alert("需要重起更换IP地址,将在退出app后的1秒中自动执行", "温馨提示") {
                    yesButton {
                        it.dismiss()
                        val intent = Intent(this@HostSettingActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        val restartIntent = PendingIntent.getActivity(this@HostSettingActivity, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent) // 1秒钟后重启应用
                        AppManager.exitApp()
                    }
                }.show()

            }
        }

        initRecycler()

    }

    private fun initRecycler() {
        rv_host_record.layoutManager = LinearLayoutManager(this@HostSettingActivity)
        mAdapter = IpRecordAdapter(ipData)
        rv_host_record.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : IpRecordAdapter.OnItemClickListener {
            override fun onLongClick(position: Int) {
                alert("确定删除此数据吗?", "温馨提示") {
                    yesButton {
                        ipData.removeAt(position)
                        mAdapter.notifyItemRemoved(position)
                        mAdapter.notifyItemRangeChanged(position, ipData.size)
                        it.dismiss()
                    }
                    noButton { it.dismiss() }
                }.show()
            }

            override fun onClick(position: Int) {
                val item = ipData[position]
                if (item.proxy == "http") {
                    rb_host_http.isChecked = true
                } else {
                    rb_host_https.isChecked = true
                }
                et_host_ip.setText(item.ip)
                et_host_port.setText(item.port)

            }
        })
    }

    override fun onStop() {
        super.onStop()
        saveListData()
    }

    private fun saveListData() {
        //保存记录
        val str = gson.toJson(ipData)
        sp.edit {
            putString(IPDATALIST, str)
        }
    }
}
