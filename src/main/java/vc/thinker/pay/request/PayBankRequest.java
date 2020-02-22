package vc.thinker.pay.request;

import vc.thinker.pay.PayRequest;

/**
 * 微信转账银行卡
 * @author thinker
 *
 */
public class PayBankRequest extends PayRequest{

	private String partner_trade_no; //商户企业付款单号 只允许数字[0~9]或字母[A~Z]和[a~z]，最短8位，最长32位
	
	private String enc_bank_no; //收款方银行卡号
	
	private String enc_true_name; //收款方用户名
	
	private String bank_code; //收款方开户行
	
	private int amount; //付款金额
	
	private String desc; //付款说明

	public String getPartner_trade_no() {
		return partner_trade_no;
	}

	public void setPartner_trade_no(String partner_trade_no) {
		this.partner_trade_no = partner_trade_no;
	}

	public String getEnc_bank_no() {
		return enc_bank_no;
	}

	public void setEnc_bank_no(String enc_bank_no) {
		this.enc_bank_no = enc_bank_no;
	}

	public String getEnc_true_name() {
		return enc_true_name;
	}

	public void setEnc_true_name(String enc_true_name) {
		this.enc_true_name = enc_true_name;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
