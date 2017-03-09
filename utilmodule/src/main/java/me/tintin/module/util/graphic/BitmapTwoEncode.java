package me.tintin.module.util.graphic;

import java.util.EnumMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 创建二维码<BR>
 * 通过String返回二维码
 * @author archermind
 * @version [ODP Client R001C01LAI141, 2015-12-15]
 */
public class BitmapTwoEncode
{
    /**
     * http地址
     */
    private static final String HTTP = "http://g.10086.cn/go/hnwkhd?";

    /**
     * 创建二维码，接受一个字符串参数，返回bitmap，如果没有创建成功，返回null
     *
     * @param strInput String
     * @return bitmap Bitmap
     */
    public static Bitmap createBarcode(Context context, String strInput,Bitmap logo)
    {
//        strInput = HTTP + strInput;
        Map<EncodeHintType, Object> hints = null;
        hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 2);
        BitMatrix result;
        try
        {
            // 编码的内容，编码的格式（二维码），宽度，高度，设置文本的编码
            result = new MultiFormatWriter().encode(strInput,
                    BarcodeFormat.QR_CODE,
                    400,
                    400,
                    hints);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++)
        {
            int offset = y * width;
            for (int x = 0; x < width; x++)
            {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK
                        : Color.WHITE;
            }
        }

        // 創建bitmap
        Bitmap bitmap = Bitmap.createBitmap(width,
                height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        Bitmap mobileLogo = BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.ic_launcher);
        createBarcodeWithLogo(bitmap, logo, 8);
        return bitmap;
    }

    /**
    * 创建带有logo的二维码<BR>
    * 首先三个参数，第一个是已经创建好的二维码，第二个是logo文件，第三个是缩
    放比例
    * （注）缩放比例：logo实际显示大小 = 创建好的二维码宽（高） / 缩放比例;
    * 将logo文件覆盖在二维码上面
    * @param bitmapRDcode
    * @param bitmapLogo
    * @param fZoomRate
    * @return
    */
    private static Bitmap createBarcodeWithLogo(Bitmap bitmapRDcode,
            Bitmap bitmapLogo, int fZoomRate)
    {
        if (null == bitmapRDcode || null == bitmapLogo)
        {
            return null;
        }
        //获取生成好的二维码宽度，高度
        int rdcodeWidth = bitmapRDcode.getWidth();
        int rdcodeHeight = bitmapRDcode.getHeight();

        //获取最佳logo的宽度，高度
        int bestLogoWidth = (int) rdcodeWidth / fZoomRate;
        //获得原始logo的宽度，高度
        int logoWidth = bitmapLogo.getWidth();
        int logoHeight = bitmapLogo.getHeight();
        int bestLogoHeight = (int) (bestLogoWidth * logoHeight / logoWidth);

        //缩放logo
        Matrix matrix = new Matrix();
        matrix.postScale((float) bestLogoWidth / logoWidth,
                (float) bestLogoHeight / logoHeight);
        Bitmap newLogoBmp = Bitmap.createBitmap(bitmapLogo,
                0,
                0,
                logoWidth,
                logoHeight,
                matrix,
                true);

        if (!bitmapRDcode.isMutable())
        {
            //设置图片背景设置为透明
            bitmapRDcode = bitmapRDcode.copy(Bitmap.Config.ARGB_8888, true);
        }
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmapRDcode);
        //叠加新图b2
        int x = (rdcodeWidth - bestLogoWidth) / 2;
        int y = (rdcodeHeight - bestLogoHeight) / 2;
        canvas.drawBitmap(newLogoBmp, x, y, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bitmapRDcode;
    }
}
