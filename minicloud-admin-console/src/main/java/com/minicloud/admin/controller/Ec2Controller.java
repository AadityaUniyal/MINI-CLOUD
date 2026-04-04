package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.InstanceDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Ec2Controller {
    @FXML private TableView<InstanceDto> tableView;
    @FXML private TableColumn<InstanceDto, String> idColumn;
    @FXML private TableColumn<InstanceDto, String> nameColumn;
    @FXML private TableColumn<InstanceDto, String> stateColumn;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button terminateButton;

    private final MiniCloudClient client;
    private final ObservableList<InstanceDto> instances = FXCollections.observableArrayList();

    public Ec2Controller(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("instanceId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        tableView.setItems(instances);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean selected = (newVal != null);
            startButton.setDisable(!selected || "RUNNING".equals(newVal.getState()));
            stopButton.setDisable(!selected || !"RUNNING".equals(newVal.getState()));
            terminateButton.setDisable(!selected);
        });

        loadInstances();
    }

    private void loadInstances() {
        new Thread(() -> {
            try {
                List<InstanceDto> list = client.getInstances();
                javafx.application.Platform.runLater(() -> instances.setAll(list));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleLaunch() {
        // In a real app, this would open a dialog. For now, let's just log.
        System.out.println("Launch Instance clicked");
    }

    @FXML
    private void handleStart() {
        InstanceDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Starting instance: " + selected.getInstanceId());
            // client.startInstance(selected.getInstanceId());
            loadInstances();
        }
    }

    @FXML
    private void handleStop() {
        InstanceDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Stopping instance: " + selected.getInstanceId());
            // client.stopInstance(selected.getInstanceId());
            loadInstances();
        }
    }

    @FXML
    private void handleTerminate() {
        InstanceDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to terminate instance " + selected.getInstanceId() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(type -> {
                if (type == ButtonType.YES) {
                    System.out.println("Terminating instance: " + selected.getInstanceId());
                    // client.terminateInstance(selected.getInstanceId());
                    loadInstances();
                }
            });
        }
    }
}
