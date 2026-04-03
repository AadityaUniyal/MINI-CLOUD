package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.DynamoTableDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DynamoController {
    @FXML private TableView<DynamoTableDto> tableView;
    @FXML private TableColumn<DynamoTableDto, String> nameColumn;
    @FXML private TableColumn<DynamoTableDto, String> keyColumn;
    @FXML private TableColumn<DynamoTableDto, Long> countColumn;
    @FXML private TableColumn<DynamoTableDto, String> statusColumn;

    @FXML private Button viewItemsButton;
    @FXML private Button deleteTableButton;

    private final MiniCloudClient client;

    public DynamoController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tableName"));
        keyColumn.setCellValueFactory(new PropertyValueFactory<>("partitionKey"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));
        
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(column -> new TableCell<DynamoTableDto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item != null ? item.toUpperCase() : "");
                    getStyleClass().add("status-pill");
                    getStyleClass().removeAll("status-running", "status-stopped", "status-pending");
                    if ("active".equalsIgnoreCase(item)) {
                        getStyleClass().add("status-running");
                    } else {
                        getStyleClass().add("status-pending");
                    }
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            viewItemsButton.setDisable(!selected);
            deleteTableButton.setDisable(!selected);
        });

        loadData();
    }

    private void loadData() {
        List<DynamoTableDto> tables = client.getDynamoTables();
        tableView.setItems(FXCollections.observableArrayList(tables));
    }
}
