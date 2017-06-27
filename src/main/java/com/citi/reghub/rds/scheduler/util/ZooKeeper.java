package com.citi.reghub.rds.scheduler.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Component;

@Component
public class ZooKeeper {
	private Calendar fromTimestamp = new GregorianCalendar(2017, 5, 19, 13, 0, 0);	// month start from 0. So 5 is June.
	private Calendar toTimestamp; // = new GregorianCalendar();

	public Calendar getFromTimestamp() {
		return fromTimestamp;
	}
	public void setFromTimestamp(Calendar fromTimestamp) {
		this.fromTimestamp = fromTimestamp;
	}
	public Calendar getToTimeStamp() {
		toTimestamp = (Calendar)fromTimestamp.clone();
		toTimestamp.add(Calendar.HOUR_OF_DAY, 10);	// for testing: from the fromTimestamp to the next 10 hours
		return toTimestamp;
	}
	public void setToTimeStamp(Calendar toTimeStamp) {
		this.toTimestamp = toTimeStamp;
	}

	@Override
	public String toString() {
		return "ZooKeeper [fromTimestamp=" + Util.formatDate(fromTimestamp) + ", toTimeStamp=" + Util.formatDate(toTimestamp) + "]";
	}

}
