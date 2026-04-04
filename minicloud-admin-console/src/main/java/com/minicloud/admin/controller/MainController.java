package com.minicloud.admin.controller;

import com.minicloud.admin.client.MiniCloudClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainController {
    @FXML private StackPane contentPane;
    @FXML private javafx.scene.control.TextField searchField;
    @FXML private javafx.scene.control.ChoiceBox<String> regionSwitcher;

    private final ApplicationContext springContext;
    private final MiniCloudClient client;
    private String currentFxml;

    public MainController(ApplicationContext springContext, MiniCloudClient client) {
        this.springContext = springContext;
        this.client = client;
    }

    @FXML
    public void initialize() {
        if (regionSwitcher != null) {
            regionSwitcher.setItems(javafx.collections.FXCollections.observableArrayList(
                "us-east-1 (N. Virginia)", "us-west-2 (Oregon)", "eu-central-1 (Frankfurt)", "ap-south-1 (Mumbai)"
            ));
            regionSwitcher.setValue("us-east-1 (N. Virginia)");
            regionSwitcher.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                System.out.println("Region changed to: " + newVal);
            });
        }
        showDashboard();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) return;

        if (query.contains("ec2") || query.contains("instance")) showEc2();
        else if (query.contains("s3") || query.contains("bucket")) showS3();
        else if (query.contains("rds") || query.contains("db") || query.contains("database")) showRds();
        else if (query.contains("lambda") || query.contains("func")) showLambda();
        else if (query.contains("iam") || query.contains("user") || query.contains("role")) showIam();
        else if (query.contains("vpc") || query.contains("network")) showVpc();
        else if (query.contains("dynamo") || query.contains("nosql")) showDynamo();
        else if (query.contains("log") || query.contains("monitor") || query.contains("cloudwatch")) showMonitoring();
        else if (query.contains("trail") || query.contains("event")) showCloudTrail();
        else if (query.contains("sqs") || query.contains("sns") || query.contains("message")) showMessaging();
        else if (query.contains("bill") || query.contains("cost")) showBilling();
        else if (query.contains("sec") || query.contains("firewall")) showSecurityGroups();
        else if (query.contains("elb") || query.contains("load")) showLoadBalancers();
        else if (query.contains("subnet")) showSubnets();
        else showDashboard();

        searchField.clear();
    }

    @FXML
    public void showDashboard() {
        loadView("/fxml/DashboardView.fxml");
    }

    @FXML
    public void showEc2() {
        loadView("/fxml/Ec2View.fxml");
    }

    @FXML
    public void showS3() {
        loadView("/fxml/S3View.fxml");
    }

    public void showBucketExplorer(String bucketName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BucketExplorerView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Node view = loader.load();
            
            BucketExplorerController controller = loader.getController();
            controller.setBucket(bucketName);
            
            // Animation
            view.setOpacity(0);
            contentPane.getChildren().setAll(view);
            
            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), view);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showRds() {
        loadView("/fxml/RdsView.fxml");
    }

    @FXML
    public void showVpc() {
        loadView("/fxml/VpcView.fxml");
    }

    @FXML
    public void showIamPolicies() {
        loadView("/fxml/IamDeepView.fxml");
    }

    @FXML
    public void showLambda() {
        loadView("/fxml/LambdaView.fxml");
    }

    @FXML
    public void showMonitoring() {
        loadView("/fxml/MonitoringView.fxml");
    }

    @FXML
    public void showDynamo() {
        loadView("/fxml/DynamoView.fxml");
    }

    @FXML
    public void showSecurityGroups() {
        loadView("/fxml/SecurityGroupsView.fxml");
    }

    @FXML
    public void showBilling() {
        loadView("/fxml/BillingView.fxml");
    }

    @FXML
    public void showLoadBalancers() {
        loadView("/fxml/LoadBalancerView.fxml");
    }

    @FXML
    public void showSubnets() {
        loadView("/fxml/SubnetView.fxml");
    }

    @FXML
    public void showMessaging() {
        loadView("/fxml/MessagingView.fxml");
    }

    @FXML
    public void showCloudTrail() {
        loadView("/fxml/CloudTrailView.fxml");
    }

    @FXML
    public void showIam() {
        loadView("/fxml/IamDeepView.fxml");
    }

    @FXML
    public void onDashboardClick() { showDashboard(); }

    @FXML
    public void onInstancesClick() { showEc2(); }

    @FXML
    public void onStorageClick() { showS3(); }

    @FXML
    public void onDatabasesClick() { showRds(); }

    @FXML
    public void onIamClick() { showIam(); }

    @FXML
    public void onLogoutClick() {
        System.out.println("Logging out...");
        // Logic to return to login screen
    }

    public void showDynamoExplorer(String tableName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DynamoExplorerView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Node view = loader.load();
            
            DynamoExplorerController controller = loader.getController();
            controller.setTable(tableName);
            
            // Animation
            view.setOpacity(0);
            contentPane.getChildren().setAll(view);
            
            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), view);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showInvokeLambda(String functionName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InvokeLambdaView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Node view = loader.load();
            
            InvokeLambdaController controller = loader.getController();
            controller.setFunction(functionName);
            
            // Animation
            view.setOpacity(0);
            contentPane.getChildren().setAll(view);
            
            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), view);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showLaunchInstance() {
        loadView("/fxml/LaunchInstanceView.fxml");
    }

    @FXML
    private void refreshCurrentView() {
        if (currentFxml != null) {
            loadView(currentFxml);
        }
    }

    private void loadView(String fxmlPath) {
        this.currentFxml = fxmlPath;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Node view = loader.load();
            
            // Animation
            view.setOpacity(0);
            contentPane.getChildren().setAll(view);
            
            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), view);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
