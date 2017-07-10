package com.citi.reghub.rds.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.citi.reghub.rds.scheduler.service.InitializationService;
import com.citi.reghub.rds.scheduler.service.SchedulerService;
import com.citi.reghub.rds.scheduler.service.ValidationException;

/**
 * @author Martin Tan
 *    validate the environment after all bean registered but before
 *    launching the job scheduler
 */
@Component
public class ValidateListener implements ApplicationListener<ContextRefreshedEvent> { // ,
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidateListener.class);

	@Autowired
	private InitializationService service;
	@Autowired
	private SchedulerService schedulerService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		LOGGER.info("ValidateListener -- validating output path..............");
		try {
			service.validateOutputPath();
		}
		catch (ValidationException e) {
			LOGGER.error("ValidateListener -- output path validating failed: {}.", e.getMessage());
			throw e;
		}

		LOGGER.info("ValidateListener -- output path validated.");

		LOGGER.info("ValidateListener -- validating MongoDB ..........");
		try {
			service.validateMongoDBs();
		}
		catch (ValidationException e) {
			LOGGER.error("ValidateListener -- MongoDB validating failed: {}.", e.getMessage());
			throw e;
		}

		LOGGER.info("ValidateListener -- validating is successful.");
		schedulerService.lauchJob();
	}
}
