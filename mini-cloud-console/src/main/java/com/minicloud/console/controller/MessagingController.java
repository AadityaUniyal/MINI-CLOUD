package com.minicloud.console.controller;

import com.minicloud.console.client.MiniCloudClient;
import com.minicloud.console.dto.QueueDto;
import com.minicloud.console.dto.TopicDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessagingController {
    @FXML private TableView<QueueDto> queueTable;
    @FXML private TableColumn<QueueDto, String> qNameColumn;
    @FXML private TableColumn<QueueDto, Integer> qMsgColumn;
    @FXML private TextArea sqsTextArea;
    @FXML private Button sqsSendButton;

    @FXML private TableView<TopicDto> topicTable;
    @FXML private TableColumn<TopicDto, String> tNameColumn;
    @FXML private TableColumn<TopicDto, Integer> tSubColumn;
    @FXML private TextArea snsTextArea;
    @FXML private Button snsPublishButton;

    private final MiniCloudClient client;

    public MessagingController(MiniCloudClient client) {
        this.client = client;
    }

    @FXML
    public void initialize() {
        qNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        qMsgColumn.setCellValueFactory(new PropertyValueFactory<>("messagesAvailable"));

        tNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tSubColumn.setCellValueFactory(new PropertyValueFactory<>("subscriptions"));

        queueTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            sqsSendButton.setDisable(newSel == null);
        });

        topicTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            snsPublishButton.setDisable(newSel == null);
        });

        loadData();
    }

    @FXML
    private void handleSqsSend() {
        QueueDto selected = queueTable.getSelectionModel().getSelectedItem();
        if (selected != null && !sqsTextArea.getText().isEmpty()) {
            client.sendMessage(selected.getName(), sqsTextArea.getText());
            sqsTextArea.clear();
            loadData();
        }
    }

    @FXML
    private void handleSnsPublish() {
        TopicDto selected = topicTable.getSelectionModel().getSelectedItem();
        if (selected != null && !snsTextArea.getText().isEmpty()) {
            client.publishMessage(selected.getName(), snsTextArea.getText());
            snsTextArea.clear();
        }
    }

    private void loadData() {
        List<QueueDto> queues = client.getQueues();
        queueTable.setItems(FXCollections.observableArrayList(queues));

        List<TopicDto> topics = client.getTopics();
        topicTable.setItems(FXCollections.observableArrayList(topics));
    }
}
