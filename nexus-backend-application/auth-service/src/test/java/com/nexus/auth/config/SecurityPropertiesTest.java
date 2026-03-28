package com.nexus.auth.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SecurityProperties Tests")
class SecurityPropertiesTest {

    @Test
    @DisplayName("SecurityProperties default values are set correctly")
    void securityProperties_defaultValues() {
        // Act
        SecurityProperties properties = new SecurityProperties();

        // Assert
        assertThat(properties.getPassword()).isNotNull();
        assertThat(properties.getPassword().getBcryptStrength()).isEqualTo(12);
    }

    @Test
    @DisplayName("SecurityProperties setters work correctly")
    void securityProperties_settersWorkCorrectly() {
        // Arrange
        SecurityProperties properties = new SecurityProperties();
        SecurityProperties.PasswordConfig passwordConfig = new SecurityProperties.PasswordConfig();
        passwordConfig.setBcryptStrength(14);

        // Act
        properties.setPassword(passwordConfig);

        // Assert
        assertThat(properties.getPassword().getBcryptStrength()).isEqualTo(14);
    }

    @Test
    @DisplayName("PasswordConfig setters work correctly")
    void passwordConfig_settersWorkCorrectly() {
        // Arrange
        SecurityProperties.PasswordConfig passwordConfig = new SecurityProperties.PasswordConfig();

        // Act
        passwordConfig.setBcryptStrength(15);

        // Assert
        assertThat(passwordConfig.getBcryptStrength()).isEqualTo(15);
    }

    @Test
    @DisplayName("PasswordConfig default bcrypt strength is 12")
    void passwordConfig_defaultBcryptStrength() {
        // Act
        SecurityProperties.PasswordConfig passwordConfig = new SecurityProperties.PasswordConfig();

        // Assert
        assertThat(passwordConfig.getBcryptStrength()).isEqualTo(12);
    }

    @Test
    @DisplayName("SecurityProperties toString contains PasswordConfig")
    void securityProperties_toStringContainsPasswordConfig() {
        // Arrange
        SecurityProperties properties = new SecurityProperties();

        // Act
        String toString = properties.toString();

        // Assert
        assertThat(toString).contains("password");
    }

    @Test
    @DisplayName("PasswordConfig toString contains bcryptStrength")
    void passwordConfig_toStringContainsBcryptStrength() {
        // Arrange
        SecurityProperties.PasswordConfig passwordConfig = new SecurityProperties.PasswordConfig();
        passwordConfig.setBcryptStrength(14);

        // Act
        String toString = passwordConfig.toString();

        // Assert
        assertThat(toString).contains("14");
    }

    @Test
    @DisplayName("SecurityProperties equals and hashCode work correctly")
    void securityProperties_equalsAndHashCode() {
        // Arrange
        SecurityProperties properties1 = new SecurityProperties();
        SecurityProperties.PasswordConfig passwordConfig1 = new SecurityProperties.PasswordConfig();
        passwordConfig1.setBcryptStrength(12);
        properties1.setPassword(passwordConfig1);

        SecurityProperties properties2 = new SecurityProperties();
        SecurityProperties.PasswordConfig passwordConfig2 = new SecurityProperties.PasswordConfig();
        passwordConfig2.setBcryptStrength(12);
        properties2.setPassword(passwordConfig2);

        SecurityProperties properties3 = new SecurityProperties();
        SecurityProperties.PasswordConfig passwordConfig3 = new SecurityProperties.PasswordConfig();
        passwordConfig3.setBcryptStrength(15);
        properties3.setPassword(passwordConfig3);

        // Assert
        assertThat(properties1).isEqualTo(properties2);
        assertThat(properties1).isNotEqualTo(properties3);
        assertThat(properties1.hashCode()).isEqualTo(properties2.hashCode());
    }

    @Test
    @DisplayName("PasswordConfig equals and hashCode work correctly")
    void passwordConfig_equalsAndHashCode() {
        // Arrange
        SecurityProperties.PasswordConfig config1 = new SecurityProperties.PasswordConfig();
        config1.setBcryptStrength(12);

        SecurityProperties.PasswordConfig config2 = new SecurityProperties.PasswordConfig();
        config2.setBcryptStrength(12);

        SecurityProperties.PasswordConfig config3 = new SecurityProperties.PasswordConfig();
        config3.setBcryptStrength(15);

        // Assert
        assertThat(config1).isEqualTo(config2);
        assertThat(config1).isNotEqualTo(config3);
        assertThat(config1.hashCode()).isEqualTo(config2.hashCode());
    }
}

