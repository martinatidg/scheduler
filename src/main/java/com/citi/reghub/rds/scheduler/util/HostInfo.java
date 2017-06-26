package com.citi.reghub.rds.scheduler.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HostInfo {
	@Value("${rds.scheduler.mongo.host}")
	private String host;

	@Value("${rds.scheduler.mongo.port}")
	private int port;

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
