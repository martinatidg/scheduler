package com.citi.reghub.rds.scheduler.process;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extracts runtime process output and error streams data
 * 
 * @author Michael Rootman
 *
 */
public class RuntimeProcess {
	private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeProcess.class);

	private String processName;
	private StreamGobbler out;
	private StreamGobbler err;
	private int exitVal = -1;

	/**
	 * Creates wrapper to execute native process,, validate execution status and
	 * extract process OUTPUT and ERROR streams data
	 * 
	 * 
	 * @param processName
	 *            - Full canonical path of executable process
	 */
	public RuntimeProcess(String processName) {
		this.processName = processName;
	}

	/**
	 * Executes the process
	 * 
	 * @throws InterruptedException
	 */
	public RuntimeProcessResult execute() throws ProcessException {
		if (this.processName == null || this.processName.isEmpty()) {
			throw new IllegalArgumentException("Process name and arguments cannot be null or empty");
		}

		try {
			Runtime rt = Runtime.getRuntime();
			LOGGER.info("Starting RuntimeProcess...");
			Process process = rt.exec(processName);

			// get output streams
			
			out = new StreamGobbler(process.getInputStream(), StreamGobbler.OUTPUT_TYPE);
			out.start();

			err = new StreamGobbler(process.getErrorStream(), StreamGobbler.ERROR_TYPE);
			err.start();

			exitVal = process.waitFor();
			LOGGER.info("RuntimeProcess complete with exit value: {}", exitVal);

		} catch (IOException | InterruptedException e) {
			ProcessException pe = new ProcessException();
			pe.addSuppressed(e);

			throw pe;
		}

		return buildResult();
	}

	private RuntimeProcessResult buildResult() {
		RuntimeProcessResult result = new RuntimeProcessResult();

		result.setCompleteSuccessfully(isSuccessfull());
		result.setError(this.err.getContents());
		result.setOutput(this.out.getContents());

		return result;
	}

	/**
	 * Checks if process completed successfully or not.
	 * 
	 * @return True if successful, otherwise False
	 */
	private boolean isSuccessfull() {
		return exitVal == 0 ? true : false;
	}
}
