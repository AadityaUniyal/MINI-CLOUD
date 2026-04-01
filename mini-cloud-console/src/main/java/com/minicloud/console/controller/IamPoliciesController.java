package com.minicloud.console.controller;

import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.IamPolicyDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IamPoliciesController {
    @FXML private TableView<IamPolicyDto> tableView;
    @FXML private TableColumn<IamPolicyDto, String> nameColumn;
    @FXML private TableColumn<IamPolicyDto, String> arnColumn;
    @FXML private TableColumn<IamPolicyDto, String> descColumn;

    private final MiniCloudClient client;

    public IamPoliciesController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        arnColumn.setCellValueFactory(new PropertyValueFactory<>("arn"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadData();
    }

    private void loadData() {
        List<IamPolicyDto> policies = client.getIamPolicies();
        tableView.setItems(FXCollections.observableArrayList(policies));
    }
}
