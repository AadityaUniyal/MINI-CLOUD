package com.minicloud.controller;

import com.minicloud.service.SnsService;
import com.minicloud.service.SqsService;
import com.minicloud.model.SnsTopic;
import com.minicloud.model.SqsQueue;
import com.minicloud.model.SqsMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/messaging")
public class MessagingController {

    private final SnsService snsService;
    private final SqsService sqsService;

    public MessagingController(SnsService snsService, SqsService sqsService) {
        this.snsService = snsService;
        this.sqsService = sqsService;
    }

    @PostMapping("/sns/topic")
    public ResponseEntity<SnsTopic> createTopic(@RequestParam String name, Principal principal) {
        return ResponseEntity.ok(snsService.createTopic(principal.getName(), name));
    }

    @PostMapping("/sns/subscribe")
    public ResponseEntity<Void> subscribe(@RequestParam String topicName, @RequestParam String protocol, @RequestParam String endpoint) {
        snsService.subscribe(topicName, protocol, endpoint);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sns/publish")
    public ResponseEntity<Void> publish(@RequestParam String topicName, @RequestBody String message) {
        snsService.publish(topicName, message);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sqs/queue")
    public ResponseEntity<SqsQueue> createQueue(@RequestParam String name, Principal principal) {
        return ResponseEntity.ok(sqsService.createQueue(principal.getName(), name));
    }

    @PostMapping("/sqs/send")
    public ResponseEntity<Void> sendMessage(@RequestParam String queueName, @RequestBody String body) {
        sqsService.sendMessage(queueName, body);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sqs/receive")
    public ResponseEntity<SqsMessage> receiveMessage(@RequestParam String queueName) {
        return sqsService.receiveMessage(queueName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
