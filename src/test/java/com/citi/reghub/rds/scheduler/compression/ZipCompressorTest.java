package com.citi.reghub.rds.scheduler.compression;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ZipCompressorTest {
	private String dirname = TestZip.TESTPATH + File.separator + "dirTest";
	private String zipDirname = TestZip.TESTPATH + File.separator + "dirTest.zip";
	private String filename = TestZip.TESTPATH + File.separator + "fileTest";
	private String zipFilename = TestZip.TESTPATH + File.separator + "fileTest.zip";
	private String testdecompression = TestZip.TESTPATH + File.separator + "testdecompression";

	private TestZipDirectory testDir;
	private TestZipFile testFile;

	@Test
	public void testZipDirectoryWithDest() throws IOException {
		testDir = new TestZipDirectory(dirname, zipDirname);
		testDir.initialize();

		Compressor compressor = Compressors.zipCompressor();
		compressor.compress(dirname, zipDirname);
		assertTrue("File not zipped.", Files.exists(Paths.get(zipDirname)));
	}

	@Test
	public void testZipDirectoryNoDest() throws IOException {
		testDir = new TestZipDirectory(dirname);
		testDir.initialize();

		Compressor compressor = Compressors.zipCompressor();
		compressor.compress(dirname);
		assertTrue("File not zipped.", Files.exists(Paths.get(zipDirname)));
	}

	@Test
	public void testZipFileWithDest() throws IOException {
		testFile = new TestZipFile();
		testFile.createTextFile(filename, zipFilename);

		Compressor compressor = Compressors.zipCompressor();
		Path zipPath = compressor.compress(filename, zipFilename);

		assertTrue("File not zipped.", zipPath.toFile().exists());
	}

	@Test
	public void testZipFileNoDest() throws IOException {
		testFile = new TestZipFile();
		testFile.createTextFile(filename, FileType.ZIP);

		Compressor compressor = Compressors.zipCompressor();
		Path zipPath = compressor.compress(filename);
		assertTrue("File not zipped.", zipPath.toFile().exists());
	}

	@Test
	public void testDecompressFile() throws IOException {
		testFile = new TestZipFile();
		Path zipPath = testFile.createZipFile(testdecompression);

		Compressor compressor = Compressors.zipCompressor();
		Path decompressedPath = compressor.decompress(zipPath.toString());

		assertTrue("File not zipped.", decompressedPath.toFile().exists());
	}

	@After
	public void clean() throws IOException {
		if (testFile != null) {
			testFile.clean();
		}

		if (testDir != null) {
			testDir.clean();
		}
	}
}
