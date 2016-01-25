package com.tintin.module.util.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

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

}
