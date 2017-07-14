package com.citi.reghub.rds.scheduler.compression;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor implements Compressor {

	@Override
	public Path decompress(String zipFilePath) throws IOException {
		// Implementation not available for this version
		throw new UnsupportedOperationException();
	}

	@Override
	public Path decompress(String zipFilePath, String filePath) throws IOException {
		// Implementation not available for this version
		throw new UnsupportedOperationException();
	}

	@Override
	public Path compress(String sourceFile, String destZipFile) throws IOException {
		byte[] buffer = new byte[1024];

		try (FileInputStream inputFile = new FileInputStream(sourceFile);
				GZIPOutputStream zoutStream = new GZIPOutputStream(new FileOutputStream(destZipFile))) {
			int len;
			while ((len = inputFile.read(buffer)) > 0) {
				zoutStream.write(buffer, 0, len);
			}

			zoutStream.finish();
		}

		return Paths.get(destZipFile);
	}

	@Override
	public Path compress(String sourceFile) throws IOException {
		String destZipFile = sourceFile + ".gzip";
		return compress(sourceFile, destZipFile);
	}
}
