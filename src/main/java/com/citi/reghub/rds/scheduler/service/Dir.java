package com.citi.reghub.rds.scheduler.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author cc.martin.tan
 *
 */
public enum Dir {
    SEP (File.separator),
    HOME(System.getProperty("user.home")),
    DEFAULT_OUTPUT(HOME + SEP.dir + "rds");	// default output name, used when the user set output path is empty.

    private final String dir;
    private Path path = null;

    private Dir(String dir) {
        this.dir = dir;
        //path = Paths.get(dir);
    }

    public String dir() {
        return dir;
    }
    
    public Path path() {
        if (path == null) {
            path = Paths.get(dir);
        }
        return path;
    }

    public boolean exists() {
    	return Files.exists(path(), new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    }

    public void createDirectory() throws IOException {
    	Files.createDirectory(path);
    }

    public static boolean exists(Path path) {
    	return Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    }
    
    public static void createDirectory(Path path) throws IOException {
    	Files.createDirectory(path);
    }

    @Override
    public String toString() {
        return dir;
    }
}
