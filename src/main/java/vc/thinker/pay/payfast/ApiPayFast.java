package vc.thinker.pay.payfast;

/**
*
* @auther ZhangGaoXiang
* @time 2019年1月4日下午2:10:06
*
*/
public class ApiPayFast {

	// 响应码
	private String code;
	// 操作结果
	private String status; 
	
	private PayResult result;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public PayResult getResult() {
		return result;
	}

	public void setResult(PayResult result) {
		this.result = result;
	}

	public static class PayResult{
		
		private String response;
		
		private String message;

		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
	}
	
}
