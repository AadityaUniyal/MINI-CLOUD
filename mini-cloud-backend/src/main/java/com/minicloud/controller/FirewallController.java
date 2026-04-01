package com.minicloud.controller;

import com.minicloud.model.FirewallRule;
import com.minicloud.model.SecurityGroup;
import com.minicloud.repository.SecurityGroupRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/firewall")
public class FirewallController {
    private final SecurityGroupRepository sGr;

    public FirewallController(SecurityGroupRepository sGr) { this.sGr = sGr; }

    @GetMapping("/groups")
    public List<SecurityGroup> listGroups(Authentication auth) {
        return sGr.findByOwner(auth.getName());
    }

    @PostMapping("/groups")
    public SecurityGroup createGroup(@RequestParam String name, @RequestParam String desc, Authentication auth) {
        if (sGr.findByNameAndOwner(name, auth.getName()) != null) return null;
        SecurityGroup g = SecurityGroup.builder()
            .name(name).description(desc).owner(auth.getName()).vpcId("vpc-DEFAULT")
            .rules(Collections.singletonList(FirewallRule.builder()
                .type("INBOUND").protocol("TCP").portRange("22").source("0.0.0.0/0").description("Default SSH")
                .build()))
            .build();
        return sGr.save(g);
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id, Authentication auth) {
        sGr.findById(id).ifPresent(g -> {
            if (g.getOwner().equals(auth.getName())) sGr.delete(g);
        });
        return ResponseEntity.ok().build();
    }
}
