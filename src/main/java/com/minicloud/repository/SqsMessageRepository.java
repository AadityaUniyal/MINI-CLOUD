package com.minicloud.repository;

import com.minicloud.model.SqsMessage;
import com.minicloud.model.SqsQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SqsMessageRepository extends JpaRepository<SqsMessage, Long> {
    List<SqsMessage> findByQueueAndStatus(SqsQueue queue, String status);
}
