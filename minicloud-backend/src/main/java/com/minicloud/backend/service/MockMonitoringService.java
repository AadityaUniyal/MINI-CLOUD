package com.minicloud.backend.service.monitoring;

import com.minicloud.backend.repository.ComputeInstanceRepository;
import com.minicloud.backend.repository.DatabaseInstanceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class MockMonitoringService {

    private final MetricService metricService;
    private final ComputeInstanceRepository computeRepository;
    private final DatabaseInstanceRepository databaseRepository;
    private final Random random = new Random();

    public MockMonitoringService(MetricService metricService, 
                                 ComputeInstanceRepository computeRepository,
                                 DatabaseInstanceRepository databaseRepository) {
        this.metricService = metricService;
        this.computeRepository = computeRepository;
        this.databaseRepository = databaseRepository;
    }

    @Scheduled(fixedRate = 60000) // Every minute
    public void generateMockMetrics() {
        // CPU for EC2
        computeRepository.findAll().stream()
                .filter(inst -> "RUNNING".equals(inst.getStatus()))
                .forEach(inst -> {
                    metricService.recordMetric(inst.getName(), "CPUUtilization", 10 + random.nextDouble() * 40, "Percent");
                    metricService.recordMetric(inst.getName(), "MemoryUsage", 200 + random.nextDouble() * 800, "Megabytes");
                });

        // CPU/Connections for RDS
        databaseRepository.findAll().stream()
                .filter(db -> "RUNNING".equals(db.getStatus()))
                .forEach(db -> {
                    metricService.recordMetric(db.getName(), "CPUUtilization", 5 + random.nextDouble() * 20, "Percent");
                    metricService.recordMetric(db.getName(), "DatabaseConnections", (double) (1 + random.nextInt(10)), "Count");
                });
    }
}
