package com.minicloud.service;

import com.minicloud.model.SqsQueue;
import com.minicloud.model.SqsMessage;
import com.minicloud.repository.SqsQueueRepository;
import com.minicloud.repository.SqsMessageRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SqsService {

    private final SqsQueueRepository queueRepository;
    private final SqsMessageRepository messageRepository;

    public SqsService(SqsQueueRepository queueRepository, SqsMessageRepository messageRepository) {
        this.queueRepository = queueRepository;
        this.messageRepository = messageRepository;
    }

    public SqsQueue createQueue(String owner, String name) {
        SqsQueue queue = new SqsQueue();
        queue.setName(name);
        queue.setOwner(owner);
        queue.setQueueUrl("http://sqs.minicloud.com/" + owner + "/" + name);
        queue.setCreatedAt(LocalDateTime.now());
        queue.setVisibilityTimeout(30);
        return queueRepository.save(queue);
    }

    public void sendMessage(String queueName, String body) {
        SqsQueue queue = queueRepository.findByName(queueName)
                .orElseThrow(() -> new RuntimeException("Queue not found"));
        
        SqsMessage msg = new SqsMessage();
        msg.setQueue(queue);
        msg.setBody(body);
        msg.setStatus("PENDING");
        msg.setCreatedAt(LocalDateTime.now());
        messageRepository.save(msg);
    }

    public Optional<SqsMessage> receiveMessage(String queueName) {
        SqsQueue queue = queueRepository.findByName(queueName)
                .orElseThrow(() -> new RuntimeException("Queue not found"));
        
        List<SqsMessage> msgs = messageRepository.findByQueueAndStatus(queue, "PENDING");
        if (msgs.isEmpty()) return Optional.empty();
        
        SqsMessage msg = msgs.get(0);
        msg.setStatus("PROCESSING");
        return Optional.of(messageRepository.save(msg));
    }
}
