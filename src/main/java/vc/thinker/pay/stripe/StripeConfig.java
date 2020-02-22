package vc.thinker.pay.stripe;

import vc.thinker.pay.PayConfig;

/**
 * 支付宝配制
 * @author james
 *
 */
public class StripeConfig extends PayConfig{

	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}
