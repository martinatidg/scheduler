package com.citi.reghub.rds.scheduler.compression;

import java.io.File;
import java.io.IOException;

public interface TestZip {
	static final String TESTPATH = System.getProperty("java.io.tmpdir") + File.separator + "schedulerTest";

	void clean() throws IOException;
}
