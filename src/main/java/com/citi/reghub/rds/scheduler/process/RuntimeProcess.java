package com.citi.reghub.rds.scheduler.process;

import java.io.IOException;
import java.util.List;

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
	public void execute() throws ProcessException {
		if (this.processName == null || this.processName.isEmpty()) {
			throw new IllegalArgumentException("Process name and arguments cannot be null or empty");
		}

		try {
			Runtime rt = Runtime.getRuntime();
			LOGGER.info("Starting RuntimeProcess...");
			Process process = rt.exec(processName);

			// get output streams
			LOGGER.info("Starting StreaGobbller for {} output", StreamGobbler.OUTPUT_TYPE);
			out = new StreamGobbler(process.getInputStream(), StreamGobbler.OUTPUT_TYPE);
			out.start();

			LOGGER.info("Starting StreaGobbller for {} output", StreamGobbler.ERROR_TYPE);
			err = new StreamGobbler(process.getErrorStream(), StreamGobbler.ERROR_TYPE);
			err.start();

			exitVal = process.waitFor();
			LOGGER.info("RuntimeProcess complete with exit value: {}", exitVal);

		} catch (IOException | InterruptedException e) {
			ProcessException pe = new ProcessException();
			pe.addSuppressed(e);

			throw pe;
		}

	}

	/**
	 * Checks if process completed successfully or not.
	 * 
	 * @return True if successful, otherwise False
	 */
	public boolean isSuccessfull() {
		return exitVal == 0 ? true : false;
	}

	/**
	 * Returns one of the process output streams
	 * 
	 * @param type
	 *            either {@link StreamGobbler.OUTPUT_TYPE} or
	 *            {@link StreamGobbler.ERROR_TYPE}
	 * @return Collection of stream output data
	 */
	public List<String> getProcessOutput(String type) {
		return StreamGobbler.ERROR_TYPE.equals(type) ? err.getContents() : out.getContents();
	}

}
