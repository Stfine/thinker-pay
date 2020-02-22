package vc.thinker.refund.bean;

import java.math.BigDecimal;

/**
 * 支付宝退款参数 bean
 * @author thinker
 *
 */
public class BizContent {

	//平台订单号
	private String out_trade_no ;
	
	//支付宝交易订单号
	private String trade_no ;
	
	//退款金额
	private BigDecimal refund_amount ;
	
	//退款条件
	private String refund_reason;
	
	//退款流水
	private String out_request_no ;

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public BigDecimal getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(BigDecimal refund_amount) {
		this.refund_amount = refund_amount;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}

	public String getOut_request_no() {
		return out_request_no;
	}

	public void setOut_request_no(String out_request_no) {
		this.out_request_no = out_request_no;
	}

}
