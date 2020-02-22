package vc.thinker.pay.weixin.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import vc.thinker.pay.PayNotify;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "xml")  
public class WeixinPayNotify extends PayNotify{

	@XmlElement  
	private String appid;

	@XmlElement  
	private String attach;

	@XmlElement
	private String bank_type;
	@XmlElement
	private String cash_fee;
	@XmlElement
	private String cash_fee_type;
	@XmlElement
	private String fee_type;
	@XmlElement
	private String is_subscribe;
	@XmlElement
	private String mch_id;
	@XmlElement
	private String nonce_str;
	@XmlElement
	private String openid;
	@XmlElement
	private String out_trade_no;
	@XmlElement
	private String result_code;
	@XmlElement
	private String return_code;
	@XmlElement
	private String sign;
	@XmlElement
	private String time_end;
	@XmlElement
	private String total_fee;
	@XmlElement
	private String trade_type;
	@XmlElement
	private String transaction_id;
	@XmlElement
	private String coupon_fee;
	@XmlElement
	private String coupon_count;
	@XmlElement
	private String coupon_id_0;
	@XmlElement
	private String coupon_type_0;
	@XmlElement
	private String coupon_fee_0;
	@XmlElement
	private String coupon_id_1;
	@XmlElement
	private String coupon_fee_1;
	@XmlElement
	private String coupon_type_1;
	@XmlElement
	private String coupon_id_2;
	@XmlElement
	private String coupon_fee_2;
	@XmlElement
	private String coupon_type_2;
	@XmlElement
	private String coupon_id_3;
	@XmlElement
	private String coupon_fee_3;
	@XmlElement
	private String coupon_type_3;
	@XmlElement
	private String coupon_id_4;
	@XmlElement
	private String coupon_fee_4;
	@XmlElement
	private String coupon_type_4;
	
	private boolean paySuccess;
	@XmlElement
	private String device_info;
	
	//所有请求参数
	private Map<String, String> params; 
	
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getCoupon_id_3() {
		return coupon_id_3;
	}

	public void setCoupon_id_3(String coupon_id_3) {
		this.coupon_id_3 = coupon_id_3;
	}

	public String getCoupon_fee_3() {
		return coupon_fee_3;
	}

	public void setCoupon_fee_3(String coupon_fee_3) {
		this.coupon_fee_3 = coupon_fee_3;
	}

	public String getCoupon_type_3() {
		return coupon_type_3;
	}

	public void setCoupon_type_3(String coupon_type_3) {
		this.coupon_type_3 = coupon_type_3;
	}

	public String getCoupon_id_4() {
		return coupon_id_4;
	}

	public void setCoupon_id_4(String coupon_id_4) {
		this.coupon_id_4 = coupon_id_4;
	}

	public String getCoupon_fee_4() {
		return coupon_fee_4;
	}

	public void setCoupon_fee_4(String coupon_fee_4) {
		this.coupon_fee_4 = coupon_fee_4;
	}

	public String getCoupon_type_4() {
		return coupon_type_4;
	}

	public void setCoupon_type_4(String coupon_type_4) {
		this.coupon_type_4 = coupon_type_4;
	}

	public String getCash_fee_type() {
		return cash_fee_type;
	}

	public void setCash_fee_type(String cash_fee_type) {
		this.cash_fee_type = cash_fee_type;
	}

	public String getCoupon_fee() {
		return coupon_fee;
	}

	public void setCoupon_fee(String coupon_fee) {
		this.coupon_fee = coupon_fee;
	}

	public String getCoupon_count() {
		return coupon_count;
	}

	public void setCoupon_count(String coupon_count) {
		this.coupon_count = coupon_count;
	}

	public String getCoupon_id_0() {
		return coupon_id_0;
	}

	public void setCoupon_id_0(String coupon_id_0) {
		this.coupon_id_0 = coupon_id_0;
	}

	public String getCoupon_type_0() {
		return coupon_type_0;
	}

	public void setCoupon_type_0(String coupon_type_0) {
		this.coupon_type_0 = coupon_type_0;
	}

	public String getCoupon_fee_0() {
		return coupon_fee_0;
	}

	public void setCoupon_fee_0(String coupon_fee_0) {
		this.coupon_fee_0 = coupon_fee_0;
	}

	public String getCoupon_id_1() {
		return coupon_id_1;
	}

	public void setCoupon_id_1(String coupon_id_1) {
		this.coupon_id_1 = coupon_id_1;
	}

	public String getCoupon_fee_1() {
		return coupon_fee_1;
	}

	public void setCoupon_fee_1(String coupon_fee_1) {
		this.coupon_fee_1 = coupon_fee_1;
	}

	public String getCoupon_type_1() {
		return coupon_type_1;
	}

	public void setCoupon_type_1(String coupon_type_1) {
		this.coupon_type_1 = coupon_type_1;
	}

	public String getCoupon_id_2() {
		return coupon_id_2;
	}

	public void setCoupon_id_2(String coupon_id_2) {
		this.coupon_id_2 = coupon_id_2;
	}

	public String getCoupon_fee_2() {
		return coupon_fee_2;
	}

	public void setCoupon_fee_2(String coupon_fee_2) {
		this.coupon_fee_2 = coupon_fee_2;
	}

	public String getCoupon_type_2() {
		return coupon_type_2;
	}

	public void setCoupon_type_2(String coupon_type_2) {
		this.coupon_type_2 = coupon_type_2;
	}

	/**
	 * 请求附加属性
	 */
	private String reqAttach;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}



	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public boolean isPaySuccess() {
		return paySuccess;
	}

	public void setPaySuccess(boolean paySuccess) {
		this.paySuccess = paySuccess;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(String cash_fee) {
		this.cash_fee = cash_fee;
	}

	public String getReqAttach() {
		return reqAttach;
	}

	public void setReqAttach(String reqAttach) {
		this.reqAttach = reqAttach;
	}
}
