package com.citi.reghub.rds.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.citi.reghub.rds.scheduler.batch.MongoexportTasklet;
import com.citi.reghub.rds.scheduler.service.ValidateService;

@Component
public class ValidateListener implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoexportTasklet.class);

	@Autowired
	private ValidateService service;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		LOGGER.info("ValidateListener -- validating ..............)");

		boolean validated = service.validate();
		LOGGER.info("ValidateListener -- validated: )" + validated);

		if (!validated) {
			LOGGER.info("ValidateListener -- validating failed: )" + service.getError());
			System.exit(-1);
		}

		LOGGER.info("ValidateListener -- validating is successful.");
	}


}
