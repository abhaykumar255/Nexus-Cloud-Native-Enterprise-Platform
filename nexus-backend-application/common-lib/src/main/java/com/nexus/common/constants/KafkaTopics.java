package com.nexus.common.constants;

/**
 * Centralized Kafka Topic Names
 * All services should reference these constants instead of hardcoding topic names
 */
public final class KafkaTopics {
    
    private KafkaTopics() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // User Events
    public static final String USER_CREATED = "user.created";
    public static final String USER_UPDATED = "user.updated";
    public static final String USER_DELETED = "user.deleted";
    public static final String USER_EVENTS = "user.events";
    
    // Task Events
    public static final String TASK_CREATED = "task.created";
    public static final String TASK_UPDATED = "task.updated";
    public static final String TASK_ASSIGNED = "task.assigned";
    public static final String TASK_STATUS_CHANGED = "task.status.changed";
    public static final String TASK_EVENTS = "task.events";
    
    // File Events
    public static final String FILE_UPLOADED = "file.uploaded";
    public static final String FILE_DELETED = "file.deleted";
    
    // Notification Events
    public static final String NOTIFICATION_SENT = "notification.sent";
    public static final String NOTIFICATION_FAILED = "notification.failed";
    
    // Workflow/Saga Events
    public static final String SAGA_STARTED = "saga.started";
    public static final String SAGA_COMPLETED = "saga.completed";
    public static final String SAGA_FAILED = "saga.failed";
    public static final String SAGA_COMPENSATING = "saga.compensating";

    // E-Commerce: Product Events
    public static final String PRODUCT_CREATED = "product.created";
    public static final String PRODUCT_UPDATED = "product.updated";
    public static final String PRODUCT_PUBLISHED = "product.published";
    public static final String PRODUCT_DELETED = "product.deleted";
    public static final String PRODUCT_EVENTS = "product.events";
    public static final String PRODUCT_BULK_EVENTS = "product.bulk.events";
    public static final String PRICE_CHANGED = "price.changed";

    // E-Commerce: Inventory Events
    public static final String INVENTORY_UPDATED = "inventory.updated";
    public static final String INVENTORY_LOW_STOCK = "inventory.low.stock";
    public static final String INVENTORY_OUT_OF_STOCK = "inventory.out.of.stock";
    public static final String INVENTORY_RESERVED = "inventory.reserved";
    public static final String INVENTORY_RELEASED = "inventory.released";
    public static final String INVENTORY_EVENTS = "inventory.events";

    // E-Commerce: Order Events
    public static final String ORDER_CREATED = "order.created";
    public static final String ORDER_CONFIRMED = "order.confirmed";
    public static final String ORDER_DISPATCHED = "order.dispatched";
    public static final String ORDER_DELIVERED = "order.delivered";
    public static final String ORDER_CANCELLED = "order.cancelled";
    public static final String ORDER_RETURNED = "order.returned";
    public static final String ORDER_STATUS_CHANGED = "order.status.changed";
    public static final String ORDER_EVENTS = "order.events";

    // E-Commerce: Payment Events
    public static final String PAYMENT_INITIATED = "payment.initiated";
    public static final String PAYMENT_CONFIRMED = "payment.confirmed";
    public static final String PAYMENT_FAILED = "payment.failed";
    public static final String PAYMENT_REFUNDED = "payment.refunded";
    public static final String PAYMENT_EVENTS = "payment.events";

    // E-Commerce: Delivery Events
    public static final String DELIVERY_ASSIGNED = "delivery.assigned";
    public static final String DELIVERY_PICKED_UP = "delivery.picked.up";
    public static final String DELIVERY_IN_TRANSIT = "delivery.in.transit";
    public static final String DELIVERY_COMPLETED = "delivery.completed";
    public static final String DELIVERY_FAILED = "delivery.failed";
    public static final String DELIVERY_EVENTS = "delivery.events";

    // E-Commerce: Tracking Events
    public static final String TRACKING_GPS_UPDATE = "tracking.gps.update";
    public static final String TRACKING_ETA_UPDATE = "tracking.eta.update";
    public static final String TRACKING_EVENTS = "tracking.events";

    // E-Commerce: Restaurant Events
    public static final String RESTAURANT_CREATED = "restaurant.created";
    public static final String RESTAURANT_UPDATED = "restaurant.updated";
    public static final String RESTAURANT_STATUS_CHANGED = "restaurant.status.changed";
    public static final String MENU_UPDATED = "menu.updated";
    public static final String RESTAURANT_EVENTS = "restaurant.events";

    // E-Commerce: Review Events
    public static final String REVIEW_CREATED = "review.created";
    public static final String REVIEW_UPDATED = "review.updated";
    public static final String REVIEW_MODERATED = "review.moderated";
    public static final String REVIEW_EVENTS = "review.events";

    // E-Commerce: Seller Events
    public static final String SELLER_REGISTERED = "seller.registered";
    public static final String SELLER_VERIFIED = "seller.verified";
    public static final String SELLER_SUSPENDED = "seller.suspended";
    public static final String SELLER_PAYOUT = "seller.payout";
    public static final String SELLER_EVENTS = "seller.events";

    // E-Commerce: Coupon Events
    public static final String COUPON_CREATED = "coupon.created";
    public static final String COUPON_APPLIED = "coupon.applied";
    public static final String COUPON_EXPIRED = "coupon.expired";
    public static final String COUPON_EVENTS = "coupon.events";

    // E-Commerce: Analytics Events
    public static final String ANALYTICS_ORDER_PLACED = "analytics.order.placed";
    public static final String ANALYTICS_USER_ACTION = "analytics.user.action";
    public static final String ANALYTICS_EVENTS = "analytics.events";

    // E-Commerce: Search Sync Events
    public static final String SEARCH_SYNC_PRODUCT = "search.sync.product";
    public static final String SEARCH_SYNC_RESTAURANT = "search.sync.restaurant";
    public static final String SEARCH_SYNC_EVENTS = "search.sync.events";

    // Audit Events
    public static final String AUDIT_LOG = "audit.log";
}

