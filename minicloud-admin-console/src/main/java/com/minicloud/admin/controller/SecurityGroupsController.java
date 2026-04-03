package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.SecurityGroupDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityGroupsController {
    @FXML private TableView<SecurityGroupDto> tableView;
    @FXML private TableColumn<SecurityGroupDto, Long> idColumn;
    @FXML private TableColumn<SecurityGroupDto, String> nameColumn;
    @FXML private TableColumn<SecurityGroupDto, String> descColumn;
    @FXML private TableColumn<SecurityGroupDto, String> vpcColumn;
    @FXML private Button deleteButton;

    private final MiniCloudClient client;

    public SecurityGroupsController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        vpcColumn.setCellValueFactory(new PropertyValueFactory<>("vpcId"));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            deleteButton.setDisable(newSel == null);
        });

        loadData();
    }

    @FXML
    private void handleDelete() {
        SecurityGroupDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            client.deleteSecurityGroup(selected.getId());
            loadData();
        }
    }

    private void loadData() {
        List<SecurityGroupDto> groups = client.getSecurityGroups();
        tableView.setItems(FXCollections.observableArrayList(groups));
    }
}
