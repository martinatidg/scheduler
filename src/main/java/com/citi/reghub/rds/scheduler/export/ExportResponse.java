package com.citi.reghub.rds.scheduler.export;

public class ExportResponse {

	private boolean successful;
	private String records;
	private String lastMessage;
	private String exportPath;

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getExportPath() {
		return exportPath;
	}

	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
	}

	@Override
	public String toString() {
		return "ExportResponse [successful=" + successful + ", records=" + records + ", lastMessage=" + lastMessage
				+ ", exportPath=" + exportPath + "]";
	}
	
	
	

}
