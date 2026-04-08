package com.minicloud.compute.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class DockerService {

    private static final Logger log = LoggerFactory.getLogger(DockerService.class);

    private DockerClient dockerClient;
    private boolean dockerAvailable = false;

    @PostConstruct
    public void init() {
        try {
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
            ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                    .dockerHost(config.getDockerHost())
                    .sslConfig(config.getSSLConfig())
                    .build();
            this.dockerClient = DockerClientBuilder.getInstance(config)
                    .withDockerHttpClient(httpClient)
                    .build();
            // Verify connectivity
            this.dockerClient.pingCmd().exec();
            this.dockerAvailable = true;
            log.info("Docker daemon connected successfully.");
        } catch (Exception e) {
            this.dockerAvailable = false;
            log.warn("Docker daemon not available — compute service running in degraded mode. Cause: {}", e.getMessage());
        }
    }

    public boolean isDockerAvailable() {
        return dockerAvailable;
    }

    public String createInstance(String imageName, String name) {
        // Ensure image exists
        dockerClient.pullImageCmd(imageName).start().onComplete();
        
        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withName(name)
                .withHostConfig(HostConfig.newHostConfig())
                .exec();
        return container.getId();
    }

    public void startInstance(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    public void stopInstance(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    public void terminateInstance(String containerId) {
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();
    }

    public String getIpAddress(String containerId) {
        return dockerClient.inspectContainerCmd(containerId).exec()
                .getNetworkSettings()
                .getNetworks()
                .values()
                .stream()
                .findFirst()
                .map(network -> network.getIpAddress())
                .orElse("");
    }
}
