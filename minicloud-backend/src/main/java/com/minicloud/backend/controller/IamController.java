package com.minicloud.backend.controller;

import com.minicloud.backend.model.IamPolicy;
import com.minicloud.backend.model.IamRole;
import com.minicloud.backend.model.IamGroup;
import com.minicloud.backend.service.IamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/iam")
public class IamController {

    private final IamService iamService;

    public IamController(IamService iamService) {
        this.iamService = iamService;
    }

    @PostMapping("/policy")
    public ResponseEntity<IamPolicy> createPolicy(@RequestBody Map<String, String> request, Principal principal) {
        return ResponseEntity.ok(iamService.createPolicy(
                principal.getName(),
                request.get("name"),
                request.get("document")
        ));
    }

    @GetMapping("/policies")
    public List<IamPolicy> listPolicies(Principal principal) {
        return iamService.getPoliciesByOwner(principal.getName());
    }

    @PostMapping("/role")
    public ResponseEntity<IamRole> createRole(@RequestBody Map<String, String> request, Principal principal) {
        return ResponseEntity.ok(iamService.createRole(
                principal.getName(),
                request.get("name"),
                request.get("description")
        ));
    }

    @PostMapping("/role/{roleName}/attach")
    public ResponseEntity<Void> attachPolicy(@PathVariable String roleName, @RequestParam String policyName) {
        iamService.attachPolicyToRole(roleName, policyName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roles")
    public List<IamRole> listRoles(Principal principal) {
        return iamService.getRolesByOwner(principal.getName());
    }

    @PostMapping("/group")
    public ResponseEntity<IamGroup> createGroup(@RequestBody Map<String, String> request, Principal principal) {
        return ResponseEntity.ok(iamService.createGroup(principal.getName(), request.get("name")));
    }

    @GetMapping("/groups")
    public List<IamGroup> listGroups(Principal principal) {
        return iamService.getGroupsByOwner(principal.getName());
    }

    @PostMapping("/group/{groupName}/attach")
    public ResponseEntity<Void> attachPolicyToGroup(@PathVariable String groupName, @RequestParam String policyName) {
        iamService.attachPolicyToGroup(groupName, policyName);
        return ResponseEntity.ok().build();
    }
}
