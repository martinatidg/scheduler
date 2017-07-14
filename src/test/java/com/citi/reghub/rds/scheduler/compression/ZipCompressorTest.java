package com.citi.reghub.rds.scheduler.compression;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

	@Test
	public void testZipDirectoryWithDest() throws IOException {
		testDir = new TestZipDirectory(dirname, zipDirname);
		testDir.initialize();

		Compressor compressor = Compressors.zipCompressor();
		compressor.compress(dirname, zipDirname);
		assertTrue("File not zipped.", Files.exists(Paths.get(zipDirname)));

		testDir.clean();
	}

	@Test
	public void testZipDirectoryNoDest() throws IOException {
		testDir = new TestZipDirectory(dirname);
		testDir.initialize();

		Compressor compressor = Compressors.zipCompressor();
		compressor.compress(dirname);
		assertTrue("File not zipped.", Files.exists(Paths.get(zipDirname)));

		testDir.clean();
	}

	@Test
	public void testZipFileWithDest() throws IOException {
		testFile = new TestZipFile(filename, zipFilename);
		testFile.initialize();

		Compressor compressor = Compressors.zipCompressor();
		compressor.compress(filename, zipFilename);
		assertTrue("File not zipped.", Files.exists(Paths.get(zipFilename)));

		testFile.clean();
	}

	@Test
	public void testZipFileNoDest() throws IOException {
		testFile = new TestZipFile(filename);
		testFile.initialize();

		Compressor compressor = Compressors.zipCompressor();
		compressor.compress(filename);
		assertTrue("File not zipped.", Files.exists(Paths.get(zipFilename)));

		testFile.clean();
	}
}
