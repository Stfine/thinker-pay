package vc.thinker.pay;

public class PayRequest {
	/**
	 * 配制标识
	 */
	private String configMark;
	
	/**
	 * 币种，默认为人民币
	 */
	private String currency="CNY";
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getConfigMark() {
		return configMark;
	}

	public void setConfigMark(String configMark) {
		this.configMark = configMark;
	}
}
