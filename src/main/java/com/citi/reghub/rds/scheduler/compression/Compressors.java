package com.citi.reghub.rds.scheduler.compression;

public class Compressors {
	private Compressors() {
		throw new UnsupportedOperationException("The Util class contains static methods only and cannot be instantiated.");
	}

	public static Compressor gzipCompressor() {
		return new GzipCompressor();
	}

    public static Compressor zipCompressor() {
		return new ZipCompressor();
	}
}
