package com.hxw.wanandroid.mvp.host

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hxw.wanandroid.R


/**
 * @author hxw on 2018/9/6.
 *
 */
class IpRecordAdapter(private var data: MutableList<IpRecordEntity>) : RecyclerView.Adapter<IpRecordAdapter.IpRecordHolder>() {

    var listener: OnItemClickListener? = null

    fun setOnItemClickListener(l: OnItemClickListener) {
        this.listener = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IpRecordHolder {
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(R.layout.item_ip_record, parent, false)
        return IpRecordHolder(root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: IpRecordHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tv_description).text = data[position].description
        holder.itemView.findViewById<TextView>(R.id.tv_ip_address)
                .text = "${data[position].proxy}://${data[position].ip}:${data[position].port}"
        holder.itemView.setOnClickListener {
            listener?.onClick(position)
        }
        holder.itemView.setOnLongClickListener {
            listener?.onLongClick(position)
            true
        }
    }


    class IpRecordHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface OnItemClickListener {
        fun onClick(position: Int)
        fun onLongClick(position: Int)
    }
}