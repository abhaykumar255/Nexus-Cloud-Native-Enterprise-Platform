package com.nexus.auth.domain.repository;

import com.nexus.auth.domain.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    
    Optional<PasswordResetToken> findByToken(String token);
    
    void deleteByUserId(String userId);
}

