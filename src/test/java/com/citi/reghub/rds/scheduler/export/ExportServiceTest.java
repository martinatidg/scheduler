package com.citi.reghub.rds.scheduler.export;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.citi.reghub.rds.scheduler.RdsSchedulerConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(RdsSchedulerConfiguration.class)
public class ExportServiceTest {

	@Autowired
	private ExportService exportService;

	@Test
	public void testExport() throws InterruptedException, ExecutionException {
		ExportRequest er = new ExportRequest();

//		er.setHostname("maas-gt-d1-u0031");
//		er.setPort(37017);
//		er.setDatabase("entities-sit");
//		er.setRequestId("J2");
//		er.setCollection("entities");
//
//		ExportResponse exportResponse = exportService.submitRequest(er).get();
//
//		System.out.println(exportResponse);
	}

}
