package com.citi.reghub.rds.scheduler.export;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.citi.reghub.rds.scheduler.RdsSchedulerConfiguration;
import com.citi.reghub.rds.scheduler.service.MetadataService;
import com.citi.reghub.rds.scheduler.service.ZooKeeperService;

//import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(RdsSchedulerConfiguration.class)
@TestPropertySource(locations="classpath:test.properties")
public class ExportServiceTest {

	@Autowired
	private ExportService exportService;
	@Autowired
	MetadataService meta;
	@Autowired
	ZooKeeperService zooKeeperService;

	@Value("${rds.scheduler.mongo.host}")
	private String host;
	@Value("${rds.scheduler.mongo.port}")
	private int port;

	@Test
	public void testExport() throws InterruptedException, ExecutionException {
		ExportRequest er = new ExportRequest();

		er.setHostname(host);
		er.setPort(port);
		er.setRequestId("test");
		er.setDatabase(meta.getDatabase());
		er.setCollection(meta.getCollection());
		er.setFromTimeStamp(zooKeeperService.getFromTimestamp());
		er.setToTimeStamp(zooKeeperService.getToTimeStamp());

		ExportResponse exportResponse = exportService.submitRequest(er).get();
		System.out.println("exportResponse = " + exportResponse);

		Assert.assertTrue("mongoexport not successful.", exportResponse.isSuccessful());

		Path outputPath = Paths.get(exportResponse.getExportPath());
		Assert.assertTrue("Export file not found.", Files.exists(outputPath, LinkOption.NOFOLLOW_LINKS));

	}

}
