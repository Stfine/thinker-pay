package vc.thinker.pay.cashfree;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import vc.thinker.pay.PayConfigFactory;
import vc.thinker.pay.PayException;
import vc.thinker.pay.PayHandler;
import vc.thinker.pay.request.DirectPayRequest;
import vc.thinker.pay.response.DirectPayResponse;
import vc.thinker.pay.response.RefundResponse;
import vc.thinker.pay.util.HttpKitUtils;
import vc.thinker.refund.bean.RefundRequest;
import vc.thinker.weixin.kit.HttpKit;

/**
 * 印度 CashFree 支付
 * 
 * @author ZhangGaoXiang
 * @time Sep 4, 201911:22:26 AM
 */
public class CashfreePayHandler extends PayHandler<CashfreeConfig, CashfreeNotify> {

	private static final Logger logger = LoggerFactory.getLogger(CashfreePayHandler.class);

	// 创建订单
//	private static final String create_url = "https://api.cashfree.com/api/v1/order/create";

	// 退款
	private static final String refund_url = "https://api.cashfree.com/api/v1/order/refund";

	// 创建支付链接
	private static final String link_url = "https://api.cashfree.com/api/v1/order/info/link";

	private static final String createToken = "https://api.cashfree.com/api/v2/cftoken/order";

	public CashfreePayHandler(PayConfigFactory configFactory) {
		super(configFactory);
	}

	@Override
	public DirectPayResponse webPay(DirectPayRequest request) throws PayException {
		throw new PayException("还未实现");
	}

	@Override
	public DirectPayResponse appPay(DirectPayRequest request) throws PayException {
		
		CashfreeConfig config = getConfig(request.getConfigMark());

		DirectPayResponse resp = new DirectPayResponse(config.getChannel(), config);
		resp.setSuccess(true);

		JSONObject object = new JSONObject();
		object.put("notifyUrl", request.getNotifyUrl());
		object.put("orderId", request.getOutTradeNo());
		object.put("orderAmount", request.getTotalFee());
		object.put("orderCurrency", "INR");

		Map<String, String> headers = Maps.newHashMap();
		headers.put("x-client-id", config.getAppId());
		headers.put("x-client-secret", config.getSecretKey());

		try {
			String doPost = HttpKit.post(createToken, object.toString(), headers);
			JSONObject parseObject = JSONObject.parseObject(doPost);

			if (!"OK".equals(parseObject.getString("status"))) {
				resp.setSuccess(false);
				resp.setMsg(parseObject.getString("reason"));
				return resp;
			}
			resp.setContent(parseObject.getString("cftoken"));
			return resp;
		} catch (Exception e) {
			resp.setSuccess(false);
			resp.setMsg(e.getMessage());
			return resp;
		}
	}

	@Override
	public DirectPayResponse jsPay(DirectPayRequest request, String openid) throws PayException {
		throw new PayException("还未实现");
	}

	@Override
	public RefundResponse refund(RefundRequest request) throws PayException {

		CashfreeConfig config = getConfig(request.getConfigMark());

		RefundResponse resp = new RefundResponse(config.getChannel(), config);
		resp.setSuccess(true);

		Map<String, Object> param = Maps.newLinkedHashMap();
		param.put("appId", config.getAppId());
		param.put("secretKey", config.getSecretKey());
		param.put("referenceId", request.getOut_refund_no());
		param.put("refundAmount", request.getRefund_amount());
		param.put("refundNote", handlerStr(" Deposit Refund"));

		logger.info("退款金额:[{}]", request.getRefund_amount());

		String result = HttpKitUtils.doPost(refund_url, map2Str(param));

		JSONObject parseObject = JSONObject.parseObject(result);
		if (!"OK".equals(parseObject.getString("status"))) {
			resp.setSuccess(false);
			resp.setMsg(parseObject.getString("message"));
			return resp;
		}

		resp.setOutOrderId(parseObject.getString("refundId"));
		return resp;
	}

