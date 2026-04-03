package com.minicloud.admin.controller;


import com.minicloud.admin.client.MiniCloudClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.springframework.stereotype.Component;

@Component
public class InvokeLambdaController {
    @FXML private Label titleLabel;
    @FXML private TextArea payloadArea;
    @FXML private TextArea resultArea;

    private final MiniCloudClient client;
    private final MainController mainController;
    private String functionName;

    public InvokeLambdaController(MiniCloudClient client, MainController mainController) {
        this.client = client;
        this.mainController = mainController;
    }

    public void setFunction(String name) {
        this.functionName = name;
        this.titleLabel.setText("Invoke: " + name);
        this.resultArea.clear();
    }

    @FXML
    private void handleInvoke() {
        if (functionName != null) {
            String payload = payloadArea.getText();
            String result = client.invokeFunction(functionName, payload);
            resultArea.setText(result);
        }
    }

    @FXML
    private void handleClose() {
        mainController.showLambda();
    }
}
