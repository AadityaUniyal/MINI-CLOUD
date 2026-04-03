package com.minicloud.backend.service;

import com.minicloud.backend.model.DatabaseInstance;
import com.minicloud.backend.model.AuditLog;
import com.minicloud.backend.repository.AuditLogRepository;
import com.minicloud.backend.repository.DatabaseInstanceRepository;
import com.minicloud.common.dto.ProvisionDbRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DatabaseService {

    private final DockerService dockerService;
    private final DatabaseInstanceRepository databaseInstanceRepository;
    private final BillingService billingService;
    private final AuditLogRepository auditLogRepository;
    private static int NEXT_PORT = 33061;

    public DatabaseService(DockerService dockerService, 
                           DatabaseInstanceRepository databaseInstanceRepository,
                           BillingService billingService,
                           AuditLogRepository auditLogRepository) {
        this.dockerService = dockerService;
        this.databaseInstanceRepository = databaseInstanceRepository;
        this.billingService = billingService;
        this.auditLogRepository = auditLogRepository;
        initializeNextPort();
    }

    private void initializeNextPort() {
        databaseInstanceRepository.findAll().stream()
                .mapToInt(DatabaseInstance::getHostPort)
                .max()
                .ifPresent(maxPort -> NEXT_PORT = maxPort + 1);
    }

    /**
     * Week 2 Core: Provision a MySQL container and track it in H2.
     */
    public DatabaseInstance provisionDatabase(String owner, ProvisionDbRequest request) {
        synchronized (this) {
            int hostPort = NEXT_PORT++;
            String containerId;
            if ("postgres".equalsIgnoreCase(request.getEngine())) {
                containerId = dockerService.launchPostgresInstance(request.getName(), request.getDbName(), request.getRootPassword(), hostPort);
            } else {
                containerId = dockerService.launchMysqlInstance(request.getName(), request.getDbName(), request.getRootPassword(), hostPort);
            }

            DatabaseInstance instance = DatabaseInstance.builder()
                    .name(request.getName())
                    .containerId(containerId)
                    .dbName(request.getDbName())
                    .rootPassword(request.getRootPassword())
                    .hostPort(hostPort)
                    .status("RUNNING")
                    .owner(owner)
                    .engine(request.getEngine() != null ? request.getEngine() : "mysql")
                    .engineVersion(request.getEngineVersion() != null ? request.getEngineVersion() : "8.0")
                    .dbInstanceClass(request.getDbInstanceClass() != null ? request.getDbInstanceClass() : "db.t3.micro")
                    .vpcId(request.getVpcId())
                    .subnetId(request.getSubnetId())
                    .storageType("gp2")
                    .allocatedStorage(20)
                    .createdAt(LocalDateTime.now())
                    .build();

            DatabaseInstance savedInstance = databaseInstanceRepository.save(instance);

            // Audit Log & Billing
            auditLogRepository.save(AuditLog.builder()
                    .username(owner)
                    .action("PROVISION_DATABASE")
                    .resourceId(containerId)
                    .timestamp(LocalDateTime.now())
                    .details("Provisioned " + instance.getEngine() + " instance: " + request.getName())
                    .build());
            billingService.startBilling(owner, containerId, "DATABASE");

            return savedInstance;
        }
    }

    public List<DatabaseInstance> getDatabasesByOwner(String owner) {
        return databaseInstanceRepository.findByOwner(owner);
    }

    public List<DatabaseInstance> getAllDatabases() {
        return databaseInstanceRepository.findAll();
    }

    public void stopDatabase(String name) {
        databaseInstanceRepository.findByName(name).ifPresent(instance -> {
            dockerService.stopContainer(instance.getContainerId());
            instance.setStatus("STOPPED");
            databaseInstanceRepository.save(instance);

            // Audit Log & Billing
            auditLogRepository.save(AuditLog.builder()
                    .username(instance.getOwner())
                    .action("STOP_DATABASE")
                    .resourceId(instance.getContainerId())
                    .timestamp(LocalDateTime.now())
                    .details("Stopped database: " + name)
                    .build());
            billingService.stopBilling(instance.getContainerId());
        });
    }

    @Transactional
    public void terminateDatabase(Long id, String owner) {
        databaseInstanceRepository.findById(id).ifPresent(instance -> {
            if (instance.getOwner().equals(owner)) {
                dockerService.stopContainer(instance.getContainerId());
                dockerService.removeContainer(instance.getContainerId());
                
                // Audit Log & Billing
                auditLogRepository.save(AuditLog.builder()
                        .username(owner)
                        .action("TERMINATE_DATABASE")
                        .resourceId(instance.getContainerId())
                        .timestamp(LocalDateTime.now())
                        .details("Terminated database: " + instance.getName())
                        .build());
                billingService.stopBilling(instance.getContainerId());

                databaseInstanceRepository.delete(instance);
            }
        });
    }

    @Transactional
    public void terminateDatabase(String name) {
        databaseInstanceRepository.findByName(name).ifPresent(instance -> {
            dockerService.stopContainer(instance.getContainerId());
            dockerService.removeContainer(instance.getContainerId());
            
            // Audit Log & Billing
            auditLogRepository.save(AuditLog.builder()
                    .username(instance.getOwner())
                    .action("TERMINATE_DATABASE")
                    .resourceId(instance.getContainerId())
                    .timestamp(LocalDateTime.now())
                    .details("Terminated database: " + name)
                    .build());
            billingService.stopBilling(instance.getContainerId());

            databaseInstanceRepository.delete(instance);
        });
    }
    @Transactional
    public DatabaseInstance createReadReplica(String owner, Long sourceId) {
        DatabaseInstance source = databaseInstanceRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("Source database not found"));
        
        if (!source.getOwner().equals(owner)) {
            throw new RuntimeException("Unauthorized to create replica");
        }

        String replicaName = source.getName() + "-replica";
        int hostPort;
        synchronized (this) {
            hostPort = NEXT_PORT++;
        }
        
        String containerId = dockerService.launchMysqlInstance(replicaName, source.getDbName(), source.getRootPassword(), hostPort);

        DatabaseInstance replica = DatabaseInstance.builder()
                .name(replicaName)
                .containerId(containerId)
                .dbName(source.getDbName())
                .rootPassword(source.getRootPassword())
                .hostPort(hostPort)
                .status("RUNNING")
                .owner(owner)
                .engine(source.getEngine())
                .engineVersion(source.getEngineVersion())
                .dbInstanceClass(source.getDbInstanceClass())
                .readReplicaSourceId(source.getName())
                .vpcId(source.getVpcId())
                .subnetId(source.getSubnetId())
                .createdAt(LocalDateTime.now())
                .build();

        return databaseInstanceRepository.save(replica);
    }
}
