package com.nexus.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Email service for sending emails
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${notification.email.from}")
    private String fromEmail;
    
    @Value("${notification.email.from-name}")
    private String fromName;
    
    /**
     * Send simple email
     */
    @Async("notificationTaskExecutor")
    public void sendSimpleEmail(String to, String subject, String text) {
        log.info("Sending simple email to: {}", to);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            
            log.info("Simple email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send simple email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    /**
     * Send HTML email with template
     */
    @Async("notificationTaskExecutor")
    public void sendTemplateEmail(String to, String subject, String template, Map<String, Object> variables) {
        log.info("Sending template email to: {} using template: {}", to, template);
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            
            // Process template
            Context context = new Context();
            context.setVariables(variables);
            String html = templateEngine.process(template, context);
            
            helper.setText(html, true);
            
            mailSender.send(message);
            
            log.info("Template email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send template email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        } catch (Exception e) {
            log.error("Unexpected error sending email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

