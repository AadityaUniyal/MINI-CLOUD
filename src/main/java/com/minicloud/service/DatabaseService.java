package com.minicloud.service;

import com.minicloud.model.DatabaseInstance;
import com.minicloud.repository.DatabaseInstanceRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DatabaseService {

    private final DockerService dockerService;
    private final DatabaseInstanceRepository databaseInstanceRepository;
    private static int NEXT_PORT = 33061;

    public DatabaseService(DockerService dockerService, DatabaseInstanceRepository databaseInstanceRepository) {
        this.dockerService = dockerService;
        this.databaseInstanceRepository = databaseInstanceRepository;
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
    public DatabaseInstance provisionDatabase(String owner, String name, String dbName, String rootPassword) {
        synchronized (this) {
            int hostPort = NEXT_PORT++;
            String containerId = dockerService.launchMysqlInstance(name, dbName, rootPassword, hostPort);

        DatabaseInstance instance = DatabaseInstance.builder()
                .name(name)
                .containerId(containerId)
                .dbName(dbName)
                .rootPassword(rootPassword)
                .hostPort(hostPort)
                .status("RUNNING")
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .build();

            return databaseInstanceRepository.save(instance);
        }
    }

    public List<DatabaseInstance> getAllDatabases() {
        return databaseInstanceRepository.findAll();
    }

    public void stopDatabase(String name) {
        databaseInstanceRepository.findByName(name).ifPresent(instance -> {
            dockerService.stopContainer(instance.getContainerId());
            instance.setStatus("STOPPED");
            databaseInstanceRepository.save(instance);
        });
    }
}
