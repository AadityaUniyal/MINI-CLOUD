package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.AuditLogDto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MonitoringController {
    @FXML private LineChart<String, Number> cpuChart;
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

        configureChart();
        loadData();
    }

    private void configureChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("CPU %");
        series.getData().add(new XYChart.Data<>("10:00", 12));
        series.getData().add(new XYChart.Data<>("10:05", 15));
        series.getData().add(new XYChart.Data<>("10:10", 10));
        series.getData().add(new XYChart.Data<>("10:15", 45));
        series.getData().add(new XYChart.Data<>("10:20", 30));
        series.getData().add(new XYChart.Data<>("10:25", 14));
        cpuChart.getData().clear();
        cpuChart.getData().add(series);
    }

    @FXML
    private void loadData() {
        new Thread(() -> {
            try {
                List<AuditLogDto> logs = client.getAuditLogs();
                Platform.runLater(() -> logTable.setItems(FXCollections.observableArrayList(logs)));
            } catch (Exception e) {
                // silenty handle connection errors in monitoring background feed
            }
        }).start();
    }
}
