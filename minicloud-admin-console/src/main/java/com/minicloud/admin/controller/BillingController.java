package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.BillingRecordDto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingController {
    @FXML private Label totalCostLabel;
    @FXML private BarChart<String, Number> costChart;
    @FXML private TableView<BillingRecordDto> serviceTable;
    @FXML private TableColumn<BillingRecordDto, String> serviceColumn;
    @FXML private TableColumn<BillingRecordDto, Double> costColumn;

    private final MiniCloudClient client;

    public BillingController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("resourceType"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        configureChart();
        loadData();
    }

    private void configureChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Cost");
        series.getData().add(new XYChart.Data<>("Jan", 450));
        series.getData().add(new XYChart.Data<>("Feb", 520));
        series.getData().add(new XYChart.Data<>("Mar", 480));
        series.getData().add(new XYChart.Data<>("Apr", 610));
        costChart.getData().clear();
        costChart.getData().add(series);
    }

    @FXML
    private void handleRefresh() {
        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            try {
                List<BillingRecordDto> records = client.getBillingRecords();
                double total = records.stream().mapToDouble(BillingRecordDto::getCost).sum();
                
                Platform.runLater(() -> {
                    totalCostLabel.setText(String.format("$%.2f", total));
                    serviceTable.setItems(FXCollections.observableArrayList(records));
                });
            } catch (Exception e) {
                // Silently handle
            }
        }).start();
    }
}
