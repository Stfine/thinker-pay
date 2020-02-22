package vc.thinker.pay.weixin.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author louiseliu
 *
 */
public class Signature {

	private static Logger logger = LoggerFactory.getLogger(Signature.class);
	
	public static String generateSign(Map<String, String> map,String key){
    	Map<String, String> orderMap = MapUtil.order(map);

    	String result = MapUtil.mapJoin(orderMap,true,false);
        result += "&key=" + key;
        result = MD5.MD5Encode(result).toUpperCase();

        return result;
    }

	public static String toURL(Map<String,String> map){
		Map<String, String> orderMap = MapUtil.order(map);
		String result = MapUtil.mapJoin(orderMap,true,false);
		return result;
	}


	/**
	 * payfast签名生成
	 * @param map 参数
	 * @param key 商户平台设置的密码
	 * @return
	 */
	public static String getPayFastSignature(Map<String, String> map,String key) {
		
		String paramString = MapUtil.mapJoinNoReplace(map, true, true);
		
		paramString = paramString + "&passphrase="+key;
		
		logger.info("paramString[{}]:",paramString);
		
		System.out.println(paramString);
		
		return MD5.MD5Encode(paramString);
	}
	
}

