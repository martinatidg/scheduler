package com.citi.reghub.rds.scheduler.compression;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor implements Compressor {

	@Override
	public void uncompress(String zipFilePath) throws IOException {

	}

	@Override
	public void uncompress(String zipFilePath, String filePath) throws IOException {

	}

	@Override
	public void compress(String filePath, String zipFilePath) throws IOException {
		byte[] buffer = new byte[1024];

		try (FileInputStream inputFile = new FileInputStream(filePath);
				GZIPOutputStream zoutStream = new GZIPOutputStream(new FileOutputStream(zipFilePath))) {
			int len;
			while ((len = inputFile.read(buffer)) > 0) {
				zoutStream.write(buffer, 0, len);
			}

			zoutStream.finish();
		}
	}

	public void compress(String filePath) throws IOException {
		String zipFilePath = filePath + ".gzip";
		compress(filePath, zipFilePath);
	}
}
