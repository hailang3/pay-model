package com.neo.paymodel.common.util;

import com.neo.paymodel.common.util.ExceptionCode;

public class ServiceException extends RuntimeException {
	/**
   * 
   */
	private static final long serialVersionUID = -6859627049510052189L;
	//
	private ExceptionCode exceptionCode;

	//
	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Throwable throwable) {
		super(throwable);
	}

	public ServiceException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	//
	public ExceptionCode getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	//
	public ServiceException(String msg, ExceptionCode exceptionCode) {
		super(msg);
		this.exceptionCode = exceptionCode;
	}

	public ServiceException(Throwable throwable, ExceptionCode exceptionCode) {
		super(throwable);
		this.exceptionCode = exceptionCode;
	}

	public ServiceException(String msg, Throwable throwable,
			ExceptionCode exceptionCode) {
		super(msg, throwable);
		this.exceptionCode = exceptionCode;
	}
}
