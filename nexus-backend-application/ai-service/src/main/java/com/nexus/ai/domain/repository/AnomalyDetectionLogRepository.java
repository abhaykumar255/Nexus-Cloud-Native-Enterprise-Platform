package com.nexus.ai.domain.repository;

import com.nexus.ai.domain.document.AnomalyDetectionLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnomalyDetectionLogRepository extends MongoRepository<AnomalyDetectionLog, String> {
    
    List<AnomalyDetectionLog> findByServiceName(String serviceName);
    
    List<AnomalyDetectionLog> findByIsAnomalyTrue();
    
    List<AnomalyDetectionLog> findByTimestampAfter(LocalDateTime timestamp);
    
    List<AnomalyDetectionLog> findByServiceNameAndTimestampAfter(String serviceName, LocalDateTime timestamp);
}

