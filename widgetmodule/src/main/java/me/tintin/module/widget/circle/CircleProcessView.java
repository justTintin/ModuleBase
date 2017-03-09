package me.tintin.module.widget.circle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import me.tintin.module.widget.R;

/**
 * Created by alive on 2016/1/20.
 */
public class CircleProcessView extends View
{

    private Context mContext;

    private int defaultColor = 0xFFFFFFFF; // 控件默认长、宽

    private float circleWidth; //圆环的宽度

    private Paint paint, mPaint, nPaint;

    private Drawable circlebackground;

    private int circleColor;

    private int roundProgressColor;

    private int circleProgressColor;

    private float progress = 0; //当前进度

    private float maxProgress; //最大进度

    private float circleMargin; //与控件四边的间距

    private float circleArc; //当前进度与初始进度的夹角

    private float curPoint_x; //当前进度图标的x,y值

    private float curPoint_y;

    private Bitmap mFieldBitmap;

    private Drawable roundbackground;

    private float dx = 0.0f;

    public CircleProcessView(Context context)
    {
        super(context);
        mContext = context;
    }

    public CircleProcessView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        setCustomAttributes(attrs);
    }

    public CircleProcessView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        setCustomAttributes(attrs);
    }

    private void setCustomAttributes(AttributeSet attrs)
    {
        paint = new Paint();
        mPaint = new Paint();
        TypedArray a = mContext.obtainStyledAttributes(attrs,
                R.styleable.CircleProgressView);
        circleMargin = a.getColor(R.styleable.CircleProgressView_circleMargin,
                0);
        maxProgress = a.getColor(R.styleable.CircleProgressView_circleMaxProcess,
                100);
        circleColor = a.getColor(R.styleable.CircleProgressView_circleColor,
                Color.RED);
        circleProgressColor = a.getColor(R.styleable.CircleProgressView_circleProgressColor,
                Color.GREEN);
        roundProgressColor = a.getColor(R.styleable.CircleProgressView_roundProgressColor,
                Color.GREEN);
        circlebackground = a.getDrawable(R.styleable.CircleProgressView_circleBackground);
        roundbackground = a.getDrawable(R.styleable.CircleProgressView_roundBackground);
        circleWidth = a.getDimension(R.styleable.CircleProgressView_circleWidth,
                5);
    }

    @SuppressLint({ "ResourceAsColor", "NewApi", "DrawAllocation" })
    @Override
    protected void onDraw(Canvas canvas)
    {
        //设置进度是实心还是空心
        mPaint.setStrokeWidth(circleWidth);
        //设置圆环的宽度
        mPaint.setColor(roundProgressColor);
        //设置进度的颜色
        mPaint.setAntiAlias(true);
        //消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        nPaint = new Paint();
        nPaint.setStrokeWidth(circleWidth);
        nPaint.setColor(roundProgressColor);
        nPaint.setAntiAlias(true); //消除锯齿
        nPaint.setStyle(Paint.Style.FILL);
        Bitmap mbmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);
        /**          * 画圆背景          */
        BitmapDrawable bd = (BitmapDrawable) roundbackground;
        float left = circleWidth + circleMargin - 1;
        float top = circleWidth + circleMargin - 1;
        float right = getWidth() - left;
        float bottom = getHeight() - top;
        RectF dst = new RectF(left, top, right, bottom);
        canvas.drawBitmap(bd.getBitmap(), null, dst, mPaint);
        /**           * 画最外层的大圆环           */
        float centre = getWidth() / 2; //获取圆心的x坐标
        float cRadius = (centre - circleMargin - circleWidth / 2); //圆环的半径           
        paint.setColor(circleColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心         

        paint.setStrokeWidth(circleWidth); //设置圆环的宽度
        paint.setAntiAlias(true); //消除锯齿          

        canvas.drawCircle(centre, centre, cRadius, paint); //画出圆环
        /**           * 画圆弧 ，画圆环的进度           */
        circleArc = 360 * progress / maxProgress;
        float x0 = getWidth() / 2;
        float y0 = getHeight()
                / 2
                - (getWidth() / 2 - circleMargin - circleWidth + mbmp.getHeight() / 2);
        float r0 = getHeight() / 2 - y0;
        curPoint_x = (float) (x0 + r0 * Math.sin(circleArc * Math.PI / 180));
        curPoint_y = (float) (y0 + r0
                * (1 - Math.cos(circleArc * Math.PI / 180)));
        RectF oval = new RectF(centre - cRadius, centre - cRadius, centre
                + cRadius, centre + cRadius); //用于定义的圆弧的形状和大小的界限

        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆角    

        canvas.drawArc(oval, 270, circleArc, false, mPaint); //根据进度画圆弧
        /**          * 进度图标          */
        RectF recf = new RectF(getWidth() * 0.5f - circleWidth * 0.5f
                + circleWidth / 10.0f, circleWidth + circleMargin - circleWidth
                - circleWidth / 10.0f, getWidth() * 0.5f + circleWidth * 0.5f
                - circleWidth / 10.0f, circleWidth + circleMargin);
        canvas.save();
        canvas.rotate(circleArc, getWidth() / 2, getHeight() / 2);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(mbmp, null, recf, mPaint);
        canvas.restore();
        /**          * 增长进度          */
        //设置填充图片
        BitmapDrawable bd1 = (BitmapDrawable) circlebackground;
        mFieldBitmap = bd1.getBitmap();
        float r1 = (getWidth() - 2 * circleMargin - 2 * circleWidth) * 0.5f;//内圆半径
        float cProgress = 2 * r1 * progress / maxProgress; // 每次增加的进度
        float y1 = circleMargin + circleWidth + 2 * r1 - cProgress;
        float y2 = circleMargin + circleWidth + 2 * r1 - cProgress; //勾股定理计算水平边
        dx = (float) Math.sqrt((double) (r1 * r1 - (r1 - cProgress)
                * (r1 - cProgress)));
        float x1 = getWidth() / 2 - dx;
        float x2 = getWidth() / 2 + dx;
        RectF mRectf = new RectF(circleWidth + circleMargin, circleWidth
                + circleMargin, circleWidth + circleMargin + 2 * r1,
                circleWidth + circleMargin + 2 * r1);
        float mArc = 360 * progress / maxProgress;
        Path mpath = new Path();
        mpath.reset();// 重置path         mpath.moveTo(x2, y2);
        mpath.addArc(mRectf, 90 - mArc * 0.5f, mArc);
        mpath.moveTo(x1, y1);
        mpath.moveTo(x2, y2);
        mpath.close(); //计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((getWidth()) * 1.0f)
                / (mFieldBitmap.getWidth() * 1.0f);
        float scaleHeight = ((getHeight()) * 1.0f)
                / (mFieldBitmap.getHeight() * 1.0f); // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix(); // 缩放图片动作
        matrix.setScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(mFieldBitmap,
                0,
                0,
                mFieldBitmap.getWidth(),
                mFieldBitmap.getHeight(),
                matrix,
                true);
        /*Shader.TileMode：（一共有三种） 		CLAMP  ：如果渲染器超出原始边界范围，会复制范围内边缘染色。 		REPEAT ：横向和纵向的重复渲染器图片，平铺。 		MIRROR ：横向和纵向的重复渲染器图片，这个和REPEAT 重复方式不一样，他是以镜像方式平铺。*/
        Shader mShader = new BitmapShader(resizedBitmap,
                Shader.TileMode.MIRROR, Shader.TileMode.REPEAT);
        nPaint.setShader(mShader);
        canvas.drawPath(mpath, nPaint);
    }

    public synchronized float getMax()
    {
        return maxProgress;
    }

    /**       * 设置进度的最大值       * @param max       */
    public synchronized void setMax(int max)
    {
        if (max < 0)
        {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.maxProgress = max;
    }

    /**       * 获取进度.需要同步       * @return       */
    public synchronized float getProgress()
    {
        return progress;
    }

    /**       * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步       * 刷新界面调用postInvalidate()能在非UI线程刷新       * @param progress       */
    public synchronized void setProgress(float progress)
    {
        if (progress < 0)
        {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > maxProgress)
        {
            progress = maxProgress;
        }
        if (progress <= maxProgress)
        {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getCircleColor()
    {
        return circleColor;
    }

    public void setCircleColor(int cricleColor)
    {
        this.circleColor = cricleColor;
    }

    public int getRoundProgressColor()
    {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int cricleProgressColor)
    {
        this.roundProgressColor = cricleProgressColor;
    }

    public float getCircleWidth()
    {
        return circleWidth;
    }

    public void setCircleWidth(float roundWidth)
    {
        this.circleWidth = roundWidth;
    }

    public float getCircleMargin()
    {
        return circleMargin;
    }

    public void setCircleMargin(float circleMargin)
    {
        this.circleMargin = circleMargin;
    }

    public Drawable getCircleBackground()
    {
        return circlebackground;
    }

    public void setCircleBackgorund(int backgroundId)
    {
        this.circlebackground = mContext.getResources()
                .getDrawable(backgroundId);
    }

    public Drawable getRoundBackground()
    {
        return circlebackground;
    }

    public void setRoundBackgorund(int backgroundId)
    {

        this.circlebackground = mContext.getResources()
                .getDrawable(backgroundId);
    }
}
