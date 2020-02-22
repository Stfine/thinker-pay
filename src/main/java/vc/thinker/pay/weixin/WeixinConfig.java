package vc.thinker.pay.weixin;

import vc.thinker.pay.PayConfig;

/**
 * 支付宝配制
 * @author james
 *
 */
public class WeixinConfig extends PayConfig{

	private String tenpayPartner;
	
	private String wxPaysignkey;
	
	private String wxAppId;
	
	private String certLocalPath;
	
	private String payBankPublicKey;
	
	private CertificateType certificateType=CertificateType.classPath;
	
	public static enum CertificateType{
		url,classPath
	}
	
	public CertificateType getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}

	public String getCertLocalPath() {
		return certLocalPath;
	}

	public void setCertLocalPath(String certLocalPath) {
		this.certLocalPath = certLocalPath;
	}

	public String getTenpayPartner() {
		return tenpayPartner;
	}

	public void setTenpayPartner(String tenpayPartner) {
		this.tenpayPartner = tenpayPartner;
	}

	public String getWxPaysignkey() {
		return wxPaysignkey;
	}

	public void setWxPaysignkey(String wxPaysignkey) {
		this.wxPaysignkey = wxPaysignkey;
	}

	public String getWxAppId() {
		return wxAppId;
	}

	public void setWxAppId(String wxAppId) {
		this.wxAppId = wxAppId;
	}

	public String getPayBankPublicKey() {
		return payBankPublicKey;
	}

	public void setPayBankPublicKey(String payBankPublicKey) {
		this.payBankPublicKey = payBankPublicKey;
	}
}
