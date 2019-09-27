package com.hxw.wanandroid.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author hxw
 * @date 2019/9/26
 */
public class RingView extends View {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF inRectF;
    private Path mPathBuffer = new Path();
    public RingView(Context context) {
        super(context);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        int minXY=Math.min(cx, cy);
        //画最外边的圆线
        mPaint.setColor(Color.parseColor("#FF855B"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4f);
        int radius = minXY-2;
        canvas.drawCircle(cx, cy, radius, mPaint);

        //画圆环
        mPaint.setStrokeWidth(30f);
        mPathBuffer.reset();
        if (inRectF==null){
            inRectF=new RectF(4+15+20,4+15+20,getWidth()-20-4-15,getHeight()-20-4-15);
        }
        mPathBuffer.addArc(inRectF,-88,268);
        canvas.drawPath(mPathBuffer,mPaint);

        mPaint.setColor(Color.parseColor("#FFB79E"));
        mPathBuffer.reset();
        mPathBuffer.addArc(inRectF,182,88);
        canvas.drawPath(mPathBuffer,mPaint);

        //写字
        mTextPaint.setColor(Color.parseColor("#FF855B"));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);
        canvas.drawText("成功率:80%",cx,cy-20,mTextPaint);

    }
}
