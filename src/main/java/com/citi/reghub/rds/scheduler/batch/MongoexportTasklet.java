package com.citi.reghub.rds.scheduler.batch;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.citi.reghub.rds.scheduler.export.ExportRequest;
import com.citi.reghub.rds.scheduler.export.ExportResponse;
import com.citi.reghub.rds.scheduler.export.ExportService;

public class MongoexportTasklet implements Tasklet {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoexportTasklet.class);
	@Autowired
	private ExportService exportService;

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {

		LOGGER.info("Step 3: Invoke mongoexport.");
		ExportRequest er = new ExportRequest();

//		er.setHostname("maas-gt-d1-u0031");
//		er.setPort(37017);
//		er.setDatabase("entities-sit");
//		er.setRequestId("J2");
//		er.setCollection("entities");
//		LocalDateTime fromTimeStamp = LocalDateTime.of(2017, 6, 20, 13, 0, 0, 0);
//		LocalDateTime toTimeStamp = LocalDateTime.now();

		GregorianCalendar fromTimeStamp = new GregorianCalendar(2017, 5, 20, 13, 0, 0);	// month start from 0. So 5 is June.
		showDate(fromTimeStamp, "fromTimeStamp");
		GregorianCalendar toTimeStamp = new GregorianCalendar();
		showDate(toTimeStamp, "toTimeStamp");

		er.setHostname("localhost");
		er.setPort(27017);
		er.setDatabase("simulator");
		er.setRequestId("J2");
		er.setCollection("entities_rds");
		er.setFromTimeStamp(fromTimeStamp);
		er.setToTimeStamp(toTimeStamp);
		er.setLastTimeStamp(toTimeStamp);

		ExportResponse exportResponse = exportService.submitRequest(er).get();
		LOGGER.info("mongoexport result:\n" + exportResponse);

		arg1.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("mongoRespone", exportResponse);

		return RepeatStatus.FINISHED;

	}

	private void showDate(Calendar cal, String description) {
		String pattern = "EEEEE MMMMM dd yyyy HH:mm:ss.SSSZ";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "US"));
	
		String date = simpleDateFormat.format(cal.getTime());
		LOGGER.info(description + ": " + date);
	}
}
