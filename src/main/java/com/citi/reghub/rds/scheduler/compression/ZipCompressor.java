package com.citi.reghub.rds.scheduler.compression;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipCompressor implements Compressor {
	@Override
	public Path compress(String sourceFile) throws IOException {
		String zipFileName = sourceFile + ".zip";
		return compress(sourceFile, zipFileName);
	}

	@Override
	public Path compress(String sourceFile, String destZipFile) throws IOException {
		String zDestZipFile = destZipFile.endsWith(".zip") ? destZipFile : destZipFile + ".zip";
		Path filePath = Paths.get(sourceFile);
		Path zipFilePath = Paths.get(zDestZipFile);

		Files.deleteIfExists(zipFilePath);

		if (filePath.toFile().isDirectory()) {
			zipFolder(filePath.toString(), zipFilePath.toString());
		} else {
			zipFile(sourceFile, zDestZipFile);
		}

		return zipFilePath;
	}

	@Override
	public void decompress(String sourceZipFile) throws IOException {
		Path destUnzipDirPath = Paths.get(sourceZipFile).getParent();
		decompress(sourceZipFile, destUnzipDirPath.toString());
	}

	@Override
	public void decompress(String sourceZipFile, String destUnzipDir) throws IOException {
		try (FileInputStream fis = new FileInputStream(sourceZipFile);
				ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis))) {
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null) {
				Path outputpath = Paths.get(destUnzipDir, entry.getName());

				// to preserve the original structure, create the folder even if it's empty.
				if (entry.isDirectory() && KEEP_EMPTY_FOLDER) {
					if (!outputpath.toFile().exists()) {
						Files.createDirectories(outputpath);
					}
					continue;
				}

				Path parentpath = outputpath.getParent();
				if (!parentpath.toFile().exists()) {
					Files.createDirectories(parentpath);
				}

				int size;
				byte[] buffer = new byte[2048];

				try (FileOutputStream fos = new FileOutputStream(outputpath.toString());
						BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length)) {
					while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
						bos.write(buffer, 0, size);
					}
					bos.flush();
				}
			}
		}
	}

	public void zipFile(String filename, String zipfilename) throws IOException {
		String fname = Paths.get(filename).getFileName().toString();
		byte[] buf = new byte[1024];

		try (FileInputStream fis = new FileInputStream(filename);
				ZipOutputStream s = new ZipOutputStream((OutputStream) new FileOutputStream(zipfilename))) {
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

	private void zipFolder(String sourceFolder, String destZipFile) throws IOException {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		addFolderToZip("", sourceFolder, zip);
		zip.flush();
		zip.close();
	}

	private void addFileToZip(String parentDir, String sourceFile, ZipOutputStream zip) throws IOException {
		File folder = new File(sourceFile);
		Path fpath = Paths.get(parentDir, folder.getName());

		if (folder.isDirectory()) {
			addFolderToZip(parentDir, sourceFile, zip);

			// to preserve the original structure, include the folder even if it's empty.
			if (folder.list().length < 1 && KEEP_EMPTY_FOLDER) {
				zip.putNextEntry(new ZipEntry(fpath.toString() + "/")); // need '/' to create a directory
			}
		} else {
			byte[] buf = new byte[1024];
			int len;
			try (FileInputStream in = new FileInputStream(sourceFile)) {
				zip.putNextEntry(new ZipEntry(fpath.toString()));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
			}
		}
	}

	private void addFolderToZip(String parentDir, String sourceFolder, ZipOutputStream zip) throws IOException {
		Path fpath = Paths.get(sourceFolder);

		DirectoryStream<Path> pstream = Files.newDirectoryStream(fpath);

		for (Path p : pstream) {
			Path relDir = Paths.get(sourceFolder, p.getFileName().toString());
			Path baseDir;

			if (parentDir == null || parentDir.trim().isEmpty()) {
				baseDir = fpath.getFileName();
			} else {
				baseDir = Paths.get(parentDir, fpath.getFileName().toString());
			}

			addFileToZip(baseDir.toString(), relDir.toString(), zip);
		}

		pstream.close();
	}
}
