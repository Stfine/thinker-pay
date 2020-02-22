package vc.thinker.refund.bean;

import java.util.HashMap;
import java.util.Map;

public class RespMessage {

	private static RespMessage respMessage;
	
	private static Map<String, String> resMap ;
	
	static {
		
		resMap = new HashMap<>();
		resMap.put("ACQ.SYSTEM_ERROR", "系统错误");
		resMap.put("ACQ.INVALID_PARAMETER", "参数无效");
		resMap.put("ACQ.SELLER_BALANCE_NOT_ENOUGH", "卖家余额不足");
		resMap.put("ACQ.REFUND_AMT_NOT_EQUAL_TOTAL", "退款金额超限");
		resMap.put("ACQ.REASON_TRADE_BEEN_FREEZEN", "请求退款的交易被冻结");
		resMap.put("ACQ.TRADE_NOT_EXIST", "交易不存在");
		resMap.put("ACQ.TRADE_HAS_FINISHED", "交易已完结");
		resMap.put("ACQ.TRADE_STATUS_ERROR", "交易状态非法");
		resMap.put("ACQ.DISCORDANT_REPEAT_REQUEST", "不一致的请求");
		resMap.put("ACQ.REASON_TRADE_REFUND_FEE_ERR", "退款金额无效");
		resMap.put("ACQ.TRADE_NOT_ALLOW_REFUND", "当前交易不允许退款");
	}
	
	/**
	 * 根据错误码返回错误信息  
	 * @param errCode 错误码
	 * @return
	 */
	public static synchronized String getResMessage(String errCode) {
		
		if(null != respMessage) {
			respMessage = new RespMessage();
		}
		
		return resMap.get(errCode);
	}
}
