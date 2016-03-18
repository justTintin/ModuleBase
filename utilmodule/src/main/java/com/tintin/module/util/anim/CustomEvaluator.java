package com.tintin.module.util.anim;

import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;

/*
* [一句话功能简述]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2016/2/26] 
*/
@SuppressLint("NewApi")
public class CustomEvaluator implements TypeEvaluator<Number> {

    @Override
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        // TODO Auto-generated method stub
        float propertyResult = 0;
        /*float startFloat = startValue.floatValue();
        return (startFloat + fraction * (endValue.floatValue() - startFloat));*/
        return propertyResult;
    }
}