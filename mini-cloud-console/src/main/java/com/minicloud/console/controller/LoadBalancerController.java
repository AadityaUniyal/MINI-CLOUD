package com.minicloud.console.controller;

import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.LoadBalancerDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadBalancerController {
    @FXML private TableView<LoadBalancerDto> tableView;
    @FXML private TableColumn<LoadBalancerDto, String> nameColumn;
    @FXML private TableColumn<LoadBalancerDto, String> dnsColumn;
    @FXML private TableColumn<LoadBalancerDto, Integer> portColumn;
    @FXML private TableColumn<LoadBalancerDto, String> vpcColumn;
    @FXML private TableColumn<LoadBalancerDto, String> statusColumn;

    private final MiniCloudClient client;

    public LoadBalancerController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dnsColumn.setCellValueFactory(new PropertyValueFactory<>("dnsName"));
        portColumn.setCellValueFactory(new PropertyValueFactory<>("publicPort"));
        vpcColumn.setCellValueFactory(new PropertyValueFactory<>("vpcId"));
        
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(column -> new TableCell<LoadBalancerDto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toUpperCase());
                    getStyleClass().add("status-pill");
                    if (item.equalsIgnoreCase("active")) {
                        getStyleClass().add("status-running");
                    } else {
                        getStyleClass().add("status-pending");
                    }
                }
            }
        });

        loadData();
    }

    private void loadData() {
        List<LoadBalancerDto> lbs = client.getLoadBalancers();
        tableView.setItems(FXCollections.observableArrayList(lbs));
    }
}
