package com.minicloud.backend.repository;

import com.minicloud.backend.model.SqsMessage;
import com.minicloud.backend.model.SqsQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SqsMessageRepository extends JpaRepository<SqsMessage, Long> {
    List<SqsMessage> findByQueueAndStatus(SqsQueue queue, String status);
}
