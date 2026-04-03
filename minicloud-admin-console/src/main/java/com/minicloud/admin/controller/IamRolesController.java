package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.IamRoleDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IamRolesController {
    @FXML private TableView<IamRoleDto> tableView;
    @FXML private TableColumn<IamRoleDto, String> nameColumn;
    @FXML private TableColumn<IamRoleDto, String> arnColumn;
    @FXML private TableColumn<IamRoleDto, String> dateColumn;

    private final MiniCloudClient client;

    public IamRolesController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        arnColumn.setCellValueFactory(new PropertyValueFactory<>("arn"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));

        loadData();
    }

    private void loadData() {
        List<IamRoleDto> roles = client.getIamRoles();
        tableView.setItems(FXCollections.observableArrayList(roles));
    }
}
