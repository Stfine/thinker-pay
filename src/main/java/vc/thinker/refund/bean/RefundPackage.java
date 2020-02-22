package vc.thinker.refund.bean;

import java.math.BigDecimal;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "xml")
public class RefundPackage {

		private String appid;
		
		private String mch_id;
		
		private String nonce_str;
		
		private String sign;
		
		private String sign_type;
		
		//微信订单号
		private String transaction_id;
		
		//商户订单号
		private String  out_trade_no ;
		
		//商户退款单号
		private String out_refund_no;
		
		//订单总金额
		private Integer total_fee;
		
		//退款金额
		private Integer refund_fee;
		
		//操作员 默认为商户号
		private String op_user_id;

		/*
		 * 微信退款资金来源 
		 * REFUND_SOURCE_RECHARGE_FUNDS  可用余额退款
		 * REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款
		 */
		private String refund_account;
		
		//支付宝交易订单号
		private String trade_no;
		
		//退款金额
		private BigDecimal refund_amount ;
		
		//时间戳
		private String timestamp;
		
		//支付宝私有key
		private String app_private_key;
		
		//支付宝共有key
		private String app_public_key;
		
		
		public String getAppid() {
			return appid;
		}

		public void setAppid(String appid) {
			this.appid = appid;
		}

		public String getMch_id() {
			return mch_id;
		}

		public void setMch_id(String mch_id) {
			this.mch_id = mch_id;
		}

		public String getNonce_str() {
			return nonce_str;
		}

		public void setNonce_str(String nonce_str) {
			this.nonce_str = nonce_str;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getTransaction_id() {
			return transaction_id;
		}

		public void setTransaction_id(String transaction_id) {
			this.transaction_id = transaction_id;
		}

		public String getOut_trade_no() {
			return out_trade_no;
		}

		public void setOut_trade_no(String out_trade_no) {
			this.out_trade_no = out_trade_no;
		}

		public String getOut_refund_no() {
			return out_refund_no;
		}

		public void setOut_refund_no(String out_refund_no) {
			this.out_refund_no = out_refund_no;
		}

		public Integer getTotal_fee() {
			return total_fee;
		}

		public void setTotal_fee(Integer total_fee) {
			this.total_fee = total_fee;
		}

		public Integer getRefund_fee() {
			return refund_fee;
		}

		public void setRefund_fee(Integer refund_fee) {
			this.refund_fee = refund_fee;
		}

		public String getOp_user_id() {
			return op_user_id;
		}

		public void setOp_user_id(String op_user_id) {
			this.op_user_id = op_user_id;
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
		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getApp_private_key() {
			return app_private_key;
		}

		public void setApp_private_key(String app_private_key) {
			this.app_private_key = app_private_key;
		}

		public String getApp_public_key() {
			return app_public_key;
		}

		public void setApp_public_key(String app_public_key) {
			this.app_public_key = app_public_key;
		}

		public String getRefund_account() {
			return refund_account;
		}

		public void setRefund_account(String refund_account) {
			this.refund_account = refund_account;
		}

		public String getSign_type() {
			return sign_type;
		}

		public void setSign_type(String sign_type) {
			this.sign_type = sign_type;
		}
		
}
