package me.tintin.module.view.circle;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import me.tintin.module.view.R;

/**
 * Created by gaopengfei on 15/11/15.
 */
public class CircleSeekBar extends View
{

    private static final double RADIAN = 180 / Math.PI;

    private static final String INATANCE_STATE = "state";

    private static final String INSTANCE_MAX_PROCESS = "max_process";

    private static final String INSTANCE_CUR_PROCESS = "cur_process";

    private static final String INSTANCE_REACHED_COLOR = "reached_color";

    private static final String INSTANCE_REACHED_WIDTH = "reached_width";

    private static final String INSTANCE_REACHED_CORNER_ROUND = "reached_corner_round";

    private static final String INSTANCE_UNREACHED_COLOR = "unreached_color";

    private static final String INSTANCE_UNREACHED_WIDTH = "unreached_width";

    private static final String INSTANCE_POINTER_COLOR = "pointer_color";

    private static final String INSTANCE_POINTER_RADIUS = "pointer_radius";

    private static final String INSTANCE_POINTER_SHADOW = "pointer_shadow";

    private static final String INSTANCE_POINTER_SHADOW_RADIUS = "pointer_shadow_radius";

    private static final String INSTANCE_WHEEL_SHADOW = "wheel_shadow";

    private static final String INSTANCE_WHEEL_SHADOW_RADIUS = "wheel_shadow_radius";

    private static final String INSTANCE_WHEEL_HAS_CACHE = "wheel_has_cache";

    private static final String INSTANCE_WHEEL_CAN_TOUCH = "wheel_can_touch";

    private Paint mWheelPaint;

    private Paint mReachedPaint;

    private Paint mReachedEdgePaint;

    private Paint mPointerPaint;

    private int mMaxProcess;

    private int mCurProcess;

    private int mReachedColor, mUnreachedColor;

    private float mReachedWidth, mUnreachedWidth;

    private boolean isHasReachedCornerRound;

    private int mPointerColor;

    private float mPointerRadius;

    private double mCurAngle;

    private float mWheelCurX, mWheelCurY;

    private boolean isHasWheelShadow, isHasPointerShadow;

    private float mWheelShadowRadius, mPointerShadowRadius;

    private boolean isHasCache;

    private Canvas mCacheCanvas;

    private Bitmap mCacheBitmap;

    private boolean isCanTouch;

    private float mDefShadowOffset;

    //两个圆中心半径

    private Point mCurPoint;

    private OnSeekBarChangeListener mChangListener;

    public void setPointBitmap(Bitmap mPointBitmap)
    {
        this.mPointBitmap = mPointBitmap;
    }

    private Bitmap mPointBitmap;

    public void setPointBitmapClick(Bitmap mPointBitmapClick)
    {
        this.mPointBitmapClick = mPointBitmapClick;
    }

    private Bitmap mPointBitmapClick;

    public CircleSeekBar(Context context)
    {
        this(context, null);
    }

