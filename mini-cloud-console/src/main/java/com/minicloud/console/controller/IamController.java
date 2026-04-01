package com.minicloud.console.controller;

import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.IamDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IamController {
    @FXML private TableView<IamDto> tableView;
    @FXML private TableColumn<IamDto, Long> idColumn;
    @FXML private TableColumn<IamDto, String> usernameColumn;
    @FXML private TableColumn<IamDto, String> roleColumn;

    private final MiniCloudClient client;

    public IamController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        loadData();
    }

    private void loadData() {
        List<IamDto> users = client.getIamUsers();
        tableView.setItems(FXCollections.observableArrayList(users));
    }
}
