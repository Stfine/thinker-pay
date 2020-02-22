package vc.thinker.pay.response;

import java.math.BigDecimal;

import vc.thinker.pay.PayChannel;
import vc.thinker.pay.PayConfig;
import vc.thinker.pay.PayResponse;
import vc.thinker.pay.payfast.PayFastPayPackage;

public class DirectPayResponse extends PayResponse {

	public DirectPayResponse(PayChannel channel,PayConfig payConfig) {
		super(channel,payConfig);
	}
	
	/**
	 * 支付返回的内容
	 */
	private String content;
	
	/**
	 * 用户支付金额
	 */
	private BigDecimal totalFee;
	
	/**
	 * 支付的定单号
	 */
	private String payOrderId;
	
	/**
	 * payfast 支付的相关参数
	 */
	private PayFastPayPackage payPackage;
	
	private String notifyUrl;
	
	private String orderId;
	
	private String orderAmount;
	
	private String orderCurrency;
	
	private String customerPhone;
	
	private String currenct;
	
	private Boolean paySuccessful;
	
	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public PayFastPayPackage getPayPackage() {
		return payPackage;
	}

	public void setPayPackage(PayFastPayPackage payPackage) {
		this.payPackage = payPackage;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getOrderCurrency() {
		return orderCurrency;
	}

	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}

	public String getCurrenct() {
		return currenct;
	}

	public void setCurrenct(String currenct) {
		this.currenct = currenct;
	}

	public Boolean getPaySuccessful() {
		return paySuccessful;
	}

	public void setPaySuccessful(Boolean paySuccessful) {
		this.paySuccessful = paySuccessful;
	}
	
}
