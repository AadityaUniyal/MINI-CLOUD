package com.minicloud.console.controller;

import com.minicloud.console.MainController;
import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.DynamoTableDto;
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
    private final MainController mainController;

    public DynamoController(MiniCloudClient client, MainController mainController) {
        this.client = client;
        this.mainController = mainController;
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
                    setText(item.toUpperCase());
                    getStyleClass().add("status-pill");
                    getStyleClass().removeAll("status-running", "status-stopped", "status-pending");
                    if (item.equalsIgnoreCase("active")) {
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

    @FXML
    private void handleExplore() {
        DynamoTableDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            mainController.showDynamoExplorer(selected.getTableName());
        }
    }

    private void loadData() {
        List<DynamoTableDto> tables = client.getDynamoTables();
        tableView.setItems(FXCollections.observableArrayList(tables));
    }
}
