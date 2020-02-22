package vc.thinker.pay;

/**
 * 支付渠道
 * @author james
 *
 */
public enum PayChannel {
	
	ALIPAY, //支付宝
	WEIXIN,//微信
	STRIPE,
	PAYFAST,
	CASHFREE,
	FONDY,
	FONDYAPPLEPAY;
	
    /** 从名称实例化
     */
    public static PayChannel forName(String name)
    {
        return(
            Enum.valueOf(PayChannel.class, name)
        );
    }
}
