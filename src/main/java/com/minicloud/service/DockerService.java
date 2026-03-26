package com.minicloud.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service
public class DockerService {

    private static final Logger log = LoggerFactory.getLogger(DockerService.class);
    private DockerClient dockerClient;
    private boolean dockerAvailable = false;

    public DockerService() {
        try {
            DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

            DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                    .dockerHost(config.getDockerHost())
                    .sslConfig(config.getSSLConfig())
                    .maxConnections(100)
                    .connectionTimeout(Duration.ofSeconds(5))
                    .responseTimeout(Duration.ofSeconds(10))
                    .build();

            this.dockerClient = DockerClientBuilder.getInstance(config)
                    .withDockerHttpClient(httpClient)
                    .build();

            // Validate connection
            dockerClient.pingCmd().exec();
            dockerAvailable = true;
            log.info("DockerService: Connected to Docker daemon successfully.");
        } catch (Throwable e) {
            dockerAvailable = false;
            log.warn("DockerService: Could not connect to Docker daemon ({}). Docker features will be unavailable. Start Docker Desktop to enable them.", e.getMessage());
        }
    }

    private DockerClient getClient() {
        if (!dockerAvailable || dockerClient == null) {
            throw new RuntimeException("Docker daemon is not available. Please start Docker Desktop and restart the application.");
        }
        return dockerClient;
    }

    /**
     * Week 1: Launch a Tomcat container (EC2-like)
     */
    public String launchTomcatContainer() {
        return launchTomcatContainer("tomcat-" + System.currentTimeMillis(), 8080);
    }

    public String launchTomcatContainer(String instanceName, int hostPort) {
        try {
            getClient().pullImageCmd("tomcat:latest").start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Image pull interrupted", e);
        }

        CreateContainerResponse container = getClient().createContainerCmd("tomcat:latest")
                .withName(instanceName)
                .withHostConfig(HostConfig.newHostConfig().withPortBindings(PortBinding.parse(hostPort + ":8080")))
                .exec();

        getClient().startContainerCmd(container.getId()).exec();
        return container.getId();
    }

    /**
     * Week 2: Launch a MySQL container (RDS-like)
     */
    public String launchMysqlInstance(String instanceName, String dbName, String rootPassword, int hostPort) {
        try {
            getClient().pullImageCmd("mysql:8.0").start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Image pull interrupted", e);
        }

        CreateContainerResponse container = getClient().createContainerCmd("mysql:8.0")
                .withName(instanceName)
                .withEnv("MYSQL_ROOT_PASSWORD=" + rootPassword, "MYSQL_DATABASE=" + dbName)
                .withHostConfig(HostConfig.newHostConfig().withPortBindings(PortBinding.parse(hostPort + ":3306")))
                .exec();

        getClient().startContainerCmd(container.getId()).exec();
        return container.getId();
    }

    public void stopContainer(String containerId) {
        getClient().stopContainerCmd(containerId).exec();
    }

    public void removeContainer(String containerId) {
        getClient().removeContainerCmd(containerId).exec();
    }

    public String getContainerStatus(String containerId) {
        if (!dockerAvailable) return "DOCKER_UNAVAILABLE";
        try {
            return dockerClient.inspectContainerCmd(containerId).exec().getState().getStatus();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    public void restartContainer(String containerId) {
        getClient().restartContainerCmd(containerId).exec();
    }

    public Statistics getContainerStats(String containerId) {
        if (!dockerAvailable) return null;
        try {
            final Statistics[] statsHolder = new Statistics[1];
            dockerClient.statsCmd(containerId).withNoStream(true).exec(new com.github.dockerjava.api.async.ResultCallback.Adapter<Statistics>() {
                @Override
                public void onNext(Statistics stats) {
                    statsHolder[0] = stats;
                    try {
                        close();
                    } catch (IOException e) {
                        // Ignore
                    }
                }
            }).awaitCompletion(5, java.util.concurrent.TimeUnit.SECONDS);
            return statsHolder[0];
        } catch (Exception e) {
            return null;
        }
    }

    public String launchPostgresInstance(String instanceName, String dbName, String password, int hostPort) {
        try {
            getClient().pullImageCmd("postgres:latest").start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Image pull interrupted", e);
        }

        CreateContainerResponse container = getClient().createContainerCmd("postgres:latest")
                .withName(instanceName)
                .withEnv("POSTGRES_DB=" + dbName, "POSTGRES_PASSWORD=" + password)
                .withHostConfig(HostConfig.newHostConfig().withPortBindings(PortBinding.parse(hostPort + ":5432")))
                .exec();

        getClient().startContainerCmd(container.getId()).exec();
        return container.getId();
    }

    public boolean isDockerAvailable() {
        return dockerAvailable;
    }
}
