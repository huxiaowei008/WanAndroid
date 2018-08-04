package com.hxw.wanandroid.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.hxw.wanandroid.R
import com.hxw.wanandroid.entity.ArticleData

/**
 * @author hxw on 2018/7/28
 */
class ArticleHolder(itemView:View): RecyclerView.ViewHolder(itemView) {


    fun bindTo(item: ArticleData){
        itemView.findViewById<TextView>(R.id.tv_author).setText(item.author)
        itemView.findViewById<TextView>(R.id.tv_title).setText(item.title)
        val classification = item.superChapterName + "/" + item.chapterName
        itemView.findViewById<TextView>(R.id.tv_classification).setText(classification)
        itemView.findViewById<TextView>(R.id.tv_time).setText(item.niceDate)
    }
}