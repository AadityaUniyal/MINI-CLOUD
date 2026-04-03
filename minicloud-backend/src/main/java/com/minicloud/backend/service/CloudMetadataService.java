package com.minicloud.backend.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CloudMetadataService {

    public Map<String, List<String>> getCloudOptions() {
        Map<String, List<String>> options = new HashMap<>();

        options.put("regions", Arrays.asList(
            "us-east-1 (N. Virginia)",
            "us-west-2 (Oregon)",
            "eu-central-1 (Frankfurt)",
            "ap-south-1 (Mumbai)",
            "sa-east-1 (São Paulo)"
        ));

        options.put("instanceTypes", Arrays.asList(
            "t2.micro (1 vCPU, 1 GiB RAM)",
            "t2.small (1 vCPU, 2 GiB RAM)",
            "t2.medium (2 vCPU, 4 GiB RAM)",
            "t3.large (2 vCPU, 8 GiB RAM)",
            "m5.xlarge (4 vCPU, 16 GiB RAM)"
        ));

        options.put("dbInstanceClasses", Arrays.asList(
            "db.t3.micro",
            "db.t3.small",
            "db.m5.large",
            "db.r5.xlarge"
        ));

        options.put("dbEngines", Arrays.asList(
            "MySQL 8.0",
            "PostgreSQL 14",
            "MariaDB 10.6",
            "SQL Server Express"
        ));

        options.put("images", Arrays.asList(
            "Ubuntu Server 22.04 LTS",
            "Amazon Linux 2023",
            "Debian 12",
            "Windows Server 2022 Base"
        ));

        options.put("storageTypes", Arrays.asList(
            "General Purpose SSD (gp2)",
            "General Purpose SSD (gp3)",
            "Provisioned IOPS SSD (io1)",
            "Magnetic (standard)"
        ));

        return options;
    }
}
