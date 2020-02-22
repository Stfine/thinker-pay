package vc.thinker.pay.request;

import vc.thinker.pay.PayRequest;

public class FindCardRequest extends PayRequest{

	//Customer id
	private String cusId;

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	} 
	
}
