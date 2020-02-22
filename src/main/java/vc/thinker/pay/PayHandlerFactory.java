package vc.thinker.pay;


import vc.thinker.pay.alipay.AlipayPayHandler;
import vc.thinker.pay.cashfree.CashfreePayHandler;
import vc.thinker.pay.fondy.FondyPayHandler;
import vc.thinker.pay.payfast.PayFastPayHandler;
import vc.thinker.pay.stripe.StripePayHandler;
import vc.thinker.pay.weixin.WeixinPayHandler;
import vc.thinker.pay.weixin.WeixinPayMchHandler;

/**
 * 支付处理类工厂
 * @author james
 *
 * @param <T>
 */
public class PayHandlerFactory{
	
	private PayConfigFactory configFactory;
	
	private AlipayPayHandler alipayPayHandler;
	
	private WeixinPayHandler weixinPayHandler;
	
	private WeixinPayMchHandler weixinPayMchHandler;
	
	private StripePayHandler stripePayHandler;
	
	private PayFastPayHandler payFastPayHandler;
	
	private CashfreePayHandler cashfreePayHandler;
	
	private FondyPayHandler fondyPayHandler;
	
	public PayHandlerFactory(PayConfigFactory configFactory){
		this.configFactory=configFactory;
		alipayPayHandler=new AlipayPayHandler(configFactory);
		weixinPayHandler=new WeixinPayHandler(configFactory);
		weixinPayMchHandler=new WeixinPayMchHandler(configFactory);
		stripePayHandler=new StripePayHandler(configFactory);
		payFastPayHandler = new PayFastPayHandler(configFactory);
		cashfreePayHandler = new CashfreePayHandler(configFactory);
		fondyPayHandler = new FondyPayHandler(configFactory);
	}
	
	public PayHandler getPayHandler(String mark){
		PayConfig config=configFactory.getPayConfig(mark);
		return getPayHandler(config.getChannel());
	}
	
	public PayHandler getPayHandler(PayRequest request){
		return getPayHandler(request.getConfigMark());
	}
	
	public <T extends PayHandler> T getPayHandler(PayChannel channel){
		switch (channel) {
		case WEIXIN:
			return (T) weixinPayHandler;
		case ALIPAY:
			return (T) alipayPayHandler;
		case STRIPE:
			return (T) stripePayHandler;
		case PAYFAST:
			return (T) payFastPayHandler;
		case CASHFREE:
			return (T) cashfreePayHandler;
		case FONDY:
			return (T) fondyPayHandler;
		}
			
		return null;
	}
	
	public <T extends PayMchHandler> T getPayMchHandler(PayChannel channel){
		switch (channel) {
		case WEIXIN:
			return (T) weixinPayMchHandler;
		}
		return null;
	}
}
