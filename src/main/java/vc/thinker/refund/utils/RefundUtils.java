package vc.thinker.refund.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import vc.thinker.pay.PayException;
import vc.thinker.pay.weixin.WeixinConfig;
import vc.thinker.pay.weixin.utils.Configure;
import vc.thinker.pay.weixin.utils.HttpsRequest;
import vc.thinker.pay.weixin.utils.Signature;
import vc.thinker.refund.bean.BizContent;
import vc.thinker.refund.bean.RefundPackage;

/**
 * 退款相关操作
 * @author thinker
 *
 */
public class RefundUtils {

	private static Logger log = LoggerFactory.getLogger(RefundUtils.class);
	
	@SuppressWarnings("unchecked")
	public static String weixinRefund(RefundPackage refundPackage,String mch_id,String key,WeixinConfig config) throws PayException {
		
		Map<String, String> map;
		try {
			//将参数封装到 map 中
			map = BeanUtils.describe(refundPackage);
		} catch (Exception e) {
			throw new PayException(e);
		} 
		map.remove("class");

		//生成签名
		String sign = Signature.generateSign(map,key);
		//设置签名
		refundPackage.setSign(sign);
		
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.setSerializationInclusion(Include.NON_EMPTY);
		
		try{
			String xmlContent = xmlMapper.writeValueAsString(refundPackage);
			log.info("微信退款，请求的原始报文:");
			log.info(xmlContent);
			HttpsRequest httpsRequest = new HttpsRequest(mch_id,config);
			return httpsRequest.sendPost(Configure.REFUND_API, xmlContent,mch_id);
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
	
	/**
	 * 
	 * @param refundPackage 退款参数
	 * @param mch_id
	 * @param alipayPublicKey 支付宝公钥
	 * @return
	 * @throws PayException
	 */
	public static AlipayTradeRefundResponse aliRefund(AlipayClient alipayClient,RefundPackage refundPackage,String mch_id,String alipayPublicKey) throws PayException{
		
		/**
		 * 组装请求报文
		 */
		BizContent bizContent = new BizContent();
		
		bizContent.setTrade_no(refundPackage.getTrade_no());
		bizContent.setRefund_amount(refundPackage.getRefund_amount());
		bizContent.setRefund_reason("押金正常退款");
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String content = mapper.writeValueAsString(bizContent);
			
			AlipayTradeRefundRequest aliReqeust = new AlipayTradeRefundRequest();
			
			aliReqeust.setBizContent(content);
			
			AlipayTradeRefundResponse aliResponse = alipayClient.execute(aliReqeust);
			
			log.info("支付宝退款申请退款结果【"+aliResponse.isSuccess()+"】");
			return aliResponse;
			
		} catch (JsonProcessingException e) {
			log.info("支付宝退款 json 格式转换异常！" + e.getMessage());
			throw new PayException(e);
		} catch (AlipayApiException e) {
			
			log.info("支付宝退款 调用支付宝api异常,【错误code:"+e.getErrCode()+"】异常信息【"+e.getMessage()+"】");
			throw new PayException(e);
		} 
		
	}
}
