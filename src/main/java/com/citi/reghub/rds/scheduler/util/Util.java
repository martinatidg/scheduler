package com.citi.reghub.rds.scheduler.util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Util {
	public static String formatDate(Calendar cal, String pattern) {
		if (cal == null) {
			return "";
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "US"));
		return simpleDateFormat.format(cal.getTime());
	}

	public static String formatDate(Calendar cal) {
		String pattern = "EEEEE MMMMM dd yyyy HH:mm:ss.SSSZ";
		return formatDate(cal, pattern);
	}

	public static void deleteDirectory(String dirname) throws IOException {
		if (dirname == null || dirname.trim().isEmpty()) {
			return;
		}

		Path dirpath = Paths.get(dirname);
		if (!Files.exists(dirpath)) {
			return;
		}

		if (isDirectoryEmpty(dirpath)) {
			Files.deleteIfExists(dirpath);
			return;
		}

		revursiveDeleteDir(dirpath);
	}

	private static void revursiveDeleteDir(Path dirPath) throws IOException {
		DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);

		for (Path path : stream) {
			if (Files.isDirectory(path)) {
				if (!isDirectoryEmpty(path)) {
					revursiveDeleteDir(path);
				}
				Files.deleteIfExists(path);
			}
			else {
				Files.deleteIfExists(path);
			}
		}

		stream.close();
		Files.deleteIfExists(dirPath);
	}

	private static boolean isDirectoryEmpty(Path directory) throws IOException {
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory);
		boolean isempty = !directoryStream.iterator().hasNext();
		directoryStream.close();
		
		return isempty;
	}
}
