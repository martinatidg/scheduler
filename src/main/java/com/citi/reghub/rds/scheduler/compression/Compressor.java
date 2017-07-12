package com.citi.reghub.rds.scheduler.compression;

import java.io.IOException;

public interface Compressor {
	void compress(String filePath) throws IOException;
	void compress(String filePath, String zipFilePath) throws IOException;
	void uncompress(String zipFilePath) throws IOException;
	void uncompress(String zipFilePath, String filePath) throws IOException;
}
