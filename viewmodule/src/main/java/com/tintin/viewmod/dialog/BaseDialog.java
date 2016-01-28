package com.tintin.viewmod.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * [对话框的基类]<BR>
 * @author titin
 * @version [DoronApp, May 24, 2014]
 */
public abstract class BaseDialog extends Dialog
{

    /**
     * DIALOG_TYPE_INSIDE
     */
    public static final int DIALOG_TYPE_MATCH = 0;

    /**
     * DIALOG_TYPE_NORMAL
     */
    public static final int DIALOG_TYPE_NORMAL = 1;

    /**
     * DIALOG_TYPE_INSIDE
     */
    public static final int DIALOG_TYPE_INSIDE = 2;

    /**
     * DIALOG_TYPE_INSIDE
     */
    public static final int DIALOG_TYPE_CUSTOM = 3;

    /**
     * DIALOG_TYPE_TOP
     */
    public static final int DIALOG_TYPE_TOP = 4;

    /**
     * DIALOG_TYPE_BOTTOM
     */
    public static final int DIALOG_TYPE_BOTTOM = 5;

    private View mContainer;

    private Context mContext;

    private int mWidth;

    private int mHeight;

    /**
     * [基础对话框的构造方法]
     * @param context 传入的上下文
     * @param theme 对话框的主题样式
     */
    public BaseDialog(Context context, int theme)
    {
        super(context, theme);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    /**
     * [基础对话框的构造方法]
     * @param context 传入的止下文
     * @param theme 主题样式
     * @param layoutId 要显示的对应的layout
     * @param type 对话框的类型
     */
    @SuppressWarnings("deprecation")
    public BaseDialog(Context context, int theme, int layoutId, int type)
    {
        this(context, theme);
        this.mContext = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
        LayoutInflater inflater = LayoutInflater.from(context);
        mContainer = inflater.inflate(layoutId, null);
        switch (type)
        {

            case DIALOG_TYPE_MATCH:
            {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        (int) (mWidth * 1), (int) (mHeight * 1));
                setContentView(mContainer, lp);
            }
            break;
            case DIALOG_TYPE_INSIDE:
            {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        (int) (mWidth * 0.4), (int) (mHeight * 0.2));
                setContentView(mContainer, lp);
            }
            break;
            case DIALOG_TYPE_NORMAL:
            {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        (int) (mWidth * 0.8), (int) (mHeight * 0.75));
                setContentView(mContainer, lp);
            }
            break;
            case DIALOG_TYPE_CUSTOM:
            {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        (int) (mWidth * 0.89), (int) (mHeight * 0.93));
                setContentView(mContainer, lp);
            }
            break;
            case DIALOG_TYPE_BOTTOM:
            {
                setContentView(mContainer);
                Window window = this.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.BOTTOM);
                //                lp.x = 100; // 新位置X坐标
                //                lp.y = 100; // 新位置Y坐标
                lp.width = mWidth; // 宽度
                lp.height = (int) (mHeight * 0.5); // 高度
                lp.alpha = 0.7f; // 透明度

                // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
                // dialog.onWindowAttributesChanged(lp);
                window.setAttributes(lp);

            }
            break;
            case DIALOG_TYPE_TOP:
            {
                setContentView(mContainer);
                Window window = this.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.TOP);
                //                lp.x = 100; // 新位置X坐标
                //                lp.y = 100; // 新位置Y坐标
                lp.width = mWidth; // 宽度
                lp.height = (int) (mHeight * 0.5); // 高度
                lp.alpha = 0.7f; // 透明度

                // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
                // dialog.onWindowAttributesChanged(lp);
                window.setAttributes(lp);

            }
            break;
            default:
            {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        (int) (mWidth * 0.8),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                setContentView(mContainer, lp);
            }
            break;

        }
    }

    @Override
    public void onBackPressed()
    {

    }

    /**
     * [初始化控件]<BR>
     */
    protected abstract void initView();

    /**
     * [控件的逻辑]<BR>
     */
    protected abstract void initController();

    /**
     * 获取 Container<BR>
     * @return mContainer
     */
    public final View getContainer()
    {
        return mContainer;
    }

}
