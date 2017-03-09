package me.tintin.module.pay.ali;

import java.io.Serializable;

/**
 * [一句话功能简述] JS返回数据
 * [功能详细描述]
 *
 * @author Administrator
 * @version [DoronApp, 2015/12/7 15:45]
 */
public class PayMentInfo implements Serializable
{



    //1:支付宝 2:微信 3
    private String payType;

    //订单ID
    private String tradeNO;

    //商品标题
    private String productName;

    //端口描述
    private String productDescription;

    //端口价格
    private String amount;

    // RSA(暂时不传，Native写死)
    private String AliPayPrivateKey;

    //商家的PID
    private String AliPayPartner;

    //商家的收帐帐号
    private String AliPaySeller;

    //支付结果的回调URL(暂时不传，Native写死)
    private String notifyURL;





    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getTradeNO()
    {
        return tradeNO;
    }

    public void setTradeNO(String tradeNO)
    {
        this.tradeNO = tradeNO;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductDescription()
    {
        return productDescription;
    }

    public void setProductDescription(String productDescription)
    {
        this.productDescription = productDescription;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getAliPayPrivateKey()
    {
        return AliPayPrivateKey;
    }

    public void setAliPayPrivateKey(String aliPayPrivateKey)
    {
        AliPayPrivateKey = aliPayPrivateKey;
    }

    public String getAliPayPartner()
    {
        return AliPayPartner;
    }

    public void setAliPayPartner(String aliPayPartner)
    {
        AliPayPartner = aliPayPartner;
    }

    public String getNotifyURL()
    {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL)
    {
        this.notifyURL = notifyURL;
    }

    public String getAliPaySeller()
    {
        return AliPaySeller;
    }

    public void setAliPaySeller(String aliPaySeller)
    {
        AliPaySeller = aliPaySeller;
    }

    @Override
    public String toString() {
        return "PayMentInfo{" +
                "payType='" + payType + '\'' +
                ", tradeNO='" + tradeNO + '\'' +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", amount='" + amount + '\'' +
                ", AliPayPrivateKey='" + AliPayPrivateKey + '\'' +
                ", AliPayPartner='" + AliPayPartner + '\'' +
                ", AliPaySeller='" + AliPaySeller + '\'' +
                ", notifyURL='" + notifyURL + '\'' +
                '}';
    }
}
