package com.hxw.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;

import com.hxw.core.WatermarkConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/**
 * 图片相关工具
 *
 * @author hxw
 * @date 2018/8/6
 */
public final class ImageUtils {

    private ImageUtils() {
    }

    /**
     * 添加水印
     *
     * @param bitmap 被添加水印的图片
     * @param config 水印配置
     */
    public static Bitmap addWatermark(@NonNull Bitmap bitmap, @NonNull WatermarkConfig config) {
        Bitmap ret = bitmap.copy(bitmap.getConfig(), true);
        Canvas canvas = new Canvas(ret);
        if (config.getWatermark() != null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setAlpha(config.getAlpha());
            int w = config.getWatermark().getWidth();
            int h = config.getWatermark().getHeight();
            int x;
            int y;
            //举例这几种,其他有需要在加
            switch (config.getGravity()) {
                case Gravity.TOP:
                    x = config.getMargin();
                    y = config.getMargin();
                    break;
                case Gravity.CENTER:
                    x = (canvas.getWidth() - w) / 2;
                    y = (canvas.getHeight() - h) / 2;
                    break;
                case Gravity.BOTTOM:
                    x = config.getMargin();
                    y = canvas.getHeight() - config.getMargin() - h;
                    break;
                default:
                    x = config.getMargin();
                    y = config.getMargin();
                    break;
            }
            canvas.drawBitmap(config.getWatermark(), x, y, paint);
        }

        if (!TextUtils.isEmpty(config.getText())) {
            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setAlpha(config.getAlpha());
            canvas.save();

            //绘制白色字体内容
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(config.getTextSize());
            StaticLayout staticLayout1;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                staticLayout1 = StaticLayout.Builder
                        .obtain(config.getText(), 0, config.getText().length(), textPaint, canvas.getWidth() - config.getMargin() * 2)
                        .setLineSpacing(0, 1)
                        .setIncludePad(true)
                        .build();
            } else {
                staticLayout1 = new StaticLayout(config.getText(), textPaint, canvas.getWidth() - config.getMargin() * 2,
                        Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
            }
            //举例这几种,其他有需要在加
            //设置文字绘制的位置
            switch (config.getGravity()) {
                case Gravity.TOP:
                    canvas.translate(config.getMargin(), config.getMargin());
                    break;
                case Gravity.CENTER:
                    int x = (canvas.getWidth() - staticLayout1.getWidth()) >> 1;
                    int y = (canvas.getHeight() - staticLayout1.getHeight()) >> 1;
                    canvas.translate(x, y);
                    break;
                case Gravity.BOTTOM:
                    canvas.translate(config.getMargin(), canvas.getHeight() - config.getMargin() - staticLayout1.getHeight());
                    break;
                default:
                    canvas.translate(config.getMargin(), config.getMargin());
                    break;
            }
            staticLayout1.draw(canvas);

            //绘制黑色描边
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setColor(Color.BLACK);
            textPaint.setStrokeWidth(config.getTextSize() / 30);
            StaticLayout staticLayout2;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                staticLayout2 = StaticLayout.Builder
                        .obtain(config.getText(), 0, config.getText().length(), textPaint, canvas.getWidth() - config.getMargin() * 2)
                        .setLineSpacing(0, 1)
                        .setIncludePad(true)
                        .build();
            } else {
                staticLayout2 = new StaticLayout(config.getText(), textPaint, canvas.getWidth() - config.getMargin() * 2,
                        Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
            }
            staticLayout2.draw(canvas);
            canvas.restore();
        }

        if (config.isRecycle() && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return ret;
    }

    /**
     * 把bitmap压缩后保存为file
     *
     * @param bitmap  需要压缩的bitmap
     * @param file    保存为的文件
     * @param quality 压缩质量(0-100)
     */
    public static void compressAndSave(@NonNull Bitmap bitmap, File file, @IntRange(from = 0, to = 100) int quality) {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bitmap.recycle();
        }
    }

    /**
     * 压缩图片
     *
     * @param bitmap  要压缩的图片
     * @param quality 压缩质量(0-100)
     * @return 被压缩后的图片
     */
    public static Bitmap compress(@NonNull Bitmap bitmap, @IntRange(from = 0, to = 100) int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        byte[] bytes = outputStream.toByteArray();
        bitmap.recycle();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
