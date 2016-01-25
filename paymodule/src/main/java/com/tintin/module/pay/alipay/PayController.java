package com.tintin.module.pay.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.doron.duoduo.library.utils.L;
import com.doron.xueche.stu.out.JSOut;

public class PayController
{
    private static final String TAG = PayController.class.getSimpleName();

    private static PayController instance;

    // 商户PID
    //    public static final String PARTNER = "2088911049798088";

    // 商户收款账号
    //    public static final String SELLER = "syswsjx@163.com";

    //商户网站
    //    private static final String notifyUrl = "http://218.2.190.122:18080/isw/portal/alipay/notify.do";

    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANwXot4o1/lHuC9fGbwp2Z4BbM5thONi1ojzcsuMzQLDOawzH4KAT/aMZYl/19OeWGdiWxLMLajQ0PIgHcpVlIP6/0ZqbDrIh9S0AVDAwbWTGufoUM7UMBNCUmU2TnklKqMBcN+kSrRU8ZYmKdjKHgh3paHuYC6G5AtMN12Jm8lTAgMBAAECgYBj75+WfLlEiZpKeEY5OE+F/WOof/Y9QZ3kZZaUcoSMYpUHadc+7IBilPFt88zOXDIMD48HBAsfgmG973NXcnCSfY7dBp6Qc4QzVonJOaFaooWGM7nFlOm7kEiY9tNRHWTjEgWqGeeQEqh/3rHY8pv4b7hv+T4nOKCb4u3KVuwfUQJBAO6Vt5Pwfsfg8txKSRLQeFJ022SeOztLwHRbvkESvoX1UEgyOu2CoTLh3ul3CSBm40lvAetBUGQ7d3lnZ/KZr8UCQQDsKFyIlJ8YwEmM/8s94NKu2SSS+eqVBsyRQuD6zjMWoaCjqakJSNuXiQtK7gkS2Z2wsSo59AoSR8ZtwBFadU43AkEA3UVTbcw0+NFiy/XFUcKwZODl/KpXisXVptTTXWyx8HE0VNDpIA/vys36vBHfEAL8NsXSRMpdcahJRPonSLNKdQJAIbA8SwuQipJbq66Nyrz4sRKu4fye1zWKFyrIN18U8KSL6uz3/SgUk1BsePrt9m9uzFbppCzJBwSQLPXaQ+I6DwJAa/gLNTr1nQhIUd9yxydnnwVUvK0XYymo3D8ZkMv2e3Qxg3q1Nz/L1P4JQdmQ7AMMJWWi8h8vKvNNfgsd6kfTqQ==";

    public static final int SDK_PAY_FLAG = 20001;

    public static final int SDK_CHECK_FLAG = 20002;

    private JSOut mJsOut;

    private static Activity mActivity;

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {

                case SDK_CHECK_FLAG:
                {
                    Toast.makeText(mActivity,
                            "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                break;
            }
        };
    };

    public static synchronized PayController getInstance(Activity activity,
            Handler handler)
    {
        if (instance == null)
        {
            instance = new PayController();
        }

        mActivity = activity;
        mHandler = handler;
        return instance;
    }

    public PayController()
    {

    }

    /**
     * call alipay sdk reqPay. 调用SDK支付
     *
     */
    public void reqAliPay(JSOut jsOut)

    {
        mJsOut = jsOut;

        if (TextUtils.isEmpty(jsOut.getProductDescription()))
        {
            jsOut.setProductDescription("学车费用");
        }
        mJsOut.setAliPayPrivateKey(RSA_PRIVATE);

        L.d(TAG, "pay" + mJsOut.toString());
        if (TextUtils.isEmpty(mJsOut.getAliPayPartner())
                || TextUtils.isEmpty(mJsOut.getAliPayPrivateKey())
                || TextUtils.isEmpty(mJsOut.getAliPaySeller()))
        {
            new AlertDialog.Builder(mActivity).setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(
                                        DialogInterface dialoginterface, int i)
                                {
                                    return;
                                }
                            })
                    .show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo(jsOut);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try
        {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable()
        {

            @Override
            public void run()
            {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = mHandler.obtainMessage();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     *
     */
    private void check()
    {
        Runnable checkRunnable = new Runnable()
        {

            @Override
            public void run()
            {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(mActivity);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion()
    {
        PayTask payTask = new PayTask(mActivity);
        String version = payTask.getVersion();
        Toast.makeText(mActivity, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(JSOut jsOut)
    {

        String tradeNo = jsOut.getTradeNO();
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + jsOut.getAliPayPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + jsOut.getAliPaySeller() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + jsOut.getTradeNO() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + jsOut.getProductName() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + jsOut.getProductDescription() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + jsOut.getAmount() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + jsOut.getNotifyURL() + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"5m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        Log.d("orderinfo", orderInfo);
        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     * 
     * @param content
     *            待签名订单信息
     */
    public String sign(String content)
    {
        return SignUtils.sign(content, mJsOut.getAliPayPrivateKey());
    }

    /**
     * get the sign type we use. 获取签名方式
     * 
     */
    public String getSignType()
    {
        return "sign_type=\"RSA\"";
    }

}
