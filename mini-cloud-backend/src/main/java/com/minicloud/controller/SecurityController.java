package com.minicloud.controller;

import com.minicloud.model.SecurityFinding;
import com.minicloud.model.WafRule;
import com.minicloud.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/finding/mock")
    public ResponseEntity<SecurityFinding> generateFinding(@RequestBody Map<String, String> request, Principal principal) {
        return ResponseEntity.ok(securityService.generateMockFinding(
                principal.getName(),
                request.get("type"),
                request.get("severity"),
                request.get("resourceId"),
                request.get("description")
        ));
    }

    @GetMapping("/findings")
    public List<SecurityFinding> listFindings(Principal principal) {
        return securityService.getFindingsByOwner(principal.getName());
    }

    @PostMapping("/waf/rule")
    public ResponseEntity<WafRule> createWafRule(@RequestBody Map<String, String> request, Principal principal) {
        return ResponseEntity.ok(securityService.createWafRule(
                principal.getName(),
                request.get("name"),
                request.get("action"),
                request.get("scope"),
                request.get("pattern")
        ));
    }

    @GetMapping("/waf/rules")
    public List<WafRule> listWafRules(Principal principal) {
        return securityService.getWafRulesByOwner(principal.getName());
    }

    @DeleteMapping("/waf/rule/{name}")
    public ResponseEntity<Void> deleteWafRule(@PathVariable String name) {
        securityService.deleteWafRule(name);
        return ResponseEntity.ok().build();
    }
}
