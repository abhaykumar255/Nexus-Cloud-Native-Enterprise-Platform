package com.nexus.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway filter configuration
 * Applies JWT authentication filter to all routes
 */
@Configuration
public class GatewayFilterConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Apply JWT filter to all routes as a global filter
                .route("auth-jwt", r -> r.path("/api/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("no://op"))
                .build();
    }
}

