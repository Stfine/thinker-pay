package vc.thinker.pay.payfast;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import vc.thinker.pay.PayChannel;
import vc.thinker.pay.PayConfigFactory;
import vc.thinker.pay.PayException;
import vc.thinker.pay.PayHandler;
import vc.thinker.pay.request.DirectPayRequest;
import vc.thinker.pay.response.DirectPayResponse;
import vc.thinker.pay.response.RefundResponse;
import vc.thinker.pay.weixin.utils.MD5;
import vc.thinker.pay.weixin.utils.MapUtil;
import vc.thinker.pay.weixin.utils.Signature;
import vc.thinker.refund.bean.RefundRequest;
import vc.thinker.weixin.kit.HttpKit;

public class PayFastPayHandler extends PayHandler<PayFastConfig, PayFastNotify> {

	private static Logger logger = LoggerFactory.getLogger(PayFastPayHandler.class);
	
	private static String pingUrl = "https://api.payfast.co.za/ping";
	
	private static String  apiPayUrl = "https://api.payfast.co.za/subscriptions/";

	public PayFastPayHandler(PayConfigFactory configFactory) {
		super(configFactory);
	}

	@Override
	public DirectPayResponse webPay(DirectPayRequest request) throws PayException {

		PayFastConfig config = getConfig(request.getConfigMark());
		// 拼接参数,生成签名
		Map<String, String> param = Maps.newLinkedHashMap();
		param.put("merchant_id", config.getMerchant_id());
		param.put("merchant_key", config.getMerchant_key());
		param.put("return_url", request.getReturnUrl());
		param.put("cancel_url", request.getCancelUrl());
		param.put("notify_url", request.getNotifyUrl());
		param.put("cell_number", request.getMobile());
		param.put("m_payment_id", request.getOutTradeNo());
		param.put("amount", String.valueOf(request.getTotalFee()));
		param.put("item_name", request.getSubject());
		param.put("item_description", request.getBody());
		param.put("custom_str1", request.getReqAttach());
		// 信用卡支付
		param.put("payment_method", "cc");
		// 免密支付(第二种)
		param.put("subscription_type", "2");

		// 生成签名
		String signature = Signature.getPayFastSignature(param, config.getPassphrase());

		DirectPayResponse response = new DirectPayResponse(PayChannel.PAYFAST, config);

		// 组装返回报文
		PayFastPayPackage payPackage = new PayFastPayPackage();
		payPackage.setMerchant_id(config.getMerchant_id());
		payPackage.setMerchant_key(config.getMerchant_key());
		payPackage.setReturn_url(request.getReturnUrl());
		payPackage.setCancel_url(request.getCancelUrl());
		payPackage.setNotify_url(request.getNotifyUrl());
		payPackage.setCell_number(request.getMobile());
		payPackage.setM_payment_id(request.getOutTradeNo());
		payPackage.setAmount(String.valueOf(request.getTotalFee()));
		payPackage.setItem_name(request.getSubject());
		payPackage.setItem_description(request.getBody());
		payPackage.setCustom_str1(request.getReqAttach());
		payPackage.setPayment_method("cc");
		payPackage.setSubscription_type("2");
		payPackage.setSignature(signature);

		response.setPayPackage(payPackage);

		return response;
	}
	
	
	/**
	 * Ad Hoc Payments
	 */
	public DirectPayResponse apiPay(DirectPayRequest request) {
		
		PayFastConfig config = getConfig(request.getConfigMark());
		
		DirectPayResponse response = new DirectPayResponse(PayChannel.PAYFAST, config);
		
		if(!isCanPay(config)) {
			// 不能调用
			response.setSuccess(false);
			return response;
		}
		
		
		// 拼接接口地址
		String url = apiPayUrl + request.getToken()+"/adhoc";
		
		String time = get8601SimpleString();
		
		// 拼接字符串
		Map<String, String> paramMap = Maps.newLinkedHashMap();
		paramMap.put("merchant-id", config.getMerchant_id());
		paramMap.put("version", "v1");
		paramMap.put("timestamp",time );
		paramMap.put("passphrase", config.getPassphrase());
		
		Integer totalFee = request.getTotalFee().multiply(new BigDecimal("100")).intValue();
		paramMap.put("amount", String.valueOf(totalFee));
		paramMap.put("item_name", request.getSubject());
		paramMap.put("itn", "false");
		
		String paramString = apiMapToString(MapUtil.order(paramMap));
		
		logger.info("paramString:[{}]",paramString);
		
		String signature = MD5.MD5Encode(paramString);
		
		// 请求头
		Map<String, String> headers = Maps.newLinkedHashMap();
		headers.put("merchant-id", config.getMerchant_id());
		headers.put("version", "v1");
		headers.put("timestamp", time);
		headers.put("signature", signature);
		
		// 请求体
		StringBuilder sb = new StringBuilder();
		sb.append("amount").append("=").append(String.valueOf(totalFee));
		sb.append("&").append("item_name").append("=").append(request.getSubject());
		sb.append("&").append("itn").append("=").append("false");
		
		logger.info("Request Body:[{}]",sb.toString());
		
		String payResp = HttpKit.post(url, sb.toString(), headers);
		
		logger.info("Payfast Api Pay Result:[{}] " , payResp);
		
		ApiPayFast payFast = JSONObject.parseObject(payResp, ApiPayFast.class);
		
		// 支付成功
		if("200".equals(payFast.getCode()) && "success".equals(payFast.getStatus())) {
			response.setSuccess(true);
			return response;
		}
		response.setSuccess(false);
		
		return response;
	}

