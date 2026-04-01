package com.minicloud.console.controller;

import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.BucketDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
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
    private final com.minicloud.console.MainController mainController;

    public S3Controller(MiniCloudClient client, com.minicloud.console.MainController mainController) {
        this.client = client;
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean selected = newVal != null;
            viewObjectsButton.setDisable(!selected);
            deleteBucketButton.setDisable(!selected);
        });

        loadData();
    }

    @FXML
    private void handleViewObjects() {
        BucketDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            mainController.showBucketExplorer(selected.getName());
        }
    }

    @FXML
    private void handleDeleteBucket() {
        BucketDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // client.deleteBucket(selected.getName());
            System.out.println("Delete bucket: " + selected.getName());
        }
    }

    private void loadData() {
        List<BucketDto> buckets = client.getBuckets();
        tableView.setItems(FXCollections.observableArrayList(buckets));
    }
}
