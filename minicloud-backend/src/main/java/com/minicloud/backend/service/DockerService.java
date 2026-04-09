package com.minicloud.backend.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Service
public class DockerService {

    private static final Logger log = LoggerFactory.getLogger(DockerService.class);
    private DockerClient dockerClient;
    private boolean dockerAvailable = false;
    private final Map<String, HttpServer> simulators = new ConcurrentHashMap<>();

    public DockerService() {
        try {
            // Best configuration for Windows: Use the Named Pipe (npipe)
            String dockerHost = "npipe:////./pipe/docker_engine";
            
            DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(dockerHost)
                    .build();

            DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                    .dockerHost(config.getDockerHost())
                    .sslConfig(config.getSSLConfig())
                    .maxConnections(100)
                    .connectionTimeout(Duration.ofSeconds(5))
                    .responseTimeout(Duration.ofSeconds(10))
                    .build();

            this.dockerClient = DockerClientImpl.getInstance(config, httpClient);

            // Validate connection
            dockerClient.pingCmd().exec();
            dockerAvailable = true;
            log.info("DockerService: Connected to Docker daemon successfully.");
        } catch (Throwable e) {
            dockerAvailable = false;
            log.warn("DockerService: Could not connect to Docker daemon ({}). MiniCloud will gracefully fall back to Embedded Emulator Mode. Instances will launch as background Java threads.", e.getMessage());
        }
    }

    private DockerClient getClient() {
        if (!dockerAvailable || dockerClient == null) {
            throw new RuntimeException("Docker daemon is not available. Please start Docker Desktop and restart the application.");
        }
        return dockerClient;
    }

    private void startSimulator(String simId, int port, String type) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", exchange -> {
                String response = "<h1>MiniCloud " + type + " Simulation</h1>"
                        + "<p>This is a fallback EC2/RDS embedded emulator serving port " + port + "</p>"
                        + "<p>Instance ID: " + simId + "</p>";
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.length());
                java.io.OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            });
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            simulators.put(simId, server);
            log.info("Started Embedded Simulator [{}] on port {}", type, port);
        } catch (Exception e) {
            log.error("Failed to start embedded simulator on port {}", port, e);
        }
    }

    public String launchTomcatContainer() {
        return launchTomcatContainer("tomcat-" + System.currentTimeMillis(), 8080);
    }

    public String launchTomcatContainer(String instanceName, int hostPort) {
        if (!dockerAvailable) {
            String simId = "sim-" + instanceName + "-" + System.currentTimeMillis();
            startSimulator(simId, hostPort, "Tomcat EC2");
            return simId;
        }

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

    public String launchMysqlInstance(String instanceName, String dbName, String rootPassword, int hostPort) {
        if (!dockerAvailable) {
            String simId = "sim-" + instanceName + "-" + System.currentTimeMillis();
            startSimulator(simId, hostPort, "MySQL RDS");
            return simId;
        }

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

    public String launchPostgresInstance(String instanceName, String dbName, String password, int hostPort) {
        if (!dockerAvailable) {
            String simId = "sim-" + instanceName + "-" + System.currentTimeMillis();
            startSimulator(simId, hostPort, "Postgres RDS");
            return simId;
        }

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

    public void stopContainer(String containerId) {
        if (containerId.startsWith("sim-")) {
            if (simulators.containsKey(containerId)) {
                simulators.get(containerId).stop(0);
            }
            return;
        }
        getClient().stopContainerCmd(containerId).exec();
    }

    public void startContainer(String containerId) {
        if (containerId.startsWith("sim-")) {
            // Simulator is already running
            return;
        }
        getClient().startContainerCmd(containerId).exec();
    }

    public void removeContainer(String containerId) {
        if (containerId.startsWith("sim-")) {
            simulators.remove(containerId);
            return;
        }
        getClient().removeContainerCmd(containerId).exec();
    }

    public String getContainerStatus(String containerId) {
        if (containerId.startsWith("sim-")) {
            return simulators.containsKey(containerId) ? "running" : "exited";
        }
        if (!dockerAvailable) return "DOCKER_UNAVAILABLE";
        try {
            return dockerClient.inspectContainerCmd(containerId).exec().getState().getStatus();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    public void restartContainer(String containerId) {
        if (containerId.startsWith("sim-")) return; // Simulators don't pause easily
        getClient().restartContainerCmd(containerId).exec();
    }

    public com.github.dockerjava.api.model.Statistics getContainerStats(String containerId) {
        if (containerId.startsWith("sim-")) return null; // Can't map full Docker stats to HTTP Server
        if (!dockerAvailable) return null;
        try {
            final com.github.dockerjava.api.model.Statistics[] statsHolder = new com.github.dockerjava.api.model.Statistics[1];
            dockerClient.statsCmd(containerId).withNoStream(true).exec(new com.github.dockerjava.api.async.ResultCallback.Adapter<com.github.dockerjava.api.model.Statistics>() {
                @Override
                public void onNext(com.github.dockerjava.api.model.Statistics stats) {
                    statsHolder[0] = stats;
                    try { close(); } catch (IOException e) { }
                }
            }).awaitCompletion(5, java.util.concurrent.TimeUnit.SECONDS);
            return statsHolder[0];
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isDockerAvailable() {
        return dockerAvailable;
    }

    public String executeCommandInContainer(String image, String command) {
        return executeCommandInContainer(image, command, null);
    }

    public String executeCommandInContainer(String image, String command, Map<String, String> env) {
        if (!dockerAvailable) {
            log.info("Emulator Mode: Intercepted pseudo-lambda execution [{}]", command);
            try { Thread.sleep(600); } catch(Exception ignored){}
            return "Execution simulated in Emulator Mode: \n{ \"status\": 200, \"message\": \"Fallback Lambda Execution Successful\" }";
        }

        try {
            getClient().pullImageCmd(image).start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "ERROR: Pull interrupted";
        }

        com.github.dockerjava.api.command.CreateContainerCmd createCmd = getClient().createContainerCmd(image)
                .withCmd("sh", "-c", command);

        if (env != null && !env.isEmpty()) {
            java.util.List<String> envList = new java.util.ArrayList<>();
            env.forEach((k, v) -> envList.add(k + "=" + v));
            createCmd.withEnv(envList);
        }

        CreateContainerResponse container = createCmd.exec();
        getClient().startContainerCmd(container.getId()).exec();

        StringBuilder logs = new StringBuilder();
        try {
            getClient().logContainerCmd(container.getId())
                    .withStdOut(true)
                    .withStdErr(true)
                    .withFollowStream(true)
                    .exec(new com.github.dockerjava.api.async.ResultCallback.Adapter<com.github.dockerjava.api.model.Frame>() {
                        @Override
                        public void onNext(com.github.dockerjava.api.model.Frame item) {
                            logs.append(new String(item.getPayload()));
                        }
                    }).awaitCompletion(10, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            getClient().removeContainerCmd(container.getId()).withForce(true).exec();
        }

        return logs.toString().trim();
    }
}
