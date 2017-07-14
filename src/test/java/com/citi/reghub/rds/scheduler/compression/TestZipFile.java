package com.citi.reghub.rds.scheduler.compression;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestZipFile implements TestZip {
	private Path sourcePath;
	private Path destZipPath;
	private String content = "RDS scheduler test zip a file.";

	public Path createTextFile(String sourceFile, FileType type) throws IOException {
		sourcePath = Paths.get(sourceFile);
		destZipPath = Paths.get(type.appendExtension(sourceFile));
		Files.createFile(sourcePath);
		Files.write(sourcePath, content.getBytes());

		return destZipPath;
	}

	public Path createTextFile(String sourceFile, String destZipFile) throws IOException {
		sourcePath = Paths.get(sourceFile);
		destZipPath = Paths.get(destZipFile);
		Files.createFile(sourcePath);
		Files.write(sourcePath, content.getBytes());

		return destZipPath;
	}

	public void clean() throws IOException {
		Files.deleteIfExists(sourcePath);
		Files.deleteIfExists(destZipPath);
	}

	public Path createZipFile(String sourceFile) throws IOException {
		sourcePath = Paths.get(sourceFile);
		Files.createFile(sourcePath);
		Files.write(sourcePath, content.getBytes());

		Compressor compressor = Compressors.zipCompressor();
		destZipPath = compressor.compress(sourceFile);

		return destZipPath;
	}
}
