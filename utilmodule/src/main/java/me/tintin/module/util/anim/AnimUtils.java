package me.tintin.module.util.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

/*
* [一句话功能简述]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2016/1/19] 
*/
public class AnimUtils
{
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    public static void hide(final View view)
    {
        ViewPropertyAnimator animator = view.animate().alpha(0.0f)

        .setInterpolator(INTERPOLATOR).setDuration(800);

        animator.setListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animator)
            {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                //                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator)
            {
                //                show(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {

            }
        });
        animator.start();
    }

    public static void show(final View view)
    {
        ViewPropertyAnimator animator = view.animate()
                .alpha(1.0f)
                .setInterpolator(INTERPOLATOR)
                .setDuration(1000);
        animator.setListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animator)
            {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                //                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator)
            {
                //                hide(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {

            }
        });
        animator.start();

    }

    public static void rotation(final View view)
    {
        ViewPropertyAnimator animator = view.animate().rotation(360f)

        .setInterpolator(INTERPOLATOR).setDuration(1500);
        animator.setListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animator)
            {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                //                view.setVisibility(View.VISIBLE);
                //                animator.cancel();
            }

            @Override
            public void onAnimationCancel(Animator animator)
            {
                //                hide(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {

            }
        });
        animator.start();

    }

    public static void rotation(View view, int duration, int repeatCount)
    {

        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("rotation",
                360F,
                0);
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view,
                p1);
        mObjectAnimator.setDuration(duration);

        if (repeatCount == 0)
        {
            mObjectAnimator.setRepeatCount(-1);
        }
        else
        {
            mObjectAnimator.setRepeatCount(repeatCount);
        }

        mObjectAnimator.start();
    }

    /**
     * 使用ValueAnimator实现图片缩放动画
     */
    public static void scaleValueAnimator(final ImageView mImageViewTest)
    {
        //1.设置目标属性名及属性变化的初始值和结束值
        PropertyValuesHolder mPropertyValuesHolderScaleX = PropertyValuesHolder.ofFloat("scaleX",
                1.0f,
                0.0f);
        PropertyValuesHolder mPropertyValuesHolderScaleY = PropertyValuesHolder.ofFloat("scaleY",
                1.0f,
                0.0f);
        ValueAnimator mAnimator = ValueAnimator.ofPropertyValuesHolder(mPropertyValuesHolderScaleX,
                mPropertyValuesHolderScaleY);
        //2.为目标对象的属性变化设置监听器
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {

            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                // 3.根据属性名获取属性变化的值分别为ImageView目标对象设置X和Y轴的缩放值
                float animatorValueScaleX = (float) animation.getAnimatedValue("scaleX");
                float animatorValueScaleY = (float) animation.getAnimatedValue("scaleY");
                mImageViewTest.setScaleX(animatorValueScaleX);
                mImageViewTest.setScaleY(animatorValueScaleY);

            }
        });
        //4.为ValueAnimator设置自定义的Interpolator
        mAnimator.setInterpolator(new CustomInterpolator());
        //5.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator.setDuration(2000);
        mAnimator.setRepeatCount(3);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //6.为ValueAnimator设置目标对象并开始执行动画
        mAnimator.setTarget(mImageViewTest);
        mAnimator.start();
    }

}
