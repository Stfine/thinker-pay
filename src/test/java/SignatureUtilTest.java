

import java.io.IOException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import vc.thinker.pay.weixin.WeixinConfig;
import vc.thinker.pay.weixin.WeixinPayHandler;
import vc.thinker.pay.weixin.api.PayUtils;
import vc.thinker.pay.weixin.bean.WeixinPayNotify;
import vc.thinker.pay.weixin.utils.Signature;

/**
 * @borball on 6/3/2016.
 */
public class SignatureUtilTest {

    @Test
    public void testSign1() throws IOException {
    	
    	String xml = "<xml><appid><![CDATA[wxb1e2eee3c932aa58]]></appid>"
    			+ "<attach><![CDATA[{\"configMark\":\"wx_js\",\"attach\":\"deposit\"}]]></attach>"
    					+ "<bank_type><![CDATA[BOC_DEBIT]]></bank_type>"
    					+ "<cash_fee><![CDATA[19790]]></cash_fee>"
    					+ "<coupon_count><![CDATA[1]]></coupon_count>"
    					+ "<coupon_fee>10</coupon_fee>"
    					+ "<coupon_fee_0><![CDATA[10]]></coupon_fee_0>"
    					+ "<coupon_id_0><![CDATA[2000000000507143213]]></coupon_id_0>"
    					+ "<device_info><![CDATA[WEB]]></device_info>"
    					+ "<fee_type><![CDATA[CNY]]></fee_type>"
    					+ "<is_subscribe><![CDATA[N]]></is_subscribe>"
    					+ "<mch_id><![CDATA[1441184402]]></mch_id>"
    					+ "<nonce_str><![CDATA[727cc108957645cf86a34a7c76ee687e]]></nonce_str>"
    					+ "<openid><![CDATA[oLPrr0HmJXS25qgePoKepWA40-xk]]></openid>"
    					+ "<out_trade_no><![CDATA[26322-170615101844]]></out_trade_no>"
    					+ "<result_code><![CDATA[SUCCESS]]></result_code>"
    					+ "<return_code><![CDATA[SUCCESS]]></return_code>"
    					+ "<sign><![CDATA[837A95B9D1A079D8E1DB562179EC6174]]></sign>"
    					+ "<time_end><![CDATA[20170615101851]]></time_end>"
    					+ "<total_fee>19800</total_fee>"
    					+ "<trade_type><![CDATA[JSAPI]]></trade_type>"
    					+ "<transaction_id><![CDATA[4010092001201706155791367297]]></transaction_id>"
    					+ "</xml>";
    	
		WeixinPayNotify payCallbackNotify = PayUtils.payCallbackNotify(xml);
//		Map<String, String> attachMap=PayUtils.getAttachOut(payCallbackNotify.getAttach());
//		payCallbackNotify.setConfigMark(attachMap.get(ATTACH_KEY_CONFIG_MARK));
//		payCallbackNotify.setReqAttach(attachMap.get(ATTACH_KEY_REQ_ATTACH));
		Map<String, String> map=payCallbackNotify.getParams();
		map.remove("class");
		map.remove("paySuccess");
		map.put("sign", "");
		map.remove("configMark");
		map.remove("reqAttach");
		
		String sign = Signature.generateSign(map,"234mh98yvjnvi2yfivnwn10929u3905o");
		
    	Assert.assertTrue(payCallbackNotify.getSign().equals(sign));
    }
    @Test
    public void testSign2() throws IOException {
    	
    	String xml = "<xml><appid><![CDATA[wxb1e2eee3c932aa58]]></appid>"
    			+ "<attach><![CDATA[{\"configMark\":\"wx_js\",\"attach\":\"trip\"}]]></attach>"
    					+ "<bank_type><![CDATA[CFT]]></bank_type>"
    					+ "<cash_fee><![CDATA[89]]></cash_fee>"
    					+ "<coupon_count><![CDATA[1]]></coupon_count>"
    					+ "<coupon_fee>11</coupon_fee>"
    					+ "<coupon_fee_0><![CDATA[11]]></coupon_fee_0>"
    					+ "<coupon_id_0><![CDATA[2000000000506845290]]></coupon_id_0>"
    					+ "<device_info><![CDATA[WEB]]></device_info>"
    					+ "<fee_type><![CDATA[CNY]]></fee_type>"
    					+ "<is_subscribe><![CDATA[N]]></is_subscribe>"
    					+ "<mch_id><![CDATA[1441184402]]></mch_id>"
    					+ "<nonce_str><![CDATA[6d5b155109694273a4d33ccd46d3e3dd]]></nonce_str>"
    					+ "<openid><![CDATA[oLPrr0C3KTTVAN0nS40L5yTYbatY]]></openid>"
    					+ "<out_trade_no><![CDATA[118255-170615100251]]></out_trade_no>"
    					+ "<result_code><![CDATA[SUCCESS]]></result_code>"
    					+ "<return_code><![CDATA[SUCCESS]]></return_code>"
    					+ "<sign><![CDATA[F87258564704EEF71CE19A653B3DD604]]></sign>"
    					+ "<time_end><![CDATA[20170615100256]]></time_end>"
    					+ "<total_fee>100</total_fee>"
    					+ "<trade_type><![CDATA[JSAPI]]></trade_type>"
    					+ "<transaction_id><![CDATA[4008242001201706155790797382]]></transaction_id>"
    					+ "</xml>";
    	
    	WeixinPayNotify payCallbackNotify = PayUtils.payCallbackNotify(xml);
//		Map<String, String> attachMap=PayUtils.getAttachOut(payCallbackNotify.getAttach());
//		payCallbackNotify.setConfigMark(attachMap.get(ATTACH_KEY_CONFIG_MARK));
//		payCallbackNotify.setReqAttach(attachMap.get(ATTACH_KEY_REQ_ATTACH));
    	Map<String, String> map=payCallbackNotify.getParams();
    	map.remove("class");
    	map.remove("paySuccess");
    	map.put("sign", "");
    	map.remove("configMark");
    	map.remove("reqAttach");
    	
    	String sign = Signature.generateSign(map,"234mh98yvjnvi2yfivnwn10929u3905o");
    	
    	Assert.assertTrue(payCallbackNotify.getSign().equals(sign));
    }
    @Test
    public void testSign3() throws IOException {
    	
    	String xml = "<xml><appid><![CDATA[wxb1e2eee3c932aa58]]></appid>"
    			+ "<attach><![CDATA[{\"configMark\":\"wx_js\",\"attach\":\"trip\"}]]></attach>"
    					+ "<bank_type><![CDATA[CFT]]></bank_type>"
    					+ "<cash_fee><![CDATA[100]]></cash_fee>"
    					+ "<device_info><![CDATA[WEB]]></device_info>"
    					+ "<fee_type><![CDATA[CNY]]></fee_type>"
    					+ "<is_subscribe><![CDATA[N]]></is_subscribe>"
    					+ "<mch_id><![CDATA[1441184402]]></mch_id>"
    					+ "<nonce_str><![CDATA[feff1acf86584aefb55c66216670a3c3]]></nonce_str>"
    					+ "<openid><![CDATA[oLPrr0NNcjxNW1ZPPhm2EZJk7qnE]]></openid>"
    					+ "<out_trade_no><![CDATA[118215-170615100743]]></out_trade_no>"
    					+ "<result_code><![CDATA[SUCCESS]]></result_code>"
    					+ "<return_code><![CDATA[SUCCESS]]></return_code>"
    					+ "<sign><![CDATA[4F0F3E81C1A19966F630EE45EC1BE73B]]></sign>"
    					+ "<time_end><![CDATA[20170615100749]]></time_end>"
    					+ "<total_fee>100</total_fee>"
    					+ "<trade_type><![CDATA[JSAPI]]></trade_type>"
    					+ "<transaction_id><![CDATA[4004282001201706155788024257]]></transaction_id></xml>";
    	
    	WeixinPayNotify payCallbackNotify = PayUtils.payCallbackNotify(xml);
//		Map<String, String> attachMap=PayUtils.getAttachOut(payCallbackNotify.getAttach());
//		payCallbackNotify.setConfigMark(attachMap.get(ATTACH_KEY_CONFIG_MARK));
//		payCallbackNotify.setReqAttach(attachMap.get(ATTACH_KEY_REQ_ATTACH));
    	Map<String, String> map=payCallbackNotify.getParams();
    	map.remove("class");
    	map.remove("paySuccess");
    	map.put("sign", "");
    	map.remove("configMark");
    	map.remove("reqAttach");
    	
    	String sign = Signature.generateSign(map,"234mh98yvjnvi2yfivnwn10929u3905o");
    	Assert.assertTrue(payCallbackNotify.getSign().equals(sign));
    }
    
