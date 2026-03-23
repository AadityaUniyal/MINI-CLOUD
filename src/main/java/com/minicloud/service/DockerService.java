package com.minicloud.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DockerService {

    private final DockerClient dockerClient;

    public DockerService() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        this.dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build();
    }

    /**
     * Week 1: Launch a Tomcat container (EC2-like)
     * Overloaded method to match the signature in the task description.
     */
    public String launchTomcatContainer() {
        return launchTomcatContainer("tomcat-" + System.currentTimeMillis());
    }

    public String launchTomcatContainer(String instanceName) {
        dockerClient.pullImageCmd("tomcat:latest").start().awaitCompletion();

        CreateContainerResponse container = dockerClient.createContainerCmd("tomcat:latest")
                .withName(instanceName)
                .withHostConfig(HostConfig.newHostConfig().withPortBindings(PortBinding.parse("8080:8080")))
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();
        return container.getId();
    }

    /**
     * Week 2: Launch a MySQL container (RDS-like)
     * Maps host port dynamically starting from 33061.
     */
    public String launchMysqlInstance(String instanceName, String dbName, String rootPassword, int hostPort) {
        dockerClient.pullImageCmd("mysql:8.0").start().awaitCompletion();

        CreateContainerResponse container = dockerClient.createContainerCmd("mysql:8.0")
                .withName(instanceName)
                .withEnv("MYSQL_ROOT_PASSWORD=" + rootPassword, "MYSQL_DATABASE=" + dbName)
                .withHostConfig(HostConfig.newHostConfig().withPortBindings(PortBinding.parse(hostPort + ":3306")))
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();
        return container.getId();
    }

    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    public void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
    }

    public String getContainerStatus(String containerId) {
        try {
            return dockerClient.inspectContainerCmd(containerId).exec().getState().getStatus();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    public void restartContainer(String containerId) {
        dockerClient.restartContainerCmd(containerId).exec();
    }
}