	/**
	 * 校验是否可以发起支付
	 */
	public Boolean isCanPay(PayFastConfig config) {
		
		Map<String, String> map = Maps.newLinkedHashMap();
		map.put("merchant-id", config.getMerchant_id());
		map.put("version", "v1");
		map.put("timestamp", get8601TimeString());
		map.put("passphrase", config.getPassphrase());
		
		String paramString = mapJoinNoReplace(MapUtil.order(map), true, true);
		
		String signature = MD5.MD5Encode(paramString);
		
		map.put("signature", signature);
		
		map.remove("passphrase");
		
		String resp = HttpKit.get(pingUrl, null, map);
		
		resp = StringUtils.isNotBlank(resp)?resp.replace("\n", ""):resp;
		
		logger.info("Ping Response:[{}]",resp);
		
		Boolean result = "\"API V1\"".equals(resp);
		
		logger.info("Ping Result:[{}]",result);
		
		return result;
		
	}
	
	@Override
	public DirectPayResponse appPay(DirectPayRequest request) throws PayException {
		return null;
	}

	@Override
	public DirectPayResponse jsPay(DirectPayRequest request, String openid) throws PayException {
		return null;
	}

	@Override
	public RefundResponse refund(RefundRequest request) throws PayException {
		return null;
	}

	/**
	 *签名验证
	 */
	@Override
	public boolean verifyNotify(PayFastNotify payNotify) {
		// 获取盐值
		PayFastConfig config = getConfig("payfast");
		
		// 拼接字符串
		String paramString = payNotify.getParams();
		paramString = paramString + "&passphrase=" + config.getPassphrase();

		return payNotify.getSignature().equals(MD5.MD5Encode(paramString)) ? true : false;
	}


	
	@SuppressWarnings("rawtypes")
	@Override
	public PayFastNotify getPayNotify(HttpServletRequest request) throws PayException {

		PayFastNotify notify = new PayFastNotify();
		notify.setmPaymentId(request.getParameter("m_payment_id"));
		notify.setPfPaymentId(request.getParameter("pf_payment_id"));
		notify.setPaymentStaus(request.getParameter("payment_status"));
		notify.setItemName(request.getParameter("item_name"));
		notify.setItemDescription(request.getParameter("item_description"));
		notify.setAmountGross(request.getParameter("amount_gross"));
		notify.setAmountFee(request.getParameter("amount_fee"));
		notify.setAmountNet(request.getParameter("amount_net"));
		notify.setCcustomStr1(request.getParameter("custom_str1"));
		notify.setNameFirst(request.getParameter("name_first"));
		notify.setNameLast(request.getParameter("name_last"));
		notify.setEmailAddress(request.getParameter("email_address"));
		notify.setMerchantId(request.getParameter("merchant_id"));
		notify.setToken(request.getParameter("token"));
		notify.setSignature(request.getParameter("signature"));
		
		Map<String, String> params = Maps.newLinkedHashMap();
		params.put("m_payment_id", request.getParameter("m_payment_id"));
		params.put("pf_payment_id", request.getParameter("pf_payment_id"));
		params.put("payment_status", request.getParameter("payment_status"));
		params.put("item_name", request.getParameter("item_name"));
		params.put("item_description", request.getParameter("item_description"));
		params.put("amount_gross", request.getParameter("amount_gross"));
		params.put("amount_fee", request.getParameter("amount_fee"));
		params.put("amount_net", request.getParameter("amount_net"));
		params.put("custom_str1", request.getParameter("custom_str1"));
		params.put("custom_str2", request.getParameter("custom_str2"));
		params.put("custom_str3", request.getParameter("custom_str3"));
		params.put("custom_str4", request.getParameter("custom_str4"));
		params.put("custom_str5", request.getParameter("custom_str5"));
		params.put("custom_int1", request.getParameter("custom_int1"));
		params.put("custom_int2", request.getParameter("custom_int2"));
		params.put("custom_int3", request.getParameter("custom_int3"));
		params.put("custom_int4", request.getParameter("custom_int4"));
		params.put("custom_int5", request.getParameter("custom_int5"));
		params.put("name_first", request.getParameter("name_first"));
		params.put("name_last", request.getParameter("name_last"));
		params.put("email_address", request.getParameter("email_address"));
		params.put("merchant_id", request.getParameter("merchant_id"));
		params.put("token", request.getParameter("token"));
		params.put("billing_date", request.getParameter("billing_date"));
		
		notify.setParams(mapJoinNoReplace(params,true,true));

		logger.info("notify:[{}]", notify.toString());

		return notify;
	}
	
	
	public static String mapJoinNoReplace(Map<String, String> map, Boolean keyLower, Boolean urlEncode) {

		StringBuilder stringBuilder = new StringBuilder();
		for (String key : map.keySet()) {
			if (map.get(key) != null && !"".equals(map.get(key))) {
				try {
					String temp = (key.endsWith("_") && key.length() > 1) ? key.substring(0, key.length() - 1) : key;
					stringBuilder.append(keyLower ? temp.toLowerCase() : temp).append("=")
							.append(urlEncode ? URLEncoder.encode(map.get(key), "utf-8") : map.get(key)).append("&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else {
				stringBuilder.append(key).append("=").append("&");
			}
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		return stringBuilder.toString();
	}
	
	public static String get8601TimeString() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ssZZ";
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	public static String get8601SimpleString() {
		 String pattern = "yyyy-MM-dd'T'HH:mm:ss";
			return DateFormatUtils.format(new Date(), pattern);
	}
	
	public static String apiMapToString(Map<String, String> map) {
		try {
			StringBuilder sb = new StringBuilder();
			for (String key : map.keySet()) {
				if(StringUtils.isNotBlank(map.get(key))) {
					sb.append(key).append("=");
					sb.append(URLEncoder.encode(map.get(key), "utf-8")).append("&");
				}
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			return sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
