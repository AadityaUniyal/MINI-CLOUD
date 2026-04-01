package com.minicloud.console.controller;

import com.minicloud.console.MainController;
import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.InstanceDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Ec2Controller {
    @FXML private TableView<InstanceDto> tableView;
    @FXML private TableColumn<InstanceDto, Long> idColumn;
    @FXML private TableColumn<InstanceDto, String> nameColumn;
    @FXML private TableColumn<InstanceDto, String> stateColumn;

    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button terminateButton;

    private final MiniCloudClient client;
    private final MainController mainController;

    public Ec2Controller(MiniCloudClient client, MainController mainController) {
        this.client = client;
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        stateColumn.setCellFactory(column -> new javafx.scene.control.TableCell<InstanceDto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toUpperCase());
                    getStyleClass().add("status-pill");
                    getStyleClass().removeAll("status-running", "status-stopped", "status-pending");
                    
                    if (item.equalsIgnoreCase("running")) {
                        getStyleClass().add("status-running");
                    } else if (item.equalsIgnoreCase("stopped")) {
                        getStyleClass().add("status-stopped");
                    } else {
                        getStyleClass().add("status-pending");
                    }
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                boolean isRunning = newSelection.getState().equalsIgnoreCase("running");
                boolean isStopped = newSelection.getState().equalsIgnoreCase("stopped");
                
                startButton.setDisable(isRunning);
                stopButton.setDisable(isStopped);
                terminateButton.setDisable(false);
            } else {
                startButton.setDisable(true);
                stopButton.setDisable(true);
                terminateButton.setDisable(true);
            }
        });

        loadData();
    }

    @FXML
    private void handleStart() {
        InstanceDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            client.startInstance(selected.getId());
            loadData();
        }
    }

    @FXML
    private void handleStop() {
        InstanceDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            client.stopInstance(selected.getId());
            loadData();
        }
    }

    @FXML
    private void handleTerminate() {
        InstanceDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            client.terminateInstance(selected.getId());
            loadData();
        }
    }

    @FXML
    private void handleLaunch() {
        mainController.showLaunchInstance();
    }

    private void loadData() {
        List<InstanceDto> instances = client.getInstances();
        tableView.setItems(FXCollections.observableArrayList(instances));
    }
}
