package vc.thinker.pay.request;

import vc.thinker.pay.PayRequest;

public class CaptureRequest extends PayRequest{
	//订单号
	private String orderNo ;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
