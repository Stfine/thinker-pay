package vc.thinker.pay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import vc.thinker.pay.request.DirectPayRequest;
import vc.thinker.pay.request.PayBankRequest;
import vc.thinker.pay.request.TransfersRequest;
import vc.thinker.pay.response.PayBankResponse;
import vc.thinker.pay.response.TransfersResponse;
import vc.thinker.pay.util.JsonUtil;

/**
 * 企业相关支付
 * @author james
 *
 * @param <T>
 */
public abstract class PayMchHandler<T extends PayConfig,F extends PayNotify>{
	
	public PayMchHandler(PayConfigFactory configFactory){
		this.configFactory=configFactory;
	}
	
	private PayConfigFactory configFactory;
	
	/**
	 * 转账
	 * @param request
	 * @return
	 * @throws PayException
	 */
	public abstract TransfersResponse transferAccounts(TransfersRequest request) throws PayException;
	
	public abstract PayBankResponse payBank(PayBankRequest request) throws PayException;
	
	public abstract Map<String, String> getPublieKey() throws PayException;

	public PayConfigFactory getConfigFactory() {
		return configFactory;
	}

	public void setConfigFactory(PayConfigFactory configFactory) {
		this.configFactory = configFactory;
	}
	
	public static final String ATTACH_KEY_CONFIG_MARK="configMark";
	
	public static final String ATTACH_KEY_REQ_ATTACH="attach";
	
	/**
	 * 得到附加属性，这里将 request attach 和 config mark 组个json
	 * @param request
	 * @param configMark
	 * @return
	 */
	public String getAttachInput(DirectPayRequest request,String configMark){
		Map<String, String> exts=new HashMap<>();
		exts.put(ATTACH_KEY_CONFIG_MARK, configMark);
		exts.put(ATTACH_KEY_REQ_ATTACH, request.getReqAttach());
		return JsonUtil.getInstance().toJson(exts);
	}
	
	/**
	 * 得到输出附加属性的 map
	 * @param request
	 * @param configMark
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Map<String, String> getAttachOut(String attachJson) throws IOException{
		return JsonUtil.getInstance().readValue(attachJson, new TypeReference<Map<String, String>>() {
		});
	}
	
	/**
	 * 得到支付配制
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getConfig(String configMark){
		return (T) configFactory.getPayConfig(configMark);
	}
}
