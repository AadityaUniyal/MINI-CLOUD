package com.minicloud.database.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.UUID;

@Service
public class DatabaseService {

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

    public String provisionDatabase(String engine, String dbName, String user, String password) {
        String imageName = engine.equalsIgnoreCase("mysql") ? "mysql:8.0" : "postgres:14-alpine";
        String containerName = "db-" + UUID.randomUUID().toString().substring(0, 8);
        
        try {
            dockerClient.pullImageCmd(imageName).start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String[] env = engine.equalsIgnoreCase("mysql") ? 
            new String[]{"MYSQL_ROOT_PASSWORD=" + password, "MYSQL_DATABASE=" + dbName, "MYSQL_USER=" + user, "MYSQL_PASSWORD=" + password} :
            new String[]{"POSTGRES_PASSWORD=" + password, "POSTGRES_USER=" + user, "POSTGRES_DB=" + dbName};

        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withName(containerName)
                .withEnv(env)
                .exec();
        
        dockerClient.startContainerCmd(container.getId()).exec();
        return container.getId();
    }

    public void terminateDatabase(String containerId) {
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();
    }

    public String getDatabaseIp(String containerId) {
        return dockerClient.inspectContainerCmd(containerId).exec().getNetworkSettings().getIpAddress();
    }
}
