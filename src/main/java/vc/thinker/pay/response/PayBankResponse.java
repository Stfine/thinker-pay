package vc.thinker.pay.response;

import vc.thinker.pay.PayChannel;
import vc.thinker.pay.PayConfig;
import vc.thinker.pay.PayResponse;

public class PayBankResponse extends PayResponse{

	public PayBankResponse(PayChannel channel, PayConfig payConfig) {
		super(channel, payConfig);
	}
	
	public String partner_trade_no;
	
	private String payment_no;
	
	private int cmms_amt;

	public String getPartner_trade_no() {
		return partner_trade_no;
	}

	public void setPartner_trade_no(String partner_trade_no) {
		this.partner_trade_no = partner_trade_no;
	}

	public String getPayment_no() {
		return payment_no;
	}

	public void setPayment_no(String payment_no) {
		this.payment_no = payment_no;
	}

	public int getCmms_amt() {
		return cmms_amt;
	}

	public void setCmms_amt(int cmms_amt) {
		this.cmms_amt = cmms_amt;
	}
	

}
