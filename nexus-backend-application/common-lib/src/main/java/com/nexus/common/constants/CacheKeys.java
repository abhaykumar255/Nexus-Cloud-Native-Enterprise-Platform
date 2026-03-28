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

    // E-Commerce: Product Service
    public static final String PRODUCT_PREFIX = "product:";
    public static final String PRODUCT_DETAIL_PREFIX = "product:detail:";
    public static final String PRODUCT_LIST_PREFIX = "product:list:";
    public static final String PRODUCT_CATEGORY_PREFIX = "product:category:";
    public static final String PRODUCT_PRICE_HISTORY_PREFIX = "product:price:history:";

    // E-Commerce: Inventory Service
    public static final String INVENTORY_PREFIX = "inventory:";
    public static final String INVENTORY_SOFT_RESERVATION_PREFIX = "inventory:soft:";
    public static final String INVENTORY_LOCK_PREFIX = "inventory:lock:";

    // E-Commerce: Cart Service
    public static final String CART_PREFIX = "cart:";
    public static final String CART_TTL_SECONDS = "604800"; // 7 days

    // E-Commerce: Order Service
    public static final String ORDER_PREFIX = "order:";
    public static final String ORDER_STATUS_PREFIX = "order:status:";

    // E-Commerce: Restaurant Service
    public static final String RESTAURANT_PREFIX = "restaurant:";
    public static final String RESTAURANT_MENU_PREFIX = "restaurant:menu:";
    public static final String RESTAURANT_STATUS_PREFIX = "restaurant:status:";

    // E-Commerce: Tracking Service
    public static final String TRACKING_GPS_PREFIX = "tracking:gps:";
    public static final String TRACKING_ETA_PREFIX = "tracking:eta:";

    // E-Commerce: Seller Service
    public static final String SELLER_PREFIX = "seller:";
    public static final String SELLER_PROFILE_PREFIX = "seller:profile:";

    // E-Commerce: Coupon Service
    public static final String COUPON_PREFIX = "coupon:";
    public static final String COUPON_USAGE_PREFIX = "coupon:usage:";

    // E-Commerce: Review Service
    public static final String REVIEW_PREFIX = "review:";
    public static final String REVIEW_AGGREGATE_PREFIX = "review:aggregate:";

    // Rate Limiting
    public static final String RATE_LIMIT_PREFIX = "rate:limit:";
}

