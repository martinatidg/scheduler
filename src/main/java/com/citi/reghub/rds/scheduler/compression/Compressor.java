package com.citi.reghub.rds.scheduler.compression;

import java.io.IOException;
import java.nio.file.Path;

public interface Compressor {
	Path compress(String sourceFile) throws IOException;
	Path compress(String sourceFile, String destZipFile) throws IOException;
	void decompress(String sourceZipFile) throws IOException;
	void decompress(String sourceZipFile, String destUnzipDir) throws IOException;
}
