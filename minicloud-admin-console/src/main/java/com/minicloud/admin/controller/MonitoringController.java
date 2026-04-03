package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.AuditLogDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MonitoringController {
    @FXML private TableView<AuditLogDto> logTable;
    @FXML private TableColumn<AuditLogDto, String> timeColumn;
    @FXML private TableColumn<AuditLogDto, String> actionColumn;
    @FXML private TableColumn<AuditLogDto, String> resourceColumn;
    @FXML private TableColumn<AuditLogDto, String> actorColumn;

    private final MiniCloudClient client;

    public MonitoringController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        resourceColumn.setCellValueFactory(new PropertyValueFactory<>("resourceId"));
        actorColumn.setCellValueFactory(new PropertyValueFactory<>("actor"));

        loadData();
    }

    @FXML
    private void loadData() {
        List<AuditLogDto> logs = client.getAuditLogs();
        logTable.setItems(FXCollections.observableArrayList(logs));
    }
}
