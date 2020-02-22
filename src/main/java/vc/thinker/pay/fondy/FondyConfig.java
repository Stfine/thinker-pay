package vc.thinker.pay.fondy;

import vc.thinker.pay.PayConfig;

/**
 *
 * @author ZhangGaoXiang
 * @time Dec 14, 20195:00:40 PM
 */
public class FondyConfig extends PayConfig{

	private String merchant_id;
	
	private String primaryKey;

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
}
