package vc.thinker.pay.payfast;

/**
 * payfast 支付相关参数
 * @author ZhangGaoXiang
 * @date   2018年12月20日 上午10:04:51
 */
public class PayFastPayPackage {

	private String merchant_id;
	private String merchant_key;
	private String return_url;
	private String cancel_url;
	private String notify_url;
	private String cell_number;
	private String m_payment_id;
	private String amount;
	private String item_name;
	private String item_description;
	private String custom_str1;
	private String payment_method;
	private String subscription_type;
	private String signature;
	 
	public String getMerchant_id() {
		return merchant_id;
	}
	 
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	 
	public String getMerchant_key() {
		return merchant_key;
	}
	 
	public void setMerchant_key(String merchant_key) {
		this.merchant_key = merchant_key;
	}
	 
	public String getReturn_url() {
		return return_url;
	}
	 
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	 
	public String getCancel_url() {
		return cancel_url;
	}
	 
	public void setCancel_url(String cancel_url) {
		this.cancel_url = cancel_url;
	}
	 
	public String getNotify_url() {
		return notify_url;
	}
	 
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	
	public String getCell_number() {
		return cell_number;
	}

	public void setCell_number(String cell_number) {
		this.cell_number = cell_number;
	}

	public String getM_payment_id() {
		return m_payment_id;
	}
	 
	public void setM_payment_id(String m_payment_id) {
		this.m_payment_id = m_payment_id;
	}
	 
	public String getAmount() {
		return amount;
	}
	 
	public void setAmount(String amount) {
		this.amount = amount;
	}
	 
	public String getItem_name() {
		return item_name;
	}
	 
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
 
	public String getItem_description() {
		return item_description;
	}
 
	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}
 
	public String getCustom_str1() {
		return custom_str1;
	}
 
	public void setCustom_str1(String custom_str1) {
		this.custom_str1 = custom_str1;
	}
	 
	public String getPayment_method() {
		return payment_method;
	}
	 
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	 
	public String getSubscription_type() {
		return subscription_type;
	}
	 
	public void setSubscription_type(String subscription_type) {
		this.subscription_type = subscription_type;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
