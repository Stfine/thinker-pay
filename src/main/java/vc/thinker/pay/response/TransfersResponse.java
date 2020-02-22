package vc.thinker.pay.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import vc.thinker.pay.PayChannel;
import vc.thinker.pay.PayConfig;
import vc.thinker.pay.PayResponse;

public class TransfersResponse extends PayResponse{

	public TransfersResponse(PayChannel channel, PayConfig payConfig) {
		super(channel, payConfig);
	}
	
	private String partnerTradeNo;
	private String paymentNo;
	private String paymentTime;
	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}
	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}
	public String getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	public String getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}
}