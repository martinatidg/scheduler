package com.citi.reghub.rds.scheduler.export;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ExportService {
	@Autowired
	private AsyncTaskExecutor taskExecutor;
	@Autowired
	private ApplicationContext context;

	public Future<ExportResponse> submitRequest(ExportRequest request) {
		MongoExport exportTask = getMongoExport();
		exportTask.setRequest(request);

		return taskExecutor.submit(exportTask);
	}

	private MongoExport getMongoExport() {
		return context.getBean(MongoExport.class);
	}
}
