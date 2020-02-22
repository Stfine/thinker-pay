package vc.thinker.pay.alipay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import vc.thinker.pay.alipay.util.MD5;
import vc.thinker.pay.alipay.util.RSA;

/* *
 *类名：AlipayNotify
 *功能：支付宝通知处理类
 *详细：处理支付宝各接口通知返回
 *版本：3.2
 *日期：2011-03-25
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考

 *************************注意*************************
 *调试通知返回时，可查看或改写log日志的写入TXT里的数据，来检查通知返回是否正常
 */
public class AlipayNotify {

	/**
	 * HTTPS形式消息验证地址
	 */
	private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

	/**
	 * HTTP形式消息验证地址
	 */
	private static final String HTTP_VERIFY_URL = "http://notify.alipay.com/trade/notify_query.do?";

	/**
	 * 验证消息是否是支付宝发出的合法消息
	 *
	 * @param params
	 *            通知返回来的参数数组
	 * @return 验证结果
	 */
	public static boolean verify(AlipayConfig config, Map<String, String> params) {

		// 判断responsetTxt是否为true，isSign是否为true
		// responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
		// isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
		String responseTxt = "true";
		if (params.get("notify_id") != null) {
			String notify_id = params.get("notify_id");
			responseTxt = verifyResponse(config, notify_id);
		}
		String sign = "";
		if (params.get("sign") != null) {
			sign = params.get("sign");
		}
		boolean isSign = getSignVeryfy(config, params, sign, true);

		// 写日志记录（若要调试，请取消下面两行注释）
		String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign
				+ "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
		if (isSign && responseTxt.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 解密
	 *
	 * @param inputPara
	 *            要解密数据
	 * @return 解密后结果
	 */
	public static Map<String, String> decrypt(AlipayConfig config,
			Map<String, String> inputPara) throws Exception {
		inputPara.put("notify_data", RSA.decrypt(inputPara.get("notify_data"),
				config.getAppPrivateKey(), config.getInputCharset()));
		return inputPara;
	}

	/**
	 * 验证消息是否是支付宝发出的合法消息，验证服务器异步通知
	 *
	 * @param params
	 *            通知返回来的参数数组
	 * @return 验证结果
	 */
	public static boolean verifyWapNotify(AlipayConfig config,
			Map<String, String> params) throws Exception {
		
		// 解密
		if (config.getSignType().equals("0001")) {
			params = decrypt(config, params);
		}

		// 获取是否是支付宝服务器发来的请求的验证结果
		String responseTxt = "true";
		try {
			// XML解析notify_data数据，获取notify_id
			Document document = DocumentHelper.parseText(params
					.get("notify_data"));
			String notify_id = document.selectSingleNode("//notify/notify_id")
					.getText();
			responseTxt = verifyResponse(config, notify_id);
		} catch (Exception e) {
			responseTxt = e.toString();
		}

		// 获取返回时的签名验证结果
		String sign = "";
		if (params.get("sign") != null) {
			sign = params.get("sign");
		}
		boolean isSign = getSignVeryfy(config, params, sign, false);

		// 写日志记录（若要调试，请取消下面两行注释）
		// String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign +
		// "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
		// AlipayCore.logResult(sWord);

		// 判断responsetTxt是否为true，isSign是否为true
		// responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
		// isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
		if (isSign && responseTxt.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据反馈回来的信息，生成签名结果
	 *
	 * @param Params
	 *            通知返回来的参数数组
	 * @param sign
	 *            比对的签名结果
	 * @return 生成的签名结果
	 */
	public static boolean getSignVeryfy(AlipayConfig config,
			Map<String, String> Params, String sign, boolean isSort) {
		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
		//删除自定义参数
		sParaNew.remove(AlipayPayHandler.CUSTOM_PARAM_KEY);
		// 获取待签名字符串
		String preSignStr = "";// AlipayCore.createLinkString(sParaNew);
		if (isSort) {
			preSignStr = AlipayCore.createLinkString(sParaNew);
		} else {
			preSignStr = AlipayCore.createLinkStringNoSort(sParaNew);
		}
		// 获得签名验证结果
		boolean isSign = false;
		if (config.getSignType().equals("MD5")) {
			isSign = MD5.verify(preSignStr, sign, config.getSafeKey(),
					config.getInputCharset());
		}
		if (config.getSignType().equals("RSA")) {
			isSign = RSA.verify(preSignStr, sign, config.getAppPublicKey(),
					config.getInputCharset());
		}
		return isSign;
	}

	/**
	 * 获取远程服务器ATN结果,验证返回URL
	 *
	 * @param notify_id
	 *            通知校验ID
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	 public static String verifyResponse(AlipayConfig config, String notify_id) {
		// 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
		String veryfy_url = "";
		if (config.getTransport().equalsIgnoreCase("https")) {
			veryfy_url = HTTPS_VERIFY_URL;
		} else {
			veryfy_url = HTTP_VERIFY_URL;
		}
		String partner = config.getPartner();
		veryfy_url = veryfy_url + "partner=" + partner + "&notify_id="
				+ notify_id;

		return checkUrl(veryfy_url);
	}

	/**
	 * 获取远程服务器ATN结果
	 *
	 * @param urlvalue
	 *            指定URL路径地址
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	private static String checkUrl(String urlvalue) {
		String inputLine = "";

		try {
			URL url = new URL(urlvalue);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			inputLine = in.readLine().toString();
		} catch (Exception e) {
			e.printStackTrace();
			inputLine = "";
		}

		return inputLine;
	}
}
