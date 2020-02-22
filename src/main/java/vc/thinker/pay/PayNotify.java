package vc.thinker.pay;

/**
 * 支付回调数据
 * @author james
 *
 */
public abstract class PayNotify {
	/**
	 * 配制标识
	 */
	private String configMark;
	
	public abstract boolean isPaySuccess();

	public String getConfigMark() {
		return configMark;
	}

	public void setConfigMark(String configMark) {
		this.configMark = configMark;
	}
}
