package com.nexus.common.enums;

import java.math.BigDecimal;

/**
 * Loyalty Tier Enum
 * Points system for customer rewards
 */
public enum LoyaltyTier {
    BRONZE(0, 999, new BigDecimal("1.0"), 0),
    SILVER(1000, 4999, new BigDecimal("1.5"), 5),
    GOLD(5000, 14999, new BigDecimal("2.0"), 10),
    PLATINUM(15000, Integer.MAX_VALUE, new BigDecimal("3.0"), 15);
    
    private final int minPoints;
    private final int maxPoints;
    private final BigDecimal earnMultiplier;
    private final int maxRedemptionPercent;
    
    LoyaltyTier(int minPoints, int maxPoints, BigDecimal earnMultiplier, int maxRedemptionPercent) {
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
        this.earnMultiplier = earnMultiplier;
        this.maxRedemptionPercent = maxRedemptionPercent;
    }
    
    public int getMinPoints() {
        return minPoints;
    }
    
    public int getMaxPoints() {
        return maxPoints;
    }
    
    public BigDecimal getEarnMultiplier() {
        return earnMultiplier;
    }
    
    public int getMaxRedemptionPercent() {
        return maxRedemptionPercent;
    }
    
    public static LoyaltyTier fromPoints(int points) {
        for (LoyaltyTier tier : values()) {
            if (points >= tier.minPoints && points <= tier.maxPoints) {
                return tier;
            }
        }
        return BRONZE;
    }
}

