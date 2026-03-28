package com.nexus.notification.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailService Unit Tests")
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@nexus.com");
        ReflectionTestUtils.setField(emailService, "fromName", "Nexus Platform");
    }

    @Test
    @DisplayName("Send simple email successfully")
    void sendSimpleEmail_success() {
        // Arrange
        String to = "user@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendSimpleEmail(to, subject, text);

        // Assert
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Send simple email failure throws RuntimeException")
    void sendSimpleEmail_failure() {
        // Arrange
        String to = "user@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        doThrow(new RuntimeException("Mail server error"))
            .when(mailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        assertThatThrownBy(() -> emailService.sendSimpleEmail(to, subject, text))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Failed to send email");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Send template email successfully")
    void sendTemplateEmail_success() throws Exception {
        // Arrange
        String to = "user@example.com";
        String subject = "Welcome";
        String template = "welcome-email";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "John Doe");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq(template), any(Context.class)))
            .thenReturn("<html><body>Welcome John Doe</body></html>");
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        emailService.sendTemplateEmail(to, subject, template, variables);

        // Assert
        verify(mailSender).createMimeMessage();
        verify(templateEngine).process(eq(template), any(Context.class));
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Send template email with empty variables")
    void sendTemplateEmail_emptyVariables() throws Exception {
        // Arrange
        String to = "user@example.com";
        String subject = "Notification";
        String template = "generic-template";
        Map<String, Object> variables = new HashMap<>();

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq(template), any(Context.class)))
            .thenReturn("<html><body>Generic message</body></html>");
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        emailService.sendTemplateEmail(to, subject, template, variables);

        // Assert
        verify(mailSender).createMimeMessage();
        verify(templateEngine).process(eq(template), any(Context.class));
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Send template email failure throws RuntimeException")
    void sendTemplateEmail_failure() {
        // Arrange
        String to = "user@example.com";
        String subject = "Welcome";
        String template = "welcome-email";
        Map<String, Object> variables = new HashMap<>();

        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Template error"));

        // Act & Assert
        assertThatThrownBy(() -> emailService.sendTemplateEmail(to, subject, template, variables))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Failed to send email");

        verify(mailSender).createMimeMessage();
    }
}

