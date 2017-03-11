package me.tintin.module.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by n911917 on 2017/3/11.
 */

public class LoadingCustomView extends View implements View.OnTouchListener
{
    
    private View mContainer;
    
    private Context context;
    
    private ProgressBar mProgressBar;
    
    private TextView mTextViewAlert;
    
    public LoadingCustomView(Context context)
    {
        super(context);
        initView(context);
    }
    
    public LoadingCustomView(Context context, AttributeSet attrs,
            int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    
    public LoadingCustomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }
    
    private void initView(Context context)
    {
        super.setClickable(true);
        this.context = context;
        mContainer = inflate(context, R.layout.view_progress_container, null);
        mProgressBar = (ProgressBar) mContainer.findViewById(R.id.pb_loading);
        mTextViewAlert = (TextView) mContainer.findViewById(R.id.tv_loading_msg);
    }
    
    public void showLoadingProgress(String message)
    {
        mContainer.setVisibility(VISIBLE);
        mProgressBar.setVisibility(VISIBLE);
        mTextViewAlert.setVisibility(VISIBLE);
        mTextViewAlert.setText(message);
    }
    
    public void showLoadingResult(String message)
    {
        mContainer.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
        mTextViewAlert.setVisibility(VISIBLE);
        mTextViewAlert.setText(message);
    }
    
    public void dismiss()
    {
        mContainer.setVisibility(GONE);
    }
    
    public void setTvVisibility(boolean isShow)
    {
        if (isShow)
        {
            mTextViewAlert.setVisibility(INVISIBLE);
        }
        else
        {
            mTextViewAlert.setVisibility(VISIBLE);
        }
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return false;
    }
}
