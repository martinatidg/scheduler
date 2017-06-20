package com.citi.reghub.rds.scheduler.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ExportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportService.class);
	
	
	@Scheduled(fixedRate=5000)
	private void scheduled(){
		
		// get lock on specific collection - RegHub Zookeeper
		// get last timestamp of collection export -Haddop HBase
		// start export into the local storage
		// archive
		// append metadata information
		// upload to HDFS
		
	}
}
