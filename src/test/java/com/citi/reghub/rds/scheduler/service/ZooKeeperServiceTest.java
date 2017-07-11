package com.citi.reghub.rds.scheduler.service;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ZooKeeperServiceTest {
	ZooKeeperService zooKeeperService;

	@Before
	public void init() {
		zooKeeperService = new ZooKeeperService();
	}

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
