package vc.thinker.pay.fondy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import vc.thinker.pay.tenpay.util.Sha1Util;
import vc.thinker.weixin.kit.HttpKit;

/**
 *
 * @author ZhangGaoXiang
 * @time Dec 12, 20193:07:24 PM
 */
public class PayTest {

	public static void main(String[] args) throws IOException {
		foundyPay();
	}

	public static void foundyPay() {
		String url = "https://api.fondy.eu/api/checkout/url";

		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("amount", "100");
		param.put("currency", "UAH");
		param.put("merchant_id", "1434781");
//		param.put("merchant_id", "1396424");
		param.put("order_desc", "deposit pay");
		param.put("order_id", getOrderCode("deposit"));

		param.put("response_url", "https://api.adoozy.co.za/pay/result");
		param.put("server_callback_url", "http://thinkervc.imwork.net:59799/fondy/notify");

		param.put("signature", getSignature(param));

		JSONObject object = new JSONObject();
		object.put("request", param);

		for (int i = 0; i <= 3; i++) {

			String doPost = HttpClientUtil.doPost(url, object.toString());
			System.out.println(doPost);
			if (StringUtils.isBlank(doPost)) {
				continue;
			}
			JSONObject jsonObject = JSON.parseObject(doPost).getJSONObject("response");
			if ("success".equals(jsonObject.getString("response_status"))) {
				String checkout_url = jsonObject.getString("checkout_url");
				System.out.println(checkout_url);
			} else {
				System.out.println(jsonObject.getString("error_message"));
			}
		}
	}

	public static String getSignature(Map<String, String> param) {
		StringBuffer sb = new StringBuffer();
		sb.append("VdTEu6GRTuBlLJlMojstv9yUkYWBO1Yc").append("|");
		param.entrySet().forEach(e -> {
			sb.append(e.getValue()).append("|");
		});
		System.out.println(sb.toString());
		return Sha1Util.getSha1(sb.toString().substring(0, sb.toString().length() - 1));
	}

	public static String getOrderCode(String type) {
		StringBuffer sb = new StringBuffer();
		String valueOf = String.valueOf(Math.random());
		sb.append(type).append("-").append(99).append("-");
		sb.append(getDateTimeMils());
		sb.append(valueOf.substring(valueOf.length() - 6));
		return sb.toString();
	}

	public static String getDateTimeMils() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}
}
