package com.citi.reghub.rds.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.citi.reghub.rds.scheduler.batch.MongoexportTasklet;
import com.citi.reghub.rds.scheduler.service.InitializationService;

/**
 * @author Martin Tan
 *
 * validate the environment after all bean registered but before launching the job scheduler
 */
@Component
public class ValidateListener implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoexportTasklet.class);

	@Autowired
	private InitializationService service;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		LOGGER.info("ValidateListener -- validating output path..............");
		boolean validated = service.validateOutputPath();

		if (!validated) {
			LOGGER.info("ValidateListener -- output path validating failed: " + service.getError());
			//System.exit(-1);
		}
		LOGGER.info("ValidateListener -- output path validated.");

		LOGGER.info("ValidateListener -- validating MongoDB ..........");
		//validated = service.validateMongoDB();
		validated = service.validateMongoDBs();
		if (!validated) {
			LOGGER.info("ValidateListener -- MongoDB validating failed: " + service.getError());
			//System.exit(-1);
		}

		LOGGER.info("ValidateListener -- validating is successful.");
	}


}
