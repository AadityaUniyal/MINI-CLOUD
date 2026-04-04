package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DashboardController {
    @FXML private Label instanceCountLabel;
    @FXML private Label bucketCountLabel;
    @FXML private Label userCountLabel;
    @FXML private Label lambdaCountLabel;
    @FXML private Label apiStatusLabel;

    @FXML private TableView<AuditLogDto> activityTable;
    @FXML private TableColumn<AuditLogDto, String> timeColumn;
    @FXML private TableColumn<AuditLogDto, String> actionColumn;
    @FXML private TableColumn<AuditLogDto, String> resourceColumn;

    private final MiniCloudClient client;

    public DashboardController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        resourceColumn.setCellValueFactory(new PropertyValueFactory<>("resourceId"));

        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            try {
                int instances = client.getInstances().size();
                int buckets = client.getBuckets().size();
                int users = client.getIamUsers().size();
                int lambdas = client.getLambdas().size();
                List<AuditLogDto> recentLogs = client.getAuditLogs().stream().limit(5).collect(Collectors.toList());

                Platform.runLater(() -> {
                    instanceCountLabel.setText(String.valueOf(instances));
                    bucketCountLabel.setText(String.valueOf(buckets));
                    userCountLabel.setText(String.valueOf(users));
                    lambdaCountLabel.setText(String.valueOf(lambdas));
                    activityTable.setItems(FXCollections.observableArrayList(recentLogs));
                    
                    apiStatusLabel.setText("ONLINE");
                    apiStatusLabel.getStyleClass().setAll("status-pill", "status-running");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    apiStatusLabel.setText("OFFLINE");
                    apiStatusLabel.getStyleClass().setAll("status-pill", "status-stopped");
                });
            }
        }).start();
    }

    @FXML
    private void handleOpenDiagnostics() {
        com.minicloud.admin.swing.SystemHealthMonitor.showMonitor();
    }
}
