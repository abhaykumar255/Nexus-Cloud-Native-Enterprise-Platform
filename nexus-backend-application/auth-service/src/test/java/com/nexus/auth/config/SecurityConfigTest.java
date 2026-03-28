package com.nexus.auth.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    private SecurityProperties securityProperties;
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityProperties = new SecurityProperties();
        securityConfig = new SecurityConfig(securityProperties);
    }

    @Test
    @DisplayName("PasswordEncoder bean is BCryptPasswordEncoder with correct strength")
    void passwordEncoder_isBCryptPasswordEncoderWithCorrectStrength() {
        // Act
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Assert
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
        
        // Verify it can encode passwords
        String rawPassword = "testPassword123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
        assertThat(passwordEncoder.matches("wrongPassword", encodedPassword)).isFalse();
    }

    @Test
    @DisplayName("PasswordEncoder uses custom bcrypt strength from properties")
    void passwordEncoder_usesCustomBcryptStrength() {
        // Arrange
        SecurityProperties customProperties = new SecurityProperties();
        SecurityProperties.PasswordConfig passwordConfig = new SecurityProperties.PasswordConfig();
        passwordConfig.setBcryptStrength(10);
        customProperties.setPassword(passwordConfig);
        
        SecurityConfig customConfig = new SecurityConfig(customProperties);

        // Act
        PasswordEncoder passwordEncoder = customConfig.passwordEncoder();

        // Assert
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
        
        // Verify it can encode passwords with custom strength
        String rawPassword = "customPassword123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("PasswordEncoder encodes different passwords differently")
    void passwordEncoder_encodesDifferentPasswordsDifferently() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String password1 = "password1";
        String password2 = "password2";

        // Act
        String encoded1 = passwordEncoder.encode(password1);
        String encoded2 = passwordEncoder.encode(password2);

        // Assert
        assertThat(encoded1).isNotEqualTo(encoded2);
        assertThat(passwordEncoder.matches(password1, encoded1)).isTrue();
        assertThat(passwordEncoder.matches(password2, encoded2)).isTrue();
        assertThat(passwordEncoder.matches(password1, encoded2)).isFalse();
        assertThat(passwordEncoder.matches(password2, encoded1)).isFalse();
    }

    @Test
    @DisplayName("PasswordEncoder produces different hashes for same password")
    void passwordEncoder_producesDifferentHashesForSamePassword() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String password = "samePassword";

        // Act
        String encoded1 = passwordEncoder.encode(password);
        String encoded2 = passwordEncoder.encode(password);

        // Assert - BCrypt adds salt, so each encoding is different
        assertThat(encoded1).isNotEqualTo(encoded2);
        assertThat(passwordEncoder.matches(password, encoded1)).isTrue();
        assertThat(passwordEncoder.matches(password, encoded2)).isTrue();
    }
}

