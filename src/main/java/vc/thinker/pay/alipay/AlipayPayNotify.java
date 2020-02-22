package vc.thinker.pay.alipay;

import java.util.Map;

import vc.thinker.pay.PayNotify;

/**
 * 支付宝回调
 * @author james
 *
 */
public class AlipayPayNotify extends PayNotify{
	
	private String tradeNo; // 支付宝交易号
	private String outTradeNo; // 订单号
	private String totalFee;  // 总金额
	private String subject; //
	private String body;
	private String tradeStatus; // 交易状态
	private String notifyId;
	private String sign;
	/**
	 * 附加数据
	 */
	private String reqAttach;
	//所有请求参数
	private Map<String, String> params; 
	
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
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
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	
	public String getReqAttach() {
		return reqAttach;
	}
	public void setReqAttach(String reqAttach) {
		this.reqAttach = reqAttach;
	}
	@Override
	public boolean isPaySuccess() {
		if(tradeStatus == null){
			return false;
		}
		if (tradeStatus.equals("TRADE_FINISHED")
				|| tradeStatus.equals("TRADE_SUCCESS")) {
			return true;
		}
		return false;
	}
}
