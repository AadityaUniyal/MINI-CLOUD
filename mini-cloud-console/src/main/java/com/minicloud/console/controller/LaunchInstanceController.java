package com.minicloud.console.controller;

import com.minicloud.console.MainController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class LaunchInstanceController {
    @FXML private TextField nameField;
    @FXML private ComboBox<String> amiCombo;
    @FXML private ComboBox<String> vpcCombo;

    private final MainController mainController;

    public LaunchInstanceController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        amiCombo.setItems(FXCollections.observableArrayList("Amazon Linux 2023", "Ubuntu 22.04 LTS", "Windows Server 2022"));
        vpcCombo.setItems(FXCollections.observableArrayList("primary-vpc (10.0.0.0/16)", "secondary-vpc (192.168.0.0/24)"));
        
        amiCombo.getSelectionModel().selectFirst();
        vpcCombo.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleLaunch() {
        String name = nameField.getText();
        if (name != null && !name.isEmpty()) {
            // In a real app, we'd call client.launchInstance(...)
            System.out.println("Launching instance: " + name);
            mainController.showEc2(); // Go back after launch
        }
    }

    @FXML
    private void handleCancel() {
        mainController.showEc2();
    }
}
