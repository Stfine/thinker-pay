package vc.thinker.pay.fondy;

import vc.thinker.pay.PayNotify;

/**
 *
 * @author ZhangGaoXiang
 * @time Dec 14, 20195:01:02 PM
 */
public class FondyNotify extends PayNotify{

	private String signature;
	
	private String payment_id;
	
	private String buildStr;
	
	private String order_id;
	
	private String currency;
	
	private String amount;
	
	private Boolean success;
	
	private String order_status;
	
	private String rectoken;
	
	private String rectoken_lifetime;
	
	private String masked_card;
	
	@Override
	public boolean isPaySuccess() {
		return false;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}

	public String getBuildStr() {
		return buildStr;
	}

	public void setBuildStr(String buildStr) {
		this.buildStr = buildStr;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getRectoken() {
		return rectoken;
	}

	public void setRectoken(String rectoken) {
		this.rectoken = rectoken;
	}

	public String getRectoken_lifetime() {
		return rectoken_lifetime;
	}

	public void setRectoken_lifetime(String rectoken_lifetime) {
		this.rectoken_lifetime = rectoken_lifetime;
	}

	public String getMasked_card() {
		return masked_card;
	}

	public void setMasked_card(String masked_card) {
		this.masked_card = masked_card;
	}
	
}
