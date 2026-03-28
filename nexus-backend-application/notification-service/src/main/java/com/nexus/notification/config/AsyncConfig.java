package com.nexus.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Async configuration for notification processing
 */
@Slf4j
@Configuration
public class AsyncConfig {
    
    @Value("${async.executor.core-pool-size:5}")
    private int corePoolSize;
    
    @Value("${async.executor.max-pool-size:10}")
    private int maxPoolSize;
    
    @Value("${async.executor.queue-capacity:100}")
    private int queueCapacity;
    
    @Value("${async.executor.thread-name-prefix:notification-async-}")
    private String threadNamePrefix;
    
    @Bean(name = "notificationTaskExecutor")
    public Executor notificationTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        
        log.info("Notification task executor initialized with core pool size: {}, max pool size: {}", 
                corePoolSize, maxPoolSize);
        
        return executor;
    }
}

