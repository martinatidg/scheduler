package com.citi.reghub.rds.scheduler.compression;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ZipCompressorTest {
	private String dirname = TestZip.TESTPATH + File.separator + "dirTest";
	private String zipDirname = TestZip.TESTPATH + File.separator + "dirTest.zip";
	private String filename = TestZip.TESTPATH + File.separator + "zipFileTest";
	private String zipFilename = TestZip.TESTPATH + File.separator + "zipFileTest.zip";

	private TestZipDirectory testDir;
	private TestZipFile testFile;

	@Before
	public void init() throws IOException {
		testDir = new TestZipDirectory(dirname, zipDirname);
		testDir.clean();
		testDir.initialize();

		testFile = new TestZipFile(filename, zipFilename);
		testFile.clean();
		testFile.initialize();
	}

	@Test
	public void testZipDirectory() throws IOException {
		Compressor compressor = Compressors.zipCompressor();
		compressor.compress(dirname, zipDirname);
		assertTrue("File not zipped.", Files.exists(Paths.get(zipDirname)));
	}

	@Test
	public void testZipFile() throws IOException {
		Compressor compressor = Compressors.zipCompressor();
		compressor.compress(filename, zipFilename);
		assertTrue("File not zipped.", Files.exists(Paths.get(zipFilename)));
	}

	@After
	public void clean() throws IOException {
		testDir.clean();
		testFile.clean();
	}
}
