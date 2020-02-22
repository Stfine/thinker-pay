package vc.thinker.pay.request;

import java.math.BigDecimal;

import vc.thinker.pay.PayRequest;

public class TransfersRequest extends PayRequest {
	/**
	 * 外部订单号
	 */
	private String outTradeNo;
	/**
	 * 金额
	 */
	private BigDecimal amount;
	
	/**
	 * 用户标识
	 */
	private String userid;
	
	/**
	 * 是否检验用户名称
	 */
	private boolean isCheckName=false;
	
	/**
	 * 收款用户真实姓名。 
		如果isCheckName设置为true，则必填用户真实姓名
	 */
	private String reUserName;
	
	private String desc;
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getReUserName() {
		return reUserName;
	}
	public void setReUserName(String reUserName) {
		this.reUserName = reUserName;
	}
	public boolean isCheckName() {
		return isCheckName;
	}
	public void setCheckName(boolean isCheckName) {
		this.isCheckName = isCheckName;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
