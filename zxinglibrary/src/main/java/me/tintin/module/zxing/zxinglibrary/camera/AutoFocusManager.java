/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.tintin.module.zxing.zxinglibrary.camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 由于对焦不是一次性完成的任务（手抖），而系统提供的对焦仅有Camera.autoFocus()方法，
 */
final class AutoFocusManager implements Camera.AutoFocusCallback
{

    private static final String TAG = AutoFocusManager.class.getSimpleName();

    private static final int HANDLE_START = 0x123;

    private static final long AUTO_FOCUS_INTERVAL_MS = 2000L;

    private static final Collection<String> FOCUS_MODES_CALLING_AF;
    static
    {
        FOCUS_MODES_CALLING_AF = new ArrayList<String>(2);
        FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_AUTO);
        FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_MACRO);
    }

    private boolean active;

    private final boolean useAutoFocus;

    private final Camera camera;

    private CallThreadToStartAutoFocus mCallThreadToStartAutoFocus;

    AutoFocusManager(Context context, Camera camera)
    {
        useAutoFocus = true;
        this.camera = camera;
        startAutoFocus();
        if (null == mCallThreadToStartAutoFocus)
        {
            mCallThreadToStartAutoFocus = new CallThreadToStartAutoFocus();
            mCallThreadToStartAutoFocus.start();
        }

    }

    @Override
    public synchronized void onAutoFocus(boolean success, Camera theCamera)
    {
        if (active)
        {
            Message msg = new Message();
            msg.what = HANDLE_START;
            mCallThreadToStartAutoFocus.mHandler.sendMessageDelayed(msg,
                    AUTO_FOCUS_INTERVAL_MS);
        }
    }

    synchronized void startAutoFocus()
    {
        if (useAutoFocus)
        {
            active = true;
            try
            {
                camera.autoFocus(this);
            }
            catch (RuntimeException re)
            {
                // Have heard RuntimeException reported in Android 4.0.x+;
                // continue?
                Log.w(TAG, "Unexpected exception while focusing", re);
            }
        }
    }

    synchronized void stop()
    {
        if (useAutoFocus)
        {
            try
            {
                camera.cancelAutoFocus();
            }
            catch (RuntimeException re)
            {
                // Have heard RuntimeException reported in Android 4.0.x+;
                // continue?
                Log.w(TAG, "Unexpected exception while cancelling focusing", re);
            }
        }
        if (null != mCallThreadToStartAutoFocus.mHandler)
        {
            mCallThreadToStartAutoFocus.mHandler.removeMessages(HANDLE_START);
        }
        active = false;
    }

    // TODO:王浩修改，使用handler，取消AsyncTask
    public class CallThreadToStartAutoFocus extends Thread
    {
        private Handler mHandler;

        @Override
        public void run()
        {
            Looper.prepare();
            mHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    switch (msg.what)
                    {
                        case HANDLE_START:
                            synchronized (AutoFocusManager.this)
                            {
                                if (active)
                                {
                                    startAutoFocus();
                                    Log.i(TAG,
                                            "AutofocusManage.startAutoFocus...");
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
            };
            Looper.loop();
        }
    }
}
