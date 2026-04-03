package com.minicloud.admin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.minicloud.admin", "com.minicloud.common"})
public class MiniCloudApplication {
    // This is the Spring Boot entry point for the Admin Console
}
