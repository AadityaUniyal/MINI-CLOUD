package com.minicloud.service.compute;

import com.minicloud.model.LambdaFunction;
import com.minicloud.repository.LambdaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LambdaService {

    private final LambdaRepository lambdaRepository;

    public LambdaService(LambdaRepository lambdaRepository) {
        this.lambdaRepository = lambdaRepository;
    }

    public LambdaFunction createFunction(String owner, String name, String runtime, String handler, String code) {
        LambdaFunction function = new LambdaFunction();
        function.setName(name);
        function.setRuntime(runtime);
        function.setHandler(handler);
        function.setCode(code);
        function.setOwner(owner);
        function.setArn("arn:minicloud:lambda:region:account:function:" + name);
        function.setCreatedAt(LocalDateTime.now());
        return lambdaRepository.save(function);
    }

    public String invokeFunction(String name, String payload) {
        return lambdaRepository.findByName(name).map(f -> {
            // Mock execution
            System.out.println("Invoking Lambda [" + f.getName() + "] with payload: " + payload);
            return "SUCCESS: Output for " + f.getName();
        }).orElse("ERROR: Function not found");
    }

    public List<LambdaFunction> getFunctionsByOwner(String owner) {
        return lambdaRepository.findByOwner(owner);
    }

    public void deleteFunction(String name) {
        lambdaRepository.findByName(name).ifPresent(lambdaRepository::delete);
    }
}
