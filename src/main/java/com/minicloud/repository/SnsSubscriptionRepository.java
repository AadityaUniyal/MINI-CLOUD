package com.minicloud.repository;

import com.minicloud.model.SnsSubscription;
import com.minicloud.model.SnsTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SnsSubscriptionRepository extends JpaRepository<SnsSubscription, Long> {
    List<SnsSubscription> findByTopic(SnsTopic topic);
}
