package vc.thinker.pay;

/**
 * 支付配制
 * @author james
 *
 */
public class PayConfig {
	
	/**
	 * 配制标识
	 */
	private String mark;
	
	/**
	 * 支付渠道
	 */
	private PayChannel channel;
	
	public PayChannel getChannel() {
		return channel;
	}

	public void setChannel(PayChannel channel) {
		this.channel = channel;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}
