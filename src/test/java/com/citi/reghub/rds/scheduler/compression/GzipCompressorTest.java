package com.citi.reghub.rds.scheduler.compression;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.citi.reghub.rds.scheduler.compression.Compressor;
import com.citi.reghub.rds.scheduler.compression.Compressors;

@SpringBootTest
public class GzipCompressorTest {
	String filename = TestZip.TESTPATH + File.separator + "gzipFileTest";
	String zipFilename = TestZip.TESTPATH + File.separator + "gzipFileTest.zip";
	TestZipFile testFile;

	@Before
	public void init() throws IOException {
		testFile = new TestZipFile(filename, zipFilename);
		testFile.clean();
		testFile.initialize();
	}

	@Test
	public void testZip1() throws IOException {
		Compressor compressor = Compressors.gzipCompressor();

		compressor.compress(filename, zipFilename);
		
		assertTrue("File not zipped.", Files.exists(Paths.get(zipFilename)));
	}

	@After
	public void clean() throws IOException {
		testFile.clean();
	}
}
