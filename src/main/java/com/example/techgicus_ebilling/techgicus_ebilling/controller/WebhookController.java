package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.service.SubscriptionService;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    private SubscriptionService subscriptionService;

    @Value("${razorpay.webhook_secret:}")
    private String webhookSecret;

    @Autowired
    public WebhookController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/api/razorpay/webhook")
    public ResponseEntity<String> handleWebhook(@RequestHeader(value = "X-Razorpay-Signature", required = false) String signature,
                                                @RequestBody String payload) {
        try {
            if (webhookSecret == null || webhookSecret.isBlank()) {
                return ResponseEntity.status(500).body("webhook_secret_not_configured");
            }

            String generated = HmacUtils.hmacSha256Hex(webhookSecret, payload);
            if (!generated.equals(signature)) {
                return ResponseEntity.status(400).body("signature_mismatch");
            }

            JSONObject obj = new JSONObject(payload);
            String event = obj.optString("event");

            if ("payment.captured".equals(event) || "payment.authorized".equals(event)) {
                JSONObject payment = obj.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");
                String orderId = payment.optString("order_id");
                String paymentId = payment.optString("id");
                int amount = payment.optInt("amount");
                subscriptionService.handlePaymentCaptured(orderId, paymentId, amount);
            }

            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("error");
        }
    }


}
