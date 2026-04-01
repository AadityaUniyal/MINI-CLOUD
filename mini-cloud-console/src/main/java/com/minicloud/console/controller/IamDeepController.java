package com.minicloud.console.controller;

import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IamDeepController {
    @FXML private TableView<IamDto> userTable;
    @FXML private TableColumn<IamDto, Long> userIdColumn;
    @FXML private TableColumn<IamDto, String> userNameColumn;
    @FXML private TableColumn<IamDto, String> userRoleColumn;

    @FXML private TableView<IamGroupDto> groupTable;
    @FXML private TableColumn<IamGroupDto, String> groupNameColumn;
    @FXML private TableColumn<IamGroupDto, Integer> groupUserCountColumn;

    @FXML private TableView<IamRoleDto> roleTable;
    @FXML private TableColumn<IamRoleDto, String> roleNameColumn;
    @FXML private TableColumn<IamRoleDto, String> roleArnColumn;
    @FXML private TableColumn<IamRoleDto, String> roleDateColumn;

    @FXML private TableView<IamPolicyDto> policyTable;
    @FXML private TableColumn<IamPolicyDto, String> policyNameColumn;
    @FXML private TableColumn<IamPolicyDto, String> policyArnColumn;
    @FXML private TableColumn<IamPolicyDto, String> policyDescColumn;

    private final MiniCloudClient client;

    public IamDeepController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        // Users
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Groups
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        groupUserCountColumn.setCellValueFactory(new PropertyValueFactory<>("userCount"));

        // Roles
        roleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleArnColumn.setCellValueFactory(new PropertyValueFactory<>("arn"));
        roleDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        // Policies
        policyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        policyArnColumn.setCellValueFactory(new PropertyValueFactory<>("arn"));
        policyDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadData();
    }

    private void loadData() {
        userTable.setItems(FXCollections.observableArrayList(client.getIamUsers()));
        groupTable.setItems(FXCollections.observableArrayList(client.getIamGroups()));
        roleTable.setItems(FXCollections.observableArrayList(client.getIamRoles()));
        policyTable.setItems(FXCollections.observableArrayList(client.getIamPolicies()));
    }
}
