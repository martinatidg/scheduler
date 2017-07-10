package com.citi.reghub.rds.scheduler.service;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.citi.reghub.rds.scheduler.RdsBatchConfiguration;
import com.citi.reghub.rds.scheduler.RdsSchedulerConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({RdsSchedulerConfiguration.class, RdsBatchConfiguration.class})
public class ZooKeeperServiceTest {
	@Autowired
	ZooKeeperService zooKeeperService;

	@Test
	public void testGetFromTimestamp() {
		Calendar calendar = zooKeeperService.getFromTimestamp();
		Assert.assertNotNull(calendar);
	}
	
	@Test
	public void testGetToTimestamp() {
		Calendar calendar = zooKeeperService.getToTimeStamp();
		Assert.assertNotNull(calendar);
	}
}
