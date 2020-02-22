/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package vc.thinker.pay;

/**
 * Service层公用的Exception, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * @author ThinkGem
 */
public class PayException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public PayException() {
		super();
	}
	
	public PayException(String errorCode) {
		super();
		this.errorCode=errorCode;
	}

	public PayException(String errorCode,String message) {
		super(message);
		this.errorCode=errorCode;
	}

	public PayException(Throwable cause) {
		super(cause);
	}

	public PayException(String message, Throwable cause) {
		super(message, cause);
	}
}
