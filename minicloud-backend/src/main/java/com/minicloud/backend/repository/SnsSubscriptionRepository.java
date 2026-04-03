package com.minicloud.backend.repository;

import com.minicloud.backend.model.SnsSubscription;
import com.minicloud.backend.model.SnsTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SnsSubscriptionRepository extends JpaRepository<SnsSubscription, Long> {
    List<SnsSubscription> findByTopic(SnsTopic topic);
}
