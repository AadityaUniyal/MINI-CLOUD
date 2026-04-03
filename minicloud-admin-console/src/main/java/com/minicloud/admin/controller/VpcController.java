package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.VpcDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VpcController {
    @FXML private TableView<VpcDto> tableView;
    @FXML private TableColumn<VpcDto, String> idColumn;
    @FXML private TableColumn<VpcDto, String> nameColumn;
    @FXML private TableColumn<VpcDto, String> cidrColumn;
    @FXML private TableColumn<VpcDto, String> stateColumn;

    private final MiniCloudClient client;

    public VpcController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("vpcId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cidrColumn.setCellValueFactory(new PropertyValueFactory<>("cidrBlock"));
        
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        stateColumn.setCellFactory(column -> new TableCell<VpcDto, String>() {
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
                    
                    if (item.equalsIgnoreCase("available")) {
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
        List<VpcDto> vpcs = client.getVpcs();
        tableView.setItems(FXCollections.observableArrayList(vpcs));
    }
}
