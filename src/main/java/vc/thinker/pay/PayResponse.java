package vc.thinker.pay;

public class PayResponse {
	
	public PayResponse(PayChannel channel,PayConfig payConfig){
		this.channel=channel;
		this.payConfig=payConfig;
	}

	/**
	 * 错误消息
	 */
	private String msg;
	
	/**
	 * 是否成功
	 */
	private boolean success;
	
	/**
	 * 支付渠道
	 */
	private PayChannel channel;
	
	private PayConfig payConfig;
	
	public PayConfig getPayConfig() {
		return payConfig;
	}

	public void setPayConfig(PayConfig payConfig) {
		this.payConfig = payConfig;
	}

	public PayChannel getChannel() {
		return channel;
	}

	public void setChannel(PayChannel channel) {
		this.channel = channel;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
