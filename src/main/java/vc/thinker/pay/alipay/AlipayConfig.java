package vc.thinker.pay.alipay;

import vc.thinker.pay.PayConfig;

/**
 * 支付宝配制
 * @author james
 *
 */
public class AlipayConfig extends PayConfig{

	//交易安全检验码，由数字和字母组成的32位字符串
	private String safeKey;
	
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	private String partner;
	
	//应用APPID
	private String appId;
	
	// 签约支付宝账号或卖家收款支付宝帐户
	private String sellerEmail;
	
	// 商户的私钥
	// 如果签名方式设置为“0001”时，请设置该参数
	private String appPrivateKey;
	
	// 支付宝APP的公钥
	// 如果签名方式设置为“0001”时，请设置该参数
	private String appPublicKey;
	
	//支付宝公钥
	private String alipayPublicKey;
	
	// 字符编码格式 目前支持 gbk 或 utf-8
	private String inputCharset = "utf-8";
	
	// 签名方式 不需修改,无线的产品中，签名方式为rsa时，sign_type需赋值为0001而不是RSA
	private String signType = "MD5";
	
	// 访问模式,根据自己的服务器是否支持ssl访问，若支持请选择https；若不支持请选择http
	private String transport = "http";
	
	public String getAlipayPublicKey() {
		return alipayPublicKey;
	}

	public void setAlipayPublicKey(String alipayPublicKey) {
		this.alipayPublicKey = alipayPublicKey;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSafeKey() {
		return safeKey;
	}

	public void setSafeKey(String safeKey) {
		this.safeKey = safeKey;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public String getAppPrivateKey() {
		return appPrivateKey;
	}

	public void setAppPrivateKey(String appPrivateKey) {
		this.appPrivateKey = appPrivateKey;
	}

	public String getAppPublicKey() {
		return appPublicKey;
	}

	public void setAppPublicKey(String appPublicKey) {
		this.appPublicKey = appPublicKey;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
