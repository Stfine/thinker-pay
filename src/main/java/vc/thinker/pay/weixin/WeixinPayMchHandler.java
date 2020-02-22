package vc.thinker.pay.weixin;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.ws.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sinco.common.utils.IPUtil;

import vc.thinker.pay.PayConfigFactory;
import vc.thinker.pay.PayException;
import vc.thinker.pay.PayMchHandler;
import vc.thinker.pay.alipay.util.RSA;
import vc.thinker.pay.request.PayBankRequest;
import vc.thinker.pay.request.TransfersRequest;
import vc.thinker.pay.response.PayBankResponse;
import vc.thinker.pay.response.TransfersResponse;
import vc.thinker.pay.weixin.api.PayUtils;
import vc.thinker.pay.weixin.bean.PayBank;
import vc.thinker.pay.weixin.bean.PayBankResult;
import vc.thinker.pay.weixin.bean.Transfers;
import vc.thinker.pay.weixin.bean.TransfersResult;
import vc.thinker.pay.weixin.bean.WeixinPayNotify;
import vc.thinker.pay.weixin.utils.Configure;
import vc.thinker.pay.weixin.utils.HttpsRequest;
import vc.thinker.pay.weixin.utils.MapUtil;
import vc.thinker.pay.weixin.utils.Signature;
import vc.thinker.refund.bean.RefundPackage;

public class WeixinPayMchHandler extends PayMchHandler<WeixinConfig, WeixinPayNotify>{

	private final static Logger log=LoggerFactory.getLogger(WeixinPayMchHandler.class);
	
	private XmlMapper xmlMapper = new XmlMapper();
	
	public WeixinPayMchHandler(PayConfigFactory configFactory) {
		super(configFactory);
		xmlMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

	@Override
	public TransfersResponse transferAccounts(TransfersRequest request) throws PayException {
		WeixinConfig config=getConfig(request.getConfigMark());
		
		
		Transfers transfers=new Transfers();
		
		transfers.setMch_appid(config.getWxAppId());
		transfers.setMchid(config.getTenpayPartner());
		transfers.setPartner_trade_no(request.getOutTradeNo());
		transfers.setOpenid(request.getUserid());
		transfers.setCheck_name(request.isCheckName()?"FORCE_CHECK":"NO_CHECK");
		
		Long amount = request.getAmount().setScale(2,BigDecimal.ROUND_DOWN)
				.multiply(new BigDecimal(100)).longValue();
		transfers.setAmount(amount.toString());
		transfers.setDesc(request.getDesc());
		transfers.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		try {
			transfers.setSpbill_create_ip(IPUtil.getLocalIP());
		} catch (UnknownHostException e) {
			throw new PayException("无法得到本机IP",e);
		}
		
		String content = PayUtils.generatePayNativeReplyXML(transfers,
				config.getTenpayPartner(),
				config.getWxPaysignkey(),config);
		log.info("转账结果[{}]",content);
		try {
			TransfersResult transfersResult=xmlMapper.readValue(content, TransfersResult.class);
			
			TransfersResponse response=new TransfersResponse(config.getChannel(),config);
			
			if(transfersResult.getReturn_code().equals("SUCCESS") 
					&& transfersResult.getResult_code().equals("SUCCESS")){
				response.setSuccess(true);
				response.setPartnerTradeNo(transfersResult.getPartner_trade_no());
				response.setPaymentNo(transfersResult.getPayment_no());
				response.setPaymentTime(transfersResult.getPayment_time());
			}else{
				response.setSuccess(false);
				response.setMsg(transfersResult.getErr_code_des());
			}
			return response;
		} catch (IOException e) {
			log.error("转换支付结束错误,结果报文[{}]",content);
			throw new PayException("转换支付结束错误",e);
		}
	}

	/**
	 * 微信转账到银行卡
	 */
	public PayBankResponse payBank(PayBankRequest request) throws PayException{
		
		String content = "";
		try{
			
		WeixinConfig config=getConfig(request.getConfigMark());
		
		PayBank payBank = new PayBank();
		payBank.setMch_id(config.getTenpayPartner());
		payBank.setPartner_trade_no(request.getPartner_trade_no());
		payBank.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		
		payBank.setEnc_bank_no(RSA.encrypt(config, request.getEnc_bank_no()));
		payBank.setEnc_true_name(RSA.encrypt(config, request.getEnc_true_name()));
		
		payBank.setBank_code(request.getBank_code());
		payBank.setAmount(request.getAmount());
		payBank.setDesc(request.getDesc());
		
		content = PayUtils.generatePayBankReplyXML(payBank,
				config.getTenpayPartner(),
				config.getWxPaysignkey(),config);
		
			
		PayBankResult  result = xmlMapper.readValue(content, PayBankResult.class);
		
		PayBankResponse respoonse = new PayBankResponse(config.getChannel(), config);
		
		if("SUCCESS".equals(result.getReturn_code())  && "SUCCESS".equals(result.getResult_code())){
			respoonse.setSuccess(true);
			respoonse.setPartner_trade_no(result.getPartner_trade_no());
			respoonse.setPayment_no(result.getPayment_no());
			respoonse.setCmms_amt(result.getCmms_amt());
		}else{
			respoonse.setSuccess(false);
			respoonse.setMsg(result.getErr_code_des());
		}
		return respoonse;
			
			
		}catch (Exception e) {
			log.error("微信转账银行卡错误,结果报文[{}]",content);
			throw new PayException("微信转账银行卡错误",e);
		}
	}
	
	/**
	 * 微信转银行卡 获取公钥
	 * @return
	 * @throws PayException
	 */
	public Map<String, String> getPublieKey() throws PayException {
		String payment = "wx_js";
		WeixinConfig config= getConfig(payment);
		
		String mch_id = config.getTenpayPartner(); 
		String nonce_str = UUID.randomUUID().toString().replace("-", "");
		String sign_type = "MD5";
		Map<String, String> param  =  new LinkedHashMap<>();
		param.put("mch_id", config.getTenpayPartner());
		param.put("nonce_str", nonce_str);
		param.put("sign_type", sign_type);
		
		param.remove("class");
		
		RefundPackage refundPackage = new RefundPackage();
		refundPackage.setMch_id(config.getTenpayPartner());
		refundPackage.setNonce_str(nonce_str);
		
		String sign = Signature.generateSign(param,config.getWxPaysignkey());
		
		refundPackage.setSign(sign);
		refundPackage.setSign_type(sign_type);
		
		try{
			
			XmlMapper xmlMapper = new XmlMapper();
			xmlMapper.setSerializationInclusion(Include.NON_EMPTY);
			
			String xmlContent = xmlMapper.writeValueAsString(refundPackage);
			
			HttpsRequest httpsRequest = new HttpsRequest(mch_id,config);
			
			System.out.println(xmlContent);
			
			String result = httpsRequest.sendPost(Configure.TRANSFER_GETPUBLICKEY_API, xmlContent,mch_id);
			
			Map<String, String> resu_map = MapUtil.weixinXmlToMap(result);
			
			return resu_map;
			
		}catch (JsonProcessingException e) {
			throw new PayException(e);
		} catch (UnrecoverableKeyException e) {
			throw new PayException(e);
		} catch (KeyManagementException e) {
			throw new PayException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new PayException(e);
		} catch (KeyStoreException e) {
			throw new PayException(e);
		} catch (IOException e) {
			throw new PayException(e);
		}
	}

}
