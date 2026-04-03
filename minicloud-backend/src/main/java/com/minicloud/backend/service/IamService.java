package com.minicloud.backend.service;

import com.minicloud.backend.model.IamPolicy;
import com.minicloud.backend.model.IamRole;
import com.minicloud.backend.model.IamGroup;
import com.minicloud.backend.repository.IamPolicyRepository;
import com.minicloud.backend.repository.IamRoleRepository;
import com.minicloud.backend.repository.IamGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;

@Service
public class IamService {

    private final IamPolicyRepository policyRepository;
    private final IamRoleRepository roleRepository;
    private final IamGroupRepository groupRepository;

    public IamService(IamPolicyRepository policyRepository, IamRoleRepository roleRepository, IamGroupRepository groupRepository) {
        this.policyRepository = policyRepository;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
    }

    public IamPolicy createPolicy(String owner, String name, String document) {
        IamPolicy policy = new IamPolicy();
        policy.setName(name);
        policy.setPolicyDocument(document);
        policy.setOwner(owner);
        return policyRepository.save(policy);
    }

    public IamRole createRole(String owner, String name, String description) {
        IamRole role = new IamRole();
        role.setName(name);
        role.setDescription(description);
        role.setOwner(owner);
        role.setAttachedPolicies(new ArrayList<>());
        return roleRepository.save(role);
    }

    @Transactional
    public void attachPolicyToRole(String roleName, String policyName) {
        IamRole role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        IamPolicy policy = policyRepository.findByName(policyName)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        
        if (!role.getAttachedPolicies().contains(policy)) {
            role.getAttachedPolicies().add(policy);
            roleRepository.save(role);
        }
    }

    public IamGroup createGroup(String owner, String name) {
        IamGroup group = new IamGroup();
        group.setGroupName(name);
        group.setOwner(owner);
        return groupRepository.save(group);
    }

    @Transactional
    public void attachPolicyToGroup(String groupName, String policyName) {
        IamGroup group = groupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        IamPolicy policy = policyRepository.findByName(policyName)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        
        if (!group.getAttachedPolicies().contains(policy)) {
            group.getAttachedPolicies().add(policy);
            groupRepository.save(group);
        }
    }

    public List<IamGroup> getGroupsByOwner(String owner) {
        return groupRepository.findByOwner(owner);
    }

    public List<IamRole> getRolesByOwner(String owner) {
        return roleRepository.findByOwner(owner);
    }

    public List<IamPolicy> getPoliciesByOwner(String owner) {
        return policyRepository.findByOwner(owner);
    }
}
