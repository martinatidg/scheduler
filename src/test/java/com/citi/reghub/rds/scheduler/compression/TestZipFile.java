package com.citi.reghub.rds.scheduler.compression;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestZipFile implements TestZip {
	private Path sourcePath;
	private Path destZipPath;
	private String content = "RDS scheduler test zip a file";

	public TestZipFile(String sourceFile) throws IOException {
		sourcePath = Paths.get(sourceFile);
		String destZipFile = sourceFile + ".zip";

		destZipPath = Paths.get(destZipFile);
	}

	public TestZipFile(String sourceFile, String destZipFile) throws IOException {
		sourcePath = Paths.get(sourceFile);
		destZipPath = Paths.get(destZipFile);
	}

	public void clean() throws IOException {
		Files.deleteIfExists(sourcePath);
		Files.deleteIfExists(destZipPath);
	}

	public void initialize() throws IOException {
		Files.createFile(sourcePath);
		Files.write(sourcePath, content.getBytes());
	}
}
