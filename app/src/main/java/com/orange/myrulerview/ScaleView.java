package com.orange.myrulerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.Collections;

/**
 * 刻度尺视图$
 *
 * @author 神经大条蕾弟
 * @date 2020/04/29 14:55
 */
public class ScaleView extends View {

    /**
     * 最小刻度
     */
    private float mMinIndex = 0f;

    /**
     * 最大刻度
     */
    private float mMaxIndex = 100f;

    /**
     * 当前刻度
     */
    private float mNowIndex = 15f;

    /**
     * 每格刻度的值
     */
    private float mScaleValue = 1f;

    /**
     * 指针线宽度
     */
    private float mPointerWidth = 3f;

    /**
     * 指针头宽度
     */
    private float mPointerHead = 30;

    /**
     * 底线线宽度
     */
    private float mBaseLineWidth = 5f;

    /**
     * 低刻度线宽度
     */
    private float mLowScaleWidth = 1f;

    /**
     * 中刻度线宽度
     */
    private float mMiddleScaleWidth = 2f;

    /**
     * 高刻度线宽度
     */
    private float mHighScaleWidth = 3f;

    /**
     * 刻度与刻度之间的距离
     */
    private int mLineInterval = 30;

    /**
     * 底线与 View 底的距离（下边距为文字留位置）
     */
    private int mBaseLineMarginBottom = 50;

    /**
     * 指针与 View 顶的距离（上边距为文字留位置）
     */
    private int mPointerMarginTop = 50;

    /**
     * 低刻度的上边距
     */
    private int mLowPointerMarginTop = 150;

    /**
     * 中刻度的上边距
     */
    private int mMiddlePointerMarginTop = 100;

    /**
     * 高刻度的上边距
     */
    private int mHighPointerMarginTop = 50;

    /**
     * 左边刻度与 View 左边的距离
     */
    private int mLeftMarginLeft = 50;

    /**
     * 右边刻度与 View 右边的距离
     */
    private int mRightMarginRight= 50;

    /**
     * 中刻度线频率
     */
    private int mMiddleFrequency = 5;

    /**
     * 大刻度线频率
     */
    private int mHighFrequency = 10;

    /**
     * 字体是否要绘制在上面
     */
    private boolean mFontIsTop;

    //------------------------------------------------上面是公共控制属性，下面是内部计算变量

    /**
     * 左边最多画几条
     */
    int mLeftLineCount;

    /**
     * 右边最多画几条
     */
    int mRightLineCount;

    /**
     * 左边实际需要画的线
     */
    int mRealLeftLineCount;

    /**
     * 右边实际需要画的线
     */
    int mRealRightLineCount;

    /**
     * View 宽
     */
    private int mWidth;

    /**
     * View 高
     */
    private int mHeight;

    /**
     * 指针位置
     */
    private int mPointerPosition;

    private Canvas mCanvas;
    private Paint mPaint = new Paint();

    public ScaleView(Context context) {
        super(context);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //定位
        mWidth = getWidth();
        mHeight = getHeight();
        mPointerPosition = mWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        //绘制
        initPaint();
        drawBaseLine();
        drawPointer();
        drawScale();
        drawNum();
    }

    /**
     * 设置基础风格
     */
    private void initPaint() {
        //画线模式
        mPaint.setStyle(Paint.Style.FILL);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
    }

    /**
     * 绘制刻度线
     */
    private void drawScale() {
        drawLeftLine();
        drawRightLine();
    }

