package me.tintin.module.util.anim;

import android.animation.TimeInterpolator;

/*
* [一句话功能简述]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2016/2/26] 
*/
public class CustomInterpolator implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {
        // 编写相关的逻辑计算
        //input *= 0.8f;
        return input * input;
    }
}
