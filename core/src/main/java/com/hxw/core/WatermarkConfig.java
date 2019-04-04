package com.hxw.core;

import android.graphics.Bitmap;
import android.view.Gravity;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;


/**
 * 水印配置
 *
 * @author hxw
 * @date 2018/8/6
 */
public class WatermarkConfig {
    private String text = "";
    private float textSize = 80;
    @IntRange(from = 0, to = 255)
    private int alpha = 255;
    private boolean recycle = true;
    @Nullable
    private Bitmap watermark = null;
    private int gravity = Gravity.TOP;
    private int margin = 0;

    public String getText() {
        return text;
    }

    /**
     * 水印文字内容
     *
     * @param text 内容
     */
    public WatermarkConfig setText(String text) {
        this.text = text;
        return this;
    }

    public float getTextSize() {
        return textSize;
    }

    /**
     * 字体大小,单位是px
     *
     * @param textSize 文字大小
     */
    public WatermarkConfig setTextSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public int getAlpha() {
        return alpha;
    }

    /**
     * 透明度
     *
     * @param alpha 透明值
     */
    public WatermarkConfig setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        this.alpha = alpha;
        return this;
    }

    public boolean isRecycle() {
        return recycle;
    }

    /**
     * 是否回收原图
     *
     * @param recycle true 回收
     */
    public WatermarkConfig setRecycle(boolean recycle) {
        this.recycle = recycle;
        return this;
    }

    @Nullable
    public Bitmap getWatermark() {
        return watermark;
    }

    /**
     * 水印图片
     *
     * @param watermark 水印图片,不是被添加水印的图
     */
    public WatermarkConfig setWatermark(Bitmap watermark) {
        this.watermark = watermark;
        return this;
    }

    public int getGravity() {
        return gravity;
    }

    /**
     * 水印位置
     *
     * @param gravity {@link Gravity}方位信息
     */
    public WatermarkConfig setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public int getMargin() {
        return margin;
    }

    /**
     * 水印距整体图片边界的距离
     *
     * @param margin 距离
     */
    public WatermarkConfig setMargin(int margin) {
        this.margin = margin;
        return this;
    }
}
