package com.hxw.core.adapter

import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import androidx.viewpager.widget.PagerAdapter

/**
 * ViewPager的Adapter简单封装
 *
 * @author hxw
 * @date 2019/1/28
 */
class SimplePagerAdapter<T>(@param:LayoutRes private val layoutId: Int) : PagerAdapter() {

    private var mData: List<T>? = null
    private var initView: ((view: View, data: T, position: Int) -> Unit)? = null
    private var isLoop: Boolean = false


    override fun getCount(): Int {
        return if (mData.isNullOrEmpty()) {
            0
        } else {
            if (isLoop) {
                mData!!.size + 2
            } else {
                mData!!.size
            }
        }
    }

    fun getRealCount():Int{
        val count = if (isLoop) count - 2 else count
        return if (count < 0) 0 else count
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = View.inflate(container.context, layoutId, null)

        val realPosition = position % getRealCount()
        initView?.invoke(view, mData!![realPosition], realPosition)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun setData(data: List<T>): SimplePagerAdapter<T> {
        this.mData = data
        return this
    }

    fun setInitView(initView: (view: View, data: T, position: Int) -> Unit): SimplePagerAdapter<T> {
        this.initView = initView
        return this
    }

    fun setLoop(loop: Boolean): SimplePagerAdapter<T> {
        isLoop = loop
        return this
    }

    fun isLoop(): Boolean {
        return isLoop && getRealCount() != 0
    }
}
