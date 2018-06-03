package com.hxw.lol.utils;

import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

/**
 * 字符组合变换工具
 *
 * @author hxw on 2018/5/7.
 */
public final class SpanUtils {
    private static volatile SpanUtils INSTANCE;
    private SpannableStringBuilder mBuilder;
    private int start = 0;

    private SpanUtils() {
    }

    public static SpanUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (SpanUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SpanUtils();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 新的字符, 这需要最先调用
     */
    public SpanUtils newString(CharSequence text) {
        start = 0;
        if (mBuilder == null) {
            mBuilder = new SpannableStringBuilder();
        }
        mBuilder.clearSpans();
        mBuilder.clear();
        mBuilder.append(text);
        return this;
    }

    /**
     * 设置前景色
     */
    public SpanUtils setForegroundColor(@ColorInt int color) {
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        mBuilder.setSpan(span, start, mBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    /**
     * 设置背景色
     */
    public SpanUtils setBackgroundColor(@ColorInt int color) {
        BackgroundColorSpan span = new BackgroundColorSpan(color);
        mBuilder.setSpan(span, start, mBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param size 字体大小
     * @param isDp 是否使用dp单位
     */
    public SpanUtils setAbsoluteSize(int size, boolean isDp) {
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(size, isDp);
        mBuilder.setSpan(span, start, mBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 追加字符
     */
    public SpanUtils append(CharSequence text) {
        start = mBuilder.length();
        mBuilder.append(text);
        return this;
    }

    /**
     * 这是最后调用的
     */
    public SpannableStringBuilder create() {
        return mBuilder;
    }


}
