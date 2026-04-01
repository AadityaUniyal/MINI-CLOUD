package com.minicloud.service;

import com.minicloud.model.SnsTopic;
import com.minicloud.model.SnsSubscription;
import com.minicloud.repository.SnsTopicRepository;
import com.minicloud.repository.SnsSubscriptionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.List;

@Service
public class SnsService {

    private final SnsTopicRepository topicRepository;
    private final SnsSubscriptionRepository subscriptionRepository;
    private final SqsService sqsService;

    public SnsService(SnsTopicRepository topicRepository, 
                      SnsSubscriptionRepository subscriptionRepository,
                      SqsService sqsService) {
        this.topicRepository = topicRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.sqsService = sqsService;
    }

    public SnsTopic createTopic(String owner, String name) {
        SnsTopic topic = new SnsTopic();
        topic.setName(name);
        topic.setOwner(owner);
        topic.setArn("arn:aws:sns:us-east-1:" + owner + ":" + name);
        topic.setCreatedAt(LocalDateTime.now());
        return topicRepository.save(topic);
    }

    public void subscribe(String topicName, String protocol, String endpoint) {
        SnsTopic topic = topicRepository.findByName(topicName)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        
        SnsSubscription sub = new SnsSubscription();
        sub.setTopic(topic);
        sub.setProtocol(protocol);
        sub.setEndpoint(endpoint);
        subscriptionRepository.save(sub);
    }

    public void publish(String topicName, String message) {
        SnsTopic topic = topicRepository.findByName(topicName)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        
        List<SnsSubscription> subs = subscriptionRepository.findByTopic(topic);
        for (SnsSubscription sub : subs) {
            if ("sqs".equalsIgnoreCase(sub.getProtocol())) {
                System.out.println("SNS Delivery [sqs to " + sub.getEndpoint() + "]: Forwarding message to SQS queue.");
                sqsService.sendMessage(sub.getEndpoint(), message);
            } else {
                // Mock delivery for other protocols (email, sms, etc)
                System.out.println("SNS Delivery [" + sub.getProtocol() + " to " + sub.getEndpoint() + "]: " + message);
            }
        }
    }
}
