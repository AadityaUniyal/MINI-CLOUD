package com.minicloud.repository;

import com.minicloud.model.WafRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WafRuleRepository extends JpaRepository<WafRule, Long> {
    Optional<WafRule> findByRuleName(String ruleName);
    List<WafRule> findByOwner(String owner);
}
