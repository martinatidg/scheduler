package com.citi.reghub.rds.scheduler.service;

public class InitializationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InitializationException(Throwable cause) {
		super(cause);
	}

	public InitializationException(String message) {
		super(message);
	}
}
