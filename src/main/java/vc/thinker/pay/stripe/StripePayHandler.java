package vc.thinker.pay.stripe;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.Refund;
import com.stripe.model.Source;
import com.stripe.net.RequestOptions;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;

import vc.thinker.pay.PayConfigFactory;
import vc.thinker.pay.PayException;
import vc.thinker.pay.PayHandler;
import vc.thinker.pay.request.CaptureRequest;
import vc.thinker.pay.request.DelCardRequest;
import vc.thinker.pay.request.DirectPayRequest;
import vc.thinker.pay.request.FindCardRequest;
import vc.thinker.pay.response.DirectPayResponse;
import vc.thinker.pay.response.RefundResponse;
import vc.thinker.refund.bean.RefundRequest;

/**
 * 支付宝的方法处理
 * @author james
 *
 */
public class StripePayHandler extends PayHandler<StripeConfig,StripePayNotify>{
	
//	private final static Logger log=LoggerFactory.getLogger(StripePayHandler.class);
	
	public StripePayHandler(PayConfigFactory configFactory) {
		super(configFactory);
	}

	@Override
	public DirectPayResponse webPay(DirectPayRequest request) throws PayException{
		throw new PayException("还未实现");
	}
	
	@Override
	public DirectPayResponse appPay(DirectPayRequest request) throws PayException {
		StripeConfig config=getConfig(request.getConfigMark());
		Long totalFee = request.getTotalFee().setScale(2,BigDecimal.ROUND_DOWN)
				.multiply(new BigDecimal("100")).longValue();
		
        RequestOptions requestOptions = (new RequestOptionsBuilder())
		.setApiKey(config.getApiKey()).build();
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", totalFee);
		chargeMap.put("currency", request.getCurrency());
		chargeMap.put("customer", request.getOutTradeNo());
		chargeMap.put("description", request.getBody());
		if(request.getCapture() != null){
			chargeMap.put("capture", request.getCapture());
		}
		try {
		    Charge charge = Charge.create(chargeMap, requestOptions);
		    DirectPayResponse response=new DirectPayResponse(config.getChannel(),config);
		    BigDecimal payAmount=new BigDecimal(charge.getAmount()).multiply(new BigDecimal("0.01")).setScale(2,BigDecimal.ROUND_DOWN);
		    response.setTotalFee(payAmount);
		    response.setSuccess(true);
		    response.setPayOrderId(charge.getId());
		    return response;
		} catch (StripeException e) {
			throw new PayException(e);
		}
		
	}
	
	@Override
	public DirectPayResponse jsPay(DirectPayRequest request,String openid) throws PayException {
		throw new PayException("还未实现");
	}
	

	/**
	 * 退款
	 */
	@Override
	public RefundResponse refund(RefundRequest request) throws PayException {
		StripeConfig config=getConfig(request.getConfigMark());
		
        RequestOptions requestOptions = (new RequestOptionsBuilder())
		.setApiKey(config.getApiKey()).build();
		//获取退款配置
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("charge", request.getTransaction_id());
		try {
			Refund refund = Refund.create(params,requestOptions);
			RefundResponse response=new RefundResponse(config.getChannel(),config);
			response.setSuccess(true);
			response.setOutOrderId(refund.getId());
			return response;
		} catch (StripeException e) {
			throw new PayException(e);
		}
	}
	
	/**
	 * 锁定资金
	 * @param request
	 * @return
	 * @throws PayException
	 */
	public void capture(CaptureRequest request) throws PayException {
		StripeConfig config=getConfig(request.getConfigMark());
		
		Stripe.apiKey = config.getApiKey();
		try {
			Charge ch=Charge.retrieve(request.getOrderNo());
			ch.capture();
		} catch (StripeException e) {
			throw new PayException(e);
		}
	}
	
	/**
	 * 删除卡
	 * @param request
	 * @throws PayException
	 */
	public void delCard(DelCardRequest request) throws PayException {
		StripeConfig config=getConfig(request.getConfigMark());
		
		Stripe.apiKey = config.getApiKey();
		try {
			Customer customer = Customer.retrieve(request.getCusId());
			if(customer != null){
				ExternalAccount ea=customer.getSources().retrieve(request.getCard());
				if(ea != null){
					if(ea instanceof Source){
						Source s=(Source)ea;
						s.detach();
						return;
					}else if(ea instanceof Card){
						Card s=(Card)ea;
						s.delete();
						return;
					}
				}
			}
			throw new PayException("未找到的用户，删除卡失败");
		} catch (StripeException e) {
			throw new PayException(e);
		}
	}

	/**
	 * 获取用户信用卡数量
	 * @param request
	 * @return
	 * @throws PayException
	 */
	public int countByCusId(FindCardRequest request) throws PayException{
		try{
			StripeConfig config=getConfig(request.getConfigMark());
			Stripe.apiKey = config.getApiKey();
			Customer customer = Customer.retrieve(request.getCusId());
			if(null != customer){
				Map<String, Object> cardParams = new HashMap<String, Object>();
				cardParams.put("object", "card");
				List<ExternalAccount> list = customer.getSources().all(cardParams).getData();
				return list.isEmpty()? 0:list.size();
			}else {
				throw new PayException("未找到的用户,查询信用卡数量异常");
			}
		}catch (StripeException e) {
			throw new PayException(e);
		}
	}
	@Override
	public boolean verifyNotify(StripePayNotify payNotify) throws PayException {
		throw new PayException("该支付不需要");
	}

	@Override
	public StripePayNotify getPayNotify(HttpServletRequest request) throws PayException {
		throw new PayException("该支付不需要");
	}

}
