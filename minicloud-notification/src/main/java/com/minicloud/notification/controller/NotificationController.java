package com.minicloud.notification.controller;

import com.minicloud.notification.service.EmailService;
import com.minicloud.notification.service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final EmailService emailService;
    private final SmsService smsService;

    public NotificationController(EmailService emailService, SmsService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody Map<String, String> request) {
        emailService.sendEmail(request.get("to"), request.get("subject"), request.get("body"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sms")
    public ResponseEntity<Void> sendSms(@RequestBody Map<String, String> request) {
        smsService.sendSms(request.get("to"), request.get("message"));
        return ResponseEntity.ok().build();
    }
}
