package vc.thinker.pay.alipay;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import vc.thinker.pay.PayConfigFactory;
import vc.thinker.pay.PayException;
import vc.thinker.pay.PayHandler;
import vc.thinker.pay.request.DirectPayRequest;
import vc.thinker.pay.response.DirectPayResponse;
import vc.thinker.pay.response.RefundResponse;
import vc.thinker.refund.bean.RefundPackage;
import vc.thinker.refund.bean.RefundRequest;
import vc.thinker.refund.utils.RefundUtils;

/**
 * 支付宝的方法处理
 * @author james
 *
 */
public class AlipayPayHandler extends PayHandler<AlipayConfig,AlipayPayNotify>{
	
	/**
	 * 自定义参数
	 */
	public static final String CUSTOM_PARAM_KEY="custom_param";
	
	private static final String charset="utf-8";
	
	private final static Logger log=LoggerFactory.getLogger(AlipayPayHandler.class);
	
	public AlipayPayHandler(PayConfigFactory configFactory) {
		super(configFactory);
	}

	@Override
	public DirectPayResponse webPay(DirectPayRequest request) throws PayException{
		
		AlipayConfig config=getConfig(request.getConfigMark());
		
	       //实例化客户端
        AlipayClient alipayClient = getApipayClient(config);
        
        BigDecimal totalFee=request.getTotalFee().setScale(2,BigDecimal.ROUND_DOWN);
        
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(request.getReturnUrl());
        alipayRequest.setNotifyUrl(request.getNotifyUrl());
        
        AlipayTradePagePayModel model=new AlipayTradePagePayModel();
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setOutTradeNo(request.getOutTradeNo());
        model.setSubject(request.getSubject());
        model.setBody(request.getBody());
        model.setTotalAmount(totalFee.toString());
        model.setPassbackParams(URLEncoder.encode(getAttachInput(request, config.getMark())));; 
        alipayRequest.setBizModel(model);
        
        
	 	DirectPayResponse response=new DirectPayResponse(config.getChannel(),config);
	 	response.setSuccess(true);
	 	response.setTotalFee(request.getTotalFee());
	 	try {
	 		AlipayTradePagePayResponse resp=alipayClient.pageExecute(alipayRequest);
	 		if(resp.isSuccess()){
	 			response.setContent(resp.getBody());
	 			return response;
	 		}else{
	 			throw new PayException("创建支付宝web支付失败，body="+resp.getBody());
	 		}
		} catch (AlipayApiException e) {
			throw new PayException(e);
		}
	}

	@Override
	public AlipayPayNotify getPayNotify(HttpServletRequest request) {
		
		try {
			String passback_params=request.getParameter("passback_params");
			
			//如果api中没有 extra_common_param，使用自定义参数取（ps:移动支付没有）
			if(passback_params == null){
				passback_params=request.getParameter(CUSTOM_PARAM_KEY);
			}
			passback_params=URLDecoder.decode(passback_params);
			Map<String, String> attachMap=getAttachOut(passback_params);
		
			AlipayPayNotify payNotify=new AlipayPayNotify();
			payNotify.setBody(request.getParameter("body"));
			payNotify.setOutTradeNo(request.getParameter("out_trade_no"));
			payNotify.setConfigMark(attachMap.get(ATTACH_KEY_CONFIG_MARK));
			payNotify.setReqAttach(attachMap.get(ATTACH_KEY_REQ_ATTACH));
			payNotify.setSubject(request.getParameter("subject"));
			payNotify.setTotalFee(request.getParameter("total_amount"));
			payNotify.setTradeNo(request.getParameter("trade_no"));
			payNotify.setTradeStatus(request.getParameter("trade_status"));
			payNotify.setNotifyId(request.getParameter("notify_id"));
			payNotify.setSign(request.getParameter("sign"));
			
			//取出所有参数
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			payNotify.setParams(params);
			
			return payNotify;
		} catch (IOException e) {
			log.error("",e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean verifyNotify(AlipayPayNotify payNotify) {
		
		AlipayConfig config=getConfig(payNotify.getConfigMark());
		//调用SDK验证签名
		try {
			return  AlipaySignature.rsaCheckV1(payNotify.getParams(), config.getAppPublicKey(),
					charset, "RSA2");
		} catch (AlipayApiException e) {
			log.error("支付验证异常");
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public DirectPayResponse appPay(DirectPayRequest request) throws PayException {
		
		AlipayConfig config=getConfig(request.getConfigMark());
		
        //实例化客户端
        AlipayClient alipayClient = getApipayClient(config);
        
        BigDecimal totalFee=new BigDecimal(String.valueOf(request.getTotalFee())).setScale(2,BigDecimal.ROUND_DOWN);
       
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
        alipayRequest.setNotifyUrl(request.getNotifyUrl());
        
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(request.getSubject());
        model.setOutTradeNo(request.getOutTradeNo());
        model.setBody(request.getBody());
        model.setTotalAmount(totalFee.toString());
        model.setTimeoutExpress("30m");
        model.setPassbackParams(URLEncoder.encode(getAttachInput(request, config.getMark())));; 
        alipayRequest.setBizModel(model);

        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse alipayResponse = alipayClient.sdkExecute(alipayRequest);
            DirectPayResponse response=new DirectPayResponse(config.getChannel(),config);
            response.setSuccess(true);
            response.setTotalFee(request.getTotalFee());
            response.setContent(alipayResponse.getBody());
            return response;
         } catch (AlipayApiException e) {
        	 log.error("支付宝调用失败",e);
             throw new PayException(e);
         }
	}

	@Override
	public DirectPayResponse jsPay(DirectPayRequest request,String openId) throws PayException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 支付宝退款
	 */
	public RefundResponse refund(RefundRequest request) throws PayException {
		
		RefundPackage refundPackage = new RefundPackage();
		
		AlipayConfig config = getConfig(request.getConfigMark());
		
		refundPackage.setAppid(config.getAppId());
		
		refundPackage.setTrade_no(request.getTransaction_id());
		refundPackage.setRefund_amount(request.getRefund_amount());
		refundPackage.setApp_private_key(config.getAppPrivateKey());
		refundPackage.setApp_public_key(config.getAppPublicKey());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		refundPackage.setTimestamp(sdf.format(new Date()));
		
        //实例化客户端
        AlipayClient alipayClient = getApipayClient(config);
		
		AlipayTradeRefundResponse aliResponse = RefundUtils.aliRefund(alipayClient,refundPackage, config.getPartner(), config.getAlipayPublicKey());
		
		RefundResponse response=new RefundResponse(config.getChannel(),config);
		if(aliResponse.isSuccess()){
			response.setSuccess(true);
		}else{
			response.setSuccess(false);
			response.setErrorCode(aliResponse.getSubCode());
			response.setMsg(aliResponse.getSubMsg());
		}
		return response;
	}

	public Map<String, String> getPublieKey() throws PayException {
		return null;
	}
	
	private AlipayClient getApipayClient(AlipayConfig config){
		return new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", 
 				config.getAppId(), 
 				config.getAppPrivateKey() ,
 				"json", 
 				charset,
 				config.getAppPublicKey(), 
 				"RSA2");
	}

}
