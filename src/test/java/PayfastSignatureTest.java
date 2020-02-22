//import java.util.Map;
//
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.common.collect.Maps;
//
//import vc.thinker.pay.payfast.ApiPayFast;
//import vc.thinker.pay.payfast.PayFastPayHandler;
//import vc.thinker.pay.weixin.utils.MD5;
//import vc.thinker.pay.weixin.utils.MapUtil;
//import vc.thinker.weixin.kit.HttpKit;
//
//public class PayfastSignatureTest {
//
//	private static Logger logger = LoggerFactory.getLogger(PayfastSignatureTest.class);
//			
//	private static String pingUrl = "https://api.payfast.co.za/ping";
//	
//	private static String  apiPayUrl = "https://api.payfast.co.za/subscriptions/";
//	
//	@Test
//	public void verifySignature() {
//		
//		Map<String, String> param = Maps.newLinkedHashMap();
//		
//		param.put("m_payment_id","156-181226075751");
//		param.put("pf_payment_id","15854384");
//		param.put("payment_status","COMPLETE");
//		param.put("item_name","Deposit Pay");
//		param.put("item_description","Deposit Pay:Adoozy");
//		param.put("amount_gross","5.00");
//		param.put("amount_fee","-2.51");
//		param.put("amount_net","2.49");
//		param.put("custom_str1","deposit");
//		param.put("custom_str2","");
//		param.put("custom_str3","");
//		param.put("custom_str4","");
//		param.put("custom_str5","");
//		param.put("custom_int1","");
//		param.put("custom_int2","");
//		param.put("custom_int3","");
//		param.put("custom_int4","");
//		param.put("custom_int5","");
//		param.put("name_first","");
//		param.put("name_last","");
//		param.put("email_address","");
//		param.put("merchant_id","13277520");
//		param.put("token","f3d37721-5218-04ca-c272-ea1f78e1d56b");
//		param.put("billing_date", "2018-12-26");
//		
//		String paramString = PayFastPayHandler.mapJoinNoReplace(param,true,true);
//		
//		System.out.println(paramString);
//		
//		paramString = paramString + "&passphrase=2827f95_0D8ccc9c83e62A66_cd08Bd0";
//		
//		// 生成签名
//		String signature = MD5.MD5Encode(paramString);
//		
//		System.out.println(signature);
//
//		System.out.println(signature.equals("536cabe448447e5cec00e3c9eedd4f83"));
//	}
//	
//	
//	@Test
//	public void adHocPaySignatureVerify() {
//		
//		Map<String, String> param = Maps.newLinkedHashMap();
//		
//		param.put("m_payment_id","156-181226103810");
//		param.put("pf_payment_id","15880616");
//		param.put("payment_status","COMPLETE");
//		param.put("item_name","VIP");
//		param.put("item_description","VIP");
//		param.put("amount_gross","5.00");
//		param.put("amount_fee","-2.51");
//		param.put("amount_net","2.49");
//		param.put("custom_str1","deposit");
//		param.put("merchant_id","13277520");
//		param.put("token","98677be3-c453-545b-da5e-8e948d50e3e1");
////		param.put("passphrase", "2827f95_0D8ccc9c83e62A66_cd08Bd0");
//		
//		String paramString = PayFastPayHandler.mapJoinNoReplace(param,true,true);
//		
//		System.out.println(paramString);
//		
//		// 生成签名
//		String signature = MD5.MD5Encode(paramString);
//		
//		System.out.println(signature);
//		
//		System.out.println(signature.equals("0fe649eac6203325e59a5bad7e996463"));
//	}
//	
//	
//	@Test
//	public void isCanPay() {
//		
//		Map<String, String> map = Maps.newLinkedHashMap();
//		map.put("merchant-id", "13277520");
//		map.put("version", "v1");
//		map.put("timestamp", PayFastPayHandler.get8601TimeString());
//		map.put("passphrase", "2827f95_0D8ccc9c83e62A66_cd08Bd0");
//		
//		String paramString = PayFastPayHandler.mapJoinNoReplace(MapUtil.order(map), true, true);
//		
//		String signature = MD5.MD5Encode(paramString);
//		
//		map.put("signature", signature);
//		
//		map.remove("passphrase");
//		
//		String resp = HttpKit.get(pingUrl, null, map);
//		
//		logger.info("Ping Result:[{}]",resp);
//		
//		System.out.println("2222"+resp+"222");
//		System.out.println("\"API V1\"".equals(resp));
//		System.out.println(resp.contains("\"API V1\""));
//		
//		
//		resp = resp.replace("\n", "");
//		System.out.println("\"API V1\"".equals(resp));
//		
//	}
//	
//	@Test
//	public void adHocPay() {
//		
//		String time = PayFastPayHandler.get8601SimpleString();
//		
//		// 拼接字符串
//		Map<String, String> paramMap = Maps.newLinkedHashMap();
//		paramMap.put("merchant-id", "13277520");
//		paramMap.put("version", "v1");
//		paramMap.put("timestamp",time );
//		paramMap.put("passphrase", "2827f95_0D8ccc9c83e62A66_cd08Bd0");
//		
//		paramMap.put("amount", "500");
//		paramMap.put("item_name", "Test Fee");
//		paramMap.put("itn", "false");
//		
//		String paramString = PayFastPayHandler.apiMapToString(MapUtil.order(paramMap));
//		
//		logger.info("paramString:[{}]",paramString);
//		System.out.println(paramString);
//		
//		String signature = MD5.MD5Encode(paramString);
//		
//		// 请求头
//		Map<String, String> headers = Maps.newLinkedHashMap();
//		headers.put("merchant-id", "13277520");
//		headers.put("version", "v1");
//		headers.put("timestamp", time);
//		headers.put("signature", signature);
//		
//		// 请求体
//		StringBuilder sb = new StringBuilder();
//		sb.append("amount").append("=").append("500");
//		sb.append("&").append("item_name").append("=").append("Test Fee");
//		sb.append("&").append("itn").append("=").append("false");
//		
//		logger.info("Request Body:[{}]",sb.toString());
//		System.out.println(sb.toString());
//		
//		String url = apiPayUrl + "8f6c840d-8ab0-bb07-ea44-b4be082f0973" +"/adhoc";
//		
//		try {
//			String payResp = HttpKit.post(url, sb.toString(), headers);
//			logger.info("Payfast Api Pay Result:[{}] " , payResp);
//			System.out.println(payResp);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//		
//		
//		
//	}
//}