    @Test
    public void testSign4() throws IOException {
    	
    	String xml = "<xml><appid><![CDATA[wx76e6724a56b2d819]]></appid>"
    			+ "<attach><![CDATA[{\"configMark\":\"wx_app\",\"attach\":\"deposit\"}]]></attach>"
    					+ "<bank_type><![CDATA[CFT]]></bank_type>"
    					+ "<cash_fee><![CDATA[1]]></cash_fee>"
    					+ "<fee_type><![CDATA[CNY]]></fee_type>"
    					+ "<is_subscribe><![CDATA[N]]></is_subscribe>"
    					+ "<mch_id><![CDATA[1480750262]]></mch_id>"
    					+ "<nonce_str><![CDATA[f014e88a3a2e43fd9fcfdc69685710b9]]></nonce_str>"
    					+ "<openid><![CDATA[oaWpZwZ8IjA9idcC2DrxeF47dr5U]]></openid>"
    					+ "<out_trade_no><![CDATA[44-170621204634]]></out_trade_no>"
    					+ "<result_code><![CDATA[SUCCESS]]></result_code>"
    					+ "<return_code><![CDATA[SUCCESS]]></return_code>"
    					+ "<sign><![CDATA[0F159D32CEE449D91EFAB79DC35878B9]]></sign>"
    					+ "<time_end><![CDATA[20170621204641]]></time_end>"
    					+ "<total_fee>1</total_fee>"
    					+ "<trade_type><![CDATA[APP]]></trade_type>"
    					+ "<transaction_id><![CDATA[4003522001201706216795585378]]></transaction_id>"
    					+ "</xml>";
    	
    	WeixinPayNotify payCallbackNotify = PayUtils.payCallbackNotify(xml);
//		Map<String, String> attachMap=PayUtils.getAttachOut(payCallbackNotify.getAttach());
//		payCallbackNotify.setConfigMark(attachMap.get(ATTACH_KEY_CONFIG_MARK));
//		payCallbackNotify.setReqAttach(attachMap.get(ATTACH_KEY_REQ_ATTACH));
    	Map<String, String> map=payCallbackNotify.getParams();
    	map.remove("class");
    	map.remove("paySuccess");
    	map.put("sign", "");
    	map.remove("configMark");
    	map.remove("reqAttach");
    	
    	String sign = Signature.generateSign(map,"13jtl3m5lv90djv2lj3rl23n41092ulc");
    	
    	Assert.assertTrue(payCallbackNotify.getSign().equals(sign));
    }
    
}

