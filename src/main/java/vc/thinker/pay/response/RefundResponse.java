package vc.thinker.pay.response;

import vc.thinker.pay.PayChannel;
import vc.thinker.pay.PayConfig;
import vc.thinker.pay.PayResponse;

public class RefundResponse extends PayResponse{

	public RefundResponse(PayChannel channel, PayConfig payConfig) {
		super(channel, payConfig);
	}
	
	/**
	 * 退款的定单号
	 */
	private String outOrderId;
	
	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}
}
