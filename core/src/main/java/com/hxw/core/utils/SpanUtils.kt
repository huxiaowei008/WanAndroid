package com.hxw.core.utils

import android.support.annotation.ColorInt
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan

/**
 * 字符组合变换工具
 *
 * @author hxw on 2018/5/7.
 */
object SpanUtils {
    private val mBuilder: SpannableStringBuilder by lazy { SpannableStringBuilder() }
    private var start = 0

    /**
     * 新的字符, 这需要最先调用
     */
    fun newString(text: CharSequence): SpanUtils {
        start = 0
        mBuilder.clearSpans()
        mBuilder.clear()
        mBuilder.append(text)
        return this
    }

    /**
     * 设置前景色
     */
    fun setForegroundColor(@ColorInt color: Int): SpanUtils {
        val span = ForegroundColorSpan(color)
        mBuilder.setSpan(span, start, mBuilder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }


    /**
     * 设置背景色
     */
    fun setBackgroundColor(@ColorInt color: Int): SpanUtils {
        val span = BackgroundColorSpan(color)
        mBuilder.setSpan(span, start, mBuilder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体大小
     *
     * @param size 字体大小
     * @param isDp 是否使用dp单位
     */
    fun setAbsoluteSize(size: Int, isDp: Boolean): SpanUtils {
        val span = AbsoluteSizeSpan(size, isDp)
        mBuilder.setSpan(span, start, mBuilder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 追加字符
     */
    fun append(text: CharSequence): SpanUtils {
        start = mBuilder.length
        mBuilder.append(text)
        return this
    }

    /**
     * 这是最后调用的
     */
    fun create(): SpannableStringBuilder = mBuilder

}
