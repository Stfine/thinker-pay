package vc.thinker.pay.weixin;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vc.thinker.pay.PayConfigFactory;
import vc.thinker.pay.PayException;
import vc.thinker.pay.PayHandler;
import vc.thinker.pay.request.DirectPayRequest;
import vc.thinker.pay.response.DirectPayResponse;
import vc.thinker.pay.response.RefundResponse;
import vc.thinker.pay.weixin.api.PayUtils;
import vc.thinker.pay.weixin.bean.PayPackage;
import vc.thinker.pay.weixin.bean.WeixinPayNotify;
import vc.thinker.pay.weixin.utils.MapUtil;
import vc.thinker.pay.weixin.utils.Signature;
import vc.thinker.refund.bean.RefundPackage;
import vc.thinker.refund.bean.RefundRequest;
import vc.thinker.refund.utils.RefundUtils;

/**
 * 支付宝的方法处理
 * @author james
 *
 */
public class WeixinPayHandler extends PayHandler<WeixinConfig,WeixinPayNotify>{
	
	private final static Logger log=LoggerFactory.getLogger(WeixinPayHandler.class);
	
	public WeixinPayHandler(PayConfigFactory configFactory) {
		super(configFactory);
	}

	@Override
	public DirectPayResponse webPay(DirectPayRequest request) throws PayException{
		
		WeixinConfig config=getConfig(request.getConfigMark());
		
		PayPackage payPackage = new PayPackage();
		payPackage.setAppid(config.getWxAppId());

		payPackage.setMch_id(config.getTenpayPartner());
		payPackage.setDevice_info("WEB");
		payPackage.setTrade_type("NATIVE");
		payPackage.setSpbill_create_ip(localIp());
		payPackage.setProduct_id(request.getSubject());
		payPackage.setBody(request.getBody());
		payPackage.setAttach(getAttachInput(request, config.getMark()));
		payPackage.setOut_trade_no(request.getOutTradeNo());
		
		Long totalFee = new BigDecimal(String.valueOf(request.getTotalFee()))
				.setScale(2,BigDecimal.ROUND_DOWN)
				.multiply(new BigDecimal(100)).longValue();
		
		payPackage.setTotal_fee(totalFee.toString());
		payPackage.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		payPackage.setNotify_url(request.getNotifyUrl());
		String content = PayUtils.generatePayNativeReplyXML(payPackage,
				config.getTenpayPartner(),
				config.getWxPaysignkey(),config);
		
		Document doc=Jsoup.parse(content, "", Parser.xmlParser());
		
	 	DirectPayResponse response=new DirectPayResponse(config.getChannel(),config);
	 	response.setTotalFee(request.getTotalFee());
	 	if(doc.select("return_code").text().equals("SUCCESS") 
	 			&& doc.select("return_msg").text().equals("OK")){
	 		response.setSuccess(true);
	 		response.setContent(doc.select("code_url").text());
	 	}else{
	 		response.setSuccess(false);
	 		response.setMsg(doc.select("return_msg").text());
	 	}
	 	return response;
	}

	@Override
	public WeixinPayNotify getPayNotify(HttpServletRequest request) throws PayException {
		try {
			
			WeixinPayNotify payCallbackNotify = PayUtils.payCallbackNotify(request.getInputStream());
			Map<String, String> attachMap=getAttachOut(payCallbackNotify.getAttach());
			payCallbackNotify.setConfigMark(attachMap.get(ATTACH_KEY_CONFIG_MARK));
			payCallbackNotify.setReqAttach(attachMap.get(ATTACH_KEY_REQ_ATTACH));
			return payCallbackNotify;
		} catch (IOException e) {
			throw new PayException(e);
		}
	}

	@Override
	public boolean verifyNotify(WeixinPayNotify payNotify) throws PayException {
		
		WeixinConfig config=getConfig(payNotify.getConfigMark());
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = payNotify.getParams();
		map.remove("class");
		map.remove("paySuccess");
		map.put("sign", "");
		map.remove("configMark");
		map.remove("reqAttach");
		map.remove("#text");

		String sign = Signature.generateSign(map, config.getWxPaysignkey());
		
		return payNotify.getSign().equals(sign) ? true : false;
	}
	
	@Override
	public DirectPayResponse appPay(DirectPayRequest request) throws PayException {
		WeixinConfig config=getConfig(request.getConfigMark());
		
		Long totalFee = new BigDecimal(String.valueOf(request.getTotalFee()))
				.setScale(2,BigDecimal.ROUND_DOWN)
				.multiply(new BigDecimal(100)).longValue();
		
		PayPackage payPackage = new PayPackage();
		payPackage.setAppid(config.getWxAppId());

		payPackage.setMch_id(config.getTenpayPartner());
		//payPackage.setDevice_info("WEB");
		payPackage.setTrade_type("APP");
		payPackage.setSpbill_create_ip(localIp());
		payPackage.setProduct_id(request.getSubject());
		payPackage.setBody(request.getBody());
		payPackage.setAttach(getAttachInput(request, config.getMark()));
		payPackage.setOut_trade_no(request.getOutTradeNo());
		payPackage.setTotal_fee(totalFee.toString());//total_fee
		payPackage.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		payPackage.setNotify_url(request.getNotifyUrl());
		String content = PayUtils.generatePayNativeReplyXML(payPackage,
				config.getTenpayPartner(),
				config.getWxPaysignkey(),config);
		
		DirectPayResponse response=new DirectPayResponse(config.getChannel(),config);
		response.setTotalFee(request.getTotalFee());
		response.setSuccess(true);
		response.setContent(content);
		return response;
	}
	
