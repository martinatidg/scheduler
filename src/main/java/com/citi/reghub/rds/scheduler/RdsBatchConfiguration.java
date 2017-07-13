package com.citi.reghub.rds.scheduler;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.citi.reghub.rds.scheduler.batch.EncryptCompressTasklet;
import com.citi.reghub.rds.scheduler.batch.HdfsTasklet;
import com.citi.reghub.rds.scheduler.batch.LockTasklet;
import com.citi.reghub.rds.scheduler.batch.MetadataTasklet;
import com.citi.reghub.rds.scheduler.batch.MongoexportTasklet;
import com.citi.reghub.rds.scheduler.batch.UnlockTasklet;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties") // disable/enable batch auto start
public class RdsBatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().generateUniqueName(true).setType(EmbeddedDatabaseType.DERBY)
				.setScriptEncoding("UTF-8").build();
	}

	@Bean
	public Tasklet metadataTasklet() {
		return new MetadataTasklet();
	}

	@Bean
	public Tasklet lockTasklet() {
		return new LockTasklet();
	}

	@Bean
	public Tasklet mongoexportTasklet() {
		return new MongoexportTasklet();
	}

	@Bean
	public Tasklet encryptCompressTasklet() {
		return new EncryptCompressTasklet();
	}

	@Bean
	public Tasklet hdfsTasklet() {
		return new HdfsTasklet();
	}

	@Bean
	public Tasklet unlockTasklet() {
		return new UnlockTasklet();
	}

	// step 1
	@Bean
	public Step metadataStep() {
		return stepBuilderFactory.get("metadataStep").tasklet(metadataTasklet()).build();
	}

	// step 2
	@Bean
	public Step lockStep() {
		return stepBuilderFactory.get("lockStep").tasklet(lockTasklet()).build();
	}

	// step 3
	@Bean
	public Step mongoexportStep() {
		return stepBuilderFactory.get("mongoexportStep").tasklet(mongoexportTasklet()).build();
	}

	// step 4
	@Bean
	public Step encryptCompressStep() {
		return stepBuilderFactory.get("encryptCompressStep").tasklet(encryptCompressTasklet()).build();
	}

	// step 5
	@Bean
	public Step hdfsStep() {
		return stepBuilderFactory.get("hdfsStep").tasklet(hdfsTasklet()).build();
	}

	// step 6
	@Bean
	public Step unlockStep() {
		return stepBuilderFactory.get("unlockStep").tasklet(unlockTasklet()).build();
	}

	@Bean
	public Job rdsBackupJob() {
		return jobBuilderFactory.get("rdsBackupJob").incrementer(new RunIdIncrementer()).flow(metadataStep())
				.next(lockStep()).next(mongoexportStep()).next(encryptCompressStep()).next(hdfsStep())
				.next(unlockStep()).end().build();
	}
}
