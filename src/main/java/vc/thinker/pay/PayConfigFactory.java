package vc.thinker.pay;
/**
 * 支付配制工厂
 * @author james
 *
 */
public interface PayConfigFactory {
	/**
	 * 得到支付配制
	 * @param mark
	 * @return
	 */
	public PayConfig getPayConfig(String mark);
	
	/**
	 * 根据标识得到支付渠道
	 * @param mark
	 * @return
	 */
	public PayChannel getPayChannel(String mark);
}
