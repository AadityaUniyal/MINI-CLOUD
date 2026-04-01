package com.minicloud.console.controller;

import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.SubnetDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubnetController {
    @FXML private TableView<SubnetDto> tableView;
    @FXML private TableColumn<SubnetDto, String> idColumn;
    @FXML private TableColumn<SubnetDto, String> vpcColumn;
    @FXML private TableColumn<SubnetDto, String> cidrColumn;
    @FXML private TableColumn<SubnetDto, String> azColumn;
    @FXML private TableColumn<SubnetDto, Integer> ipColumn;

    private final MiniCloudClient client;

    public SubnetController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("subnetId"));
        vpcColumn.setCellValueFactory(new PropertyValueFactory<>("vpcId"));
        cidrColumn.setCellValueFactory(new PropertyValueFactory<>("cidrBlock"));
        azColumn.setCellValueFactory(new PropertyValueFactory<>("availabilityZone"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("availableIpAddresses"));

        loadData();
    }

    private void loadData() {
        List<SubnetDto> subnets = client.getSubnets();
        tableView.setItems(FXCollections.observableArrayList(subnets));
    }
}