    public CircleSeekBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        initAttrs(attrs, defStyleAttr);
        initPadding();
        initPaints();
    }

    private void initPaints()
    {
        mDefShadowOffset = getDimen(R.dimen.def_shadow_offset);
        /**
         * 圆环画笔
         */
        mWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWheelPaint.setColor(mUnreachedColor);
        mWheelPaint.setStyle(Paint.Style.STROKE);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        if (isHasWheelShadow)
        {
            mWheelPaint.setShadowLayer(mWheelShadowRadius,
                    mDefShadowOffset,
                    mDefShadowOffset,
                    Color.DKGRAY);
        }
        /**
         * 选中区域画笔
         */
        mReachedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedPaint.setColor(mReachedColor);
        mReachedPaint.setStyle(Paint.Style.STROKE);
        mReachedPaint.setStrokeWidth(mReachedWidth);
        if (isHasReachedCornerRound)
        {
            mReachedPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        /**
         * 锚点画笔
         */
        mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerPaint.setColor(mPointerColor);
        mPointerPaint.setStyle(Paint.Style.FILL);
        if (isHasPointerShadow)
        {
            mPointerPaint.setShadowLayer(mPointerShadowRadius,
                    mDefShadowOffset,
                    mDefShadowOffset,
                    Color.DKGRAY);
        }
        /**
         * 选中区域两头的圆角画笔
         */
        mReachedEdgePaint = new Paint(mReachedPaint);
        mReachedEdgePaint.setStyle(Paint.Style.FILL);
    }

    private void initAttrs(AttributeSet attrs, int defStyle)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.CircleSeekBar,
                defStyle,
                0);
        mMaxProcess = a.getInt(R.styleable.CircleSeekBar_wheel_max_process, 360);
        mCurProcess = a.getInt(R.styleable.CircleSeekBar_wheel_cur_process, 0);
        if (mCurProcess > mMaxProcess)
            mCurProcess = mMaxProcess;
        mReachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color,
                getColor(R.color.def_reached_color));
        mUnreachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_unreached_color,
                getColor(R.color.def_wheel_color));
        mUnreachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_unreached_width,
                getDimen(R.dimen.def_wheel_width));
        isHasReachedCornerRound = a.getBoolean(R.styleable.CircleSeekBar_wheel_reached_has_corner_round,
                true);
        mReachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_reached_width,
                mUnreachedWidth);
        mPointerColor = a.getColor(R.styleable.CircleSeekBar_wheel_pointer_color,
                getColor(R.color.def_pointer_color));

        mPointerRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_pointer_radius,
                mReachedWidth / 2);
        isHasWheelShadow = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_wheel_shadow,
                false);
        if (isHasWheelShadow)
        {
            mWheelShadowRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_shadow_radius,
                    getDimen(R.dimen.def_shadow_radius));
        }
        isHasPointerShadow = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_pointer_shadow,
                false);
        if (isHasPointerShadow)
        {
            mPointerShadowRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_pointer_shadow_radius,
                    getDimen(R.dimen.def_shadow_radius));
        }
        isHasCache = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_cache,
                isHasWheelShadow);
        isCanTouch = a.getBoolean(R.styleable.CircleSeekBar_wheel_can_touch,
                true);

        if (isHasPointerShadow | isHasWheelShadow)
        {
            setSoftwareLayer();
        }
        a.recycle();
    }

    private void initPadding()
    {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingStart = 0, paddingEnd = 0;
        if (Build.VERSION.SDK_INT >= 17)
        {
            paddingStart = getPaddingStart();
            paddingEnd = getPaddingEnd();
        }
        int maxPadding = Math.max(paddingLeft,
                Math.max(paddingTop,
                        Math.max(paddingRight,
                                Math.max(paddingBottom,
                                        Math.max(paddingStart, paddingEnd)))));
        setPadding(maxPadding, maxPadding, maxPadding, maxPadding);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private int getColor(int colorId)
    {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23)
        {
            return getContext().getColor(colorId);
        }
        else
        {
            return getResources().getColor(colorId);
        }
    }

    private float getDimen(int dimenId)
    {
        return getResources().getDimension(dimenId);
    }

    private void setSoftwareLayer()
    {
        if (Build.VERSION.SDK_INT >= 11)
        {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private float mMiddleRadius;

    private float mCentX;

    private float mCentY;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        mCurAngle = (double) mCurProcess / mMaxProcess * 360.0;
        double cos = -Math.cos(Math.toRadians(mCurAngle));
        float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;

        mMiddleRadius = radius;
        mWheelCurX = calcXLocationInWheel(mCurAngle > 180 ? 0 : min,
                (float) cos,
                radius);
        mWheelCurY = calcYLocationInWheel((float) cos, radius);
    }

    private Canvas mCanvas;

    @Override
    protected void onDraw(Canvas canvas)
    {
        mCanvas = canvas;
        float left = getPaddingLeft() + mUnreachedWidth / 2;
        float top = getPaddingTop() + mUnreachedWidth / 2;
        float right = canvas.getWidth() - getPaddingRight() - mUnreachedWidth
                / 2;
        float bottom = canvas.getHeight() - getPaddingBottom()
                - mUnreachedWidth / 2;
        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2;

        mCentX = centerX;
        mCentY = centerY;
        float wheelRadius = (canvas.getWidth() - getPaddingLeft() - getPaddingRight())
                / 2 - mUnreachedWidth / 2;

        if (isHasCache)
        {
            if (mCacheCanvas == null)
            {
                buildCache(centerX, centerY, wheelRadius);
            }
            canvas.drawBitmap(mCacheBitmap, 0, 0, null);
        }
        else
        {
            canvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
        }

        //画选中区域
        canvas.drawArc(new RectF(left, top, right, bottom),
                -90,
                (float) mCurAngle,
                false,
                mReachedPaint);
        //设置填充图片

        //画锚点
        if (mPointBitmap == null)
        {
            canvas.drawCircle(mWheelCurX,
                    mWheelCurY,
                    mPointerRadius,
                    mPointerPaint);
        }
        else
        {
            canvas.drawBitmap(mPointBitmap,
                    mWheelCurX - mPointBitmap.getWidth() / 2,
                    mWheelCurY - mPointBitmap.getHeight() / 2,
                    mPointerPaint);
        }
    }

    private void buildCache(float centerX, float centerY, float wheelRadius)
    {
        mCacheBitmap = Bitmap.createBitmap(getWidth(),
                getHeight(),
                Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCacheBitmap);

        //画环
        mCacheCanvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        if (isCanTouch
                && (event.getAction() == MotionEvent.ACTION_MOVE || isTouch(x,
                        y)))
        {
            // 通过当前触摸点搞到cos角度值
            float cos = computeCos(x, y);
            // 通过反三角函数获得角度值
            if (x < getWidth() / 2)
            { // 滑动超过180度
                mCurAngle = Math.PI * RADIAN + Math.acos(cos) * RADIAN;
            }
            else
            { // 没有超过180度
                mCurAngle = Math.PI * RADIAN - Math.acos(cos) * RADIAN;
            }
            mCurProcess = getSelectedValue();

            float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;
            mWheelCurX = calcXLocationInWheel(x, cos, radius);
            mWheelCurY = calcYLocationInWheel(cos, radius);
            if (mChangListener != null)
            {
                mChangListener.onChanged(this, mMaxProcess, mCurProcess);
            }
            invalidate();
            return true;
        }
        else
        {
            return super.onTouchEvent(event);
        }
    }

    private void startInvalidate(float x, float y)
    {
        // 通过当前触摸点搞到cos角度值
        float cos = computeCos(x, y);
        // 通过反三角函数获得角度值
        if (x < getWidth() / 2)
        { // 滑动超过180度
            mCurAngle = Math.PI * RADIAN + Math.acos(cos) * RADIAN;
        }
        else
        { // 没有超过180度
            mCurAngle = Math.PI * RADIAN - Math.acos(cos) * RADIAN;
        }
        mCurProcess = getSelectedValue();

        float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;
        mWheelCurX = calcXLocationInWheel(x, cos, radius);
        mWheelCurY = calcYLocationInWheel(cos, radius);
        if (mChangListener != null)
        {
            mChangListener.onChanged(this, mMaxProcess, mCurProcess);
        }
        invalidate();

    }

    private void startAnim(float endProgress, final int duration)
    {
        //取得最后设置进度的弧度值
        float endAngle = (float) (endProgress / mMaxProcess * Math.PI * 2);
        //设置动画开始的点
        Point startPoint = new Point(
                (float) (Math.sin(0) * mMiddleRadius + mCentX),

                mCentY - (float) (Math.cos(0) * mMiddleRadius));
        final Point endPoint = new Point((float) (Math.sin(endAngle)
                * mMiddleRadius + mCentX),

        mCentY - (float) (Math.cos(endAngle) * mMiddleRadius));
        PointEvaluator pointEvaluator = new PointEvaluator(endAngle,
                mMiddleRadius, new Point(mCentX, mCentY));

        ValueAnimator valueAnimator1 = ValueAnimator.ofObject(pointEvaluator,
                startPoint,
                endPoint);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {

            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mCurPoint = (Point) animation.getAnimatedValue();

                startInvalidate(mCurPoint.getX(), mCurPoint.getY());
            }
        });
        valueAnimator1.setDuration(duration);
        valueAnimator1.setRepeatCount(0);
        valueAnimator1.start();
        initScaleAnim();
        ValueAnimator valueAnimator2 = ValueAnimator.ofInt(1, 15);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                Integer value = (Integer) animation.getAnimatedValue();

                if (value % 2 != 0)
                {
                    mPointBitmap = newBitmap;

                }
                else
                {
                    mPointBitmap = temBitmap;

                }
                invalidate();

            }
        });
        valueAnimator2.setDuration(duration + 1000);
        valueAnimator2.start();

    }

    private Bitmap temBitmap = mPointBitmap;

    private Bitmap newBitmap;

    private void initScaleAnim()
    {
        if (mPointBitmap != null && mPointBitmapClick != null)
        {

            final Bitmap temBitmap = mPointBitmap;
            // 获得图片的宽高
            int width = mPointBitmap.getWidth();
            int height = mPointBitmap.getHeight();
            // 设置想要的大小
            int newWidth = width * 3 / 2;
            int newHeight = height * 3 / 2;
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            newBitmap = Bitmap.createBitmap(mPointBitmap,
                    0,
                    0,
                    width,
                    height,
                    matrix,
                    true);

        }

    }

    private boolean isTouch(float x, float y)
    {
        double radius = (getWidth() - getPaddingLeft() - getPaddingRight() + getCircleWidth()) / 2;
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        return Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) < radius
                * radius;
    }

    private float getCircleWidth()
    {
        return Math.max(mUnreachedWidth,
                Math.max(mReachedWidth, mPointerRadius));
    }

    private float calcXLocationInWheel(float x, float cos, float radius)
    {
        if (x > (getWidth() / 2))
        {
            return (float) (getWidth() / 2 + Math.sqrt(1 - cos * cos) * radius);
        }
        else
        {
            return (float) (getWidth() / 2 - Math.sqrt(1 - cos * cos) * radius);
        }
    }

    private float calcYLocationInWheel(float cos, float radius)
    {
        return getWidth() / 2 + radius * cos;
    }

    /**
     * 拿到倾斜的cos值
     */
    private float computeCos(float x, float y)
    {
        float width = x - getWidth() / 2;
        float height = y - getHeight() / 2;
        float slope = (float) Math.sqrt(width * width + height * height);
        return height / slope;
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INATANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_MAX_PROCESS, mMaxProcess);
        bundle.putInt(INSTANCE_CUR_PROCESS, mCurProcess);
        bundle.putInt(INSTANCE_REACHED_COLOR, mReachedColor);
        bundle.putFloat(INSTANCE_REACHED_WIDTH, mReachedWidth);
        bundle.putBoolean(INSTANCE_REACHED_CORNER_ROUND,
                isHasReachedCornerRound);
        bundle.putInt(INSTANCE_UNREACHED_COLOR, mUnreachedColor);
        bundle.putFloat(INSTANCE_UNREACHED_WIDTH, mUnreachedWidth);
        bundle.putInt(INSTANCE_POINTER_COLOR, mPointerColor);
        bundle.putFloat(INSTANCE_POINTER_RADIUS, mPointerRadius);
        bundle.putBoolean(INSTANCE_POINTER_SHADOW, isHasPointerShadow);
        bundle.putFloat(INSTANCE_POINTER_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBoolean(INSTANCE_WHEEL_SHADOW, isHasWheelShadow);
        bundle.putFloat(INSTANCE_WHEEL_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBoolean(INSTANCE_WHEEL_HAS_CACHE, isHasCache);
        bundle.putBoolean(INSTANCE_WHEEL_CAN_TOUCH, isCanTouch);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INATANCE_STATE));
            mMaxProcess = bundle.getInt(INSTANCE_MAX_PROCESS);
            mCurProcess = bundle.getInt(INSTANCE_CUR_PROCESS);
            mReachedColor = bundle.getInt(INSTANCE_REACHED_COLOR);
            mReachedWidth = bundle.getFloat(INSTANCE_REACHED_WIDTH);
            isHasReachedCornerRound = bundle.getBoolean(INSTANCE_REACHED_CORNER_ROUND);
            mUnreachedColor = bundle.getInt(INSTANCE_UNREACHED_COLOR);
            mUnreachedWidth = bundle.getFloat(INSTANCE_UNREACHED_WIDTH);
            mPointerColor = bundle.getInt(INSTANCE_POINTER_COLOR);
            mPointerRadius = bundle.getFloat(INSTANCE_POINTER_RADIUS);
            isHasPointerShadow = bundle.getBoolean(INSTANCE_POINTER_SHADOW);
            mPointerShadowRadius = bundle.getFloat(INSTANCE_POINTER_SHADOW_RADIUS);
            isHasWheelShadow = bundle.getBoolean(INSTANCE_WHEEL_SHADOW);
            mPointerShadowRadius = bundle.getFloat(INSTANCE_WHEEL_SHADOW_RADIUS);
            isHasCache = bundle.getBoolean(INSTANCE_WHEEL_HAS_CACHE);
            isCanTouch = bundle.getBoolean(INSTANCE_WHEEL_CAN_TOUCH);
            initPaints();
        }
        else
        {
            super.onRestoreInstanceState(state);
        }

        if (mChangListener != null)
        {
            mChangListener.onChanged(this, mMaxProcess, mCurProcess);
        }
    }

    private int getSelectedValue()
    {
        return Math.round(mMaxProcess * ((float) mCurAngle / 360));
    }

    public int getCurProcess()
    {
        return mCurProcess;
    }

    public void setCurProcess(int curProcess)
    {
        this.mCurProcess = curProcess > mMaxProcess ? mMaxProcess : curProcess;
        if (mChangListener != null)
        {
            mChangListener.onChanged(this, mMaxProcess, curProcess);
        }
        invalidate();
    }

    public void setEndProcessByAnim(int curProcess, int duration)
    {
        this.mCurProcess = curProcess > mMaxProcess ? mMaxProcess : curProcess;
        startAnim(curProcess, duration);
    }

    public int getReachedColor()
    {
        return mReachedColor;
    }

    public void setReachedColor(int reachedColor)
    {
        this.mReachedColor = reachedColor;
        mReachedPaint.setColor(reachedColor);
        mReachedEdgePaint.setColor(reachedColor);
        invalidate();
    }

    public int getUnreachedColor()
    {
        return mUnreachedColor;
    }

    public void setUnreachedColor(int unreachedColor)
    {
        this.mUnreachedColor = unreachedColor;
        mWheelPaint.setColor(unreachedColor);
        invalidate();
    }

    public float getReachedWidth()
    {
        return mReachedWidth;
    }

    public void setReachedWidth(float reachedWidth)
    {
        this.mReachedWidth = reachedWidth;
        mReachedPaint.setStrokeWidth(reachedWidth);
        mReachedEdgePaint.setStrokeWidth(reachedWidth);
        invalidate();
    }

    public boolean isHasReachedCornerRound()
    {
        return isHasReachedCornerRound;
    }

    public void setHasReachedCornerRound(boolean hasReachedCornerRound)
    {
        isHasReachedCornerRound = hasReachedCornerRound;
        mReachedPaint.setStrokeCap(hasReachedCornerRound ? Paint.Cap.ROUND
                : Paint.Cap.BUTT);
        invalidate();
    }

    public float getUnreachedWidth()
    {
        return mUnreachedWidth;
    }

    public void setUnreachedWidth(float unreachedWidth)
    {
        this.mUnreachedWidth = unreachedWidth;
        mWheelPaint.setStrokeWidth(unreachedWidth);
        invalidate();
    }

    public int getPointerColor()
    {
        return mPointerColor;
    }

    public void setPointerColor(int pointerColor)
    {
        this.mPointerColor = pointerColor;
        mPointerPaint.setColor(pointerColor);
    }

    public float getPointerRadius()
    {
        return mPointerRadius;
    }

    public void setPointerRadius(float pointerRadius)
    {
        this.mPointerRadius = pointerRadius;
        mPointerPaint.setStrokeWidth(pointerRadius);
        invalidate();
    }

    public boolean isHasWheelShadow()
    {
        return isHasWheelShadow;
    }

    public void setWheelShadow(float wheelShadow)
    {
        this.mWheelShadowRadius = wheelShadow;
        if (wheelShadow == 0)
        {
            isHasWheelShadow = false;
            mWheelPaint.clearShadowLayer();
            mCacheCanvas = null;
            mCacheBitmap.recycle();
            mCacheBitmap = null;
        }
        else
        {
            mWheelPaint.setShadowLayer(mWheelShadowRadius,
                    mDefShadowOffset,
                    mDefShadowOffset,
                    Color.DKGRAY);
            setSoftwareLayer();
        }
        invalidate();
    }

    public float getWheelShadowRadius()
    {
        return mWheelShadowRadius;
    }

    public boolean isHasPointerShadow()
    {
        return isHasPointerShadow;
    }

    public float getPointerShadowRadius()
    {
        return mPointerShadowRadius;
    }

    public void setPointerShadowRadius(float pointerShadowRadius)
    {
        this.mPointerShadowRadius = pointerShadowRadius;
        if (mPointerShadowRadius == 0)
        {
            isHasPointerShadow = false;
            mPointerPaint.clearShadowLayer();
        }
        else
        {
            mPointerPaint.setShadowLayer(pointerShadowRadius,
                    mDefShadowOffset,
                    mDefShadowOffset,
                    Color.DKGRAY);
            setSoftwareLayer();
        }
        invalidate();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener)
    {
        mChangListener = listener;
    }

    public interface OnSeekBarChangeListener
    {
        void onChanged(CircleSeekBar seekbar, int maxValue, int curValue);
    }
}