package com.nexus.common.constants;

/**
 * Centralized Cache Key Prefixes
 * All services should use these constants for Redis cache keys
 */
public final class CacheKeys {
    
    private CacheKeys() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // External Integration Service
    public static final String EXT_WEATHER_PREFIX = "ext:weather:";
    public static final String EXT_NEWS_PREFIX = "ext:news:";
    public static final String EXT_MAPS_PREFIX = "ext:maps:";
    public static final String EXT_PAYMENT_PREFIX = "ext:payment:";
    
    // AI Service
    public static final String AI_RECOMMENDATIONS = "task-recommendations";
    public static final String AI_ANOMALY_PREFIX = "ai:anomaly:";
    public static final String AI_PREDICTION_PREFIX = "ai:prediction:";
    
    // User Service
    public static final String USER_PROFILE_PREFIX = "user:profile:";
    public static final String USER_LIST = "user:list";
    
    // Task Service
    public static final String TASK_PREFIX = "task:";
    public static final String TASK_LIST_PREFIX = "task:list:";
    
    // Auth Service
    public static final String TOKEN_BLACKLIST_PREFIX = "auth:blacklist:";
    public static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";
    
    // Search Service
    public static final String SEARCH_RESULT_PREFIX = "search:result:";
}

