package com.minicloud.console.controller;

import com.minicloud.console.client.MiniCloudClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BillingController {
    @FXML private Label totalCostLabel;
    @FXML private TableView<ServiceCost> serviceTable;
    @FXML private TableColumn<ServiceCost, String> serviceColumn;
    @FXML private TableColumn<ServiceCost, String> costColumn;

    private final MiniCloudClient client;

    public BillingController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("costAmount"));

        loadData();
    }

    @FXML
    private void handleRefresh() {
        loadData();
    }

    private void loadData() {
        Map<String, Object> summary = client.getBillingSummary();
        
        Double total = (Double) summary.get("totalCost");
        totalCostLabel.setText(String.format("$%.2f", total));

        @SuppressWarnings("unchecked")
        Map<String, Double> byService = (Map<String, Double>) summary.get("byService");
        List<ServiceCost> data = new ArrayList<>();
        byService.forEach((name, cost) -> data.add(new ServiceCost(name, String.format("$%.2f", cost))));
        
        serviceTable.setItems(FXCollections.observableArrayList(data));
    }

    public static class ServiceCost {
        private String serviceName;
        private String costAmount;

        public ServiceCost(String serviceName, String costAmount) {
            this.serviceName = serviceName;
            this.costAmount = costAmount;
        }

        public String getServiceName() { return serviceName; }
        public String getCostAmount() { return costAmount; }
    }
}