    private void drawLeftLine() {
        //装坐标的数组
//        float[] mPoints = new float[mRealLeftLineCount * 4];

        int mHighLength = 0;
        int mMiddleLength = 0;
        int mLowLength = 0;


        //当前绘制的x坐标
        int mIndex = mPointerPosition;
        //找出各个刻度的数量
        for (int i = 0; i < mRealLeftLineCount; i++) {
            mIndex -= mLineInterval;
            if (((mPointerPosition - mIndex) / mHighFrequency) % mHighFrequency == 0) {
                //高刻度
                mHighLength += 1;
            } else if (((mPointerPosition - mIndex) / mMiddleFrequency) % mMiddleFrequency == 0){
                //中刻度
                mMiddleLength += 1;
            } else {
                //低刻度
                mLowLength += 1;
            }
        }

        float[] mPointsHigh = new float[mHighLength * 4];
        float[] mPointsMiddle = new float[mMiddleLength * 4];
        float[] mPointsLow = new float[mLowLength * 4];


        //重置临时坐标--从左往右
        int mStarIndex = mPointerPosition;

        //各个线的起始下标
        int mTypeIndexHigh = 0;
        int mTypeIndexMiddle = 0;
        int mTypeIndexLow = 0;

        //绘制所有的线
        for (int i = 0; i < mRealLeftLineCount; i++) {

            //每次绘制完一条线就减一次间距
            mStarIndex -= mLineInterval;

            //每条线画确定4个坐标点
            if (((mPointerPosition - mStarIndex) / mHighFrequency) % mHighFrequency == 0) {
                //高刻度
                for (int j = 0; j < 4; j++) {
                    if (j % 2 == 0) {
                        //奇数--确定x
                        mPointsHigh[mTypeIndexHigh] = mStarIndex;
                    } else {
                        //偶数--确定y
                        if (j == 1) {
                            //y轴起点--下端
                            mPointsHigh[mTypeIndexHigh] = mHeight - mBaseLineMarginBottom - mBaseLineWidth;
                        } else {
                            //y轴终点--上端
                            mPointsHigh[mTypeIndexHigh] = mPointerMarginTop + mHighPointerMarginTop;
                        }
                    }
                    mTypeIndexHigh ++;
                }
            } else if (((mPointerPosition - mStarIndex) / mMiddleFrequency) % mMiddleFrequency == 0){
                //中刻度
                for (int j = 0; j < 4; j++) {
                    if (j % 2 == 0) {
                        //奇数--确定x
                        mPointsMiddle[mTypeIndexMiddle] = mStarIndex;
                    } else {
                        //偶数--确定y
                        if (j == 1) {
                            //y轴起点--下端
                            mPointsMiddle[mTypeIndexMiddle] = mHeight - mBaseLineMarginBottom - mBaseLineWidth;
                        } else {
                            //y轴终点--上端
                            mPointsMiddle[mTypeIndexMiddle] = mPointerMarginTop + mMiddlePointerMarginTop;
                        }
                    }
                    mTypeIndexMiddle ++;
                }
            } else {
                //低刻度
                for (int j = 0; j < 4; j++) {
                    if (j % 2 == 0) {
                        //奇数--确定x
                        mPointsLow[mTypeIndexLow] = mStarIndex;
                    } else {
                        //偶数--确定y
                        if (j == 1) {
                            //y轴起点--下端
                            mPointsLow[mTypeIndexLow] = mHeight - mBaseLineMarginBottom - mBaseLineWidth;
                        } else {
                            //y轴终点--上端
                            mPointsLow[mTypeIndexLow] = mPointerMarginTop + mLowPointerMarginTop;
                        }
                    }
                    mTypeIndexLow ++;
                }
            }

        }
        //画刻度线
//        mCanvas.drawLines(mPoints, mPaint);

        //绘制低刻度线
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorDEB8B1));
        mPaint.setStrokeWidth(mLowScaleWidth);
        mCanvas.drawLines(mPointsLow, mPaint);
        //绘制中刻度线
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorDEB8B1));
        mPaint.setStrokeWidth(mMiddleScaleWidth);
        mCanvas.drawLines(mPointsMiddle, mPaint);
        //绘制高刻度线
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color3379F6));
        mPaint.setStrokeWidth(mHighScaleWidth);
        mCanvas.drawLines(mPointsHigh, mPaint);
    }

    private void drawRightLine() {

    }

    /**
     * 绘制指针
     */
    private void drawPointer() {
        //设置颜色
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        //设置线条宽度
        mPaint.setStrokeWidth(mPointerWidth);

        mCanvas.drawLine(mPointerPosition, mHeight - mPointerMarginTop - mBaseLineWidth, mPointerPosition, mPointerHead + mPointerMarginTop, mPaint);
        Path mPath = new Path();
        mPath.moveTo(mPointerPosition, mPointerMarginTop);
        mPath.lineTo(mPointerPosition - (mPointerHead / 2), mPointerHead + mPointerMarginTop);
        mPath.lineTo(mPointerPosition + (mPointerHead / 2), mPointerHead + mPointerMarginTop);
        mPath.close();
        mCanvas.drawPath(mPath, mPaint);
    }

    /**
     * 绘制数值文字
     */
    private void drawNum() {
        //设置文字居中
        mPaint.setTextAlign(Paint.Align.CENTER);
        //设置文字颜色
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    /**
     * 绘制底线
     */
    private void drawBaseLine() {
        //设置颜色
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        //设置线条宽度
        mPaint.setStrokeWidth(mBaseLineWidth);

//        mCanvas.drawLine(mLeftMarginLeft, mHeight - mBaseLineMarginBottom, mWidth - mRightMarginRight, mHeight - mBaseLineMarginBottom, mPaint);

        //算出左边和右边最多画几条
        mLeftLineCount = (mPointerPosition - mLeftMarginLeft) / mLineInterval;
        mRightLineCount = ((mWidth - mRightMarginRight) - mPointerPosition) / mLineInterval;
        //配合上数字算出实际需要绘制的线--左边
        //临时下标
        float mTypeIndex = mNowIndex;
        for (int i = 0; i <= mLeftLineCount; i++) {
            if (mTypeIndex - mScaleValue >= mMinIndex) {
                //大于最小刻度--符合
                mRealLeftLineCount = i + 1;
                mTypeIndex -= mScaleValue;
            } else {
                //超出最小刻度--不符合

            }
        }
        //配合上数字算出实际需要绘制的线--右边
        mTypeIndex = mNowIndex;
        for (int i = 0; i <= mRightLineCount; i++) {
            if (mTypeIndex + mScaleValue <= mMaxIndex) {
                //小于最大刻度--符合
                mRealRightLineCount = i + 1;
                mTypeIndex += mScaleValue;
            } else {
                //超出最大刻度--不符合

            }
        }

        //中间指针到左右的距离
        int mLeftX = mPointerPosition - (mRealLeftLineCount * mLineInterval);
        int mRightX = (mRealRightLineCount * mLineInterval) + mPointerPosition;
        mCanvas.drawLine(mLeftX, mHeight - mBaseLineMarginBottom, mRightX, mHeight - mBaseLineMarginBottom, mPaint);

    }

    /**
     * 改变监听
     */
    public interface OnScaleChangeListener{

        /**
         * 回调当前的刻度
         * @param index
         */
        void OnChange(double index);
    }

}
