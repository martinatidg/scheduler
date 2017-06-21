# Remote Data Store Scheduler Service 

## MicroService Built using Spring Boot

RDS scheduler provides functionality of scheduled data export from MongoDB and pushing it into Hadoop cluster 

Main settings are available in application.properties. User should set mongoDb username and password wither in application.properties fiels, environmen variables or in command line. Following settings should be provided:

	--rds.scheduler.mongo.username
	--rds.scheduler.mongo.password

