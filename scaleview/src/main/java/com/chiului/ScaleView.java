package com.chiului;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chiului.scrollview.R;

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
    private int mMinIndex = 0;

    /**
     * 最大刻度
     */
    private int mMaxIndex = 100;

    /**
     * 当前刻度
     */
    private int mNowIndex = 50;

    /**
     * 每格刻度的值
     */
    private int mScaleValue = 1;

    /**
     * 刻度数值的字体大小
     */
    private float mTextSize = 50f;

    /**
     * 指针线宽度
     */
    private float mPointerWidth = 3f;

    /**
     * 指针头宽度
     */
    private float mPointerHead = 30;

    /**
     * 指针是否向上
     */
    private boolean mPointerIsTop = true;

    /**
     * 指针是否向上突出
     */
    private boolean mPointerTopProtruding = false;

    /**
     * 指针是否向下突出(相对于刻度线来说，如下对齐情况中，指针线下面要长于刻度线)
     */
    private boolean mPointerBottomProtruding = false;

    /**
     * 是否显示针头
     */
    private boolean mIsShowPointerHead = true;

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
     * 所有元素（除了文字）与 View 底的距离（下边距为文字留位置）
     */
    private int mBaseLineMarginBottom = 50;

    /**
     * 所有元素（除了文字）与 View 顶的距离（上边距为文字留位置）
     */
    private int mPointerMarginTop = 50;

    /**
     * 所有元素（除了文字）与  View 左边的距离
     */
    private int mLeftMarginLeft = 0;

    /**
     * 所有元素（除了文字）与  View 右边的距离
     */
    private int mRightMarginRight = 0;

    /**
     * 文字下边距（避免显示不全）
     */
    private int mFontMarginBottom = 0;

    /**
     * 文字上边距（避免显示不全）
     */
    private int mFontMarginTop = 40;

    /**
     * 低刻度的边距（控制长度）
     */
    private int mLowPointerMargin = 150;

    /**
     * 中刻度的边距（控制长度）
     */
    private int mMiddlePointerMargin = 100;

    /**
     * 高刻度的边距（控制长度）
     */
    private int mHighPointerMargin = 50;

    /**
     * 中刻度线频率
     */
    private int mMiddleFrequency = 5;

    /**
     * 高刻度线频率
     */
    private int mHighFrequency = 10;

    /**
     * 底线颜色
     */
    private int mBaseLineColor = ContextCompat.getColor(getContext(), R.color.colorScale);

    /**
     * 高刻度线颜色
     */
    private int mHighScaleColor = ContextCompat.getColor(getContext(), R.color.colorScale);

    /**
     * 中刻度线颜色
     */
    private int mMiddleScaleColor = ContextCompat.getColor(getContext(), R.color.colorScale);

    /**
     * 低刻度线颜色
     */
    private int mLowScaleColor = ContextCompat.getColor(getContext(), R.color.colorScale);

    /**
     * 刻度数值颜色
     */
    private int mNumColor = ContextCompat.getColor(getContext(), R.color.colorText);

    /**
     * 指针颜色
     */
    private int mPointerColor = ContextCompat.getColor(getContext(), R.color.colorPointer);

    /**
     * 字体是否要绘制在上面
     */
    private boolean mFontIsTop = true;

    /**
     * 显示的刻度数字与刻度比例（比如要显示小数的情况）：刻度 / 比例 = 显示刻度
     */
    private double mScaleRatio = 10;

    /**
     * 滑动距离比例（用于调整滑动速度）：刻度间距离 * 滑动速度比例 = 每滑动多少距离改变状态
     */
    private double mSlidingRatio = 0.5;

    /**
     * 滑动监听
     */
    private OnScaleChangeListener onScaleChangeListener;

    /**
     * 是否显示底线
     */
    private boolean isShowBaseLine = true;

    /**
     * 刻度线位置：上，中间，下
     */
    private int mScalePosition = 2;

    /**
     * 惯性滑动速率时间单位
     * 多少毫秒时间单位内运动了多少个像素
     */
    private int mUnits = 500;

    /**
     * 最大滑动数率
     */
    private int mMaxVelocity = 15000;

    /**
     * paint 的线帽
     */
    private Paint.Cap mStrokeCap = Paint.Cap.SQUARE;

    //------------------------------------------------上面是公共控制属性，下面是内部计算变量

    /**
     * 左边最多画几条
     */
    private int mLeftLineCount;

    /**
     * 右边最多画几条
     */
    private int mRightLineCount;

    /**
     * 左边实际需要画的线
     */
    private int mRealLeftLineCount;

    /**
     * 右边实际需要画的线
     */
    private int mRealRightLineCount;

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

    private float[] mPointsHighLeft;
    private float[] mPointsMiddleLeft;
    private float[] mPointsLowLeft;

    private float[] mPointsHighRight;
    private float[] mPointsMiddleRight;
    private float[] mPointsLowRight;

    /**
     * 记录手指点下X
     */
    float mPosX = 0;
    /**
     * 记录手指点下Y
     */
    float mPosY = 0;
    /**
     * 记录当前手指滑动x
     */
    float mCurPosX = 0;
    /**
     * 记录当前手指滑动y
     */
    float mCurPosY = 0;
    /**
     * 录上一次滑动的点
     */
    float mCurPosX_ing = 0;
    /**
     * 记录上一次改变刻度的点
     */
    float mChangeIndex = 0;
    /**
     * 记录当前是向左滑还是向右滑
     * 0:左滑
     * 1：右滑
     */
    int mDirection = -1;

    float x_down = 0;
    float y_down = 0;

    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();

    private VelocityTracker mVelocityTracker;
    private int xVelocity;

    private static final int WHAT_PLUS = 1;
    private static final int WHAT_MINUS = 2;
    private static final int WHAT_STOP = 3;

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case WHAT_PLUS:
                    setPlusScale();
                    break;
                case WHAT_MINUS:
                    setMinusScale();
                    break;
                case WHAT_STOP:
                    mHandler.removeMessages(WHAT_PLUS);
                    mHandler.removeMessages(WHAT_MINUS);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public ScaleView(Context context) {
        super(context);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleView);
        mVelocityTracker = VelocityTracker.obtain();
        mMinIndex = typedArray.getInteger(R.styleable.ScaleView_min_index, mMinIndex);
        mMaxIndex = typedArray.getInteger(R.styleable.ScaleView_max_index, mMaxIndex);
        mNowIndex = typedArray.getInteger(R.styleable.ScaleView_now_index, mNowIndex);
        mScaleValue = typedArray.getInteger(R.styleable.ScaleView_scale_value, mScaleValue);
        mTextSize = typedArray.getFloat(R.styleable.ScaleView_text_size, mTextSize);
        mPointerWidth = typedArray.getFloat(R.styleable.ScaleView_pointer_width, mPointerWidth);
        mPointerHead = typedArray.getFloat(R.styleable.ScaleView_pointer_head, mPointerHead);
        mPointerIsTop = typedArray.getBoolean(R.styleable.ScaleView_pointer_top, mPointerIsTop);
        mPointerTopProtruding = typedArray.getBoolean(R.styleable.ScaleView_pointer_top_protruding, mPointerTopProtruding);
        mPointerBottomProtruding = typedArray.getBoolean(R.styleable.ScaleView_pointer_bottom_protruding, mPointerBottomProtruding);
        mIsShowPointerHead = typedArray.getBoolean(R.styleable.ScaleView_show_pointer_head, mIsShowPointerHead);
        mBaseLineWidth = typedArray.getFloat(R.styleable.ScaleView_baseLine_width, mBaseLineWidth);
        mLowScaleWidth = typedArray.getFloat(R.styleable.ScaleView_low_scale_width, mLowScaleWidth);
        mMiddleScaleWidth = typedArray.getFloat(R.styleable.ScaleView_middle_scale_width, mMiddleScaleWidth);
        mHighScaleWidth = typedArray.getFloat(R.styleable.ScaleView_high_scale_width, mHighScaleWidth);
        mLineInterval = typedArray.getInteger(R.styleable.ScaleView_line_interval, mLineInterval);
        mBaseLineMarginBottom = typedArray.getInteger(R.styleable.ScaleView_baseLine_margin_bottom, mBaseLineMarginBottom);
        mPointerMarginTop = typedArray.getInteger(R.styleable.ScaleView_pointer_margin_top, mPointerMarginTop);
        mLeftMarginLeft = typedArray.getInteger(R.styleable.ScaleView_left_margin_left, mLeftMarginLeft);
        mRightMarginRight = typedArray.getInteger(R.styleable.ScaleView_right_margin_right, mRightMarginRight);
        mFontMarginBottom = typedArray.getInteger(R.styleable.ScaleView_font_margin_bottom, mFontMarginBottom);
        mFontMarginTop = typedArray.getInteger(R.styleable.ScaleView_font_margin_top, mFontMarginTop);
        mLowPointerMargin = typedArray.getInteger(R.styleable.ScaleView_low_pointer_margin, mLowPointerMargin);
        mMiddlePointerMargin = typedArray.getInteger(R.styleable.ScaleView_middle_pointer_margin, mMiddlePointerMargin);
        mHighPointerMargin = typedArray.getInteger(R.styleable.ScaleView_high_pointer_margin, mHighPointerMargin);
        mMiddleFrequency = typedArray.getInteger(R.styleable.ScaleView_middle_frequency, mMiddleFrequency);
        mHighFrequency = typedArray.getInteger(R.styleable.ScaleView_high_frequency, mHighFrequency);
        mBaseLineColor = typedArray.getColor(R.styleable.ScaleView_baseLine_color, mBaseLineColor);
        mHighScaleColor = typedArray.getColor(R.styleable.ScaleView_high_scale_color, mHighScaleColor);
        mMiddleScaleColor = typedArray.getColor(R.styleable.ScaleView_middle_scale_color, mMiddleScaleColor);
        mLowScaleColor = typedArray.getColor(R.styleable.ScaleView_low_scale_color, mLowScaleColor);
        mNumColor = typedArray.getColor(R.styleable.ScaleView_num_color, mNumColor);
        mPointerColor = typedArray.getColor(R.styleable.ScaleView_pointer_color, mPointerColor);
        mFontIsTop = typedArray.getBoolean(R.styleable.ScaleView_font_top, mFontIsTop);
        mScaleRatio = typedArray.getFloat(R.styleable.ScaleView_scale_ratio, (float) mScaleRatio);
        mSlidingRatio = typedArray.getFloat(R.styleable.ScaleView_sliding_ratio, (float) mSlidingRatio);
        isShowBaseLine = typedArray.getBoolean(R.styleable.ScaleView_show_baseLine, isShowBaseLine);
        mScalePosition = typedArray.getInt(R.styleable.ScaleView_scale_position, mScalePosition);
        mUnits = typedArray.getInteger(R.styleable.ScaleView_units, mUnits);
        mMaxVelocity = typedArray.getInteger(R.styleable.ScaleView_max_velocity, mMaxVelocity);
        int strokeCap = typedArray.getInt(R.styleable.ScaleView_strokeCap, 2);
        switch (strokeCap) {
            case 0:
                mStrokeCap = Paint.Cap.BUTT;
                break;
            case 1:
                mStrokeCap = Paint.Cap.ROUND;
                break;
            case 2:
            default:
                mStrokeCap = Paint.Cap.SQUARE;
        }
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
        //初始化画笔
        initPaint();
        //绘制底线
        drawBaseLine();
        //绘制指针
        drawPointer();
        //绘制刻度线
        drawScale();
        //绘制数值
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
        // 线帽，即画的线条两端是否带有圆角，SQUARE，矩形
        mPaint.setStrokeCap(mStrokeCap);
    }

    /**
     * 绘制刻度线
     */
    private void drawScale() {
        drawLeftLine();
        drawRightLine();
    }

    /**
     * 判断刻度线的边距
     *
     * @param isTopMargin true:上边距  false:下边距
     * @param margin      边距值
     * @return
     */
    private int getHighPointerMargin(boolean isTopMargin, int margin) {
        switch (mScalePosition) {
            case 0:
                if (isTopMargin) {
                    //上边距
                    return 0;
                } else {
                    //下边距
                    return margin;
                }
            case 1:
                if (isTopMargin) {
                    //上边距
                    return margin;
                } else {
                    //下边距
                    return margin;
                }
            case 2:
            default:
                if (isTopMargin) {
                    //上边距
                    return margin;
                } else {
                    //下边距
                    return 0;
                }
        }
    }

    private void drawLeftLine() {

        //计算装下标的数组大小
        int mHighLength = 0;
        int mMiddleLength = 0;
        int mLowLength = 0;

        //当前绘制的x坐标
        int mIndex = mPointerPosition - mRealLeftLineCount * mLineInterval;
        //找出各个刻度的数量
        for (int i = 0; i < mRealLeftLineCount; i++) {
            int mNowIndexValue = getNowIndexValue(mIndex);
            if (mNowIndexValue % mHighFrequency == 0) {
                //高刻度
                mHighLength = mHighLength + 1;
            } else if (mNowIndexValue % mMiddleFrequency == 0) {
                //中刻度
                mMiddleLength = mMiddleLength + 1;
            } else if (mNowIndexValue % mScaleValue == 0 && mNowIndexValue > mMinIndex) {
                //低刻度
                mLowLength = mLowLength + 1;
            }
            //每次绘制完一条线就加一次间距
            mIndex += mLineInterval;
        }

        //创建装下标的数组(判断是为了减少内存抖动)
        if (mPointsHighLeft == null || mPointsHighLeft.length != mHighLength * 4) {
            mPointsHighLeft = new float[mHighLength * 4];
        }
        if (mPointsMiddleLeft == null || mPointsMiddleLeft.length != mMiddleLength * 4) {
            mPointsMiddleLeft = new float[mMiddleLength * 4];
        }
        if (mPointsLowLeft == null || mPointsLowLeft.length != mLowLength * 4) {
            mPointsLowLeft = new float[mLowLength * 4];
        }

        //重置临时坐标--从左往右
        //求出最左边的点
        int mStarIndex = mPointerPosition - mRealLeftLineCount * mLineInterval;

        //各个线的起始下标
        int mTypeIndexHigh = 0;
        int mTypeIndexMiddle = 0;
        int mTypeIndexLow = 0;

        //绘制所有的线
        for (int i = 0; i < mRealLeftLineCount; i++) {

            //每条线画确定4个坐标点
            //下标差距
            //根据下标算差距值
            int mNowIndexValue = getNowIndexValue(mStarIndex);

            if (mNowIndexValue % mHighFrequency == 0) {
                //高刻度
                for (int j = 0; j < 4; j++) {
                    if (j % 2 == 0) {
                        //奇数--确定x
                        mPointsHighLeft[mTypeIndexHigh] = mStarIndex;
                    } else {
                        //偶数--确定y
                        if (j == 1) {
                            //y轴起点--下端
                            mPointsHighLeft[mTypeIndexHigh] = mHeight - mBaseLineMarginBottom - mBaseLineWidth - getHighPointerMargin(false, mHighPointerMargin);
                        } else {
                            //y轴终点--上端
                            mPointsHighLeft[mTypeIndexHigh] = mPointerMarginTop + getHighPointerMargin(true, mHighPointerMargin);
                        }
                    }
                    mTypeIndexHigh++;
                }
            } else if (mNowIndexValue % mMiddleFrequency == 0) {
                //中刻度
                for (int j = 0; j < 4; j++) {
                    if (j % 2 == 0) {
                        //奇数--确定x
                        mPointsMiddleLeft[mTypeIndexMiddle] = mStarIndex;
                    } else {
                        //偶数--确定y
                        if (j == 1) {
                            //y轴起点--下端
                            mPointsMiddleLeft[mTypeIndexMiddle] = mHeight - mBaseLineMarginBottom - mBaseLineWidth - getHighPointerMargin(false, mMiddlePointerMargin);
                        } else {
                            //y轴终点--上端
                            mPointsMiddleLeft[mTypeIndexMiddle] = mPointerMarginTop + getHighPointerMargin(true, mMiddlePointerMargin);
                        }
                    }
                    mTypeIndexMiddle++;
                }
            } else if (mNowIndexValue % mScaleValue == 0 && mNowIndexValue > mMinIndex) {
                //低刻度
                for (int j = 0; j < 4; j++) {
                    if (j % 2 == 0) {
                        //奇数--确定x
                        mPointsLowLeft[mTypeIndexLow] = mStarIndex;
                    } else {
                        //偶数--确定y
                        if (j == 1) {
                            //y轴起点--下端
                            mPointsLowLeft[mTypeIndexLow] = mHeight - mBaseLineMarginBottom - mBaseLineWidth - getHighPointerMargin(false, mLowPointerMargin);
                        } else {
                            //y轴终点--上端
                            mPointsLowLeft[mTypeIndexLow] = mPointerMarginTop + getHighPointerMargin(true, mLowPointerMargin);
                        }
                    }
                    mTypeIndexLow++;
                }
            }

            //每次绘制完一条线就加一次间距
            mStarIndex += mLineInterval;

        }

        //绘制低刻度线
        mPaint.setColor(mLowScaleColor);
        mPaint.setStrokeWidth(mLowScaleWidth);
        mCanvas.drawLines(mPointsLowLeft, mPaint);
        //绘制中刻度线
        mPaint.setColor(mMiddleScaleColor);
        mPaint.setStrokeWidth(mMiddleScaleWidth);
        mCanvas.drawLines(mPointsMiddleLeft, mPaint);
        //绘制高刻度线
        mPaint.setColor(mHighScaleColor);
        mPaint.setStrokeWidth(mHighScaleWidth);
        mCanvas.drawLines(mPointsHighLeft, mPaint);
    }

    private void drawRightLine() {

        //计算装下标的数组大小
        int mHighLength = 0;
        int mMiddleLength = 0;
        int mLowLength = 0;

        //当前绘制的x坐标
        int mIndex = mPointerPosition + mRealRightLineCount * mLineInterval;
        //找出各个刻度的数量
        for (int i = 0; i < mRealRightLineCount; i++) {
            int mNowIndexValue = getNowIndexValue(mIndex);
            if (mNowIndexValue % mHighFrequency == 0) {
                //高刻度
                mHighLength = mHighLength + 1;
            } else if (mNowIndexValue % mMiddleFrequency == 0) {
                //中刻度
                mMiddleLength = mMiddleLength + 1;
            } else if (mNowIndexValue % mScaleValue == 0 && mNowIndexValue < mMaxIndex) {
                //低刻度
                mLowLength = mLowLength + 1;
            }
            //每次绘制完一条线就加一次间距
            mIndex -= mLineInterval;
        }

        //创建装下标的数组(判断是为了减少内存抖动)
        if (mPointsHighRight == null || mPointsHighRight.length != mHighLength * 4) {
            mPointsHighRight = new float[mHighLength * 4];
        }
        if (mPointsMiddleRight == null || mPointsMiddleRight.length != mMiddleLength * 4) {
            mPointsMiddleRight = new float[mMiddleLength * 4];
        }
        if (mPointsLowRight == null || mPointsLowRight.length != mLowLength * 4) {
            mPointsLowRight = new float[mLowLength * 4];
        }

        //重置临时坐标--从右往左
        //求出最右边的点
        int mStarIndex = mPointerPosition + mRealRightLineCount * mLineInterval;

        //各个线的起始下标
        int mTypeIndexHigh = 0;
        int mTypeIndexMiddle = 0;
        int mTypeIndexLow = 0;

        //绘制所有的线
        for (int i = 0; i < mRealRightLineCount; i++) {

            //每条线画确定4个坐标点
            //下标差距
            //根据下标算差距值
            int mNowIndexValue = getNowIndexValue(mStarIndex);

            if (mNowIndexValue % mHighFrequency == 0) {
                //高刻度
                for (int j = 0; j < 4; j++) {
                    if (j % 2 == 0) {
                        //奇数--确定x
                        mPointsHighRight[mTypeIndexHigh] = mStarIndex;
                    } else {
                        //偶数--确定y
                        if (j == 1) {
                            //y轴起点--下端
                            mPointsHighRight[mTypeIndexHigh] = mHeight - mBaseLineMarginBottom - mBaseLineWidth - getHighPointerMargin(false, mHighPointerMargin);
                        } else {
                            //y轴终点--上端
                            mPointsHighRight[mTypeIndexHigh] = mPointerMarginTop + getHighPointerMargin(true, mHighPointerMargin);
                        }
                    }
                    mTypeIndexHigh++;
                }
            } else if (mNowIndexValue % mMiddleFrequency == 0) {
                //中刻度
                for (int j = 0; j < 4; j++) {
                    if (j % 2 == 0) {
                        //奇数--确定x
                        mPointsMiddleRight[mTypeIndexMiddle] = mStarIndex;
                    } else {
                        //偶数--确定y
                        if (j == 1) {
                            //y轴起点--下端
                            mPointsMiddleRight[mTypeIndexMiddle] = mHeight - mBaseLineMarginBottom - mBaseLineWidth - getHighPointerMargin(false, mMiddlePointerMargin);
                        } else {
                            //y轴终点--上端
                            mPointsMiddleRight[mTypeIndexMiddle] = mPointerMarginTop + getHighPointerMargin(true, mMiddlePointerMargin);
                        }
                    }
                    mTypeIndexMiddle++;
                }
            } else if (mNowIndexValue % mScaleValue == 0 && mNowIndexValue < mMaxIndex) {
                //低刻度
                for (int j = 0; j < 4; j++) {
                    if (j % 2 == 0) {
                        //奇数--确定x
                        mPointsLowRight[mTypeIndexLow] = mStarIndex;
                    } else {
                        //偶数--确定y
                        if (j == 1) {
                            //y轴起点--下端
                            mPointsLowRight[mTypeIndexLow] = mHeight - mBaseLineMarginBottom - mBaseLineWidth - getHighPointerMargin(false, mLowPointerMargin);
                        } else {
                            //y轴终点--上端
                            mPointsLowRight[mTypeIndexLow] = mPointerMarginTop + getHighPointerMargin(true, mLowPointerMargin);
                        }
                    }
                    mTypeIndexLow++;
                }
            }

            //每次绘制完一条线就减一次间距
            mStarIndex -= mLineInterval;

        }

        //绘制低刻度线
        mPaint.setColor(mLowScaleColor);
        mPaint.setStrokeWidth(mLowScaleWidth);
        mCanvas.drawLines(mPointsLowRight, mPaint);
        //绘制中刻度线
        mPaint.setColor(mMiddleScaleColor);
        mPaint.setStrokeWidth(mMiddleScaleWidth);
        mCanvas.drawLines(mPointsMiddleRight, mPaint);
        //绘制高刻度线
        mPaint.setColor(mHighScaleColor);
        mPaint.setStrokeWidth(mHighScaleWidth);
        mCanvas.drawLines(mPointsHighRight, mPaint);
    }

    /**
     * 绘制指针
     */
    private void drawPointer() {
        //设置颜色
        mPaint.setColor(mPointerColor);
        //设置线条宽度
        mPaint.setStrokeWidth(mPointerWidth);

        float stopY;
        if (mIsShowPointerHead) {
            //显示指针
            stopY = mPointerHead + mPointerMarginTop;
        } else {
            //不显示指针
            stopY = mPointerMarginTop;
        }
        if (mPointerTopProtruding) {
            //指针向上突出
            stopY -= mPointerMarginTop;
        }

        float starY = mHeight - mBaseLineWidth - mBaseLineMarginBottom;
        if (mPointerBottomProtruding){
            //指针向下突出
            starY += mBaseLineMarginBottom;
        }

        if (mPointerIsTop) {
            //指针指向上
            mCanvas.drawLine(mPointerPosition, starY, mPointerPosition, stopY, mPaint);
            if (mIsShowPointerHead) {
                mPath.moveTo(mPointerPosition, mPointerMarginTop);
                mPath.lineTo(mPointerPosition - (mPointerHead / 2), mPointerHead + mPointerMarginTop);
                mPath.lineTo(mPointerPosition + (mPointerHead / 2), mPointerHead + mPointerMarginTop);
                mPath.close();
                mCanvas.drawPath(mPath, mPaint);
            }
        } else {
            //指针指向下
            mCanvas.drawLine(mPointerPosition, starY - mPointerHead, mPointerPosition, stopY - mPointerHead, mPaint);
            if (mIsShowPointerHead) {
                mPath.moveTo(mPointerPosition, starY);
                mPath.lineTo(mPointerPosition - (mPointerHead / 2), starY - mPointerHead);
                mPath.lineTo(mPointerPosition + (mPointerHead / 2), starY - mPointerHead);
                mPath.close();
                mCanvas.drawPath(mPath, mPaint);
            }
        }
    }

    /**
     * 绘制数值文字
     */
    private void drawNum() {
        //设置文字居中
        mPaint.setTextAlign(Paint.Align.CENTER);
        //设置文字大小
        mPaint.setTextSize(mTextSize);
        //设置文字颜色
        mPaint.setColor(mNumColor);

        float y = mHeight - mFontMarginBottom;
        if (mFontIsTop) {
            y = mFontMarginTop;
        }

        onDrawLeftNum(y);
        onDrawRightNum(y);
        onDrawCenter(y);
        if (onScaleChangeListener != null) {
            onScaleChangeListener.OnChange(mNowIndex / mScaleRatio);
        }
    }

    /**
     * 绘制中间
     */
    private void onDrawCenter(float y) {
        //当中间下标为大刻度时才绘制，否则不绘制
        if (mNowIndex % mHighFrequency == 0) {
            mCanvas.drawText(getShowNum(mNowIndex), mPointerPosition, y, mPaint);
        }
    }

    private void onDrawRightNum(float y) {
        //从刻度线数组或取到文字的下标
        for (int i = 0; i < mPointsHighRight.length; i++) {
            if (i % 4 == 0) {
                int mNowIndexValue = getNowIndexValue((int) mPointsHighRight[i]);
                float x = mPointsHighRight[i];
                mCanvas.drawText(getShowNum(mNowIndexValue), x, y, mPaint);
            }
        }
    }

    private void onDrawLeftNum(float y) {
        //从刻度线数组或取到文字的下标
        for (int i = 0; i < mPointsHighLeft.length; i++) {
            if (i % 4 == 0) {
                int mNowIndexValue = getNowIndexValue((int) mPointsHighLeft[i]);
                float x = mPointsHighLeft[i];
                mCanvas.drawText(getShowNum(mNowIndexValue), x, y, mPaint);
            }
        }
    }

    /**
     * 最终绘制的数值
     * @param nowIndex
     * @return
     */
    private String getShowNum(int nowIndex){
        String numStr = "";
        if (mScaleRatio == 1) {
            numStr = String.valueOf(nowIndex);
        } else {
            numStr = String.valueOf(nowIndex / mScaleRatio);
        }
        return numStr;
    }

    /**
     * 根据当前的x轴获取当前的值
     *
     * @param v
     * @return
     */
    private int getNowIndexValue(int v) {
        if (v < mPointerPosition) {
            return mNowIndex - ((mPointerPosition - v) / mLineInterval * mScaleValue);
        } else {
            return mNowIndex + ((v - mPointerPosition) / mLineInterval * mScaleValue);
        }
    }

    /**
     * 绘制底线
     */
    private void drawBaseLine() {
        //重置真实需要画的刻度数量
        mRealLeftLineCount = 0;
        mRealRightLineCount = 0;
        //设置颜色
        mPaint.setColor(mBaseLineColor);
        //设置线条宽度
        mPaint.setStrokeWidth(mBaseLineWidth);

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
        if (isShowBaseLine) {
            mCanvas.drawLine(mLeftX, mHeight - mBaseLineMarginBottom, mRightX, mHeight - mBaseLineMarginBottom, mPaint);
        } else {
            mBaseLineWidth = 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(mUnits, mMaxVelocity);//（xx毫秒）时间单位内运动了多少个像素

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                mPosY = event.getY();
                mHandler.sendEmptyMessage(WHAT_STOP);
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosX = event.getX();
                mCurPosY = event.getY();

                Log.e("TAG", "mCurPosX =" + mCurPosX);
                Log.e("TAG", "mCurPosY =" + mCurPosY);

                //限制触摸范围
                if ((mCurPosX < 0 || mCurPosX > mWidth || mCurPosY < 0 || mCurPosY > mHeight)
                        //同时限制手指按压刷新
                        || (mCurPosX == mCurPosX_ing)) {
                    return true;
                }

                //判断向左滑还是向右滑
                if (mCurPosX - mPosX > 0 && (Math.abs(mCurPosX - mPosX) > mLineInterval)) {
                    Log.e("TAG", "向右" + mCurPosX);
                    mDirection = 1;
                } else if (mCurPosX - mPosX < 0 && (Math.abs(mCurPosX - mPosX) > mLineInterval)) {
                    Log.e("TAG", "向左" + mCurPosX);
                    mDirection = 0;
                }

                //根据左滑还是右滑判断变向
                if (mDirection == 0) {
                    //当前为向左滑状态--mCurPosX递减
                    if (mCurPosX > mCurPosX_ing) {
                        Log.e("TAG", "------------------------换向右边" + mCurPosX);
                        mPosX = mCurPosX_ing;
                        mDirection = 1;
                        setChangeNowIndex(1);
                    } else {
                        setChangeNowIndex(0);
                    }
                } else if (mDirection == 1) {
                    //当前为向右滑状态--mCurPosX递增
                    if (mCurPosX < mCurPosX_ing) {
                        Log.e("TAG", "------------------------换向左边" + mCurPosX);
                        mPosX = mCurPosX_ing;
                        mDirection = 0;
                        setChangeNowIndex(0);
                    } else {
                        setChangeNowIndex(1);
                    }
                }
                refresh();

                break;
            case MotionEvent.ACTION_UP:
                mDirection = -1;
                //在MotionEvent.ACTION_UP回调中获取抬手之后的移动速率
                xVelocity = (int) mVelocityTracker.getXVelocity();
                Log.e("TAG", "onTouchEvent: ACTION_UP" + "移动速率=" + xVelocity);
                inertiaScroll(xVelocity);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVelocityTracker.recycle();
        mHandler.removeMessages(WHAT_PLUS);
        mHandler.removeMessages(WHAT_MINUS);
    }

    /**
     * 惯性滑动
     * @param xVelocity 滑动速率
     */
    private void inertiaScroll(int xVelocity) {
        int absX = Math.abs(xVelocity);
        int proportion = 3;
        if (absX < mLineInterval || ((absX / proportion) / mLineInterval) <= 0) {
            return;
        }

        //算出惯性的像素能画几条线
        int count = (absX / proportion) / mLineInterval;

        //算出总时长
        int duration = mUnits;
        //算出画每条线的平均时间
        int interval = duration / count;

        int what;
        if (xVelocity > 0) {
            //往右边滑--减少
            what = WHAT_MINUS;
        } else {
            //往左边滑--增加
            what = WHAT_PLUS;
        }

        int postDuration = 0;
        for (int i = 0; i < count; i++) {
            mHandler.sendEmptyMessageDelayed(what, postDuration);
            //匀速时间
            postDuration = postDuration + interval;
            //模拟惯性
            if (i < count * 0.5) {
                //第一段。不衰减
                postDuration = postDuration + 0;
            } else if (i < count * 0.6) {
                //第二段衰减
                postDuration = postDuration + (int) (i * 0.1);
            } else if (i < count * 0.7) {
                //第三段衰减
                postDuration = postDuration + (int) (i * 0.2);
            } else if (i < count * 0.8) {
                //第四段衰减
                postDuration = postDuration + (int) (i * 0.3);
            } else if (i < count * 0.9) {
                //第五段衰减
                postDuration = postDuration + (int) (i * 0.4);
            } else {
                //最后一段衰减
                postDuration = postDuration + (int) (i * 0.5);
            }
        }

    }


    /**
     * 刷新视图
     */
    private void refresh() {
        //判断是否刷新
        if (mNowIndex < mMinIndex) {
            mNowIndex = mMinIndex;
            mHandler.sendEmptyMessage(WHAT_STOP);
        } else if (mNowIndex > mMaxIndex) {
            mNowIndex = mMaxIndex;
            mHandler.sendEmptyMessage(WHAT_STOP);
        } else {
            invalidate();
            //记录上一次
            mCurPosX_ing = mCurPosX;
        }
    }

    /**
     * 事件分发：处理滑动冲突处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x_down = ev.getX();
                y_down = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float x_move = ev.getX();
                float y_move = ev.getY();
                //处理滑动冲突条件 当Y轴滑动超过一个刻度 && Y轴滑动距离大于X轴 就交给父类去处理
                if (Math.abs(y_down - y_move) > mLineInterval && Math.abs(y_down - y_move) > Math.abs(x_down - x_move)) {
                    //允许外层控件拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //需要内部控件处理该事件，不允许上层viewGroup拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 改变现在的下标
     *
     * @param type 0:加 1：减
     */
    private void setChangeNowIndex(int type) {
        float StarDistance = Math.abs(mPosX - mCurPosX);
        float LastTimeDistance = Math.abs(mChangeIndex - mCurPosX);
        if (StarDistance > mLineInterval * mSlidingRatio && LastTimeDistance > mLineInterval * mSlidingRatio) {
            if (type == 0) {
                mNowIndex += mScaleValue;
            } else if (type == 1) {
                mNowIndex -= mScaleValue;
            }
            mChangeIndex = mCurPosX;
        }
    }

    /**
     * 加刻度
     */
    public void setPlusScale() {
        mNowIndex += mScaleValue;
        refresh();
    }

    /**
     * 减刻度
     */
    public void setMinusScale() {
        mNowIndex -= mScaleValue;
        refresh();
    }

    /**
     * 设置最小刻度范围
     */
    public void setMinIndex(double scale){
        int index = (int) (scale * mScaleRatio);
        mMinIndex = index;
    }

    /**
     * 设置最大刻度范围
     */
    public void setMaxIndex(double scale){
        int index = (int) (scale * mScaleRatio);
        mMaxIndex = index;
    }

    /**
     * 设置刻度
     *
     * @param scale
     */
    public void setNowIndex(double scale) {
        int index = (int) (scale * mScaleRatio);
        mNowIndex = index;
        refresh();
    }

    /**
     * 设置改变刻度回调监听
     *
     * @param onScaleChangeListener
     */
    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.onScaleChangeListener = onScaleChangeListener;
    }

    /**
     * 改变监听
     */
    public interface OnScaleChangeListener {
        /**
         * 回调当前的刻度
         *
         * @param index
         */
        void OnChange(double index);
    }

}
