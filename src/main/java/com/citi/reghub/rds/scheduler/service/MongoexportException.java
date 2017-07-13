package com.citi.reghub.rds.scheduler.service;

public class MongoexportException extends Exception {
	private static final long serialVersionUID = 1L;

	public MongoexportException(Throwable cause) {
		super(cause);
	}

	public MongoexportException(String message) {
		super(message);
	}
}
