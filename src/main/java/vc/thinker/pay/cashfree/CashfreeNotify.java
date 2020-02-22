package vc.thinker.pay.cashfree;
/**
 *
 * @author ZhangGaoXiang
 * @time Sep 4, 201911:23:52 AM
 */

import vc.thinker.pay.PayNotify;

public class CashfreeNotify extends PayNotify {
	@Override
	public boolean isPaySuccess() {
		return false;
	}

	private String orderId;
	private String referenceId;
	private String orderAmount;
	private String txStatus;
	private String txMsg;
	private String txTime;
	private String paymentMode;
	private String signature;
	private String paySuccess;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getTxStatus() {
		return txStatus;
	}

	public void setTxStatus(String txStatus) {
		this.txStatus = txStatus;
	}

	public String getTxMsg() {
		return txMsg;
	}

	public void setTxMsg(String txMsg) {
		this.txMsg = txMsg;
	}

	public String getTxTime() {
		return txTime;
	}

	public void setTxTime(String txTime) {
		this.txTime = txTime;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPaySuccess() {
		return paySuccess;
	}

	public void setPaySuccess(String paySuccess) {
		this.paySuccess = paySuccess;
	}
	
}
