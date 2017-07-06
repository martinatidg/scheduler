package com.citi.reghub.rds.scheduler.process;

import java.util.Collection;

public class RuntimeProcessResult {
	private boolean completeSuccessfully;
	private Collection<String> output;
	private Collection<String> error;

	public boolean isCompleteSuccessfully() {
		return completeSuccessfully;
	}

	public void setCompleteSuccessfully(boolean completeSuccessfully) {
		this.completeSuccessfully = completeSuccessfully;
	}

	public Collection<String> getOutput() {
		return output;
	}

	public void setOutput(Collection<String> output) {
		this.output = output;
	}

	public Collection<String> getError() {
		return error;
	}

	public void setError(Collection<String> error) {
		this.error = error;
	}
}
