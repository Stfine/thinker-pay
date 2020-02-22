package vc.thinker.pay.payfast;

import vc.thinker.pay.PayConfig;

/**
 * PayFast 相关参数
 * @author ZhangGaoXiang
 * @date   2018年12月18日 下午8:28:11
 */
public class PayFastConfig extends PayConfig{

	private String merchant_id;
	
	private String merchant_key;
	
	private String passphrase;

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getMerchant_key() {
		return merchant_key;
	}

	public void setMerchant_key(String merchant_key) {
		this.merchant_key = merchant_key;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	
	
}
