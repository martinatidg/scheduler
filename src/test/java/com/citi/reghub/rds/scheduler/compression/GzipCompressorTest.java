package com.citi.reghub.rds.scheduler.compression;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GzipCompressorTest {
	String sourceFile = TestZip.TESTPATH + File.separator + "gzipFileTest";
	String destZipFile = TestZip.TESTPATH + File.separator + "gzipFileTest.gzip";
	TestZipFile testFile;

	@Test
	public void testCompressWithDest() throws IOException {
		testFile = new TestZipFile();
		testFile.createTextFile(sourceFile, destZipFile);

		Compressor compressor = Compressors.gzipCompressor();
		Path zipPath = compressor.compress(sourceFile, destZipFile);
		
		assertTrue("File not zipped.", zipPath.toFile().exists());
	}

	@Test
	public void testCompressNoDest() throws IOException {
		testFile = new TestZipFile();
		testFile.createTextFile(sourceFile, FileType.GZIP);

		Compressor compressor = Compressors.gzipCompressor();
		Path zipPath = compressor.compress(sourceFile);
		
		assertTrue("File not zipped.", zipPath.toFile().exists());
	}

	@After
	public void clean() throws IOException {
		testFile.clean();
	}
}
