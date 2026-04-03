package com.minicloud.compute.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class DockerService {

    private DockerClient dockerClient;

    @PostConstruct
    public void init() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
        this.dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build();
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
