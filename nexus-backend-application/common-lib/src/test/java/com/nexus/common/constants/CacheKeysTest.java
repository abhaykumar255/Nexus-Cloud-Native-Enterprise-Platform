package com.nexus.common.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for CacheKeys constant class
 */
@DisplayName("CacheKeys Tests")
class CacheKeysTest {

    @Test
    @DisplayName("Should not be able to instantiate CacheKeys")
    void shouldNotInstantiate() throws NoSuchMethodException {
        // Arrange
        Constructor<CacheKeys> constructor = CacheKeys.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        // Act & Assert
        assertThatThrownBy(constructor::newInstance)
            .isInstanceOf(InvocationTargetException.class)
            .hasCauseInstanceOf(UnsupportedOperationException.class)
            .cause()
            .hasMessage("This is a utility class and cannot be instantiated");
    }

    @Test
    @DisplayName("External Integration cache keys are defined")
    void externalIntegrationCacheKeys() {
        // Assert
        assertThat(CacheKeys.EXT_WEATHER_PREFIX).isEqualTo("ext:weather:");
        assertThat(CacheKeys.EXT_NEWS_PREFIX).isEqualTo("ext:news:");
        assertThat(CacheKeys.EXT_MAPS_PREFIX).isEqualTo("ext:maps:");
        assertThat(CacheKeys.EXT_PAYMENT_PREFIX).isEqualTo("ext:payment:");
    }

    @Test
    @DisplayName("AI Service cache keys are defined")
    void aiServiceCacheKeys() {
        // Assert
        assertThat(CacheKeys.AI_RECOMMENDATIONS).isEqualTo("task-recommendations");
        assertThat(CacheKeys.AI_ANOMALY_PREFIX).isEqualTo("ai:anomaly:");
        assertThat(CacheKeys.AI_PREDICTION_PREFIX).isEqualTo("ai:prediction:");
    }

    @Test
    @DisplayName("User Service cache keys are defined")
    void userServiceCacheKeys() {
        // Assert
        assertThat(CacheKeys.USER_PROFILE_PREFIX).isEqualTo("user:profile:");
        assertThat(CacheKeys.USER_LIST).isEqualTo("user:list");
    }

    @Test
    @DisplayName("Task Service cache keys are defined")
    void taskServiceCacheKeys() {
        // Assert
        assertThat(CacheKeys.TASK_PREFIX).isEqualTo("task:");
        assertThat(CacheKeys.TASK_LIST_PREFIX).isEqualTo("task:list:");
    }

    @Test
    @DisplayName("Auth Service cache keys are defined")
    void authServiceCacheKeys() {
        // Assert
        assertThat(CacheKeys.TOKEN_BLACKLIST_PREFIX).isEqualTo("auth:blacklist:");
        assertThat(CacheKeys.REFRESH_TOKEN_PREFIX).isEqualTo("auth:refresh:");
    }

    @Test
    @DisplayName("Search Service cache keys are defined")
    void searchServiceCacheKeys() {
        // Assert
        assertThat(CacheKeys.SEARCH_RESULT_PREFIX).isEqualTo("search:result:");
    }

    @Test
    @DisplayName("All cache keys follow consistent naming convention")
    void cacheKeysFollowNamingConvention() {
        // Assert - prefix keys should end with ":"
        assertThat(CacheKeys.EXT_WEATHER_PREFIX).endsWith(":");
        assertThat(CacheKeys.EXT_NEWS_PREFIX).endsWith(":");
        assertThat(CacheKeys.EXT_MAPS_PREFIX).endsWith(":");
        assertThat(CacheKeys.EXT_PAYMENT_PREFIX).endsWith(":");
        assertThat(CacheKeys.AI_ANOMALY_PREFIX).endsWith(":");
        assertThat(CacheKeys.AI_PREDICTION_PREFIX).endsWith(":");
        assertThat(CacheKeys.USER_PROFILE_PREFIX).endsWith(":");
        assertThat(CacheKeys.TASK_PREFIX).endsWith(":");
        assertThat(CacheKeys.TASK_LIST_PREFIX).endsWith(":");
        assertThat(CacheKeys.TOKEN_BLACKLIST_PREFIX).endsWith(":");
        assertThat(CacheKeys.REFRESH_TOKEN_PREFIX).endsWith(":");
        assertThat(CacheKeys.SEARCH_RESULT_PREFIX).endsWith(":");
    }

    @Test
    @DisplayName("Cache keys are not null or empty")
    void cacheKeysAreNotNullOrEmpty() {
        // Assert
        assertThat(CacheKeys.EXT_WEATHER_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.EXT_NEWS_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.EXT_MAPS_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.EXT_PAYMENT_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.AI_RECOMMENDATIONS).isNotNull().isNotEmpty();
        assertThat(CacheKeys.AI_ANOMALY_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.AI_PREDICTION_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.USER_PROFILE_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.USER_LIST).isNotNull().isNotEmpty();
        assertThat(CacheKeys.TASK_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.TASK_LIST_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.TOKEN_BLACKLIST_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.REFRESH_TOKEN_PREFIX).isNotNull().isNotEmpty();
        assertThat(CacheKeys.SEARCH_RESULT_PREFIX).isNotNull().isNotEmpty();
    }
}

