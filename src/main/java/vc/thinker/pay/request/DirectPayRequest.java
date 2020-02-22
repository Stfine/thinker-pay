package vc.thinker.pay.request;

import java.math.BigDecimal;

import vc.thinker.pay.PayRequest;

public class DirectPayRequest extends PayRequest {
	/**
	 * 外部订单号
	 */
	private String outTradeNo;
	
	/**
	 * 订单名称
	 */
	private String subject;
	
	/**
	 * 订单描述、订单详细、订单备注
	 */
	private String body;
	
	/**
	 * 订单金额，
	 */
	private BigDecimal totalFee;
	
	/**
	 * 通知地址
	 */
	private String notifyUrl;
	
	/**
	 * 回调地址
	 */
	private String returnUrl;
	
	/**
	 * 目前只有支付宝支持,默认银行
	 */
	private String defaultbank;
	
	/**
	 * 附加数据
	 */
	private String reqAttach;
	
	//是否锁定
	private Boolean capture;
	
	private String mobile;
	
	private String cancelUrl;
	
	private String token;
	
	private String customerName;
	
	private String customerPhone;
	
	private String customerEmail;
	
	public Boolean getCapture() {
		return capture;
	}

	public void setCapture(Boolean capture) {
		this.capture = capture;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getDefaultbank() {
		return defaultbank;
	}

	public void setDefaultbank(String defaultbank) {
		this.defaultbank = defaultbank;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getReqAttach() {
		return reqAttach;
	}

	public void setReqAttach(String reqAttach) {
		this.reqAttach = reqAttach;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	
	
}
