package com.nexus.external.adapter;

import com.nexus.external.dto.PaymentRequest;
import com.nexus.external.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Stripe Payment Adapter Implementation
 * Integrates with Stripe API for real payment processing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentAdapter implements PaymentService {

    private final WebClient.Builder webClientBuilder;

    @Value("${external.api.payment.api-key}")
    private String apiKey;

    @Value("${external.api.payment.base-url:https://api.stripe.com/v1}")
    private String baseUrl;

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        try {
            log.info("Processing Stripe payment: amount={} {}", request.getAmount(), request.getCurrency());

            // Build payment intent request
            Map<String, Object> paymentIntentRequest = new HashMap<>();
            paymentIntentRequest.put("amount", request.getAmount().multiply(new java.math.BigDecimal("100")).intValue()); // Convert to cents
            paymentIntentRequest.put("currency", request.getCurrency().toLowerCase());
            paymentIntentRequest.put("customer", request.getCustomerId());
            paymentIntentRequest.put("payment_method", request.getPaymentMethodId());
            paymentIntentRequest.put("description", request.getDescription());
            paymentIntentRequest.put("confirm", true);
            paymentIntentRequest.put("automatic_payment_methods[enabled]", true);

            // Call Stripe API
            Map<String, Object> response = webClientBuilder.build()
                    .post()
                    .uri(baseUrl + "/payment_intents")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .bodyValue(buildFormData(paymentIntentRequest))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                log.error("Stripe API returned null response");
                return PaymentResult.failed("PAYMENT_FAILED");
            }

            String status = (String) response.get("status");
            String intentId = (String) response.get("id");

            if ("succeeded".equals(status) || "processing".equals(status)) {
                log.info("Payment successful: intentId={}, status={}", intentId, status);
                return PaymentResult.succeeded(intentId);
            } else {
                log.warn("Payment failed: status={}", status);
                return PaymentResult.failed("PAYMENT_" + status.toUpperCase());
            }

        } catch (Exception e) {
            log.error("Failed to process payment via Stripe", e);
            throw new RuntimeException("Stripe payment failed", e);
        }
    }

    /**
     * Build form-encoded data for Stripe API
     */
    private String buildFormData(Map<String, Object> data) {
        StringBuilder builder = new StringBuilder();
        data.forEach((key, value) -> {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(key).append("=").append(value);
        });
        return builder.toString();
    }
}

