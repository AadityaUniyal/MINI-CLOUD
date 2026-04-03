package com.minicloud.notification.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class SmsService {

    @Value("${twilio.account-sid:ACxxxx}")
    private String accountSid;

    @Value("${twilio.auth-token:token}")
    private String authToken;

    @Value("${twilio.from-number:+1234567}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        if (!"ACxxxx".equals(accountSid)) {
            Twilio.init(accountSid, authToken);
        }
    }

    public void sendSms(String to, String messageBody) {
        if ("ACxxxx".equals(accountSid)) {
            System.out.println("MOCK SMS to " + to + ": " + messageBody);
            return;
        }
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(fromNumber),
                messageBody
        ).create();
    }
}
