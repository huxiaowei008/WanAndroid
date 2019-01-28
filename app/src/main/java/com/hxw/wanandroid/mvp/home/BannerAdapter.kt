package com.hxw.wanandroid.mvp.home

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.hxw.wanandroid.R
import com.hxw.wanandroid.entity.BannerEntity
import timber.log.Timber




/**
 * @author hxw
 * @date 2019/1/26
 */
class BannerAdapter(private var mData: MutableList<BannerEntity>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`


    override fun getCount(): Int = mData.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Timber.i("创建视图->$position")
        val view = View.inflate(container.context, R.layout.item_banner, null)
        view.findViewById<TextView>(R.id.tv_title).text=mData[position].title
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        Timber.i("删除视图->$position")
        container.removeView(`object` as View)
    }
}