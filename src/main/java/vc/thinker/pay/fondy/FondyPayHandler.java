package vc.thinker.pay.fondy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import vc.thinker.pay.PayConfigFactory;
import vc.thinker.pay.PayException;
import vc.thinker.pay.PayHandler;
import vc.thinker.pay.request.DirectPayRequest;
import vc.thinker.pay.response.DirectPayResponse;
import vc.thinker.pay.response.RefundResponse;
import vc.thinker.pay.tenpay.util.Sha1Util;
import vc.thinker.refund.bean.RefundRequest;

/**
 *
 * @author ZhangGaoXiang
 * @time Dec 14, 20194:59:37 PM
 */
public class FondyPayHandler extends PayHandler<FondyConfig, FondyNotify> {

	private static final Logger logger = LoggerFactory.getLogger(FondyPayHandler.class);

	private static final String url = "https://api.fondy.eu/api/checkout/url";
	
	private static final String tokenPayUrl = "https://api.fondy.eu/api/recurring";
	
	private static final String refund = "https://api.fondy.eu/api/reverse/order_id";
	

	public FondyPayHandler(PayConfigFactory configFactory) {
		super(configFactory);
	}

	@Override
	public DirectPayResponse webPay(DirectPayRequest request) throws PayException {
		return null;
	}

	@Override
	public DirectPayResponse appPay(DirectPayRequest request) throws PayException {
		FondyConfig config = getConfig(request.getConfigMark());
		DirectPayResponse response = new DirectPayResponse(config.getChannel(), config);
		response.setSuccess(true);
		response.setPaySuccessful(false);

		Map<String, String> param = new LinkedHashMap<String, String>();
		BigDecimal multiply = request.getTotalFee().multiply(new BigDecimal("100"));
		param.put("amount", String.valueOf(multiply.intValue()));
		param.put("currency", request.getCurrency());
		param.put("merchant_id", config.getMerchant_id());
		param.put("order_desc", request.getSubject());
		param.put("order_id", request.getOutTradeNo());
		param.put("response_url", request.getNotifyUrl());
		param.put("server_callback_url", request.getReturnUrl());
		param.put("required_rectoken", "Y");
		
		Map<String, String> orderMap = mapOrder(param);

		logger.info("param:{}",JSON.toJSONString(orderMap));
		
		param.put("signature", getSignature(orderMap, config.getPrimaryKey()));

		JSONObject object = new JSONObject();
		object.put("request", param);

		String doPost = HttpClientUtil.doPost(url, object.toString());
		if (StringUtils.isBlank(doPost)) {
			response.setSuccess(false);
			response.setMsg("连接支付网关超时");
			return response;
		}

		JSONObject jsonObject = JSON.parseObject(doPost).getJSONObject("response");
		if ("success".equals(jsonObject.getString("response_status"))) {
			response.setContent(jsonObject.getString("checkout_url"));
			return response;
		}

		response.setSuccess(false);
		response.setMsg(jsonObject.getString("error_message"));
		return response;
	}
	

	public DirectPayResponse rectokenPay(DirectPayRequest request) throws PayException {
		FondyConfig config = getConfig(request.getConfigMark());
		DirectPayResponse response = new DirectPayResponse(config.getChannel(), config);
		response.setSuccess(true);
		response.setPaySuccessful(false);
		
		Map<String, String> param = new LinkedHashMap<String, String>();
		BigDecimal multiply = request.getTotalFee().multiply(new BigDecimal("100"));
		
		param.put("order_id", request.getOutTradeNo());
		param.put("order_desc", request.getSubject());
		param.put("currency", request.getCurrency());
		param.put("amount", String.valueOf(multiply.intValue()));
		param.put("merchant_id", config.getMerchant_id());
		param.put("rectoken", request.getToken());
		
		Map<String, String> orderMap = mapOrder(param);
		
		logger.info("param:{}",JSON.toJSONString(orderMap));
		
		param.put("signature", getSignature(orderMap, config.getPrimaryKey()));
		
		JSONObject object = new JSONObject();
		object.put("request", param);
		
		String doPost = HttpClientUtil.doPost(tokenPayUrl, object.toString());
		
		logger.info("rectokenPay:[{}]",doPost);
		
		if (StringUtils.isBlank(doPost)) {
			response.setSuccess(false);
			response.setMsg("连接支付网关超时");
			return response;
		}
		
		JSONObject jsonObject = JSON.parseObject(doPost).getJSONObject("response");
		if ("approved".equals(jsonObject.getString("order_status"))) {
			response.setPaySuccessful(true);
			response.setPayOrderId(jsonObject.getString("payment_id"));
			return response;
		}
		
		response.setSuccess(false);
		response.setMsg(jsonObject.getString("response_description"));
		return response;
	}

