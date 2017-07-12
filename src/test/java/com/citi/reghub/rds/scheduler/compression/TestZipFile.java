package com.citi.reghub.rds.scheduler.compression;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestZipFile implements TestZip {
	private Path path;
	private Path zipPath;
	private String content = "RDS scheduler test zip a file";

	public TestZipFile(String filename, String zipname) throws IOException {
		path = Paths.get(filename);
		zipPath = Paths.get(zipname);
	}

	public void clean() throws IOException {
		Files.deleteIfExists(path);
		Files.deleteIfExists(zipPath);
	}

	public void initialize() throws IOException {
		Files.createFile(path);
		Files.write(path, content.getBytes());
	}
}
