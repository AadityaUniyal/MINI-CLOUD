package com.minicloud.admin.controller;


import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.DynamoItemDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DynamoExplorerController {
    @FXML private Label tableNameLabel;
    @FXML private TableView<DynamoItemDto> tableView;
    @FXML private TableColumn<DynamoItemDto, String> keyColumn;
    @FXML private TableColumn<DynamoItemDto, String> valueColumn;

    private final MiniCloudClient client;
    private final MainController mainController;
    private String currentTable;

    public DynamoExplorerController(MiniCloudClient client, MainController mainController) {
        this.client = client;
        this.mainController = mainController;
    }

    public void setTable(String tableName) {
        this.currentTable = tableName;
        tableNameLabel.setText("Table: " + tableName);
        loadData();
    }

    @FXML
    public void initialize() {
        keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    @FXML
    private void handleBack() {
        mainController.showDynamo();
    }

    private void loadData() {
        if (currentTable != null) {
            List<DynamoItemDto> items = client.getDynamoItems(currentTable);
            tableView.setItems(FXCollections.observableArrayList(items));
        }
    }
}
