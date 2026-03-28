package com.nexus.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = {"com.nexus.coupon", "com.nexus.common"})
@EntityScan(basePackages = {"com.nexus.coupon.domain.entity"})
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableCaching
@EnableKafka
public class CouponServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponServiceApplication.class, args);
    }
}

