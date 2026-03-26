package com.minicloud.service;

import com.minicloud.model.SecurityFinding;
import com.minicloud.model.WafRule;
import com.minicloud.repository.SecurityFindingRepository;
import com.minicloud.repository.WafRuleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SecurityService {

    private final SecurityFindingRepository findingRepository;
    private final WafRuleRepository wafRuleRepository;

    public SecurityService(SecurityFindingRepository findingRepository, WafRuleRepository wafRuleRepository) {
        this.findingRepository = findingRepository;
        this.wafRuleRepository = wafRuleRepository;
    }

    public SecurityFinding generateMockFinding(String owner, String type, String severity, String resourceId, String description) {
        SecurityFinding finding = new SecurityFinding();
        finding.setOwner(owner);
        finding.setType(type);
        finding.setSeverity(severity);
        finding.setResourceId(resourceId);
        finding.setDescription(description);
        finding.setTimestamp(LocalDateTime.now());
        return findingRepository.save(finding);
    }

    public List<SecurityFinding> getFindingsByOwner(String owner) {
        return findingRepository.findByOwner(owner);
    }

    public WafRule createWafRule(String owner, String name, String action, String scope, String pattern) {
        WafRule rule = new WafRule();
        rule.setOwner(owner);
        rule.setRuleName(name);
        rule.setAction(action);
        rule.setScope(scope);
        rule.setPattern(pattern);
        return wafRuleRepository.save(rule);
    }

    public List<WafRule> getWafRulesByOwner(String owner) {
        return wafRuleRepository.findByOwner(owner);
    }

    public void deleteWafRule(String ruleName) {
        wafRuleRepository.findByRuleName(ruleName).ifPresent(wafRuleRepository::delete);
    }
}
