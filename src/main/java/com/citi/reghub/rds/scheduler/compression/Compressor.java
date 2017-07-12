package com.citi.reghub.rds.scheduler.compression;

import java.io.IOException;
import java.nio.file.Path;

public interface Compressor {
	Path compress(String filePath) throws IOException;
	Path compress(String filePath, String zipFilePath) throws IOException;
	void decompress(String zipFilePath) throws IOException;
	void decompress(String zipFilePath, String filePath) throws IOException;
}