	 /**
     * 获取本机Ip
     *
     *  通过 获取系统所有的networkInterface网络接口 然后遍历 每个网络下的InterfaceAddress组。
     *  获得符合 <code>InetAddress instanceof Inet4Address</code> 条件的一个IpV4地址
     * @return
     */
    @SuppressWarnings("rawtypes")
    private String localIp(){
        String ip = null;
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
                for (InterfaceAddress add : InterfaceAddress) {
                    InetAddress Ip = add.getAddress();
                    if (Ip != null && Ip instanceof Inet4Address) {
                        ip = Ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
        }
        return ip;
    }

	@Override
	public DirectPayResponse jsPay(DirectPayRequest request,String openid) throws PayException {
		WeixinConfig config=getConfig(request.getConfigMark());
		
		Long totalFee = new BigDecimal(String.valueOf(request.getTotalFee()))
				.setScale(2,BigDecimal.ROUND_DOWN)
				.multiply(new BigDecimal(100)).longValue();
		
		PayPackage payPackage = new PayPackage();
		payPackage.setAppid(config.getWxAppId());

		payPackage.setMch_id(config.getTenpayPartner());
		payPackage.setDevice_info("WEB");
		payPackage.setTrade_type("JSAPI");
		payPackage.setOpenid(openid);
		payPackage.setSpbill_create_ip(localIp());
		payPackage.setProduct_id(request.getSubject());
		payPackage.setBody(request.getBody());
		payPackage.setAttach(getAttachInput(request, config.getMark()));
		payPackage.setOut_trade_no(request.getOutTradeNo());
		payPackage.setTotal_fee(totalFee.toString());//total_fee
		payPackage.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		payPackage.setNotify_url(request.getNotifyUrl());
		String content = PayUtils.generatePayNativeReplyXML(payPackage,
				config.getTenpayPartner(),
				config.getWxPaysignkey(),config);
		log.info("weixin js pay content:"+content);
		
		DirectPayResponse response=new DirectPayResponse(config.getChannel(),config);
		
		Document doc = Jsoup.parse(content, "", Parser.xmlParser());
		String returnCode=doc.select("return_code").text();
		String resultCode=doc.select("result_code").text();
		if("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)){
			response.setSuccess(true);
		}else{
			response.setSuccess(false);
			response.setMsg(doc.select("err_code_des").text());
		}
		response.setContent(content);
		return response;
	}
	
	public static String generatePaySuccessReplyXML(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<xml>")
					.append("<return_code><![CDATA[SUCCESS]]></return_code>")
					.append("<return_msg><![CDATA[OK]]></return_msg>")
					.append("</xml>");
		return stringBuffer.toString();
	}

	/**
	 * 退款
	 */
	@Override
	public RefundResponse refund(RefundRequest request) throws PayException {
		
		//获取退款配置
		WeixinConfig config= getConfig(request.getConfigMark());
		
		RefundPackage refundPackage = new RefundPackage();
		//appid
		refundPackage.setAppid(config.getWxAppId());
		//商户号
		refundPackage.setMch_id(config.getTenpayPartner());
		
		String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
		refundPackage.setNonce_str(nonce_str);
		
		refundPackage.setTransaction_id(request.getTransaction_id());
		refundPackage.setOut_trade_no(request.getOut_trade_no());
		
		refundPackage.setOut_refund_no(request.getOut_refund_no());
		refundPackage.setTotal_fee(request.getTotal_fee());
		refundPackage.setRefund_fee(request.getRefund_fee());
		refundPackage.setOp_user_id(config.getTenpayPartner());
		refundPackage.setRefund_account(request.getRefund_account());
		
		String content = RefundUtils.weixinRefund(refundPackage, config.getTenpayPartner(), config.getWxPaysignkey(),config);
		
		log.info("微信退款，返回的原始报文:");
		log.info(content);
		
		Map<String, String> resu_map = MapUtil.weixinXmlToMap(content);
		
		RefundResponse response=new RefundResponse(config.getChannel(),config);
		if("SUCCESS".equals(resu_map.get("return_code")) && "SUCCESS".equals(resu_map.get("result_code"))){
			response.setSuccess(true);
			response.setOutOrderId(resu_map.get("refund_id"));
		}else{
			response.setSuccess(false);
			String err_code = resu_map.get("err_code");
			String err_code_des = resu_map.get("err_code_des");
			response.setMsg(err_code_des);
			response.setErrorCode(err_code);
		}
		return response;
	}

}
