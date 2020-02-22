package vc.thinker.pay.cashfree;

import vc.thinker.pay.PayConfig;

/**
 *
 * @author ZhangGaoXiang
 * @time Sep 4, 201911:23:23 AM
 */
public class CashfreeConfig extends PayConfig{

	private String appId;
	
	private String secretKey;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
}
