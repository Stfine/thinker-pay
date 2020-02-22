package vc.thinker.pay.request;

import vc.thinker.pay.PayRequest;

public class DelCardRequest extends PayRequest{
	//Customer id
	private String cusId;
	//卡号
	private String card;
	
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
}
