package vc.thinker.pay.weixin.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sinco.common.utils.JaxbUtil;

import vc.thinker.pay.PayException;
import vc.thinker.pay.weixin.WeixinConfig;
import vc.thinker.pay.weixin.bean.PayBank;
import vc.thinker.pay.weixin.bean.PayNativeInput;
import vc.thinker.pay.weixin.bean.PayPackage;
import vc.thinker.pay.weixin.bean.PayQrCode;
import vc.thinker.pay.weixin.bean.Transfers;
import vc.thinker.pay.weixin.bean.WeixinPayNotify;
import vc.thinker.pay.weixin.utils.Configure;
import vc.thinker.pay.weixin.utils.HttpsRequest;
import vc.thinker.pay.weixin.utils.MapUtil;
import vc.thinker.pay.weixin.utils.Signature;

/**
 *
 * @author louiseliu
 *
 */
public class PayUtils {
	private static final Logger logger = LoggerFactory.getLogger(PayUtils.class);

	public static String generateMchPayNativeRequestURL(String appid,String mch_id,String productid,String key){
		PayQrCode qrCode = new PayQrCode(appid,mch_id,productid,key);
		Map<String, String> map = new HashMap<String, String>();
		map.put("sign", qrCode.getSign());
		map.put("appid", qrCode.getAppid());
		map.put("mch_id", qrCode.getMch_id());
		map.put("product_id", qrCode.getProduct_id());
		map.put("time_stamp", qrCode.getTime_stamp());
		map.put("nonce_str", qrCode.getNonce_str());

		return "weixin://wxpay/bizpayurl?" + MapUtil.mapJoin(map, false, false);
	}

