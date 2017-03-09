package me.tintin.module.zxing.zxingactivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import me.tintin.module.zxing.R;
import me.tintin.module.zxing.zxinglibrary.android.DecodeFormatManager;
import me.tintin.module.zxing.zxinglibrary.camera.CameraManager;
import me.tintin.module.zxing.zxinglibrary.view.ViewfinderResultPointCallback;
import me.tintin.module.zxing.zxinglibrary.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.HybridBinarizer;
import me.tintin.module.zxing.zxingutil.ZxingUtil;

/**
 * 扫描二维码界面
 *
 * @author 910282
 */
public class ScanQRCodeActivity extends AppCompatActivity implements
        View.OnClickListener
{
    /**
     * TAG:打印日志需用到
     */
    private static final String TAG = ScanQRCodeActivity.class.getSimpleName();

    private static final float BEEP_VOLUME = 0.80f;

    private static final String KEY_BITMAP = "bitmap";

    private static final String KEY_RESULT = "result";

    /**
     * 记录的上下文对象
     */
    private Context mContext = this;

    private boolean mHasSurface;

    private ViewfinderView mViewfinderView;

    private CaptureActivityHandler mCaptureActivityHandler;

    private Vector<BarcodeFormat> mDecodeFormats;

    private String mCharacterSet;

    private InactivityTimer mInactivityTimer;

    private MediaPlayer mMediaPlayer;

    private boolean mPlayBeep;

    private boolean mVibrate;

    private ImageView mIvLight;

    private boolean isOpenTorch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        //        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scanner_code);

        initView();

    }

    private void initView()
    {
        //初始化摄像机
        CameraManager.init(getApplication());

        mIvLight = (ImageView) findViewById(R.id.iv_open_light);
        mViewfinderView = (ViewfinderView) findViewById(R.id.view_finder);
        mHasSurface = false;
        mInactivityTimer = new InactivityTimer(this);
        mIvLight.setOnClickListener(this);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        //扫描时保持屏幕常亮
        ZxingUtil.acquireWakeLock(ScanQRCodeActivity.this);
        //初始化surfaceView对象并把surfaceHolder给摄像机
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (mHasSurface)
        {
            initCamera(surfaceHolder);
        }
        else
        {
            //添加回调
            surfaceHolder.addCallback(new SurfaceHolder.Callback()
            {

                @Override
                public void surfaceDestroyed(SurfaceHolder holder)
                {
                    mHasSurface = false;
                }

                @Override
                public void surfaceCreated(SurfaceHolder holder)
                {
                    if (!mHasSurface)
                    {
                        mHasSurface = true;
                        initCamera(holder);
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,
                        int width, int height)
                {

                }
            });
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        mDecodeFormats = null;
        mCharacterSet = null;
        mPlayBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
        {
            mPlayBeep = false;
        }
        initBeepSound();
        mVibrate = true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        ZxingUtil.releaseWakeLock();
        if (mCaptureActivityHandler != null)
        {
            mCaptureActivityHandler.quitSynchronously();
            mCaptureActivityHandler = null;
        }
        CameraManager.get().closeDriver();
        if (mMediaPlayer != null)
        {
            try
            {
                if (mMediaPlayer.isPlaying())
                {
                    mMediaPlayer.stop();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try
        {
            mMediaPlayer.release();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mInactivityTimer.shutdown();
    }

    private void initCamera(SurfaceHolder surfaceHolder)
    {
        try
        {
            CameraManager.get().openDriver(surfaceHolder);
        }
        catch (IOException ioe)
        {
            return;
        }
        catch (RuntimeException e)
        {
            return;
        }
        if (mCaptureActivityHandler == null)
        {
            mCaptureActivityHandler = new CaptureActivityHandler(this,
                    mDecodeFormats, mCharacterSet);
        }
    }

    private void initBeepSound()
    {
        if (mPlayBeep && mMediaPlayer == null)
        {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    mp.seekTo(0);
                    mp.release();
                }
            });
            mMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.beep);
            mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
        }
    }

    public Handler getHandler()
    {
        return mCaptureActivityHandler;
    }

    public ViewfinderView getViewfinderView()
    {
        return mViewfinderView;
    }

    /**
     * 绘制查找View<BR>
     */
    public void drawViewfinder()
    {
        if (null != mViewfinderView)
        {
            mViewfinderView.drawViewfinder();
        }
    }

    private void handleDecode(Result result, Bitmap barcode)
    {
        mInactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        onResultHandler(resultString, barcode);
    }

    private void playBeepSoundAndVibrate()
    {
        if (mPlayBeep && mMediaPlayer != null)
        {
            //mMediaPlayer.reset();
            mMediaPlayer.start();
        }
        if (mVibrate)
        {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(200L);
        }
    }

    private void onResultHandler(String resultString, Bitmap bitmap)
    {
        if (TextUtils.isEmpty(resultString))
        {
            Toast.makeText(this,
                    getString(R.string.scan_failed),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RESULT, resultString);
        //bundle.putParcelable(KEY_BITMAP, bitmap);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.iv_open_light)
        {

            if (isOpenTorch)
            {
                CameraManager.get().setTorch(false);
                mIvLight.setImageResource(R.mipmap.torch_on);
                isOpenTorch = false;
            }
            else
            {
                CameraManager.get().setTorch(true);
                mIvLight.setImageResource(R.mipmap.torch_off);
                isOpenTorch = true;
            }
        }

    }

    /**
     * 二维码扫描线程<BR>
     *
     * @author archermind
     * @version [ODP Client R001C01LAI141, 2014-4-11]
     */
    private class DecodeThread extends Thread
    {
        public static final String BARCODE_BITMAP = "barcode_bitmap";

        public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

        private final ScanQRCodeActivity activity;

        private final Map<DecodeHintType, Object> hints;

        private Handler handler;

        private final CountDownLatch handlerInitLatch;

        DecodeThread(ScanQRCodeActivity activity,
                Vector<BarcodeFormat> decodeFormats, String characterSet,
                ResultPointCallback resultPointCallback)
        {

            this.activity = activity;
            handlerInitLatch = new CountDownLatch(1);
            hints = new Hashtable<DecodeHintType, Object>(3);
            if (decodeFormats == null || decodeFormats.isEmpty())
            {
                decodeFormats = new Vector<BarcodeFormat>();
                decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
                decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
                decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
            }
            hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
            if (characterSet != null)
            {
                hints.put(DecodeHintType.CHARACTER_SET, characterSet);
            }
            hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
                    resultPointCallback);
        }

        Handler getHandler()
        {
            try
            {
                handlerInitLatch.await();
            }
            catch (InterruptedException ie)
            {
                ie.printStackTrace();
            }
            return handler;
        }

        @Override
        public void run()
        {
            Looper.prepare();
            handler = new DecodeHandler(activity, hints);
            handlerInitLatch.countDown();
            Looper.loop();
        }
    }

    /**
     * 二维码处理的Handler<BR>
     *
     * @author archermind
     * @version [ODP Client R001C01LAI141, 2014-4-11]
     */
    private class DecodeHandler extends Handler
    {

        //解析
        public static final int HANDLER_WHAT_DECODE = 1;

        //结束
        public static final int HANDLER_WHAT_QUIT = 2;

        private final ScanQRCodeActivity activity;

        private final MultiFormatReader multiFormatReader;

        private boolean running = true;

        /**
         * 二维码处理的Handler
         *
         * @param activity 二维码的Actvity
         * @param hints    解析参数
         */
        public DecodeHandler(ScanQRCodeActivity activity,
                Map<DecodeHintType, Object> hints)
        {
            multiFormatReader = new MultiFormatReader();
            multiFormatReader.setHints(hints);
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message message)
        {
            if (!running)
            {
                return;
            }
            switch (message.what)
            {
                case HANDLER_WHAT_DECODE:
                    decode((byte[]) message.obj, message.arg1, message.arg2);
                break;
                case HANDLER_WHAT_QUIT:
                    running = false;

                    Looper.myLooper().quit();
                break;
            }
        }

        private void decode(byte[] data, int width, int height)
        {
            Result rawResult = null;
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    rotatedData[x * height + height - y - 1] = data[x + y
                            * width];
                }
            }
            int tmp = width;
            width = height;
            height = tmp;
            PlanarYUVLuminanceSource source = CameraManager.get()
                    .buildLuminanceSource(rotatedData, width, height);
            if (source != null)
            {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                        source));
                try
                {
                    // 预览界面最终取到的是个bitmap，然后对其进行解码
                    rawResult = multiFormatReader.decodeWithState(bitmap);
                }
                catch (ReaderException re)
                {
                    re.printStackTrace();
                }
                finally
                {
                    multiFormatReader.reset();
                }
            }

            Handler handler = activity.getHandler();
            if (rawResult != null)
            {
                // Don't log the barcode contents for security.
                if (handler != null)
                {
                    Message message = Message.obtain(handler,
                            CaptureActivityHandler.HANDLER_WHAT_DECODE_SUCCESS,
                            rawResult);
                    Bundle bundle = new Bundle();
                    bundleThumbnail(source, bundle);
                    message.setData(bundle);
                    message.sendToTarget();
                }
            }
            else
            {
                if (handler != null)
                {
                    Message message = Message.obtain(handler,
                            CaptureActivityHandler.HANDLER_WHAT_DECODE_FAILED);
                    message.sendToTarget();
                }
            }
        }

        private void bundleThumbnail(PlanarYUVLuminanceSource source,
                Bundle bundle)
        {
            int[] pixels = source.renderThumbnail();
            int width = source.getThumbnailWidth();
            int height = source.getThumbnailHeight();
            Bitmap bitmap = Bitmap.createBitmap(pixels,
                    0,
                    width,
                    width,
                    height,
                    Bitmap.Config.ARGB_8888);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
            bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width
                    / source.getWidth());
        }
    }

    /**
     * 二维码捕捉线程<BR>
     *
     * @author archermind
     * @version [ODP Client R001C01LAI141, 2014-4-11]
     */
    private class CaptureActivityHandler extends Handler
    {

        public static final int STATE_PREVIEW = 1;

        public static final int STATE_SUCCESS = 2;

        public static final int STATE_DONE = 3;

        public static final int HANDLER_WHAT_DECODE_SUCCESS = 1;

        public static final int HANDLER_WHAT_RESTART_PREVIEW = 2;

        public static final int HANDLER_WHAT_DECODE_FAILED = 3;

        public static final int HANDLER_WHAT_RETURN_SCAN_RESULT = 4;

        private final ScanQRCodeActivity activity;

        /**
         * 真正负责扫描任务的核心线程
         */
        private final DecodeThread decodeThread;

        private int state;

        /**
         * 二维码捕捉线程
         *
         * @param activity      activity对象
         * @param decodeFormats 解析参数
         * @param characterSet
         */
        public CaptureActivityHandler(ScanQRCodeActivity activity,
                Vector<BarcodeFormat> decodeFormats, String characterSet)
        {
            this.activity = activity;
            decodeThread = new DecodeThread(activity, decodeFormats,
                    characterSet, new ViewfinderResultPointCallback(
                            activity.getViewfinderView()));
            decodeThread.start();
            state = STATE_SUCCESS;
            // Start ourselves capturing previews and decoding.
            CameraManager.get().startPreview();
            restartPreviewAndDecode();
        }

        @Override
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case HANDLER_WHAT_RESTART_PREVIEW: // 准备进行下一次扫描
                    restartPreviewAndDecode();
                break;
                case CaptureActivityHandler.HANDLER_WHAT_DECODE_SUCCESS:
                    state = STATE_SUCCESS;
                    Bundle bundle = message.getData();
                    Bitmap barcode = null;
                    float scaleFactor = 1.0f;
                    if (bundle != null)
                    {
                        byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
                        if (compressedBitmap != null)
                        {
                            barcode = BitmapFactory.decodeByteArray(compressedBitmap,
                                    0,
                                    compressedBitmap.length,
                                    null);
                            // Mutable copy:
                            barcode = barcode.copy(Bitmap.Config.ARGB_8888,
                                    true);
                        }
                        scaleFactor = bundle.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);
                    }
                    activity.handleDecode((Result) message.obj, barcode);
                break;
                case HANDLER_WHAT_DECODE_FAILED:
                    // We're decoding as fast as possible, so when one decode fails,
                    // start another.
                    state = STATE_PREVIEW;
                    CameraManager.get()
                            .requestPreviewFrame(decodeThread.getHandler(),
                                    DecodeHandler.HANDLER_WHAT_DECODE);
                break;
                case HANDLER_WHAT_RETURN_SCAN_RESULT:
                    activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
                    activity.finish();
                break;
            }
        }

        public void quitSynchronously()
        {
            state = STATE_DONE;
            CameraManager.get().stopPreview();
            Message quit = Message.obtain(decodeThread.getHandler(),
                    DecodeHandler.HANDLER_WHAT_QUIT);
            quit.sendToTarget();

            try
            {
                // Wait at most half a second; should be enough time, and onPause()
                // will timeout quickly
                decodeThread.join(500L);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            // Be absolutely sure we don't send any queued up messages
            removeMessages(CaptureActivityHandler.HANDLER_WHAT_DECODE_SUCCESS);
            removeMessages(CaptureActivityHandler.HANDLER_WHAT_DECODE_FAILED);
        }

        /**
         * 完成一次扫描后，只需要再调用此方法即可
         */
        private void restartPreviewAndDecode()
        {
            if (state == STATE_SUCCESS)
            {
                state = STATE_PREVIEW;

                // 向decodeThread绑定的handler（DecodeHandler)发送解码消息
                CameraManager.get()
                        .requestPreviewFrame(decodeThread.getHandler(),
                                DecodeHandler.HANDLER_WHAT_DECODE);
                activity.drawViewfinder();
            }
        }
    }

    /**
     * 扫描器任务<BR>
     *
     * @author tintin
     */
    private class InactivityTimer
    {

        /**
         * 如果在5min内扫描器没有被使用过，则自动finish掉activity
         */
        private static final long INACTIVITY_DELAY_MS = 3 * 60 * 1000L;

        /**
         * 在本app中，此activity即为CaptureActivity
         */
        private final Activity activity;

        /**
         * 接受系统广播：手机是否连通电源
         */
        private final BroadcastReceiver powerStatusReceiver;

        private boolean registered;

        private AsyncTask<Object, Object, Object> inactivityTask;

        public InactivityTimer(Activity activity)
        {
            this.activity = activity;
            powerStatusReceiver = new PowerStatusReceiver();
            registered = false;
            onActivity();
        }

        /**
         * 首先终止之前的监控任务，然后新起一个监控任务
         */
        @SuppressLint("NewApi")
        public synchronized void onActivity()
        {
            cancel();
            inactivityTask = new InactivityAsyncTask();
            inactivityTask.execute(null, null, null);
        }

        public synchronized void onPause()
        {
            cancel();
            if (registered)
            {
                activity.unregisterReceiver(powerStatusReceiver);
                registered = false;
            }
            else
            {
                Log.e(TAG, "PowerStatusReceiver was never registered?");
            }
        }

        public synchronized void onResume()
        {
            if (registered)
            {
                Log.e(TAG, "PowerStatusReceiver was already registered?");
            }
            else
            {
                activity.registerReceiver(powerStatusReceiver,
                        new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                registered = true;
            }
            onActivity();
        }

        /**
         * 取消监控任务
         */
        private synchronized void cancel()
        {
            AsyncTask<?, ?, ?> task = inactivityTask;
            if (task != null)
            {
                task.cancel(true);
                inactivityTask = null;
            }
        }

        public void shutdown()
        {
            cancel();
        }

        /**
         * 监听是否连通电源的系统广播。如果连通电源，则停止监控任务，否则重启监控任务
         */
        private final class PowerStatusReceiver extends BroadcastReceiver
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction()))
                {
                    // 0 indicates that we're on battery
                    boolean onBatteryNow = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,
                            -1) <= 0;
                    if (onBatteryNow)
                    {
                        InactivityTimer.this.onActivity();
                    }
                    else
                    {
                        InactivityTimer.this.cancel();
                    }
                }
            }
        }

        /**
         * 该任务很简单，就是在INACTIVITY_DELAY_MS时间后终结activity
         */
        private final class InactivityAsyncTask extends
                AsyncTask<Object, Object, Object>
        {
            @Override
            protected Object doInBackground(Object... objects)
            {
                try
                {
                    Thread.sleep(INACTIVITY_DELAY_MS);
                    activity.finish();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        }

    }
}