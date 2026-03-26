package com.minicloud.config;

import com.minicloud.model.*;
import com.minicloud.repository.*;
import com.minicloud.service.BucketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import com.minicloud.model.VPC;
import com.minicloud.model.Subnet;
import com.minicloud.model.User;
import com.minicloud.model.ComputeInstance;
import com.minicloud.model.DatabaseInstance;
import com.minicloud.model.Stack;
import com.minicloud.model.SecurityGroup;
import com.minicloud.model.FirewallRule;
import com.minicloud.repository.*;
import java.util.Collections;
import java.util.Arrays;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ComputeInstanceRepository computeInstanceRepository;
    private final DatabaseInstanceRepository databaseInstanceRepository;
    private final StackRepository stackRepository;
    private final PasswordEncoder passwordEncoder;
    private final BucketRepository bucketRepository;
    private final SecurityGroupRepository securityGroupRepository;
    private final BucketService bucketService;
    private final VPCRepository vpcRepository;
    private final SubnetRepository subnetRepository;

    public DataSeeder(UserRepository userRepository, 
                      ComputeInstanceRepository computeInstanceRepository,
                      DatabaseInstanceRepository databaseInstanceRepository,
                      StackRepository stackRepository,
                      PasswordEncoder passwordEncoder,
                      BucketRepository bucketRepository,
                      SecurityGroupRepository securityGroupRepository,
                      BucketService bucketService,
                      VPCRepository vpcRepository,
                      SubnetRepository subnetRepository) {
        this.userRepository = userRepository;
        this.computeInstanceRepository = computeInstanceRepository;
        this.databaseInstanceRepository = databaseInstanceRepository;
        this.stackRepository = stackRepository;
        this.passwordEncoder = passwordEncoder;
        this.bucketRepository = bucketRepository;
        this.securityGroupRepository = securityGroupRepository;
        this.bucketService = bucketService;
        this.vpcRepository = vpcRepository;
        this.subnetRepository = subnetRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) return;

        // 1. Create Sample AWS Root Account
        String adminAccountId = "123456789012";
        User rootAdmin = User.builder()
                .username("root@minicloud.com")
                .email("root@minicloud.com")
                .password(passwordEncoder.encode("root123"))
                .role("ROLE_ADMIN")
                .isRootUser(true)
                .accountId(adminAccountId)
                .accountAlias("minicloud-production")
                .fullName("MiniCloud Admin")
                .phoneNumber("+1-555-0199")
                .address("123 Cloud Way, Seattle, WA")
                .country("USA")
                .balance(1000.0)
                .status("ACTIVE")
                .build();
        userRepository.save(rootAdmin);

        // 1.1 Create Default VPC & Subnet for Root
        VPC defaultVpc = VPC.builder()
                .vpcId("vpc-0a1b2c3d")
                .cidrBlock("10.0.0.0/16")
                .state("available")
                .tenancy("default")
                .owner("root@minicloud.com")
                .enableDnsSupport(true)
                .enableDnsHostnames(true)
                .creationTime(LocalDateTime.now())
                .build();
        vpcRepository.save(defaultVpc);

        Subnet defaultSubnet = Subnet.builder()
                .subnetId("subnet-0e1f2g3h")
                .vpcId("vpc-0a1b2c3d")
                .cidrBlock("10.0.1.0/24")
                .availabilityZone("us-east-1a")
                .state("available")
                .owner("root@minicloud.com")
                .availableIpAddressCount(251)
                .creationTime(LocalDateTime.now())
                .build();
        subnetRepository.save(defaultSubnet);

        // Security Group for Root
        if (securityGroupRepository.findByOwner("root@minicloud.com").isEmpty()) {
            securityGroupRepository.save(SecurityGroup.builder()
                .name("default").description("Default SG").owner("root@minicloud.com").vpcId("vpc-0a1b2c3d")
                .rules(Collections.singletonList(FirewallRule.builder()
                    .type("INBOUND").protocol("TCP").portRange("0-65535").source("0.0.0.0/0").description("Allow all for Root")
                    .build()))
                .build());
        }

        // 2. Create Sample IAM User under the same account
        User iamUser = User.builder()
                .username(adminAccountId + "/iam-admin")
                .iamUsername("iam-admin")
                .accountId(adminAccountId)
                .isRootUser(false)
                .password(passwordEncoder.encode("iam123"))
                .role("ROLE_USER")
                .owner("root@minicloud.com") // Linked to Root
                .balance(500.0)
                .status("ACTIVE")
                .build();
        userRepository.save(iamUser);

        // 3. Create Sample Compute Instance (owned by rootAdmin)
        ComputeInstance instance = ComputeInstance.builder()
                .name("prod-web-server")
                .containerId("mock-container-123")
                .image("tomcat:latest")
                .status("RUNNING")
                .owner("root@minicloud.com")
                .instanceType("t2.medium")
                .region("us-east-1")
                .availabilityZone("us-east-1a")
                .publicIp("54.214.12.88")
                .privateIp("172.31.22.5")
                .vpcId("vpc-0a1b2c3d")
                .subnetId("subnet-0e1f2g3h")
                .amiId("ami-12345678")
                .ebsOptimized(true)
                .securityGroups(Arrays.asList("web-sg", "default"))
                .keyPairName("prod-key")
                .hostPort(8081)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();
        computeInstanceRepository.save(instance);

        // 3. Create Sample Database
        DatabaseInstance db = DatabaseInstance.builder()
                .name("prod-db-instance")
                .containerId("mock-db-456")
                .dbName("minicloud_db")
                .rootPassword("dbpass123")
                .hostPort(33061)
                .status("RUNNING")
                .owner("root@minicloud.com")
                .engine("MySQL")
                .engineVersion("8.0")
                .dbInstanceClass("db.t3.small")
                .storageType("gp2")
                .allocatedStorage(20)
                .multiAz(true)
                .publiclyAccessible(false)
                .vpcId("vpc-0a1b2c3d")
                .subnetId("subnet-0e1f2g3h")
                .backupRetention(7)
                .createdAt(LocalDateTime.now().minusDays(5))
                .build();
        databaseInstanceRepository.save(db);

        // 4. Create Sample Stack
        Stack stack = Stack.builder()
                .stackId(UUID.randomUUID().toString())
                .name("Production-Stack")
                .owner("root@minicloud.com")
                .databaseId(db.getId())
                .computeInstanceIds(Arrays.asList(instance.getId()))
                .createdAt(LocalDateTime.now().minusHours(12))
                .build();
        stackRepository.save(stack);
        
        // 5. Create Sample Bucket
        if (bucketRepository.findByName("sample-logs-bucket").isEmpty()) {
            try {
                bucketService.createBucket("sample-logs-bucket", "root@minicloud.com");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("DataSeeder: Database seeded with high-fidelity cloud metadata.");
    }
}