	public static String getSignature(Map<String, String> param, String primaryKey) {
		StringBuffer sb = new StringBuffer();
		if (!param.isEmpty()) {
			sb.append(primaryKey);
			param.entrySet().forEach(e -> {
				sb.append("|").append(e.getValue());
			});
			logger.debug(sb.toString());
			return Sha1Util.getSha1(sb.toString());
		}
		return sb.toString();
	}

	@Override
	public DirectPayResponse jsPay(DirectPayRequest request, String openid) throws PayException {
		return null;
	}

	@Override
	public RefundResponse refund(RefundRequest request) throws PayException {
		
		FondyConfig config = getConfig(request.getConfigMark());
		
		RefundResponse resp = new RefundResponse(config.getChannel(), config);
		resp.setSuccess(true);
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("order_id", request.getOut_refund_no());
		param.put("merchant_id", config.getMerchant_id());
		param.put("version", "1.0");
		param.put("amount", String.valueOf(request.getTotal_fee()));
		param.put("currency", request.getCurrency());
		param.put("comment", "Normal Refund");
		
		Map<String, String> orderMap = mapOrder(param);
		
		param.put("signature", getSignature(orderMap, config.getPrimaryKey()));

		JSONObject object = new JSONObject();
		object.put("request", param);

		logger.info("退款请求:[{}]",object.toString());
		
		String doPost = HttpClientUtil.doPost(refund, object.toString());
		
		logger.info("退款报文:[{}]",doPost);
		
		if (StringUtils.isBlank(doPost)) {
			resp.setSuccess(false);
			resp.setMsg("Connection Fondy timeout");
			return resp;
		}
		JSONObject jsonObject = JSON.parseObject(doPost).getJSONObject("response");
		String response_status = jsonObject.getString("response_status");
		String reverse_status = jsonObject.getString("reverse_status");
		if ("success".equals(response_status) && "approved".equals(reverse_status)) {
			logger.info("退款成功");
			resp.setOutOrderId(jsonObject.getString("order_id"));
			return resp;
		}

		resp.setSuccess(false);
		resp.setMsg(jsonObject.getString("error_message"));
		return resp;
	}

	@Override
	public boolean verifyNotify(FondyNotify payNotify) throws PayException {
		return false;
	}

	/**
	 * 验证签名的正确性
	 * 
	 * @param request
	 * @return
	 */
	public Boolean verifyNotify(HttpServletRequest request) {
		
		FondyConfig config = getConfig("fondy");
		// 排序
		Map<String, String> order = mapOrder(toMap(request.getParameterMap()));
		// 生成签名
		String signature = getSignature(order, config.getPrimaryKey());
		//返回
		return signature.equals(request.getParameter("signature"));
	}

	@Override
	public FondyNotify getPayNotify(HttpServletRequest request) throws PayException {
		FondyNotify foondyNotify = new FondyNotify();
		foondyNotify.setOrder_id(request.getParameter("order_id"));
		foondyNotify.setPayment_id(request.getParameter("payment_id"));
		foondyNotify.setAmount(request.getParameter("amount"));
		foondyNotify.setOrder_status(request.getParameter("order_status"));
		foondyNotify.setRectoken(request.getParameter("rectoken"));
		foondyNotify.setRectoken_lifetime(request.getParameter("rectoken_lifetime"));
		foondyNotify.setMasked_card(request.getParameter("masked_card"));
		return foondyNotify;
	}

