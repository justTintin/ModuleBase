package com.tintin.module.util.app;

/*
* [堆栈管理]
* [功能详细描述]
* @author Administrator
* @version [DoronApp, 2016/2/24] 
*/

import java.util.Collections;
import java.util.Stack;

import android.app.Activity;
import android.util.Log;

public class StackManager
{
    private static final String TAG = StackManager.class.getSimpleName();

    public static int getFragmentId()
    {
        return fragmentId;
    }

    public static void setFragmentId(int fragmentId)
    {
        StackManager.fragmentId = fragmentId;
    }

    private static int fragmentId;

    public static Stack<Activity> getActivityStack()
    {
        return activityStack;
    }

    public static Stack<Activity> getActivityStackReverse()
    {
        Stack<Activity> activityStackTemp = new Stack<>();
        for (int i = 0; i < activityStack.size(); i++)
        {
            activityStackTemp.add(activityStack.get(i));
        }
        Collections.reverse(activityStackTemp);
        return activityStackTemp;
    }

    private static Stack<Activity> activityStack = new Stack<Activity>();

    private static StackManager instance;

    /**
     * <单例方法>
     * <功能详细描述>
     * @return 该对象的实例
     * @see [类、类#方法、类#成员]
     */
    public static StackManager getScreenManager()
    {
        if (instance == null)
        {
            instance = new StackManager();
        }
        return instance;
    }

    /**
     * <获取当前栈顶Activity>
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Activity currentActivity()
    {
        if (activityStack == null || activityStack.size() == 0)
        {
            return null;
        }
        Activity activity = activityStack.lastElement();
        //
        //        Log.e(TAG, "get current activity:"
        //                + activity.getClass().getSimpleName());
        return activity;
    }

    /**
     * <将Activity入栈>
     * <功能详细描述>
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void pushActivity(Activity activity)
    {
        try
        {
            if (activityStack == null)
            {
                activityStack = new Stack<Activity>();
            }

            for (int i = activityStack.size() - 1; i > 0; i--)
            {
                if (activity.equals(activityStack.get(i)))
                {
                    activityStack.remove(activityStack.get(i));
                }
            }

            activityStack.add(activity);
            Log.e(TAG, "push stack activity:" + activity.toString());
            int i = 0;
            for (Activity activity1 : activityStack)
            {
                Log.e(TAG, "i==" + i + activity1.toString());
                i++;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

    }

    /**
     * <退出栈顶Activity>
     * <功能详细描述>
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void popActivity(Activity activity)
    {
        if (activity != null)
        {
            activity.finish();
            Log.e(TAG, "remove current activity:"
                    + activity.getClass().getSimpleName());
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * <退出栈中所有Activity,当前的activity除外>
     * <功能详细描述>
     * @param cls
     * @see [类、类#方法、类#成员]
     */
    public void popAllActivityExceptMain(Class cls)
    {
        while (true)
        {
            Activity activity = currentActivity();
            if (activity == null)
            {
                break;
            }
            if (activity.getClass().equals(cls))
            {
                break;
            }

            popActivity(activity);
        }
    }

    /**
     * <删除栈中Activity>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void removeActivity(Activity activity)
    {

        for (int i = activityStack.size() - 1; i > 0; i--)
        {
            if (activity.equals(activityStack.get(i)))
            {
                activityStack.remove(activityStack.get(i));
            }
        }

    }
}
