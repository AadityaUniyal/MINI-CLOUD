package com.minicloud.console.controller;

import com.minicloud.console.MainController;
import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.LambdaDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LambdaController {
    @FXML private TableView<LambdaDto> tableView;
    @FXML private TableColumn<LambdaDto, Long> idColumn;
    @FXML private TableColumn<LambdaDto, String> nameColumn;
    @FXML private TableColumn<LambdaDto, String> runtimeColumn;
    @FXML private TableColumn<LambdaDto, String> statusColumn;

    @FXML private Button invokeButton;
    @FXML private Button deleteButton;

    private final MiniCloudClient client;
    private final MainController mainController;

    public LambdaController(MiniCloudClient client, MainController mainController) {
        this.client = client;
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        runtimeColumn.setCellValueFactory(new PropertyValueFactory<>("runtime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean selected = newVal != null;
            invokeButton.setDisable(!selected);
            deleteButton.setDisable(!selected);
        });

        loadData();
    }

    @FXML
    private void handleInvoke() {
        LambdaDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            mainController.showInvokeLambda(selected.getName());
        }
    }

    @FXML
    private void handleDelete() {
        LambdaDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Delete lambda: " + selected.getName());
        }
    }

    private void loadData() {
        List<LambdaDto> functions = client.getLambdas();
        tableView.setItems(FXCollections.observableArrayList(functions));
    }
}
