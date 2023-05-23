package com.admin4j.job.config;

 
import com.admin4j.job.prop.JobProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.*;
import org.springframework.boot.autoconfigure.sql.init.OnDatabaseInitializationCondition;
import org.springframework.boot.sql.init.dependency.DatabaseInitializationDependencyConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author andanyang
 * @since 2023/5/18 16:51
 */
@Configuration(proxyBeanMethods = false)
@Import(DatabaseInitializationDependencyConfigurer.class)
@ConditionalOnSingleCandidate(DataSource.class)
@ConditionalOnProperty(prefix = "admin4j.job", name = "job-store-type", havingValue = "jdbc", matchIfMissing = true)
@AutoConfigureAfter(QuartzAutoConfiguration.class)
public class JdbcStoreTypeConfiguration {

    @Bean
    @Order(0)
    public SchedulerFactoryBeanCustomizer dataSourceCustomizer(DataSource dataSource,
                                                               @QuartzDataSource ObjectProvider<DataSource> quartzDataSource,
                                                               ObjectProvider<PlatformTransactionManager> transactionManager,
                                                               @QuartzTransactionManager ObjectProvider<PlatformTransactionManager> quartzTransactionManager) {
        return (schedulerFactoryBean) -> {
            DataSource dataSourceToUse = getDataSource(dataSource, quartzDataSource);
            schedulerFactoryBean.setDataSource(dataSourceToUse);
            PlatformTransactionManager txManager = getTransactionManager(transactionManager,
                    quartzTransactionManager);
            if (txManager != null) {
                schedulerFactoryBean.setTransactionManager(txManager);
            }
        };
    }

    private DataSource getDataSource(DataSource dataSource, ObjectProvider<DataSource> quartzDataSource) {
        DataSource dataSourceIfAvailable = quartzDataSource.getIfAvailable();
        return (dataSourceIfAvailable != null) ? dataSourceIfAvailable : dataSource;
    }

    private PlatformTransactionManager getTransactionManager(
            ObjectProvider<PlatformTransactionManager> transactionManager,
            ObjectProvider<PlatformTransactionManager> quartzTransactionManager) {
        PlatformTransactionManager transactionManagerIfAvailable = quartzTransactionManager.getIfAvailable();
        return (transactionManagerIfAvailable != null) ? transactionManagerIfAvailable
                : transactionManager.getIfUnique();
    }

    @Bean
    @SuppressWarnings("deprecation")
    @ConditionalOnMissingBean({QuartzDataSourceScriptDatabaseInitializer.class,
            QuartzDataSourceInitializer.class})
    @Conditional(OnQuartzDatasourceInitializationCondition.class)
    public QuartzDataSourceScriptDatabaseInitializer quartzDataSourceScriptDatabaseInitializer(
            DataSource dataSource, @QuartzDataSource ObjectProvider<DataSource> quartzDataSource,
            JobProperties properties) {
        DataSource dataSourceToUse = getDataSource(dataSource, quartzDataSource);
        return new QuartzDataSourceScriptDatabaseInitializer(dataSourceToUse, properties);
    }

    static class OnQuartzDatasourceInitializationCondition extends OnDatabaseInitializationCondition {

        OnQuartzDatasourceInitializationCondition() {
            super("Quartz", "admin4j.job.jdbc.initialize-schema");
        }

    }
}
