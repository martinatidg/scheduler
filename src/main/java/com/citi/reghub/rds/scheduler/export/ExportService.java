package com.citi.reghub.rds.scheduler.export;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ExportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportService.class);

	@Autowired
	private AsyncTaskExecutor taskExecutor;
	@Autowired
	private ApplicationContext context;

	public Future<ExportResponse> submitRequest(ExportRequest request) {
		MongoExport exportTask = getMongoExport();
		exportTask.setRequest(request);

		Future<ExportResponse> task = taskExecutor.submit(exportTask);

		return task;
	}

	private MongoExport getMongoExport() {
		return context.getBean(MongoExport.class);
	}
}
