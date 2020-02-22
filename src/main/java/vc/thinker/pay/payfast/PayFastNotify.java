package vc.thinker.pay.payfast;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import vc.thinker.pay.PayNotify;

public class PayFastNotify extends PayNotify{

	// 商户订单号
	private String mPaymentId;
	// payfast 订单号
	private String pfPaymentId;
	// 支付状态
	private String paymentStaus;
	// 名称
	private String itemName;
	// 描述
	private String itemDescription;
	// 实际支付金额
	private String amountGross;
	// 手续费
	private String amountFee;
	// 商户收入金额
	private String amountNet;
	// 自定义字符串
	private String ccustomStr1;
	
	private String nameFirst;
	private String nameLast;
	private String emailAddress;
	private String merchantId;
	private String token;
	private String signature;
	
	//待签名的参数
	private String params;

	public String getmPaymentId() {
		return mPaymentId;
	}
	public void setmPaymentId(String mPaymentId) {
		this.mPaymentId = mPaymentId;
	}

	public String getPfPaymentId() {
		return pfPaymentId;
	}
	public void setPfPaymentId(String pfPaymentId) {
		this.pfPaymentId = pfPaymentId;
	}
	public String getPaymentStaus() {
		return paymentStaus;
	}
	public void setPaymentStaus(String paymentStaus) {
		this.paymentStaus = paymentStaus;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getAmountGross() {
		return amountGross;
	}
	public void setAmountGross(String amountGross) {
		this.amountGross = amountGross;
	}
	public String getAmountFee() {
		return amountFee;
	}
	public void setAmountFee(String amountFee) {
		this.amountFee = amountFee;
	}
	public String getAmountNet() {
		return amountNet;
	}
	public void setAmountNet(String amountNet) {
		this.amountNet = amountNet;
	}
	public String getCcustomStr1() {
		return ccustomStr1;
	}
	public void setCcustomStr1(String ccustomStr1) {
		this.ccustomStr1 = ccustomStr1;
	}
	public String getNameFirst() {
		return nameFirst;
	}
	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}
	public String getNameLast() {
		return nameLast;
	}
	public void setNameLast(String nameLast) {
		this.nameLast = nameLast;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	@Override
	public boolean isPaySuccess() {
		
		if(StringUtils.isBlank(paymentStaus)) {
			return false;
		}
		if("COMPLETE".equals(paymentStaus)) {
			return true;
		}
		return false;
	}
}
