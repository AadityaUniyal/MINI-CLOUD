package com.minicloud.backend.service.compute;

import com.minicloud.backend.model.LambdaFunction;
import com.minicloud.backend.repository.LambdaRepository;
import com.minicloud.backend.service.DockerService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LambdaService {

    private final LambdaRepository lambdaRepository;
    private final DockerService dockerService;

    public LambdaService(LambdaRepository lambdaRepository, DockerService dockerService) {
        this.lambdaRepository = lambdaRepository;
        this.dockerService = dockerService;
    }

    public LambdaFunction createFunction(String owner, String name, String runtime, String handler, String code) {
        LambdaFunction function = new LambdaFunction();
        function.setName(name);
        function.setRuntime(runtime);
        function.setHandler(handler);
        function.setCode(code);
        function.setOwner(owner);
        function.setArn("arn:aws:lambda:us-east-1:" + owner + ":function:" + name);
        function.setCreatedAt(LocalDateTime.now());
        return lambdaRepository.save(function);
    }

    public String invokeFunction(String name, String payload) {
        return lambdaRepository.findByName(name).map(f -> {
            String image = getImageForRuntime(f.getRuntime());
            String command = f.getCode(); // Execute code exactly as provided, expecting script to read from env
            
            System.out.println("Invoking Lambda [" + f.getName() + "] in container " + image);
            
            java.util.Map<String, String> env = new java.util.HashMap<>();
            env.put("LAMBDA_EVENT", payload);
            
            return dockerService.executeCommandInContainer(image, command, env);
        }).orElse("ERROR: Function not found");
    }

    private String getImageForRuntime(String runtime) {
        if (runtime == null) return "alpine:latest";
        if (runtime.contains("python")) return "python:3.9-alpine";
        if (runtime.contains("node")) return "node:18-alpine";
        if (runtime.contains("java")) return "openjdk:17-alpine";
        return "alpine:latest";
    }

    public List<LambdaFunction> getFunctionsByOwner(String owner) {
        return lambdaRepository.findByOwner(owner);
    }

    public void deleteFunction(String name) {
        lambdaRepository.findByName(name).ifPresent(lambdaRepository::delete);
    }
}
