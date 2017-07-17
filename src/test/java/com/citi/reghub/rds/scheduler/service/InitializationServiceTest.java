package com.citi.reghub.rds.scheduler.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

public class InitializationServiceTest {
	private InitializationService initializationService;

	@Before
	public void init() {
		initializationService = new InitializationService();
	}

	@Test
	public void testValidateOutputPath() {
		initializationService.validateOutputPath();
		Assert.assertEquals("Default output path property failed to set in the systme properties.",
				InitializationService.DEFAULT_OUTPUT_PATH,
				System.getProperty("rds.scheduler.export.outputpath"));
		Path outputPath = Paths.get(InitializationService.DEFAULT_OUTPUT_PATH);
		Assert.assertTrue("Failed to create the default output path.", outputPath.toFile().exists());
	}

	@After
	public void clean() {
		if (initializationService != null) {
			Path outputPath = Paths.get(InitializationService.DEFAULT_OUTPUT_PATH);
			if (outputPath.toFile().exists()) {
				FileSystemUtils.deleteRecursively(outputPath.toFile());
			}
			
		}
	}
}