	@Override
	public boolean verifyNotify(CashfreeNotify payNotify) throws PayException {

//		logger.info("收到支付回调:[{}]", JSON.toJSON(payNotify));

		CashfreeConfig config = getConfig(payNotify.getConfigMark());

		LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>();

		postData.put("orderId", payNotify.getOrderId());
		postData.put("orderAmount", payNotify.getOrderAmount());
		postData.put("referenceId", payNotify.getReferenceId());
		postData.put("txStatus", payNotify.getTxStatus());
		postData.put("paymentMode", payNotify.getPaymentMode());
		postData.put("txMsg", payNotify.getTxMsg());
		postData.put("txTime", payNotify.getTxTime());

		String data = "";
		Set<String> keys = postData.keySet();

		for (String key : keys) {
			data = data + postData.get(key);
		}
		String secretKey = config.getSecretKey(); // Get secret key from config;
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key_spec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key_spec);
			String signature = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
			return payNotify.getSignature().equals(signature);
		} catch (NoSuchAlgorithmException e) {
			logger.error("验证签名异常:[{}]", e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error("验证签名异常:[{}]", e.getMessage());
		}
		return false;
	}

	@Override
	public CashfreeNotify getPayNotify(HttpServletRequest request) throws PayException {
		CashfreeNotify notify = new CashfreeNotify();
		notify.setOrderId(request.getParameter("orderId"));
		notify.setReferenceId(request.getParameter("referenceId"));
		notify.setOrderAmount(request.getParameter("orderAmount"));
		notify.setTxStatus(request.getParameter("txStatus"));
		notify.setTxMsg(request.getParameter("txMsg"));
		notify.setTxTime(request.getParameter("txTime"));
		notify.setSignature(request.getParameter("signature"));
		notify.setPaymentMode(request.getParameter("paymentMode"));
		notify.setPaySuccess(request.getParameter("paySuccess"));
		return notify;
	}

	/**
	 * 字符串处理
	 * 
	 * @param str
	 * @return
	 */
	public String handlerStr(String str) {

		if (StringUtils.isNotBlank(str)) {
			return str.replaceAll(":", "%3A").replaceAll("/", "%2F").replaceAll(" ", "%20").replaceAll("@", "%40");
		}
		return null;
	}

	/**
	 * map 转字符串
	 * 
	 * @param param
	 * @return
	 */
	public static String map2Str(Map<String, Object> param) {
		StringBuffer sb = new StringBuffer();
		param.entrySet().forEach(d -> {
			sb.append("&").append(d.getKey()).append("=").append(d.getValue());
		});
		return sb.toString().substring(1);
	}

	/**
	 * 根据已有订单创建支付连接
	 * 
	 * @param paymentMark
	 * @param payOrderCode
	 * @return
	 */
	public DirectPayResponse getPayLink(String paymentMark, String payOrderCode) {

		CashfreeConfig config = getConfig(paymentMark);

		DirectPayResponse resp = new DirectPayResponse(config.getChannel(), config);
		resp.setSuccess(true);

		Map<String, Object> param = Maps.newLinkedHashMap();
		param.put("appId", config.getAppId());
		param.put("secretKey", config.getSecretKey());
		param.put("orderId", payOrderCode);

		String doPost = HttpKitUtils.doPost(link_url, map2Str(param));

		JSONObject parseObject = JSONObject.parseObject(doPost);

		if (!"OK".equals(parseObject.getString("status"))) {
			resp.setSuccess(false);
			resp.setMsg(parseObject.getString("reason"));
			return resp;
		}
		resp.setContent(parseObject.getString("paymentLink").replaceAll("\\/", "/"));
		return resp;
	}

//	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
//
//		LinkedHashMap<String, String> postData = new LinkedHashMap<String, String>();
//
//		postData.put("orderId", "balance-201910171544028_15");
//		postData.put("orderAmount", "10.00");
//		postData.put("referenceId", "55484863");
//		postData.put("txStatus", "SUCCESS");
//		postData.put("paymentMode", "DEBIT_CARD");
//		postData.put("txMsg", "100::Successful transaction");
//		postData.put("txTime", "2019-10-17 13:15:29");
//
//		String data = "";
//		Set<String> keys = postData.keySet();
//
//		for (String key : keys) {
//			data = data + postData.get(key);
//		}
//		String secretKey = "f968044e18451c5920c45afe321cf55f125c4e1f";
//		// Get secret key from config;
//		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//		SecretKeySpec secret_key_spec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
//		sha256_HMAC.init(secret_key_spec);
//		String signature = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
//		System.out.println(signature);
//	}
}
