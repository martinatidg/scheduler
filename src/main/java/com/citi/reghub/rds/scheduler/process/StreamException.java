package com.citi.reghub.rds.scheduler.process;

public class StreamException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public StreamException(Throwable cause) {
		super(cause);
	}

	public StreamException(String message) {
		super(message);
	}
}
