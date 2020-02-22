package vc.thinker.pay.response;

import vc.thinker.pay.PayChannel;
import vc.thinker.pay.PayConfig;
import vc.thinker.pay.PayResponse;

public class ZhimaAuthonResponse extends PayResponse{

	public ZhimaAuthonResponse(PayChannel channel, PayConfig payConfig) {
		super(channel, payConfig);
	}

	private String errorCode;
	
	private String errorMsg;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
