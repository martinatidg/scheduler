package com.citi.reghub.rds.scheduler.compression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressor implements Compressor {
	@Override
	public void compress(String fileName) throws IOException {
		String zipFileName = fileName + ".zip";
		compress(fileName, zipFileName);
	}

	@Override
	public void compress(String fileName, String zipFileName) throws IOException {
		zipFileName += (zipFileName.endsWith(".zip") ? "" : ".zip");
		Path filePath = Paths.get(fileName);
		Path zipFilePath = Paths.get(zipFileName);

		Files.deleteIfExists(zipFilePath);

		if (Files.isDirectory(filePath)) {
			zipFolder(filePath.toString(), zipFilePath.toString());
		} else {
			zipFile(fileName, zipFileName);
		}
	}

	@Override
	public void uncompress(String zipFilePath) throws IOException {

	}

	@Override
	public void uncompress(String zipFilePath, String filePath) throws IOException {

	}

	public void zipFile(String filename, String zipfilename) throws IOException {
		String fname = Paths.get(filename).getFileName().toString();
		byte[] buf = new byte[1024];
		
		try (FileInputStream fis = new FileInputStream(filename);
				ZipOutputStream s = new ZipOutputStream((OutputStream) new FileOutputStream(zipfilename))){
			fis.read(buf, 0, buf.length);
			CRC32 crc = new CRC32();
			s.setLevel(6);
	
			ZipEntry entry = new ZipEntry(fname);
			entry.setSize((long) buf.length);
			crc.reset();
			crc.update(buf);
			entry.setCrc(crc.getValue());
			s.putNextEntry(entry);
			s.write(buf, 0, buf.length);
			s.finish();
		}
	}

	private void zipFolder(String srcFolder, String destZipFile) throws IOException {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		addFolderToZip("", srcFolder, zip);
		zip.flush();
		zip.close();
	}

	private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws IOException {

		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			try(FileInputStream in = new FileInputStream(srcFile)) {
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
			}
		}
	}

	private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}
}
