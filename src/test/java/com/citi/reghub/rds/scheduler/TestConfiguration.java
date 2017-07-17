package com.citi.reghub.rds.scheduler;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.citi.reghub.rds.scheduler.export.ExportService;
import com.citi.reghub.rds.scheduler.export.MongoExport;
import com.citi.reghub.rds.scheduler.service.InitializationService;
import com.citi.reghub.rds.scheduler.service.SchedulerService;

@Profile("test")
@Configuration
public class TestConfiguration {
    @Bean
    @Primary
    public ExportService exportService() {
        return Mockito.mock(ExportService.class);
    }

    @Bean
    @Primary
    public MongoExport mongoExport() {
        return Mockito.mock(MongoExport.class);
    }

    @Bean
    @Primary
    public InitializationService initializationService() {
        return Mockito.mock(InitializationService.class);
    }

    @Bean
    @Primary
    public SchedulerService schedulerService() {
    	return Mockito.mock(SchedulerService.class);
    }
}
