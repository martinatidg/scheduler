package com.citi.reghub.rds.scheduler.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps stream data extraction out of native process
 * 
 * @author Michael Rootman
 *
 */
public class StreamGobbler extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(StreamGobbler.class);
	
	public static final String ERROR_TYPE = "ERROR";
	public static final String OUTPUT_TYPE = "OUTPUT";

	private final InputStreamReader stream;
	private String type;
	private final List<String> contents = new ArrayList<>();

	public StreamGobbler(InputStream stream, String type) {
		this.stream = new InputStreamReader(stream);
		this.type = type;
	}

	/**
	 * Returns collection of process stream data
	 * 
	 * @return
	 */
	public List<String> getContents() {
		return new ArrayList<>(contents);
	}

	public String getType() {
		return type;
	}

	@Override
	public void run() {
		LOGGER.info("Starting StreaGobbller for {} output", type);
		
		try (BufferedReader br = new BufferedReader(stream)) {
			String line = null;
			while ((line = br.readLine()) != null) {
				contents.add(line);
			}
		} catch (IOException e) {
			throw new StreamException(e);
		}
	}
}