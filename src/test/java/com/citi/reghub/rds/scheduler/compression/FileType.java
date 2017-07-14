package com.citi.reghub.rds.scheduler.compression;

public enum FileType {
	NONE(""), TEXT(".txt"), ZIP(".zip"), GZIP("gzip");
	private String extension;

	private FileType(String ext) {
		this.extension = ext;
	}

	public String extension() {
		return extension;
	}

	public String appendExtension(String filename) {
		if (!filename.endsWith(this.extension)) {
			return filename + this.extension;
		}

		return filename;
	}
}