	/**
	 *
	 * @param inputStream request.getInputStream()
	 * @return
	 */
	public static PayNativeInput convertRequest(InputStream inputStream){
		try {
			String content = IOUtils.toString(inputStream);

			XmlMapper xmlMapper = new XmlMapper();
			PayNativeInput payNativeInput = xmlMapper.readValue(content, PayNativeInput.class);

			return payNativeInput;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean validateAppSignature(PayNativeInput payNativeInput,String key){
		try {
			Map<String, String> map = BeanUtils.describe(payNativeInput);
			map.remove("class");
			map.put("sign", "");

			String sign = Signature.generateSign(map,key);
			return payNativeInput.getSign().equals(sign) ? true : false;
		} catch (Exception e) {
		}

		return false;
	}

	public static String generatePayNativeReplyXML(PayPackage payPackage,String mch_id,String key,WeixinConfig config) throws  PayException{

		Map<String, String> map;
		try {
			map = BeanUtils.describe(payPackage);
		} catch (Exception e) {
			throw new PayException(e);
		} 
		map.remove("class");

		String sign = Signature.generateSign(map,key);
		payPackage.setSign(sign);

		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.setSerializationInclusion(Include.NON_EMPTY);

		try {
			String xmlContent = xmlMapper.writeValueAsString(payPackage);
			HttpsRequest httpsRequest = new HttpsRequest(mch_id,config);
			logger.info("weixin js pay request:"+xmlContent);
			return httpsRequest.sendPost(Configure.UNIFY_PAY_API, xmlContent,mch_id);
		} catch (JsonProcessingException e) {
			throw new PayException(e);
		} catch (UnrecoverableKeyException e) {
			throw new PayException(e);
		} catch (KeyManagementException e) {
			throw new PayException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new PayException(e);
		} catch (KeyStoreException e) {
			throw new PayException(e);
		} catch (IOException e) {
			throw new PayException(e);
		}
	}
	
	public static String generatePayNativeReplyXML(Transfers transfers,String mch_id,String key,WeixinConfig config) throws  PayException{
		
		Map<String, String> map;
		try {
			map = BeanUtils.describe(transfers);
		} catch (Exception e) {
			throw new PayException(e);
		} 
		map.remove("class");
		
		String sign = Signature.generateSign(map,key);
		transfers.setSign(sign);
		
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.setSerializationInclusion(Include.NON_EMPTY);
		
		try {
			String xmlContent = xmlMapper.writeValueAsString(transfers);
			HttpsRequest httpsRequest = new HttpsRequest(mch_id,config);
			logger.info("weixin transfers request:"+xmlContent);
			return httpsRequest.sendPost(Configure.TRANSFER_ACCOUNTS_API, xmlContent,mch_id);
		} catch (JsonProcessingException e) {
			throw new PayException(e);
		} catch (UnrecoverableKeyException e) {
			throw new PayException(e);
		} catch (KeyManagementException e) {
			throw new PayException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new PayException(e);
		} catch (KeyStoreException e) {
			throw new PayException(e);
		} catch (IOException e) {
			throw new PayException(e);
		}
	}
	public static String generatePayBankReplyXML(PayBank payBank,String mch_id,String key,WeixinConfig config) throws  PayException{
		
		Map<String, String> map;
		try {
			map = BeanUtils.describe(payBank);
		} catch (Exception e) {
			throw new PayException(e);
		} 
		map.remove("class");
		
		String sign = Signature.generateSign(map,key);
		payBank.setSign(sign);
		
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.setSerializationInclusion(Include.NON_EMPTY);
		
		try {
			String xmlContent = xmlMapper.writeValueAsString(payBank);
			HttpsRequest httpsRequest = new HttpsRequest(mch_id,config);
			logger.info("weixin payBank request:"+xmlContent);
			return httpsRequest.sendPost(Configure.TRANSFER_PAY_BANK, xmlContent,mch_id);
		} catch (JsonProcessingException e) {
			throw new PayException(e);
		} catch (UnrecoverableKeyException e) {
			throw new PayException(e);
		} catch (KeyManagementException e) {
			throw new PayException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new PayException(e);
		} catch (KeyStoreException e) {
			throw new PayException(e);
		} catch (IOException e) {
			throw new PayException(e);
		}
	}
	
	public static WeixinPayNotify payCallbackNotify(String content){
		WeixinPayNotify payCallbackNotify = JaxbUtil.converyToJavaBean(content, WeixinPayNotify.class);
		
		Map<String,String> params=parserXmlParam(content);
		payCallbackNotify.setParams(params);
		
		if(payCallbackNotify.getResult_code().equals("SUCCESS")){
			payCallbackNotify.setPaySuccess(true);
		}
		return payCallbackNotify;
	}

	private static Map<String, String> parserXmlParam(String content) {
		Map<String, String> param=new HashMap<>();
		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			//  禁止访问外部实体 用于解决微信XXE漏洞
			dbf.setExpandEntityReferences(false);
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//将xml文件解析
			Document document = db.parse(new ByteArrayInputStream(content.getBytes()));
			
			if( document.getChildNodes().getLength() > 0){
				//获得所有节点，递归遍历节点
				NodeList employees = document.getChildNodes().item(0).getChildNodes();
				for (int i = 0; i < employees.getLength(); i++) {
					//取得一个节点
					Node employee = employees.item(i);
					String name=employee.getNodeName();
					String val=employee.getTextContent();
					param.put(name, val);
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			logger.error("",e);
		} catch (SAXException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		}
		return param;
	}

	public static WeixinPayNotify payCallbackNotify(InputStream inputStream) throws IOException{
		String content = IOUtils.toString(inputStream);
		logger.info("payCallbackNotify InputStream content [{}] ",content);
		return payCallbackNotify(content);
	}

	public static boolean validateAppSignature(WeixinPayNotify payCallbackNotify,String key){
		try {
			Map<String, String> map = BeanUtils.describe(payCallbackNotify);
			map.remove("class");
			map.put("sign", "");
			map.remove("paySuccess");

			String sign = Signature.generateSign(map,key);
			return payCallbackNotify.getSign().equals(sign) ? true : false;
		} catch (Exception e) {
		}

		return false;
	}


	public static String generatePaySuccessReplyXML(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<xml>")
					.append("<return_code><![CDATA[SUCCESS]]></return_code>")
					.append("<return_msg><![CDATA[OK]]></return_msg>")
					.append("</xml>");
		return stringBuffer.toString();
	}
	
	public static String generatePayFailReplyXML(String msg){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<xml>")
		.append("<return_code><![CDATA[FAIL]]></return_code>")
		.append("<return_msg><![CDATA["+msg+"]]></return_msg>")
		.append("</xml>");
		return stringBuffer.toString();
	}
}
