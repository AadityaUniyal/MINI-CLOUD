package com.minicloud.console.controller;

import com.minicloud.console.MainController;
import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.FileDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BucketExplorerController {
    @FXML private Label bucketNameLabel;
    @FXML private TableView<FileDto> tableView;
    @FXML private TableColumn<FileDto, String> nameColumn;
    @FXML private TableColumn<FileDto, String> sizeColumn;
    @FXML private TableColumn<FileDto, String> dateColumn;

    @FXML private Button downloadButton;
    @FXML private Button deleteButton;

    private final MiniCloudClient client;
    private final MainController mainController;
    private String currentBucket;

    public BucketExplorerController(MiniCloudClient client, MainController mainController) {
        this.client = client;
        this.mainController = mainController;
    }

    public void setBucket(String bucketName) {
        this.currentBucket = bucketName;
        bucketNameLabel.setText("Bucket: " + bucketName);
        loadData();
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("lastModified"));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            downloadButton.setDisable(!selected);
            deleteButton.setDisable(!selected);
        });
    }

    @FXML
    private void handleBack() {
        mainController.showS3();
    }

    @FXML
    private void handleUpload() {
        if (currentBucket != null) {
            client.uploadFile(currentBucket, "new-file-" + System.currentTimeMillis() + ".txt");
            loadData();
        }
    }

    @FXML
    private void handleDownload() {
        FileDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Downloading: " + selected.getName());
        }
    }

    @FXML
    private void handleDelete() {
        FileDto selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null && currentBucket != null) {
            client.deleteFile(currentBucket, selected.getName());
            loadData();
        }
    }

    private void loadData() {
        if (currentBucket != null) {
            List<FileDto> files = client.getFiles(currentBucket);
            tableView.setItems(FXCollections.observableArrayList(files));
        }
    }
}
