package com.minicloud.console.controller;

import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.AuditLogDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CloudTrailController {
    @FXML private TableView<AuditLogDto> tableView;
    @FXML private TableColumn<AuditLogDto, String> timeColumn;
    @FXML private TableColumn<AuditLogDto, String> nameColumn;
    @FXML private TableColumn<AuditLogDto, String> userColumn;
    @FXML private TableColumn<AuditLogDto, String> resourceColumn;
    @FXML private TableColumn<AuditLogDto, String> ipColumn;
    @FXML private TableColumn<AuditLogDto, String> detailsColumn;

    private final MiniCloudClient client;

    public CloudTrailController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        resourceColumn.setCellValueFactory(new PropertyValueFactory<>("resourceId"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("sourceIp"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));

        loadData();
    }

    private void loadData() {
        List<AuditLogDto> logs = client.getAuditLogs();
        tableView.setItems(FXCollections.observableArrayList(logs));
    }
}
