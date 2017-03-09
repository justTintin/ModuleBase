package me.tintin.module.widget.circle;

import android.animation.TypeEvaluator;

/**
 * Created by alive on 2016/1/21.
 */
public class PointEvaluator implements TypeEvaluator
{
    private float mEndAngle;

    private Point mCentPoint;

    private float mRadius;

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue)
    {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        //            float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        //            float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());

        float x = (float) (Math.sin(fraction * mEndAngle) * mRadius + mCentPoint.getX());

        float y = mCentPoint.getY()
                - (float) (Math.cos(fraction * mEndAngle) * mRadius);
        Point point = new Point(x, y);
        return point;
    }

    public PointEvaluator(float angle, float radius, Point point)
    {
        mEndAngle = angle;
        mCentPoint = point;
        mRadius = radius;
    }

}
