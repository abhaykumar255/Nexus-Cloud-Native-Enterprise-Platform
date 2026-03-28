package com.nexus.notification.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for AsyncConfig
 */
@DisplayName("AsyncConfig Unit Tests")
class AsyncConfigTest {

    private AsyncConfig asyncConfig;

    @BeforeEach
    void setUp() {
        asyncConfig = new AsyncConfig();
        // Inject field values using reflection
        ReflectionTestUtils.setField(asyncConfig, "corePoolSize", 5);
        ReflectionTestUtils.setField(asyncConfig, "maxPoolSize", 10);
        ReflectionTestUtils.setField(asyncConfig, "queueCapacity", 100);
        ReflectionTestUtils.setField(asyncConfig, "threadNamePrefix", "test-async-");
    }

    @Test
    @DisplayName("notificationTaskExecutor creates configured ThreadPoolTaskExecutor")
    void notificationTaskExecutor_createsExecutor() {
        // Act
        Executor executor = asyncConfig.notificationTaskExecutor();

        // Assert
        assertThat(executor).isNotNull();
        assertThat(executor).isInstanceOf(ThreadPoolTaskExecutor.class);
        
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) executor;
        assertThat(taskExecutor.getCorePoolSize()).isEqualTo(5);
        assertThat(taskExecutor.getMaxPoolSize()).isEqualTo(10);
        assertThat(taskExecutor.getThreadNamePrefix()).isEqualTo("test-async-");
    }

    @Test
    @DisplayName("notificationTaskExecutor can execute tasks")
    void notificationTaskExecutor_executesTask() throws InterruptedException {
        // Arrange
        Executor executor = asyncConfig.notificationTaskExecutor();
        final boolean[] taskRan = {false};

        // Act
        executor.execute(() -> {
            taskRan[0] = true;
        });

        // Wait for async execution
        Thread.sleep(100);

        // Assert
        assertThat(taskRan[0]).isTrue();
    }

    @Test
    @DisplayName("notificationTaskExecutor is properly initialized")
    void notificationTaskExecutor_isInitialized() {
        // Act
        Executor executor = asyncConfig.notificationTaskExecutor();

        // Assert
        assertThat(executor).isNotNull();
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) executor;
        assertThat(taskExecutor.getThreadNamePrefix()).startsWith("test-async-");
    }
}

