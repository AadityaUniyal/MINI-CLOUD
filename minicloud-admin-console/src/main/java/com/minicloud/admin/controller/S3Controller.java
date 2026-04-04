package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import com.minicloud.common.dto.BucketDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class S3Controller {
    @FXML private TableView<BucketDto> tableView;
    @FXML private TableColumn<BucketDto, Long> idColumn;
    @FXML private TableColumn<BucketDto, String> nameColumn;
    @FXML private TableColumn<BucketDto, String> regionColumn;
    @FXML private Button viewObjectsButton;
    @FXML private Button deleteBucketButton;

    private final MiniCloudClient client;
    private final ObservableList<BucketDto> buckets = FXCollections.observableArrayList();

    public S3Controller(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));

        tableView.setItems(buckets);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean selected = (newVal != null);
            viewObjectsButton.setDisable(!selected);
            deleteBucketButton.setDisable(!selected);
        });

        loadBuckets();
    }

    private void loadBuckets() {
        new Thread(() -> {
            try {
                List<BucketDto> list = client.getBuckets();
                javafx.application.Platform.runLater(() -> buckets.setAll(list));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleViewObjects() {
        BucketDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Viewing objects for bucket: " + selected.getName());
            // Logic to switch to BucketExplorerView.fxml
        }
    }

    @FXML
    private void handleDeleteBucket() {
        BucketDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, 
                "Are you sure you want to delete bucket " + selected.getName() + "?", 
                ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(type -> {
                if (type == ButtonType.YES) {
                    System.out.println("Deleting bucket: " + selected.getName());
                    // client.deleteBucket(selected.getName());
                    loadBuckets();
                }
            });
        }
    }
}
