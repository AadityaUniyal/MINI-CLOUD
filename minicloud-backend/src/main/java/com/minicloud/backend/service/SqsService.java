package com.minicloud.backend.service;

import com.minicloud.backend.model.SqsQueue;
import com.minicloud.backend.model.SqsMessage;
import com.minicloud.backend.repository.SqsQueueRepository;
import com.minicloud.backend.repository.SqsMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SqsService {

    private static final Logger log = LoggerFactory.getLogger(SqsService.class);
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
        queue.setArn("arn:aws:sqs:us-east-1:" + owner + ":" + name);
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
        log.info("Message sent to queue [{}]: {}", queueName, body);
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

    /**
     * True Cloud Background Worker
     * Runs every 10 seconds to process pending SQS messages dynamically
     */
    @Scheduled(fixedRate = 10000)
    public void processSqsQueueWorker() {
        List<SqsQueue> queues = queueRepository.findAll();
        for (SqsQueue queue : queues) {
            List<SqsMessage> pending = messageRepository.findByQueueAndStatus(queue, "PENDING");
            for (SqsMessage msg : pending) {
                try {
                    msg.setStatus("PROCESSING");
                    messageRepository.save(msg);
                    
                    log.info("SQS Worker processing message [{}] from queue [{}]...", msg.getId(), queue.getName());
                    Thread.sleep(1000); // simulate delay constraint

                    msg.setStatus("PROCESSED");
                    messageRepository.save(msg);
                    log.info("SQS message [{}] processed successfully.", msg.getId());
                } catch (Exception e) {
                    msg.setStatus("FAILED");
                    messageRepository.save(msg);
                }
            }
        }
    }
}
