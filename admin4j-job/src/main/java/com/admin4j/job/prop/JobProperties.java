package com.admin4j.job.prop;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.quartz.JobStoreType;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andanyang
 * @since 2023/5/18 15:33
 */
@Data
@ConfigurationProperties("admin4j.job")
public class JobProperties extends QuartzProperties {


    /**
     * Additional Quartz Scheduler properties.
     */
    private final Map<String, String> properties = new HashMap<>();
    private final QuartzProperties.Jdbc jdbc = new QuartzProperties.Jdbc();
    /**
     * Quartz job store type.
     */
    private JobStoreType jobStoreType = JobStoreType.JDBC;
    /**
     * Name of the scheduler.
     */
    @Value("${admin4j.job.schedulerName:${spring.application.name}}")
    private String schedulerName;
    /**
     * Whether to automatically start the scheduler after initialization.
     */
    private boolean autoStartup = true;
    /**
     * Delay after which the scheduler is started once initialization completes. Setting
     * this property makes sense if no jobs should be run before the entire application
     * has started up.
     */
    private Duration startupDelay = Duration.ofSeconds(0);
    /**
     * Whether to wait for running jobs to complete on shutdown.
     */
    private boolean waitForJobsToCompleteOnShutdown = false;
    /**
     * Whether configured jobs should overwrite existing job definitions.
     */
    private boolean overwriteExistingJobs = false;
}
