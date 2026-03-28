package com.nexus.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.nexus.tracking", "com.nexus.common"})
public class TrackingServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TrackingServiceApplication.class, args);
    }
}

