package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.DatabaseDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RdsController {
    @FXML private TableView<DatabaseDto> tableView;
    @FXML private TableColumn<DatabaseDto, Long> idColumn;
    @FXML private TableColumn<DatabaseDto, String> nameColumn;
    @FXML private TableColumn<DatabaseDto, String> engineColumn;
    @FXML private TableColumn<DatabaseDto, String> statusColumn;
    @FXML private Button stopButton;

    private final MiniCloudClient client;

    public RdsController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        engineColumn.setCellValueFactory(new PropertyValueFactory<>("engine"));
        
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(column -> new TableCell<DatabaseDto, String>() {
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
                    } else if (item.equalsIgnoreCase("stopped") || item.equalsIgnoreCase("deleted")) {
                        getStyleClass().add("status-stopped");
                    } else {
                        getStyleClass().add("status-pending");
                    }
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                stopButton.setDisable(!newSelection.getStatus().equalsIgnoreCase("available"));
            } else {
                stopButton.setDisable(true);
            }
        });

        loadData();
    }

    @FXML
    private void handleStop() {
        DatabaseDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            client.stopDatabase(selected.getId());
            loadData();
        }
    }

    private void loadData() {
        List<DatabaseDto> databases = client.getDatabases();
        tableView.setItems(FXCollections.observableArrayList(databases));
    }
}