	public Map<String, String> toMap(Map<String, String[]> parameter) {
		Map<String, String> params = new HashMap<String, String>();
		parameter.entrySet().forEach(e -> {
			if (StringUtils.isNotBlank(e.getValue()[0]) && !e.getKey().equals("signature")) {
				params.put(e.getKey(), e.getValue()[0]);
			}
		});
		return params;
	}

	public static Boolean refundVerify(Map<String, Object> respMap, String primaryKey) {
		
		Map<String, String> map = new HashMap<String, String>();
		respMap.entrySet().forEach(e->{
			if(StringUtils.isNotBlank(e.getValue().toString()) && !"signature".equals(e.getKey())) {
				map.put(e.getKey(), e.getValue().toString());
			}
		});
		
		Map<String, String> mapOrder = mapOrder(map);
		String signature = getSignature(mapOrder, primaryKey);
		System.out.println(signature);
		System.out.println(respMap.get("signature"));
		System.out.println(respMap.get("signature").equals(signature));
		return respMap.get("signature").equals(signature);
	}
	
	public static void main(String[] args) {

		String primaryKey = "VdTEu6GRTuBlLJlMojstv9yUkYWBO1Yc";
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("order_id", "deposit-65-202002180954002198279");
		param.put("merchant_id", "1434781");
		param.put("version", "1.0");
		param.put("amount", "100");
		param.put("currency", "UAH");
		param.put("comment", "Normal Refund");
		
		Map<String, String> orderMap = mapOrder(param);
		
		param.put("signature", getSignature(orderMap, primaryKey));

		JSONObject object = new JSONObject();
		object.put("request", param);

		String doPost = HttpClientUtil.doPost(refund, object.toString());
		
		logger.info("退款返回报文:[{}]",doPost);
		
		if(StringUtils.isBlank(doPost)) {
			logger.info("连接超时");
			return;
		}
		JSONObject parseObject = JSONObject.parseObject(doPost);
		
		Map<String, Object> respMap = JSON.parseObject(parseObject.getString("response"));
		
		if(!"approved".equals(respMap.get("reverse_status")) && "success".equals(respMap.get("response_status"))) {
			logger.info("退款未成功");
			return;
		}
		
		Boolean refundVerify = refundVerify(respMap, primaryKey);
		
		if(!refundVerify) {
			logger.info("签名验证失败");
		}
		
//		Map<String, String> map = new HashMap<String, String>();
//
//		map.put("actual_amount", "100");
//		map.put("eci", "5");
//		map.put("order_time", "19.12.2019 11:06:56");
//		map.put("merchant_id", "1434781");
//		map.put("order_status", "approved");
//		map.put("payment_id", "184255631");
//		map.put("currency", "UAH");
//		map.put("card_bin", "462705");
//		map.put("sender_email", "sas@uft.ua");
//		map.put("approval_code", "159010");
//		map.put("amount", "100");
//		map.put("response_status", "success");
//		map.put("tran_type", "purchase");
//		map.put("card_type", "VISA");
//		map.put("actual_currency", "UAH");
//		map.put("payment_system", "card");
//		map.put("reversal_amount", "0");
//		map.put("masked_card", "462705XXXXXX8370");
//		map.put("settlement_amount", "0");
//		map.put("order_id", "deposit-2-201912191706054551417");
//
//		StringBuffer sb = new StringBuffer();
//		sb.append("VdTEu6GRTuBlLJlMojstv9yUkYWBO1Yc");
//
//		Map<String, String> order = mapOrder(map);
//		order.entrySet().forEach(e -> {
//			sb.append("|").append(e.getValue());
//		});
//
//		System.out.println(sb.toString());
//		String signature = Sha1Util.getSha1(sb.toString());
//		System.out.println(signature);

	}

	public static Map<String, String> mapOrder(Map<String, String> map) {
		HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());

		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});

		for (int i = 0; i < infoIds.size(); i++) {
			Map.Entry<String, String> item = infoIds.get(i);
			tempMap.put(item.getKey(), item.getValue());
		}
		return tempMap;
	}

}
