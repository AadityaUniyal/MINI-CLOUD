package com.minicloud.admin;

import com.minicloud.admin.MiniCloudApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import java.lang.reflect.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.minicloud.common.dto.DashboardMetricsDTO;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import com.minicloud.common.dto.BackupRecordDto;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import java.util.List;

public class MiniCloudFxApp extends Application {

    private ConfigurableApplicationContext springContext;
    private Stage primaryStage;
    private String jwtToken;
    private String username;
    private String accountId;
    private String currentTab = "Compute";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_BASE = "http://localhost:8080/api/v1";
    private VBox detailsPane;
    private javafx.animation.Timeline autoRefreshTimeline;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private DashboardMetricsDTO currentMetrics = new DashboardMetricsDTO();
    private javafx.animation.Timeline globalTelemetryTimeline;
    
    private String getTelemetry(String key, String fallback) {
        try {
            Field field = DashboardMetricsDTO.class.getDeclaredField(key);
            field.setAccessible(true);
            Object val = field.get(currentMetrics);
            return val != null ? val.toString() : fallback;
        } catch (Exception e) {
            return fallback;
        }
    }
    
    private void fetchTelemetry(Runnable onDone) {
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/monitoring/dashboard"))
            .header("Authorization", "Bearer " + jwtToken)
            .GET().build();
        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
            try {
                if(res.statusCode() == 200) {
                    currentMetrics = objectMapper.readValue(res.body(), DashboardMetricsDTO.class);
                    if (onDone != null) Platform.runLater(onDone);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.springContext = new SpringApplicationBuilder()
                .sources(MiniCloudApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScene();
        primaryStage.setTitle("Mini-Cloud Management Console");
        primaryStage.show();
    }

    private void applyStyles(Scene scene) {
        String css = getClass().getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);
    }

    @Override
    public void stop() {
        if (springContext != null) springContext.close();
        Platform.exit();
    }

    private void showLoginScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #0f172a;");

        Label title = new Label("MiniCloud");
        title.setFont(Font.font("Outfit", FontWeight.BOLD, 48));
        title.setTextFill(Color.WHITE);

        VBox loginCard = new VBox(15);
        loginCard.setPadding(new Insets(25));
        loginCard.setStyle("-fx-background-color: white; -fx-background-radius: 5;");
        loginCard.setMaxWidth(400);

        Label signinLbl = new Label("Sign in");
        signinLbl.setFont(Font.font("Outfit", FontWeight.NORMAL, 24));

        ToggleGroup userTypeGroup = new ToggleGroup();
        RadioButton rootRadio = new RadioButton("Root user");
        rootRadio.setToggleGroup(userTypeGroup);
        rootRadio.setSelected(true);
        RadioButton iamRadio = new RadioButton("IAM user");
        iamRadio.setToggleGroup(userTypeGroup);

        Label mainInputLabel = new Label("Root user email address");
        TextField mainInputField = new TextField();
        mainInputField.setPromptText("root@example.com");

        VBox iamExtraArea = new VBox(10);
        iamExtraArea.setVisible(false);
        iamExtraArea.setManaged(false);
        Label iamUserLabel = new Label("IAM user name");
        TextField iamUserField = new TextField();

        iamExtraArea.getChildren().addAll(iamUserLabel, iamUserField);

        rootRadio.setOnAction(e -> {
            mainInputLabel.setText("Root user email address");
            mainInputField.setPromptText("root@example.com");
            iamExtraArea.setVisible(false);
            iamExtraArea.setManaged(false);
        });

        iamRadio.setOnAction(e -> {
            mainInputLabel.setText("Account ID (12-digit) or account alias");
            mainInputField.setPromptText("123456789012 or alias");
            iamExtraArea.setVisible(true);
            iamExtraArea.setManaged(true);
        });

        Label passLabel = new Label("Password");
        PasswordField passField = new PasswordField();

        Button nextBtn = new Button("Sign In");
        nextBtn.setMinWidth(350);
        nextBtn.setStyle("-fx-background-color: #ff9900; -fx-text-fill: black; -fx-font-weight: bold;");

        loginCard.getChildren().addAll(signinLbl, new Label("Select a type of user"), rootRadio, iamRadio, 
                                     mainInputLabel, mainInputField, iamExtraArea, passLabel, passField, nextBtn);

        Hyperlink regLink = new Hyperlink("Create a new MiniCloud account");
        regLink.setTextFill(Color.web("#0066cc"));
        regLink.setOnAction(e -> showRegisterScene());

        Label statusLabel = new Label();
        statusLabel.setTextFill(Color.RED);

        nextBtn.setOnAction(e -> {
            boolean isRoot = rootRadio.isSelected();
            String mainIn = mainInputField.getText();
            String iamIn = iamUserField.getText();
            String pass = passField.getText();
            
            authenticateAws(isRoot, mainIn, iamIn, pass).thenAccept(success -> {
                if (success) {
                    Platform.runLater(this::showDashboardScene);
                } else {
                    Platform.runLater(() -> statusLabel.setText("Authentication Failed. Check credentials."));
                }
            });
        });

        root.getChildren().addAll(title, loginCard, regLink, statusLabel);
        Scene scene = new Scene(root, 500, 700);
        applyStyles(scene);
        primaryStage.setScene(scene);
    }

    private void showRegisterScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f2f2f2;");

        Label title = new Label("Create a new MiniCloud account");
        title.setFont(Font.font("Outfit", FontWeight.BOLD, 24));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField emailField = new TextField();
        emailField.setPromptText("Email address");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        TextField aliasField = new TextField();
        aliasField.setPromptText("MiniCloud account alias");
        TextField nameField = new TextField();
        nameField.setPromptText("Full name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone number");
        TextField addressField = new TextField();
        addressField.setPromptText("Street Address");
        TextField countryField = new TextField();
        countryField.setPromptText("Country");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Personal", "Professional");
        typeBox.setValue("Personal");

        grid.add(new Label("Email:"), 0, 0); grid.add(emailField, 1, 0);
        grid.add(new Label("Password:"), 0, 1); grid.add(passField, 1, 1);
        grid.add(new Label("Alias:"), 0, 2); grid.add(aliasField, 1, 2);
        grid.add(new Label("Full Name:"), 0, 3); grid.add(nameField, 1, 3);
        grid.add(new Label("Phone:"), 0, 4); grid.add(phoneField, 1, 4);
        grid.add(new Label("Address:"), 0, 5); grid.add(addressField, 1, 5);
        grid.add(new Label("Country:"), 0, 6); grid.add(countryField, 1, 6);
        grid.add(new Label("Type:"), 0, 7); grid.add(typeBox, 1, 7);

        Button regBtn = new Button("Register");
        regBtn.setMinWidth(300);
        regBtn.setStyle("-fx-background-color: #ff9900; -fx-font-weight: bold;");

        Hyperlink loginLink = new Hyperlink("Sign in to an existing account");
        loginLink.setOnAction(e -> showLoginScene());

        regBtn.setOnAction(e -> {
            try {
                String json = new ObjectMapper().createObjectNode()
                        .put("email", emailField.getText())
                        .put("password", passField.getText())
                        .put("accountAlias", aliasField.getText())
                        .put("fullName", nameField.getText())
                        .put("phoneNumber", phoneField.getText())
                        .put("address", addressField.getText())
                        .put("country", countryField.getText())
                        .put("accountType", typeBox.getValue().toUpperCase())
                        .toString();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/auth/register-MiniCloud"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                    Platform.runLater(() -> {
                        if (res.statusCode() == 200) {
                            new Alert(Alert.AlertType.INFORMATION, res.body()).showAndWait();
                            showLoginScene();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Registration Failed: " + res.body()).show();
                        }
                    });
                });
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        root.getChildren().addAll(title, grid, regBtn, loginLink);
        Scene scene = new Scene(root, 600, 700);
        applyStyles(scene);
        primaryStage.setScene(scene);
    }

    private CompletableFuture<Boolean> authenticateAws(boolean isRoot, String main, String iam, String pass) {
        try {
            String json = new ObjectMapper().createObjectNode()
                    .put("rootUser", isRoot)
                    .put("email", isRoot ? main : null)
                    .put("accountId", !isRoot ? main : null)
                    .put("iamUsername", iam)
                    .put("password", pass)
                    .toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/auth/login-MiniCloud"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            String body = response.body();
                            if (body.contains("\"token\"")) {
                                this.jwtToken = getJsonValue(body, "token");
                                this.username = getJsonValue(body, "username");
                                this.accountId = getJsonValue(body, "accountId");
                                return true;
                            }
                        }
                        return false;
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    private void showDashboardScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // --- MiniCloud Header ---
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 20, 0, 20));
        header.getStyleClass().add("MiniCloud-header");
        header.setPrefHeight(50);

        Label statsLabel = new Label("Total Balance: $1,500.00"); // Standardized mock
        statsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Button servicesBtn = new Button("Services");
        servicesBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold;");
        Label servicesIcon = new Label("\u2630"); // Hamburger-like or grid icon
        servicesIcon.setTextFill(Color.WHITE);
        HBox servicesBox = new HBox(5, servicesIcon, servicesBtn);
        servicesBox.setAlignment(Pos.CENTER);
        servicesBox.setCursor(javafx.scene.Cursor.HAND);

        Label MiniCloudLogo = new Label("MiniCloud");
        MiniCloudLogo.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        MiniCloudLogo.setTextFill(Color.WHITE);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search services, features, blogs...");
        searchBar.setPrefWidth(450);
        searchBar.getStyleClass().add("MiniCloud-search-bar");

        Region spacerH = new Region();
        HBox.setHgrow(spacerH, Priority.ALWAYS);

        // Region Selector (Mock)
        ComboBox<String> regionSelector = new ComboBox<>();
        regionSelector.getItems().addAll("us-east-1 (N. Virginia)", "us-west-2 (Oregon)", "eu-west-1 (Ireland)", "ap-southeast-1 (Singapore)");
        regionSelector.setValue("us-east-1 (N. Virginia)");
        regionSelector.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        Label userLabel = new Label(username + " @ " + (accountId != null ? accountId : "Root"));
        userLabel.setTextFill(Color.WHITE);
        userLabel.setFont(Font.font(12));

        Button logoutBtn = new Button("Sign Out");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> showLoginScene());

        Label bellIcon = new Label("\uD83D\uDD14"); // Bell icon
        bellIcon.setTextFill(Color.WHITE);
        bellIcon.setCursor(javafx.scene.Cursor.HAND);
        Tooltip.install(bellIcon, new Tooltip("Notifications"));

        header.getChildren().addAll(MiniCloudLogo, servicesBox, searchBar, spacerH, bellIcon, regionSelector, userLabel, logoutBtn);
        root.setTop(header);

        // --- Sidebar (TreeView) ---
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(240);
        sidebar.getStyleClass().add("sidebar-area");
        
        TreeView<String> navTree = new TreeView<>();
        TreeItem<String> navRoot = new TreeItem<>("Console Home");
        
        TreeItem<String> home = new TreeItem<>("Console Home");

        TreeItem<String> ec2 = new TreeItem<>("\u2601 EC2");
        ec2.getChildren().addAll(new TreeItem<>("Instances"), new TreeItem<>("AMIs"), new TreeItem<>("Security Groups"));

        TreeItem<String> rds = new TreeItem<>("\u2615 RDS");
        rds.getChildren().addAll(new TreeItem<>("Databases"), new TreeItem<>("Snapshots"));

        TreeItem<String> dynamo = new TreeItem<>("\uD83D\uDDBE DynamoDB");
        dynamo.getChildren().addAll(new TreeItem<>("Tables"));

        TreeItem<String> s3 = new TreeItem<>("\u26E9 S3");
        s3.getChildren().addAll(new TreeItem<>("Buckets"));
        
        TreeItem<String> lambda = new TreeItem<>("\u26A1 Lambda");
        lambda.getChildren().addAll(new TreeItem<>("Functions"));

        TreeItem<String> sns = new TreeItem<>("\uD83D\uDCE2 SNS");
        sns.getChildren().addAll(new TreeItem<>("Topics"));

        TreeItem<String> sqs = new TreeItem<>("\u2709 SQS");
        sqs.getChildren().addAll(new TreeItem<>("Queues"));

        TreeItem<String> backup = new TreeItem<>("\uD83D\uDD04 Backup & Recovery");
        backup.getChildren().addAll(new TreeItem<>("Jobs"), new TreeItem<>("Vaults"));

        TreeItem<String> vpc = new TreeItem<>("\u26C5 VPC");
        vpc.getChildren().addAll(new TreeItem<>("Your VPCs"), new TreeItem<>("Subnets"), new TreeItem<>("VPC Security Groups"));

        TreeItem<String> route53 = new TreeItem<>("\uD83C\uDF10 Route 53");
        route53.getChildren().addAll(new TreeItem<>("Hosted Zones"));

        TreeItem<String> cf = new TreeItem<>("\uD83D\uDCDC CloudFormation");
        cf.getChildren().addAll(new TreeItem<>("Stacks"));

        TreeItem<String> security = new TreeItem<>("\uD83D\uDEE1 Security");
        security.getChildren().addAll(new TreeItem<>("GuardDuty Findings"), new TreeItem<>("WAF Rules"), new TreeItem<>("IAM Groups"));

        TreeItem<String> billing = new TreeItem<>("\uD83D\uDCB0 Billing");
        billing.getChildren().addAll(new TreeItem<>("Cost Explorer"));

        TreeItem<String> account = new TreeItem<>("\uD83D\uDCB3 My Account");

        navRoot.getChildren().addAll(home, ec2, rds, dynamo, s3, lambda, sns, sqs, backup, vpc, route53, cf, security, billing, account);
        navTree.setRoot(navRoot);
        navTree.setShowRoot(false);
        navTree.getStyleClass().add("MiniCloud-tree");
        
        sidebar.getChildren().add(navTree);
        root.setLeft(sidebar);

        // --- Content Area Wrapper ---
        VBox mainContentWrapper = new VBox(0);
        mainContentWrapper.getStyleClass().add("content-area");
        VBox.setVgrow(mainContentWrapper, Priority.ALWAYS);

        // Breadcrumbs
        Label breadcrumbs = new Label("Console Home");
        breadcrumbs.getStyleClass().add("breadcrumb-label");
        
        Label viewTitle = new Label("Console Home");
        viewTitle.getStyleClass().add("title-label");
        
        HBox titleArea = new HBox(breadcrumbs);
        titleArea.setPadding(new Insets(0, 0, 5, 0));

        VBox contentHeader = new VBox(5, titleArea, viewTitle);
        contentHeader.setPadding(new Insets(0, 0, 20, 0));

        HBox topActionArea = new HBox(10);
        topActionArea.setAlignment(Pos.CENTER_RIGHT);
        
        StackPane mainContainer = new StackPane();
        mainContainer.getStyleClass().add("content-pane");
        VBox.setVgrow(mainContainer, Priority.ALWAYS);

        navTree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String val = newVal.getValue();
                // Strip icons for logic
                String cleanVal = val.replaceAll("[^\\w\\s&]+", "").trim();
                this.currentTab = cleanVal;
                viewTitle.setText(cleanVal);
                breadcrumbs.setText("Services > " + (newVal.getParent() != null && newVal.getParent() != navRoot ? newVal.getParent().getValue().replaceAll("[^\\w\\s&]+", "").trim() + " > " : "") + cleanVal);
                updateMainView(cleanVal, topActionArea, mainContainer);
            }
        });

        // Master-Detail Pane (Collapsible)
        VBox detailsPane = new VBox(10);
        detailsPane.getStyleClass().add("MiniCloud-details-pane");
        detailsPane.setVisible(false);
        detailsPane.setManaged(false);
        this.detailsPane = detailsPane; // Store in field

        mainContentWrapper.getChildren().addAll(contentHeader, topActionArea, mainContainer, detailsPane);
        root.setCenter(mainContentWrapper);

        // Services Menu Popover
        servicesBox.setOnMouseClicked(e -> showServicesMenu(servicesBox));

        // Search Bar Logic (Global)
        searchBar.textProperty().addListener((obs, old, text) -> {
            filterSidebar(navRoot, text);
            if (text.length() > 2) {
                // Show floating search results or just jump to first match
                TreeItem<String> match = findInTree(navRoot, text);
                if (match != null) navTree.getSelectionModel().select(match);
            }
        });

        Scene scene = new Scene(root, 1200, 800);
        applyStyles(scene);
        primaryStage.setScene(scene);
        
        navTree.getSelectionModel().select(home);
        
        if (globalTelemetryTimeline != null) globalTelemetryTimeline.stop();
        globalTelemetryTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(15), e -> fetchTelemetry(null))
        );
        globalTelemetryTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        globalTelemetryTimeline.play();
        fetchTelemetry(null);
    }

    private void updateMainView(String tab, HBox actions, StackPane container) {
        actions.getChildren().clear();
        container.getChildren().clear();
        
        // Hide actions for Home
        if ("Console Home".equals(tab)) {
            setupConsoleHome(container);
            return;
        }

        Button refreshBtn = new Button("Refresh");
        styleActionBtn(refreshBtn, false);
        actions.getChildren().add(refreshBtn);

        switch (tab) {
            case "EC2":
            case "Instances": setupInstancesView(actions, container, refreshBtn); break;
            
            case "RDS":
            case "Databases": setupDatabasesView(actions, container, refreshBtn); break;
            
            case "DynamoDB":
            case "Tables": setupDynamoDbView(actions, container, refreshBtn); break;
            
            case "S3":
            case "Buckets": setupBucketsView(actions, container, refreshBtn); break;
            
            case "Lambda":
            case "Functions": setupLambdaView(actions, container, refreshBtn); break;
            
            case "SNS":
            case "Topics": setupSnsView(actions, container, refreshBtn); break;
            
            case "SQS":
            case "Queues": setupSqsView(actions, container, refreshBtn); break;
            
            case "VPC":
            case "Your VPCs": setupVpcView(actions, container, refreshBtn); break;
            case "Subnets": setupSubnetView(actions, container, refreshBtn); break;
            
            case "Route 53":
            case "Hosted Zones": setupRoute53View(actions, container, refreshBtn); break;
            
            case "CloudFormation":
            case "Stacks": setupCloudFormationView(actions, container, refreshBtn); break;
            
            case "WAF":
            case "WAF Rules": setupWafView(actions, container, refreshBtn); break;
            
            case "Security Hub":
            case "GuardDuty Findings": setupGuardDutyView(actions, container, refreshBtn); break;
            
            case "IAM":
            case "IAM Groups": setupIamGroupsView(actions, container, refreshBtn); break;
            
            case "Billing":
            case "Cost Explorer": setupBillingView(actions, container, refreshBtn); break;
            
            case "My Account": setupAccountView(container); break;
            
            case "Backup & Recovery":
            case "Jobs":
            case "Vaults": setupBackupView(container); break;
            
            case "Security Groups": 
            case "VPC Security Groups": setupSecurityGroupsView(actions, container, refreshBtn); break;
            
            default:
                Label placeholder = new Label("Coming Soon: " + tab);
                placeholder.setTextFill(Color.GRAY);
                container.getChildren().add(placeholder);
                break;
        }
    }

    private void setupConsoleHome(StackPane container) {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("widget-grid");
        grid.setPadding(new Insets(10));

        // Widget 1: Welcome
        VBox welcome = createWidget("Welcome to Mini-Cloud", "Getting started with your cloud resources.");
        Label ec2Count = new Label("EC2: Loading...");
        Label s3Count = new Label("S3: Loading...");
        Label rdsCount = new Label("RDS: Loading...");
        Label vpcCount = new Label("VPC: Loading...");
        welcome.getChildren().addAll(ec2Count, s3Count, rdsCount, vpcCount);

        HBox countsBox = new HBox(20);
        countsBox.setPadding(new Insets(10, 0, 0, 0));
        VBox v1 = new VBox(2, new Label("Instances"), ec2Count);
        VBox v2 = new VBox(2, new Label("Buckets"), s3Count);
        VBox v3 = new VBox(2, new Label("Databases"), rdsCount);
        VBox v4 = new VBox(2, new Label("Networks"), vpcCount);
        countsBox.getChildren().addAll(v1, v2, v3, v4);
        welcome.getChildren().add(countsBox);

        // Fetch counts
        fetchList("/compute/list", res -> Platform.runLater(() -> ec2Count.setText(String.valueOf(res.split("\\{").length-1))));
        fetchList("/buckets", res -> Platform.runLater(() -> s3Count.setText(String.valueOf(res.split("\\{").length-1))));
        fetchList("/database/instances", res -> Platform.runLater(() -> rdsCount.setText(String.valueOf(res.split("\\{").length-1))));
        fetchList("/firewall/vpcs", res -> Platform.runLater(() -> vpcCount.setText(String.valueOf(res.split("\\{").length-1))));

        // Widget 2: Security Health
        VBox securityStatus = createWidget("Security Health", "GuardDuty & WAF findings.");
        Label findingsLabel = new Label("Findings: Loading...");
        securityStatus.getChildren().add(findingsLabel);
        fetchList("/security/findings", res -> {
            int count = res.split("\\{").length - 1;
            findingsLabel.setText(count + " Security Findings detected");
            findingsLabel.setTextFill(count > 0 ? Color.RED : Color.GREEN);
        });

        // Widget 3: MiniCloud Health
        VBox health = createWidget("Service Health", "Your infrastructure status.");
        Label hLabel = new Label("Checking service status...");
        health.getChildren().add(hLabel);
        fetchList("/audit/logs", res -> {
            if (res.contains("AUTO_HEAL")) {
                hLabel.setText("\u26A0 Recent self-recovery events");
                hLabel.setTextFill(Color.ORANGE);
            } else {
                hLabel.setText("\u2705 All systems operational");
                hLabel.setTextFill(Color.GREEN);
            }
        });

        // Widget 4: Cost Optimization
        VBox billing = createWidget("Cost Explorer", "Month-to-date summary.");
        Label balanceLabel = new Label("Current Balance: $...");
        billing.getChildren().addAll(balanceLabel, new Hyperlink("View detailed breakdown"));
        fetchList("/auth/me", res -> balanceLabel.setText("Account Balance: $" + getJsonValue(res, "balance")));

        grid.add(welcome, 0, 0);
        grid.add(securityStatus, 1, 0);
        grid.add(health, 0, 1);
        grid.add(billing, 1, 1);

        // Widget 5: Host Performance (Live Graphs)
        VBox perfWidget = createWidget("Local Host Infrastructure", "Real-time CPU and RAM utilization");
        perfWidget.setMinHeight(250);
        
        NumberAxis xAxis = new NumberAxis();
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setOpacity(0);
        
        NumberAxis yAxis = new NumberAxis(0, 100, 10);
        yAxis.setLabel("Utilization %");
        
        AreaChart<Number, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
        chart.setLegendVisible(true);
        chart.setPrefHeight(200);
        
        XYChart.Series<Number, Number> cpuSeries = new XYChart.Series<>();
        cpuSeries.setName("Host CPU Load");
        XYChart.Series<Number, Number> ramSeries = new XYChart.Series<>();
        ramSeries.setName("Host RAM Usage");
        
        chart.getData().addAll(cpuSeries, ramSeries);
        perfWidget.getChildren().add(chart);
        
        grid.add(perfWidget, 0, 2, 2, 1); // Spanning two columns
        
        final long[] timeCount = {0};
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), ev -> {
                double cpuLoad = 0;
                double ramLoad = 0;
                try {
                    java.lang.management.OperatingSystemMXBean osBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
                    if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                        com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
                        double processCpu = sunOsBean.getCpuLoad();
                        cpuLoad = processCpu < 0 ? 0 : processCpu * 100;
                        long totalMem = sunOsBean.getTotalMemorySize();
                        long freeMem = sunOsBean.getFreeMemorySize();
                        ramLoad = ((double) (totalMem - freeMem) / totalMem) * 100;
                    } else {
                        double sysLoad = osBean.getSystemLoadAverage();
                        cpuLoad = sysLoad < 0 ? new java.util.Random().nextDouble() * 10 + 5 : sysLoad * 100;
                        Runtime rt = Runtime.getRuntime();
                        ramLoad = ((double) (rt.totalMemory() - rt.freeMemory()) / rt.totalMemory()) * 100;
                    }
                } catch (Exception ex) {
                    Runtime rt = Runtime.getRuntime();
                    ramLoad = ((double) (rt.totalMemory() - rt.freeMemory()) / rt.totalMemory()) * 100;
                    cpuLoad = new java.util.Random().nextDouble() * 10 + 5;
                }
                
                timeCount[0]++;
                cpuSeries.getData().add(new XYChart.Data<>(timeCount[0], cpuLoad));
                ramSeries.getData().add(new XYChart.Data<>(timeCount[0], ramLoad));
                
                xAxis.setLowerBound(timeCount[0] - 60);
                xAxis.setUpperBound(timeCount[0] + 5);
                
                if (cpuSeries.getData().size() > 65) cpuSeries.getData().remove(0);
                if (ramSeries.getData().size() > 65) ramSeries.getData().remove(0);
            })
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
        
        if (this.autoRefreshTimeline != null) {
            this.autoRefreshTimeline.stop();
        }
        this.autoRefreshTimeline = timeline;

        container.getChildren().add(grid);
    }

    private VBox createWidget(String title, String subtitle) {
        VBox card = new VBox(10);
        card.getStyleClass().add("MiniCloud-card");
        card.setMinWidth(400);
        card.setMinHeight(200);

        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("MiniCloud-card-title");
        
        Label subLbl = new Label(subtitle);
        subLbl.setTextFill(Color.GRAY);
        subLbl.setFont(Font.font(12));

        card.getChildren().addAll(titleLbl, subLbl, new Separator());
        return card;
    }

    private void setupInstancesView(HBox actions, StackPane container, Button refreshBtn) {
        Button launchBtn = new Button("Launch Instances");
        styleActionBtn(launchBtn, true);
        launchBtn.setOnAction(e -> handleLaunchEC2(null));
        actions.getChildren().add(0, launchBtn);

        TableView<InstanceRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<InstanceRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().name));
        
        TableColumn<InstanceRow, String> idCol = new TableColumn<>("Instance ID");
        idCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().id));
        
        TableColumn<InstanceRow, String> stateCol = new TableColumn<>("Instance state");
        stateCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().state));
        
        TableColumn<InstanceRow, String> typeCol = new TableColumn<>("Instance type");
        typeCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().type));

        TableColumn<InstanceRow, String> ipCol = new TableColumn<>("Public IPv4 Address");
        ipCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().publicIp));

        table.getColumns().addAll(nameCol, idCol, stateCol, typeCol, ipCol);
        table.setRowFactory(tv -> {
            TableRow<InstanceRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    InstanceRow data = row.getItem();
                    showStatsDialog(data.name, data.id);
                }
            });
            return row;
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                java.util.Map<String, String> info = new java.util.LinkedHashMap<>();
                info.put("Instance ID", val.id);
                info.put("Instance State", val.state);
                info.put("Instance Type", val.type);
                info.put("Private IP", val.publicIp);
                showDetails(val.name, info);
            }
        });
        
        GridPane metricsGrid = new GridPane();
        metricsGrid.setHgap(15);
        metricsGrid.setVgap(15);
        metricsGrid.setPadding(new Insets(0, 0, 15, 0));
        
        VBox t1 = createWidget("Resources", "Total instances: " + getTelemetry("totalInstances", "0") + "\nVolumes: " + getTelemetry("totalVolumes", "0"));
        VBox t2 = createWidget("Instance Health", "Running instances: 0");
        VBox t3 = createWidget("Network & Security", "Elastic IPs: " + getTelemetry("elasticIps", "1") + "\nSecurity Groups: " + getTelemetry("securityGroups", "1"));
        VBox t4 = createWidget("Key Pairs", "Active: " + getTelemetry("keyPairs", "1"));
        
        metricsGrid.addRow(0, t1, t2, t3, t4);
        VBox contentBox = new VBox(metricsGrid, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        container.getChildren().add(contentBox);

        Button terminateBtn = new Button("Terminate Instance");
        styleActionBtn(terminateBtn, false);
        terminateBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        
        Runnable refresh = () -> {
            fetchList("/compute/list", res -> {
                try {
                    JsonNode root = objectMapper.readTree(res);
                    table.getItems().clear();
                    if (root.isArray()) {
                        for (JsonNode item : root) {
                            table.getItems().add(new InstanceRow(
                                item.get("name") != null ? item.get("name").asText() : "N/A",
                                item.get("id") != null ? item.get("id").asText() : "N/A",
                                item.get("status") != null ? item.get("status").asText() : "N/A",
                                item.get("instanceType") != null ? item.get("instanceType").asText() : "N/A",
                                item.get("publicIp") != null ? item.get("publicIp").asText() : "N/A",
                                item.get("hostPort") != null ? item.get("hostPort").asText() : "N/A"
                            ));
                        }
                    }
                    Platform.runLater(() -> {
                        ((Label)t1.getChildren().get(1)).setText("Total instances: " + table.getItems().size() + "\nVolumes: " + getTelemetry("totalVolumes", "0"));
                        long running = table.getItems().stream().filter(r -> "RUNNING".equals(r.state)).count();
                        ((Label)t2.getChildren().get(1)).setText("Running instances: " + running);
                    });
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        };

        Button stopBtn = new Button("Stop");
        styleActionBtn(stopBtn, false);
        stopBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        stopBtn.setOnAction(e -> handleLifecycleEC2(table.getSelectionModel().getSelectedItem(), "stop", refresh));

        Button startBtn = new Button("Start");
        styleActionBtn(startBtn, false);
        startBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        startBtn.setOnAction(e -> handleLifecycleEC2(table.getSelectionModel().getSelectedItem(), "start", refresh));
        
        terminateBtn.setOnAction(e -> handleTerminateEC2(table.getSelectionModel().getSelectedItem(), refresh));
        actions.getChildren().add(1, stopBtn);
        actions.getChildren().add(2, startBtn);
        actions.getChildren().add(3, terminateBtn);

        refreshBtn.setOnAction(e -> refresh.run());
        styleActionBtn(refreshBtn, false);
        refresh.run();
        addAutoRefreshToggle(actions, refresh);
    }

    private TreeItem<String> findInTree(TreeItem<String> root, String text) {
        for (TreeItem<String> child : root.getChildren()) {
            if (child.getValue().toLowerCase().contains(text.toLowerCase())) return child;
            TreeItem<String> res = findInTree(child, text);
            if (res != null) return res;
        }
        return null;
    }



    private void setupDatabasesView(HBox actions, StackPane container, Button refreshBtn) {
        Button createBtn = new Button("Create database");
        styleActionBtn(createBtn, true);
        createBtn.setOnAction(e -> handleCreateRDS(null));
        actions.getChildren().add(0, createBtn);

        TableView<DatabaseRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<DatabaseRow, String> idCol = new TableColumn<>("DB identifier");
        idCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().id));
        
        TableColumn<DatabaseRow, String> engineCol = new TableColumn<>("Engine");
        engineCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().engine));
        
        TableColumn<DatabaseRow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().status));
        
        TableColumn<DatabaseRow, String> classCol = new TableColumn<>("Size");
        classCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().dbClass));

        table.getColumns().addAll(idCol, engineCol, statusCol, classCol);
        table.setRowFactory(tv -> {
            TableRow<DatabaseRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    DatabaseRow data = row.getItem();
                    showStatsDialog(data.id, data.id); // Using ID for both for DB
                }
            });
            return row;
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                java.util.Map<String, String> info = new java.util.LinkedHashMap<>();
                info.put("DB Identifier", val.id);
                info.put("Engine", val.engine);
                info.put("Status", val.status);
                info.put("Size", val.dbClass);
                showDetails(val.id, info);
            }
        });
        
        GridPane dbMetricsGrid = new GridPane();
        dbMetricsGrid.setHgap(15);
        dbMetricsGrid.setVgap(15);
        dbMetricsGrid.setPadding(new Insets(0, 0, 15, 0));
        
        VBox dt1 = createWidget("DB Instances", "Active DBs: 0");
        VBox dt2 = createWidget("Storage", "Allocated (GB): " + getTelemetry("usedStorageTb", "20"));
        VBox dt3 = createWidget("Snapshots", "Automated: " + getTelemetry("automatedBackups", "5"));
        VBox dt4 = createWidget("Engines", "MySQL / PostgreSQL");
        
        dbMetricsGrid.addRow(0, dt1, dt2, dt3, dt4);
        VBox dbContentBox = new VBox(dbMetricsGrid, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        container.getChildren().add(dbContentBox);

        Button deleteBtn = new Button("Delete Database");
        styleActionBtn(deleteBtn, false);
        deleteBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        Runnable refresh = () -> {
            fetchList("/database/instances", res -> {
                try {
                    JsonNode root = objectMapper.readTree(res);
                    table.getItems().clear();
                    if (root.isArray()) {
                        for (JsonNode item : root) {
                            table.getItems().add(new DatabaseRow(
                                item.get("id") != null ? item.get("id").asText() : "N/A",
                                item.get("engine") != null ? item.get("engine").asText() : "N/A",
                                item.get("status") != null ? item.get("status").asText() : "N/A",
                                item.get("dbInstanceClass") != null ? item.get("dbInstanceClass").asText() : "N/A"
                            ));
                        }
                    }
                    Platform.runLater(() -> {
                        ((Label)dt1.getChildren().get(1)).setText("Active DBs: " + table.getItems().size());
                    });
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        };
        
        deleteBtn.setOnAction(e -> handleDeleteRDS(table.getSelectionModel().getSelectedItem(), refresh));
        actions.getChildren().add(1, deleteBtn);

        refreshBtn.setOnAction(e -> refresh.run());
        styleActionBtn(refreshBtn, false);
        refresh.run();
        addAutoRefreshToggle(actions, refresh);
    }

    private void setupDynamoDbView(HBox actions, StackPane container, Button refreshBtn) {
        Button createBtn = new Button("Create table");
        styleActionBtn(createBtn, true);
        createBtn.setOnAction(e -> handleCreateDynamo());
        actions.getChildren().add(0, createBtn);

        TableView<DynamoTableRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        table.getColumns().addAll(
            createCol("Table name", "name"),
            createCol("Status", "status"),
            createCol("Partition key", "partitionKey"),
            createCol("Sort key", "sortKey")
        );

        table.setRowFactory(tv -> {
            TableRow<DynamoTableRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    DynamoTableRow data = row.getItem();
                    showDynamoDetails(data.name);
                }
            });
            return row;
        });

        GridPane dGrid = new GridPane();
        dGrid.setHgap(15);
        dGrid.setVgap(15);
        dGrid.setPadding(new Insets(0, 0, 15, 0));
        
        VBox t1 = createWidget("Tables", "Active tables: 0");
        VBox t2 = createWidget("Capacity totals", "Read capacity: 25\nWrite capacity: 25");
        VBox t3 = createWidget("Global secondary indexes", "Active: 0");
        VBox t4 = createWidget("DAX Clusters", "Running: 0");
        
        dGrid.addRow(0, t1, t2, t3, t4);
        VBox contentBox = new VBox(dGrid, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        container.getChildren().add(contentBox);

        Runnable refresh = () -> {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(API_BASE + "/dynamodb/tables"))
                .header("Authorization", "Bearer " + jwtToken).GET().build();
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                try {
                    JsonNode root = objectMapper.readTree(res.body());
                    Platform.runLater(() -> {
                        table.getItems().clear();
                        if (root.isArray()) {
                            for (JsonNode item : root) {
                                table.getItems().add(new DynamoTableRow(
                                    item.get("tableName") != null ? item.get("tableName").asText() : "N/A",
                                    item.get("status") != null ? item.get("status").asText() : "N/A",
                                    item.get("partitionKey") != null ? item.get("partitionKey").asText() : "N/A",
                                    item.get("sortKey") != null ? item.get("sortKey").asText() : "N/A"
                                ));
                            }
                        }
                        ((Label)t1.getChildren().get(1)).setText("Active tables: " + table.getItems().size());
                    });
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        };

        refreshBtn.setOnAction(e -> refresh.run());
        styleActionBtn(refreshBtn, false);
        refresh.run();
        addAutoRefreshToggle(actions, refresh);
    }
    
    private void handleCreateDynamo() {
        VBox vb = new VBox(15);
        vb.setPadding(new Insets(20));
        vb.setMinWidth(450);

        TextField nameFd = new TextField("Animals");
        TextField pkFd = new TextField("Species");
        TextField skFd = new TextField("");
        skFd.setPromptText("Optional");

        vb.getChildren().addAll(
            new Label("Table details\nTable name"), nameFd,
            new Label("Partition key"), pkFd,
            new Label("Sort key (Optional)"), skFd
        );

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create DynamoDB table");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(vb);

        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    String body = String.format("{\"tableName\":\"%s\", \"partitionKey\":\"%s\", \"sortKey\":\"%s\"}", 
                        nameFd.getText(), pkFd.getText(), skFd.getText());
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(API_BASE + "/dynamodb/table"))
                            .header("Authorization", "Bearer " + jwtToken)
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(body))
                            .build();
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
                } catch(Exception e) {}
            }
        });
    }

    private void showDynamoDetails(String tableName) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("DynamoDB Table: " + tableName);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setMinWidth(500);
        
        HBox queryArea = new HBox(10);
        TextField searchFd = new TextField();
        searchFd.setPromptText("Enter Partition Key value");
        Button getBtn = new Button("Query / Get");
        styleActionBtn(getBtn, true);
        queryArea.getChildren().addAll(searchFd, getBtn);
        
        TextArea resArea = new TextArea();
        resArea.setEditable(false);
        resArea.setPrefRowCount(10);
        
        HBox putArea = new HBox(10);
        TextField putKey = new TextField(); putKey.setPromptText("Key");
        TextField putVal = new TextField(); putVal.setPromptText("String Value");
        Button putBtn = new Button("Put Item");
        styleActionBtn(putBtn, false);
        putArea.getChildren().addAll(putKey, putVal, putBtn);
        
        getBtn.setOnAction(e -> {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(API_BASE + "/dynamodb/table/" + tableName + "/item/" + searchFd.getText()))
                .header("Authorization", "Bearer " + jwtToken).GET().build();
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                Platform.runLater(() -> {
                    resArea.setText(res.statusCode() == 200 ? res.body() : "Item not found or error.");
                });
            });
        });
        
        putBtn.setOnAction(e -> {
            String body = String.format("{\"key\":\"%s\", \"value\":\"%s\"}", putKey.getText(), putVal.getText());
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(API_BASE + "/dynamodb/table/" + tableName + "/item"))
                .header("Authorization", "Bearer " + jwtToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                Platform.runLater(() -> resArea.setText("Successfully put item."));
            });
        });

        box.getChildren().addAll(new Label("Explore Items"), queryArea, resArea, new Separator(), new Label("Insert Item"), putArea);
        dialog.getDialogPane().setContent(box);
        dialog.showAndWait();
    }

    private void setupBucketsView(HBox actions, StackPane container, Button refreshBtn) {
        Button createBtn = new Button("Create bucket");
        styleActionBtn(createBtn, true);
        createBtn.setOnAction(e -> handleCreateS3(null));
        actions.getChildren().add(0, createBtn);

        TableView<BucketRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<BucketRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().name));
        
        TableColumn<BucketRow, String> regionCol = new TableColumn<>("MiniCloud Region");
        regionCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().region));
        
        TableColumn<BucketRow, String> accessCol = new TableColumn<>("Access");
        accessCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().access));

        TableColumn<BucketRow, String> websiteCol = new TableColumn<>("Website");
        websiteCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().website));

        table.getColumns().addAll(nameCol, regionCol, accessCol, websiteCol);
        
        Button openBtn = new Button("Open Bucket");
        styleActionBtn(openBtn, false);
        openBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        openBtn.setOnAction(e -> showBucketExplorer(table.getSelectionModel().getSelectedItem().name));
        actions.getChildren().add(1, openBtn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                java.util.Map<String, String> info = new java.util.LinkedHashMap<>();
                info.put("Bucket Name", val.name);
                info.put("Region", val.region);
                info.put("Access", val.access);
                showDetails(val.name, info);
            }
        });
        
        GridPane s3Grid = new GridPane();
        s3Grid.setHgap(15);
        s3Grid.setVgap(15);
        s3Grid.setPadding(new Insets(0, 0, 15, 0));
        
        VBox st1 = createWidget("Storage Lens", "Total Buckets: 0");
        VBox st2 = createWidget("Total Storage", "Used: " + getTelemetry("usedStorageTb", "0") + " TB");
        VBox st3 = createWidget("Security", "Public Contexts: 0");
        VBox st4 = createWidget("Transfer Stats", "Requests: " + getTelemetry("transferRequests", "1000"));
        
        s3Grid.addRow(0, st1, st2, st3, st4);
        VBox s3Content = new VBox(s3Grid, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        container.getChildren().add(s3Content);

        Button deleteBtn = new Button("Delete Bucket");
        styleActionBtn(deleteBtn, false);
        deleteBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        Runnable refresh = () -> {
            fetchList("/buckets", res -> {
                try {
                    JsonNode root = objectMapper.readTree(res);
                    table.getItems().clear();
                    if (root.isArray()) {
                        for (JsonNode item : root) {
                            String ws = item.get("websiteEnabled") != null && item.get("websiteEnabled").asBoolean() ? "Active" : "Disabled";
                            table.getItems().add(new BucketRow(
                                item.get("name") != null ? item.get("name").asText() : "N/A",
                                item.get("region") != null ? item.get("region").asText() : "N/A",
                                item.get("accessControl") != null ? item.get("accessControl").asText() : "N/A",
                                ws
                            ));
                        }
                    }
                    Platform.runLater(() -> {
                        ((Label)st1.getChildren().get(1)).setText("Total Buckets: " + table.getItems().size());
                        long publicCount = table.getItems().stream().filter(b -> b.access != null && b.access.toLowerCase().contains("public")).count();
                        ((Label)st3.getChildren().get(1)).setText("Public Contexts: " + publicCount);
                    });
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        };

        deleteBtn.setOnAction(e -> handleDeleteS3(table.getSelectionModel().getSelectedItem(), refresh));
        actions.getChildren().add(2, deleteBtn); // After launch and open

        refreshBtn.setOnAction(e -> refresh.run());
        styleActionBtn(refreshBtn, false);
        refresh.run();
        addAutoRefreshToggle(actions, refresh);
    }

    private void setupUsersView(HBox actions, StackPane container, Button refreshBtn) {
        TableView<UserRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<UserRow, String> userCol = new TableColumn<>("User name");
        userCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().name));
        
        TableColumn<UserRow, String> idCol = new TableColumn<>("Account ID");
        idCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().accountId));
        
        TableColumn<UserRow, String> mfaCol = new TableColumn<>("MFA");
        mfaCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().mfa));

        table.getColumns().addAll(userCol, idCol, mfaCol);
        
        Button createIamBtn = new Button("Create IAM User");
        styleActionBtn(createIamBtn, true);
        actions.getChildren().add(0, createIamBtn);
        createIamBtn.setOnAction(e -> showCreateIamDialog());

        Runnable refreshUsers = () -> {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/auth/iam/list"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET().build();
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                Platform.runLater(() -> {
                    table.getItems().clear();
                    table.getItems().add(new UserRow(username, accountId != null ? accountId : "Root", "Virtual"));
                    if (res.statusCode() == 200) {
                        for (String line : res.body().split("\\{")) {
                            if (line.contains("\"username\"")) {
                                table.getItems().add(new UserRow(
                                    getJsonValue(line, "iamUsername"),
                                    getJsonValue(line, "accountId"),
                                    "No"
                                ));
                            }
                        }
                    }
                });
            });
        };

        createIamBtn.setOnAction(e -> {
            VBox vb = new VBox(15);
            vb.setPadding(new Insets(20));
            TextField nameFd = new TextField();
            nameFd.setPromptText("iam-user-name");
            PasswordField passFd = new PasswordField();
            passFd.setPromptText("Console password");
            
            CheckBox adminCb = new CheckBox("Add user to 'Administrators' group");
            adminCb.setSelected(true);
            
            vb.getChildren().addAll(new Label("User name"), nameFd, new Label("Console password"), passFd, adminCb);
            
            Dialog<ButtonType> d = new Dialog<>();
            d.setTitle("Add User");
            d.setHeaderText("Step 1: Specify user details");
            d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            d.getDialogPane().setContent(vb);
            
            d.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.OK) {
                    String name = nameFd.getText();
                    String pwd = passFd.getText().isEmpty() ? "password123" : passFd.getText();
                    HttpRequest postReq = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/auth/iam/create?iamUsername=" + name + "&password=" + pwd))
                        .header("Authorization", "Bearer " + jwtToken)
                        .POST(HttpRequest.BodyPublishers.noBody()).build();
                    httpClient.sendAsync(postReq, HttpResponse.BodyHandlers.ofString()).thenAccept(r -> {
                        Platform.runLater(refreshUsers);
                    });
                }
            });
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                java.util.Map<String, String> info = new java.util.LinkedHashMap<>();
                info.put("User Name", val.name);
                info.put("Account ID", val.accountId);
                info.put("MFA Status", val.mfa);
                showDetails(val.name, info);
            }
        });
        container.getChildren().add(table);
        refreshUsers.run();
    }

    private void showCreateIamDialog() {
        // Logic handled in setOnAction above for brevity
    }

    private void setupBillingView(HBox actions, StackPane container, Button refreshBtn) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10));

        Label summaryHeader = new Label("Monthly Cost Summary");
        summaryHeader.getStyleClass().add("MiniCloud-card-title");
        
        TableView<BillingRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(createCol("Resource ID", "resource"), createCol("Service", "type"), createCol("Total Aggregated Cost ($)", "cost"));

        PieChart chart = new PieChart();
        chart.setTitle("Spend by Service");
        chart.setPrefHeight(300);

        layout.getChildren().addAll(summaryHeader, table, chart);
        container.getChildren().add(layout);

        Runnable refresh = () -> {
            fetchList("/billing/history", res -> {
                table.getItems().clear();
                chart.getData().clear();
                double computeTotal = 0, rdsTotal = 0;
                for (String item : res.split("\\{")) if (item.contains("\"resourceId\"")) {
                    String type = getJsonValue(item, "resourceType");
                    String costRaw = getJsonValue(item, "amount");
                    double cost = 0;
                    try { cost = Double.parseDouble(costRaw); } catch(Exception ex) {}
                    table.getItems().add(new BillingRow(getJsonValue(item, "resourceId"), type, String.format("%.2f", cost)));
                    if ("COMPUTE".equals(type)) computeTotal += cost;
                    else if ("DATABASE".equals(type) || "RDS".equals(type)) rdsTotal += cost;
                }
                if (computeTotal > 0) chart.getData().add(new PieChart.Data("EC2", computeTotal));
                if (rdsTotal > 0) chart.getData().add(new PieChart.Data("RDS", rdsTotal));
            });
        };
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }
    private void setupVpcView(HBox actions, StackPane container, Button refreshBtn) {
        TableView<VpcRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(createCol("VPC ID", "id"), createCol("Name", "name"), createCol("IPv4 CIDR", "cidr"), createCol("Status", "status"));
        
        GridPane vpcGrid = new GridPane();
        vpcGrid.setHgap(15); vpcGrid.setVgap(15); vpcGrid.setPadding(new Insets(0, 0, 15, 0));
        VBox vt1 = createWidget("Your VPCs", "Total: 0");
        VBox vt2 = createWidget("Subnets", "Available: " + getTelemetry("subnets", "0"));
        VBox vt3 = createWidget("Internet Gateways", "Attached: 1");
        VBox vt4 = createWidget("VPC Peering", "Connections: 0");
        vpcGrid.addRow(0, vt1, vt2, vt3, vt4);
        VBox content = new VBox(vpcGrid, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        container.getChildren().add(content);

        Runnable refresh = () -> {
            fetchList("/vpc/list", res -> {
                try {
                    JsonNode root = objectMapper.readTree(res);
                    table.getItems().clear();
                    if (root.isArray()) {
                        for (JsonNode item : root) {
                            table.getItems().add(new VpcRow(
                                item.has("id") ? item.get("id").asText() : "N/A",
                                item.has("name") ? item.get("name").asText() : "N/A",
                                item.has("cidrBlock") ? item.get("cidrBlock").asText() : "N/A",
                                item.has("state") ? item.get("state").asText() : "N/A"
                            ));
                        }
                    }
                    Platform.runLater(() -> { ((Label)vt1.getChildren().get(1)).setText("Total: " + table.getItems().size()); });
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        };
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void setupSubnetView(HBox actions, StackPane container, Button refreshBtn) {
        TableView<SubnetRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(createCol("Subnet ID", "id"), createCol("VPC ID", "vpcId"), createCol("IPv4 CIDR", "cidr"), createCol("Availability Zone", "az"));
        container.getChildren().add(table);
        Runnable refresh = () -> {
            fetchList("/subnet/list", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) if (item.contains("\"subnetId\"")) {
                    table.getItems().add(new SubnetRow(getJsonValue(item, "subnetId"), getJsonValue(item, "vpcId"), getJsonValue(item, "cidrBlock"), getJsonValue(item, "availabilityZone")));
                }
            });
        };
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void setupRoute53View(HBox actions, StackPane container, Button refreshBtn) {
        TableView<DnsRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(createCol("Hosted Zone ID", "id"), createCol("Domain Name", "name"), createCol("Type", "type"));
        
        GridPane r53Grid = new GridPane();
        r53Grid.setHgap(15); r53Grid.setVgap(15); r53Grid.setPadding(new Insets(0, 0, 15, 0));
        VBox r1 = createWidget("Hosted Zones", "Total: 0");
        VBox r2 = createWidget("Health Checks", "Healthy: " + getTelemetry("domains", "0"));
        VBox r3 = createWidget("Traffic Policies", "Active: 1");
        VBox r4 = createWidget("Domains", "Registered: " + getTelemetry("domains", "0"));
        r53Grid.addRow(0, r1, r2, r3, r4);
        VBox r53Content = new VBox(r53Grid, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        container.getChildren().add(r53Content);

        Runnable refresh = () -> {
            fetchList("/dns/zones", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) if (item.contains("\"name\"")) {
                    table.getItems().add(new DnsRow(getJsonValue(item, "id"), getJsonValue(item, "name"), "Public", ""));
                }
                Platform.runLater(() -> { ((Label)r1.getChildren().get(1)).setText("Total: " + table.getItems().size()); });
            });
        };
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void setupCloudFormationView(HBox actions, StackPane container, Button refreshBtn) {
        TableView<StackRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(createCol("Stack Name", "name"), createCol("Status", "status"), createCol("Created At", "created"));
        container.getChildren().add(table);
        Runnable refresh = () -> {
            fetchList("/cloudformation/stacks", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) if (item.contains("\"stackName\"")) {
                    table.getItems().add(new StackRow(getJsonValue(item, "stackName"), getJsonValue(item, "status"), getJsonValue(item, "createdAt")));
                }
            });
        };
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void setupGuardDutyView(HBox actions, StackPane container, Button refreshBtn) {
        TableView<FindingRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(createCol("Finding Type", "type"), createCol("Severity", "severity"), createCol("Status", "status"), createCol("Discovery Time", "created"));
        container.getChildren().add(table);
        Runnable refresh = () -> {
            fetchList("/security/findings", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) if (item.contains("\"type\"")) {
                    table.getItems().add(new FindingRow(getJsonValue(item, "type"), getJsonValue(item, "severity"), getJsonValue(item, "status"), getJsonValue(item, "timestamp")));
                }
            });
        };
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void setupWafView(HBox actions, StackPane container, Button refreshBtn) {
        TableView<WafRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(createCol("Rule Name", "name"), createCol("Type", "type"), createCol("Action", "action"), createCol("Description", "desc"));
        container.getChildren().add(table);
        Runnable refresh = () -> {
            fetchList("/security/waf/rules", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) if (item.contains("\"ruleName\"")) {
                    table.getItems().add(new WafRow(getJsonValue(item, "ruleName"), "REGULAR", getJsonValue(item, "action"), getJsonValue(item, "scope")));
                }
            });
        };
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void setupIamGroupsView(HBox actions, StackPane container, Button refreshBtn) {
        TableView<IamGroupRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(createCol("Group Name", "name"), createCol("Policies Attached", "policies"));
        container.getChildren().add(table);
        Runnable refresh = () -> {
            fetchList("/iam/groups", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) if (item.contains("\"groupName\"")) {
                    table.getItems().add(new IamGroupRow(getJsonValue(item, "groupName"), "AdministratorAccess")); // Mock policy name
                }
            });
        };
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private <T> TableColumn<T, String> createCol(String title, String prop) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setCellValueFactory(f -> {
            try {
                // Use reflection for concise column mapping
                T item = f.getValue();
                Field field = item.getClass().getField(prop);
                Object val = field.get(item);
                return new javafx.beans.property.SimpleStringProperty(val != null ? val.toString() : "");
            } catch (Exception e) { return new javafx.beans.property.SimpleStringProperty(""); }
        });
        return col;
    }

    private void fetchList(String path, java.util.function.Consumer<String> callback) {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(API_BASE + path)).header("Authorization", "Bearer " + jwtToken).GET().build();
        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> Platform.runLater(() -> callback.accept(res.body())));
    }

    // --- Row Models ---
    public static class InstanceRow { public String name, id, state, type, publicIp, hostPort; public InstanceRow(String n, String i, String s, String t, String ip, String hp) { name=n; id=i; state=s; type=t; publicIp=ip; hostPort=hp; } }
    public static class DatabaseRow { public String id, engine, status, dbClass; public DatabaseRow(String i, String e, String s, String c) { id=i; engine=e; status=s; dbClass=c; } }
    public static class DynamoTableRow { public String name, status, partitionKey, sortKey; public DynamoTableRow(String n, String s, String pk, String sk) { name=n; status=s; partitionKey=pk; sortKey=sk; } }
    public static class BucketRow { public String name, region, access, website; public BucketRow(String n, String r, String a, String w) { name=n; region=r; access=a; website=w; } }
    public static class UserRow { public String name, accountId, mfa; public UserRow(String n, String i, String m) { name=n; accountId=i; mfa=m; } }
    public static class BillingRow { public String resource, type, cost; public BillingRow(String r, String t, String c) { resource=r; type=t; cost=c; } }
    public static class VpcRow { public String id, name, cidr, status; public VpcRow(String i, String n, String c, String s) { id=i; name=n; cidr=c; status=s; } }
    public static class SubnetRow { public String id, vpcId, cidr, az; public SubnetRow(String i, String v, String c, String a) { id=i; vpcId=v; cidr=c; az=a; } }
    public static class DnsRow { public String id, name, type, records; public DnsRow(String i, String n, String t, String r) { id=i; name=n; type=t; records=r; } }
    public static class StackRow { public String name, status, created; public StackRow(String n, String s, String c) { name=n; status=s; created=c; } }
    public static class FindingRow { public String type, severity, status, created; public FindingRow(String t, String s, String st, String c) { type=t; severity=s; status=st; created=c; } }
    public static class WafRow { public String name, type, action, desc; public WafRow(String n, String t, String a, String d) { name=n; type=t; action=a; desc=d; } }
    public static class IamGroupRow { public String name, policies; public IamGroupRow(String n, String p) { name=n; policies=p; } }


    private void handleLaunchEC2(Runnable onComplete) {
        VBox vb = new VBox(15);
        vb.setPadding(new Insets(20));
        vb.setMinWidth(400);

        TextField nameFd = new TextField("my-web-server");
        ComboBox<String> amiBox = new ComboBox<>();
        amiBox.getItems().addAll("Amazon Linux 2023 (ami-0c55b159)", "Ubuntu 22.04 LTS (ami-07d9b9d1)", "Red Hat Enterprise Linux 9 (ami-0fe472d8)");
        amiBox.setValue(amiBox.getItems().get(0));

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("t2.micro (1 vCPU, 1 GiB RAM)", "t2.small (1 vCPU, 2 GiB RAM)", "t2.medium (2 vCPU, 4 GiB RAM)", "t3.large (2 vCPU, 8 GiB RAM)");
        typeBox.setValue(typeBox.getItems().get(0));

        ComboBox<String> regionBox = new ComboBox<>();
        regionBox.getItems().addAll("us-east-1", "us-west-2", "eu-west-1", "ap-south-1");
        regionBox.setValue("us-east-1");

        ComboBox<String> vpcBox = new ComboBox<>();
        vpcBox.getItems().add("vpc-default (172.31.0.0/16)");
        vpcBox.setValue("vpc-default (172.31.0.0/16)");

        vb.getChildren().addAll(
            new Label("Name and tags"), nameFd,
            new Label("Application and OS Images (Amazon Machine Image)"), amiBox,
            new Label("Instance type"), typeBox,
            new Label("Region"), regionBox,
            new Label("VPC"), vpcBox
        );

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Launch an instance");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(vb);
        
        // Fetch real VPCs to make it better
        fetchList("/vpc/list", res -> {
            Platform.runLater(() -> {
                vpcBox.getItems().clear();
                for (String item : res.split("\\{")) if (item.contains("\"id\"")) {
                    vpcBox.getItems().add(getJsonValue(item, "id") + " (" + getJsonValue(item, "cidrBlock") + ")");
                }
                if (!vpcBox.getItems().isEmpty()) vpcBox.setValue(vpcBox.getItems().get(0));
                else vpcBox.getItems().add("vpc-default");
            });
        });

        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                String name = nameFd.getText();
                String type = typeBox.getValue().split(" ")[0];
                String vpc = vpcBox.getValue().split(" ")[0];
                String region = regionBox.getValue();
                String ami = amiBox.getValue().split("\\(")[1].replace(")", "");

                String body = String.format("{\"instanceName\":\"%s\", \"instanceType\":\"%s\", \"region\":\"%s\", \"vpcId\":\"%s\", \"subnetId\":\"subnet-default\", \"amiId\":\"%s\"}", 
                                           name, type, region, vpc, ami);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/compute/launch"))
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                    if (onComplete != null) Platform.runLater(onComplete);
                });
            }
        });
    }

    private void handleCreateRDS(Runnable onComplete) {
        VBox vb = new VBox(15);
        vb.setPadding(new Insets(20));
        vb.setMinWidth(450);

        TextField nameFd = new TextField("database-1");
        ComboBox<String> engineBox = new ComboBox<>();
        engineBox.getItems().addAll("MySQL 8.0.35", "PostgreSQL 16.1", "MariaDB 10.11", "Oracle 19c");
        engineBox.setValue("MySQL 8.0.35");

        ComboBox<String> classBox = new ComboBox<>();
        classBox.getItems().addAll("db.t3.micro (2 vCPU, 1 GiB)", "db.t3.small (2 vCPU, 2 GiB)", "db.m5.large (2 vCPU, 8 GiB)");
        classBox.setValue("db.t3.micro (2 vCPU, 1 GiB)");

        PasswordField passFd = new PasswordField();
        passFd.setPromptText("Master password");

        ComboBox<String> vpcBox = new ComboBox<>();
        vpcBox.getItems().add("vpc-default");
        vpcBox.setValue("vpc-default");

        vb.getChildren().addAll(
            new Label("DB instance identifier"), nameFd,
            new Label("Engine options"), engineBox,
            new Label("Instance configuration"), classBox,
            new Label("Master password"), passFd,
            new Label("Connectivity (VPC)"), vpcBox
        );

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create database");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(vb);
        
        fetchList("/vpc/list", res -> {
            Platform.runLater(() -> {
                vpcBox.getItems().clear();
                for (String item : res.split("\\{")) if (item.contains("\"id\"")) {
                    vpcBox.getItems().add(getJsonValue(item, "id"));
                }
                if (!vpcBox.getItems().isEmpty()) vpcBox.setValue(vpcBox.getItems().get(0));
            });
        });

        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                String name = nameFd.getText();
                String engine = engineBox.getValue().split(" ")[0].toLowerCase();
                String dbClass = classBox.getValue().split(" ")[0];
                String pwd = passFd.getText().isEmpty() ? "password" : passFd.getText();

                String body = String.format("{\"name\":\"%s\", \"dbName\":\"%s_db\", \"rootPassword\":\"%s\", \"engine\":\"%s\", \"dbInstanceClass\":\"%s\", \"vpcId\":\"%s\", \"subnetId\":\"subnet-default\"}", 
                                           name, name, pwd, engine, dbClass, vpcBox.getValue());
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/database/provision"))
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                    if (onComplete != null) Platform.runLater(onComplete);
                });
            }
        });
    }

    private void handleCreateS3(Runnable onComplete) {
        VBox vb = new VBox(15);
        vb.setPadding(new Insets(20));
        
        TextField nameFd = new TextField("");
        nameFd.setPromptText("my-bucket-name");
        
        ComboBox<String> regionBox = new ComboBox<>();
        regionBox.getItems().addAll("us-east-1 (N. Virginia)", "us-west-2 (Oregon)", "eu-west-1 (Ireland)", "ap-south-1 (Mumbai)");
        regionBox.setValue("us-east-1 (N. Virginia)");
        
        vb.getChildren().addAll(new Label("Bucket name"), nameFd, new Label("MiniCloud Region"), regionBox);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create bucket");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(vb);
        
        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                String name = nameFd.getText();
                String region = regionBox.getValue().split(" ")[0];
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/buckets?name=" + name + "&region=" + region))
                        .header("Authorization", "Bearer " + jwtToken)
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                    if (onComplete != null) Platform.runLater(onComplete);
                });
            }
        });
    }

    private void styleActionBtn(Button btn, boolean isPrimary) {
        if (isPrimary) {
            btn.getStyleClass().add("btn-orange");
        } else {
            btn.getStyleClass().add("btn-secondary");
        }
    }

    private void showStatsDialog(String instanceName, String containerId) {
        if (containerId == null || containerId.isEmpty() || "N/A".equals(containerId)) return;

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("CloudWatch Metrics: " + instanceName);
        
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: white;");

        Label header = new Label("Monitoring for " + instanceName + " (" + containerId + ")");
        header.getStyleClass().add("details-title");

        // Real-time LineChart
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (s)");
        xAxis.setForceZeroInRange(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Utilization (%)");
        
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Resource Usage (CPU/Memory)");
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);

        XYChart.Series<Number, Number> cpuSeries = new XYChart.Series<>();
        cpuSeries.setName("CPU (%)");
        XYChart.Series<Number, Number> memSeries = new XYChart.Series<>();
        memSeries.setName("Memory (%)");
        
        lineChart.getData().addAll(cpuSeries, memSeries);

        // Simulation logic for live updates
        final int[] time = {0};
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), event -> {
                double cpuVal = 20 + Math.random() * 30; // Simulated live data
                double memVal = 40 + Math.random() * 10;
                cpuSeries.getData().add(new XYChart.Data<>(time[0], cpuVal));
                memSeries.getData().add(new XYChart.Data<>(time[0], memVal));
                time[0]++;
                if (cpuSeries.getData().size() > 20) {
                    cpuSeries.getData().remove(0);
                    xAxis.setLowerBound(time[0] - 20);
                    xAxis.setUpperBound(time[0]);
                }
            })
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();

        dialog.setOnCloseRequest(e -> timeline.stop());

        Button closeBtn = new Button("Close");
        styleActionBtn(closeBtn, false);
        closeBtn.setOnAction(e -> {
            timeline.stop();
            dialog.close();
        });

        layout.getChildren().addAll(header, lineChart, closeBtn);
        dialog.setScene(new Scene(layout, 650, 480));
        dialog.show();
    }

    private void showDetailsDialog(String selectedItem) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Instance Details - MiniCloud Simulation");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        applyStyles(dialog.getDialogPane().getScene());
        
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setMinWidth(450);
        box.getStyleClass().add("card");

        Label header = new Label("Resource Details");
        header.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        header.setTextFill(Color.web("#818cf8"));

        GridPane grid = new GridPane();
        grid.setHgap(20); grid.setVgap(10);

        String[] sparts = selectedItem.split(" \\| ");

        if (selectedItem.contains("ID: ")) {
            String namePart = selectedItem.split(" [\\(\\[|]")[0];
            String id = selectedItem.split("ID: ")[1];
            header.setText("Resource: " + namePart);
            
            addGridRow(grid, 0, "Resource ID:", id);
            addGridRow(grid, 1, "Status:", "Running");
            addGridRow(grid, 2, "Type:", currentTab);
            
            if ("Compute".equals(currentTab)) {
                String region = selectedItem.split("\\| ")[1];
                addGridRow(grid, 3, "Region:", region);
                addGridRow(grid, 4, "Instance Type:", selectedItem.contains("(") ? selectedItem.split("\\(")[1].split("\\)")[0] : "t2.micro");
            } else if ("Database".equals(currentTab)) {
                addGridRow(grid, 3, "Engine:", selectedItem.contains("[") ? selectedItem.split("\\[")[1].split("\\]")[0] : "mysql");
                addGridRow(grid, 4, "Class:", selectedItem.split("\\| ")[1]);
            }
        } else if ("Storage".equals(currentTab)) {
            header.setText("S3 Bucket: " + sparts[0]);
            addGridRow(grid, 0, "Region:", sparts[1]);
            addGridRow(grid, 1, "Access:", sparts[2]);
            addGridRow(grid, 2, "ARN:", "arn:MiniCloud:s3:::" + sparts[0]);
        }

        box.getChildren().addAll(header, new Separator(), grid);
        
        if ("Compute".equals(currentTab) || "Storage".equals(currentTab)) {
            Button openBtn = new Button("\uD83C\uDF10 Open Website");
            styleActionBtn(openBtn, true);
            openBtn.setMaxWidth(Double.MAX_VALUE);
            openBtn.setOnAction(e -> {
                String url = "";
                if ("Compute".equals(currentTab)) {
                    // Try to find the host port from the grid or selected item
                    String port = selectedItem.contains("Port: ") ? selectedItem.split("Port: ")[1].split("\\|")[0].trim() : "8081";
                    url = "http://localhost:" + port;
                } else {
                    url = API_BASE + "/buckets/public/" + sparts[0] + "/index.html";
                }
                getHostServices().showDocument(url);
            });
            box.getChildren().add(openBtn);
        }

        dialog.getDialogPane().setContent(box);
        dialog.showAndWait();
    }

    private void showServicesMenu(Node owner) {
        ContextMenu menu = new ContextMenu();
        menu.getStyleClass().add("services-dropdown");
        
        menu.getItems().addAll(
            new MenuItem("\u2601 Compute (EC2, Lambda, Batch)"),
            new MenuItem("\uD83D\uDCC1 Storage (S3, EFS, Glacier)"),
            new MenuItem("\uD83D\uDDB3 Database (RDS, DynamoDB, ElastiCache)"),
            new MenuItem("\uD83D\uDDA5 Networking (VPC, Route 53, CloudFront)"),
            new MenuItem("\uD83D\uDE00 Identity & Compliance (IAM, KMS, WAF)"),
            new MenuItem("\u1F4B0 Billing & Cost Management")
        );
        menu.show(owner, javafx.geometry.Side.BOTTOM, 0, 5);
    }

    private void filterSidebar(TreeItem<String> root, String text) {
        if (text == null || text.isEmpty()) return;
        expandMatchingNodes(root, text.toLowerCase());
    }

    private void expandMatchingNodes(TreeItem<String> item, String text) {
        if (item.getValue().toLowerCase().contains(text)) {
            TreeItem<String> p = item.getParent();
            while (p != null) {
                p.setExpanded(true);
                p = p.getParent();
            }
        }
        for (TreeItem<String> child : item.getChildren()) {
            expandMatchingNodes(child, text);
        }
    }

    private void showDetails(String title, java.util.Map<String, String> data) {
        detailsPane.getChildren().clear();
        detailsPane.setVisible(true);
        detailsPane.setManaged(true);

        Label titleLbl = new Label(title + " details");
        titleLbl.getStyleClass().add("details-title");
        
        GridPane grid = new GridPane();
        grid.getStyleClass().add("details-grid");
        int row = 0, col = 0;
        
        for (java.util.Map.Entry<String, String> entry : data.entrySet()) {
            VBox cell = new VBox(2);
            Label lb = new Label(entry.getKey());
            lb.getStyleClass().add("details-label");
            Label val = new Label(entry.getValue());
            val.getStyleClass().add("details-value");
            cell.getChildren().addAll(lb, val);
            grid.add(cell, col, row);
            col++;
            if (col > 3) { col = 0; row++; }
        }

        Button closeBtn = new Button("\u2715");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: gray; -fx-font-size: 16px;");
        closeBtn.setOnAction(e -> {
            detailsPane.setVisible(false);
            detailsPane.setManaged(false);
        });

        HBox top = new HBox(titleLbl, new Region(), closeBtn);
        HBox.setHgrow(top.getChildren().get(1), Priority.ALWAYS);
        
        detailsPane.getChildren().addAll(top, new Separator(), grid);

        if (title.toLowerCase().contains("bucket") || currentTab.equals("Storage")) {
            VBox websiteBox = new VBox(10);
            websiteBox.setPadding(new Insets(10, 0, 0, 0));
            Label wsLabel = new Label("Static website hosting");
            wsLabel.getStyleClass().add("details-label");
            
            Button enableWsBtn = new Button("Enable bucket hosting");
            styleActionBtn(enableWsBtn, false);
            enableWsBtn.setOnAction(e -> handleEnableWebsite(title.replace(" details", "").trim()));
            
            websiteBox.getChildren().addAll(wsLabel, enableWsBtn);
            detailsPane.getChildren().add(websiteBox);
        } else if (currentTab.equals("Compute") || title.toLowerCase().contains("instance")) {
            Button browseBtn = new Button("\u2197 Browse Website");
            browseBtn.getStyleClass().add("btn-orange");
            browseBtn.setOnAction(e -> {
                String port = data.getOrDefault("Host Port", "8080");
                getHostServices().showDocument("http://localhost:" + port);
            });
            detailsPane.getChildren().add(new VBox(10, new Separator(), browseBtn));
        }
    }

    private void handleEnableWebsite(String bucketName) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Enable Static Website Hosting");
        dialog.setHeaderText("Configure hosting for " + bucketName);
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField indexField = new TextField("index.html");
        TextField errorField = new TextField("error.html");
        
        grid.add(new Label("Index Document:"), 0, 0); grid.add(indexField, 1, 0);
        grid.add(new Label("Error Document:"), 0, 1); grid.add(errorField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/buckets/" + bucketName + "/website?indexDocument=" + java.net.URLEncoder.encode(indexField.getText(), "UTF-8") + "&errorDocument=" + java.net.URLEncoder.encode(errorField.getText(), "UTF-8")))
                        .header("Authorization", "Bearer " + jwtToken)
                        .POST(HttpRequest.BodyPublishers.noBody()).build();
                    httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                        Platform.runLater(() -> {
                            new Alert(Alert.AlertType.INFORMATION, "Static website hosting enabled for " + bucketName).show();
                        });
                    });
                } catch(Exception ex) { ex.printStackTrace(); }
            }
            return true;
        });
        dialog.showAndWait();
    }

    private void addGridRow(GridPane grid, int row, String labelText, String valueText) {
        Label label = new Label(labelText);
        label.setTextFill(Color.web("#94a3b8"));
        label.setFont(Font.font(null, FontWeight.BOLD, 12));
        
        Label value = new Label(valueText);
        value.setTextFill(Color.WHITE);
        
        grid.add(label, 0, row);
        grid.add(value, 1, row);
    }


    private String getJsonValue(String json, String key) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode node = root.get(key);
            if (node == null) {
                // Try case-insensitive or common variants if needed, or just return N/A
                return "N/A";
            }
            return node.asText();
        } catch (Exception e) {
            return "N/A";
        }
    }

    private void addAutoRefreshToggle(HBox actions, Runnable refreshTask) {
        CheckBox cb = new CheckBox("Auto-refresh");
        cb.getStyleClass().add("MiniCloud-label");
        cb.setStyle("-fx-text-fill: #444; -fx-padding: 0 0 0 20;");
        
        if (autoRefreshTimeline != null) autoRefreshTimeline.stop();
        
        autoRefreshTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(30), e -> {
                if (cb.isSelected()) refreshTask.run();
            })
        );
        autoRefreshTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        autoRefreshTimeline.play();
        
        actions.getChildren().add(cb);
    }

    private void setupLambdaView(HBox actions, StackPane container, Button refreshBtn) {
        Button createBtn = new Button("Create Function");
        styleActionBtn(createBtn, true);
        createBtn.setOnAction(e -> handleCreateLambda());
        actions.getChildren().add(0, createBtn);

        Button invokeBtn = new Button("Invoke");
        styleActionBtn(invokeBtn, false);
        actions.getChildren().add(1, invokeBtn);

        TableView<LambdaFunctionRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<LambdaFunctionRow, String> nameCol = new TableColumn<>("Function Name");
        nameCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().name));
        
        TableColumn<LambdaFunctionRow, String> runtimeCol = new TableColumn<>("Runtime");
        runtimeCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().runtime));
        
        TableColumn<LambdaFunctionRow, String> handlerCol = new TableColumn<>("Handler");
        handlerCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().handler));

        table.getColumns().addAll(nameCol, runtimeCol, handlerCol);
        
        GridPane lambdaGrid = new GridPane();
        lambdaGrid.setHgap(15);
        lambdaGrid.setVgap(15);
        lambdaGrid.setPadding(new Insets(0, 0, 15, 0));
        
        VBox lt1 = createWidget("Functions", "Total Functions: 0");
        VBox lt2 = createWidget("Invocations", "Last 30 Days: " + getTelemetry("invocations30d", "0"));
        VBox lt3 = createWidget("Performance", "Error Rate: " + getTelemetry("errorRate", "0.0") + "%\nAvg Duration: " + getTelemetry("avgDurationMs", "100") + "ms");
        VBox lt4 = createWidget("Concurrency", "Reserved: 1000");
        
        lambdaGrid.addRow(0, lt1, lt2, lt3, lt4);
        VBox lambdaContent = new VBox(lambdaGrid, table);
        VBox.setVgrow(table, Priority.ALWAYS);

        Runnable refresh = () -> {
            fetchList("/compute/lambda", res -> {
                try {
                    JsonNode root = objectMapper.readTree(res);
                    table.getItems().clear();
                    if (root.isArray()) {
                        for (JsonNode item : root) {
                            table.getItems().add(new LambdaFunctionRow(
                                item.has("name") ? item.get("name").asText() : "N/A",
                                item.has("runtime") ? item.get("runtime").asText() : "N/A",
                                item.has("handler") ? item.get("handler").asText() : "N/A",
                                item.has("code") ? item.get("code").asText() : ""
                            ));
                        }
                    }
                    Platform.runLater(() -> {
                        ((Label)lt1.getChildren().get(1)).setText("Total Functions: " + table.getItems().size());
                    });
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        };

        invokeBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        invokeBtn.setOnAction(e -> {
            LambdaFunctionRow row = table.getSelectionModel().getSelectedItem();
            handleInvokeLambda(row);
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                java.util.Map<String, String> info = new java.util.LinkedHashMap<>();
                info.put("Function Name", val.name);
                info.put("Runtime", val.runtime);
                info.put("Handler", val.handler);
                showDetails(val.name, info);
            }
        });

        container.getChildren().add(lambdaContent);
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void handleCreateLambda() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Create Lambda Function");
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        ComboBox<String> runtimeBox = new ComboBox<>();
        runtimeBox.getItems().addAll("python3.9", "nodejs18.x", "java17");
        runtimeBox.setValue("python3.9");
        TextField handlerField = new TextField("handler");
        TextArea codeArea = new TextArea("import os\n\ndef handler(event, context):\n  print('MiniCloud Event:', os.environ.get('LAMBDA_EVENT'))");
        codeArea.setPrefRowCount(10);
        
        grid.add(new Label("Function name:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Runtime:"), 0, 1); grid.add(runtimeBox, 1, 1);
        grid.add(new Label("Handler:"), 0, 2); grid.add(handlerField, 1, 2);
        grid.add(new Label("Code:"), 0, 3); grid.add(codeArea, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    String payload = String.format(
                        "{\"name\":\"%s\",\"runtime\":\"%s\",\"handler\":\"%s\",\"code\":\"%s\"}",
                        nameField.getText(), runtimeBox.getValue(), handlerField.getText(), codeArea.getText().replace("\"", "\\\"").replace("\n", "\\n")
                    );
                    HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/compute/lambda"))
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload)).build();
                    httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
                } catch(Exception ex) { ex.printStackTrace(); }
            }
            return true;
        });
        dialog.showAndWait();
    }

    private void setupSnsView(HBox actions, StackPane container, Button refreshBtn) {
        Button createBtn = new Button("Create Topic");
        styleActionBtn(createBtn, true);
        createBtn.setOnAction(e -> handleCreateSns());
        actions.getChildren().add(0, createBtn);

        Button subBtn = new Button("Subscribe");
        styleActionBtn(subBtn, false);
        actions.getChildren().add(1, subBtn);

        TableView<SnsTopicRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<SnsTopicRow, String> nameCol = new TableColumn<>("Topic Name");
        nameCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().name));
        
        TableColumn<SnsTopicRow, String> arnCol = new TableColumn<>("ARN");
        arnCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().arn));
        
        table.getColumns().addAll(nameCol, arnCol);
        
        GridPane snsGrid = new GridPane();
        snsGrid.setHgap(15);
        snsGrid.setVgap(15);
        snsGrid.setPadding(new Insets(0, 0, 15, 0));
        
        VBox snt1 = createWidget("Topics", "Total Topics: 0");
        VBox snt2 = createWidget("Subscriptions", "Active: " + getTelemetry("subscriptions", "0"));
        VBox snt3 = createWidget("Deliveries", "Last 24h: " + getTelemetry("deliveries24h", "0"));
        VBox snt4 = createWidget("Reliability", "Delivery Rate: 99.99%");
        
        snsGrid.addRow(0, snt1, snt2, snt3, snt4);
        VBox snsContent = new VBox(snsGrid, table);
        VBox.setVgrow(table, Priority.ALWAYS);

        Runnable refresh = () -> {
            fetchList("/messaging/topics", res -> {
                try {
                    JsonNode root = objectMapper.readTree(res);
                    table.getItems().clear();
                    if (root.isArray()) {
                        for (JsonNode item : root) {
                            table.getItems().add(new SnsTopicRow(
                                item.has("name") ? item.get("name").asText() : "N/A",
                                item.has("arn") ? item.get("arn").asText() : "N/A"
                            ));
                        }
                    }
                    Platform.runLater(() -> {
                        ((Label)snt1.getChildren().get(1)).setText("Total Topics: " + table.getItems().size());
                    });
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        };

        subBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        subBtn.setOnAction(e -> {
            SnsTopicRow row = table.getSelectionModel().getSelectedItem();
            handleSnsSubscribe(row.name);
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                java.util.Map<String, String> info = new java.util.LinkedHashMap<>();
                info.put("Topic Name", val.name);
                info.put("ARN", val.arn);
                showDetails(val.name, info);
            }
        });

        container.getChildren().add(snsContent);
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void handleCreateSns() {
        TextInputDialog d = new TextInputDialog();
        d.setTitle("Create SNS Topic");
        d.setHeaderText("Enter topic name:");
        d.showAndWait().ifPresent(name -> {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/messaging/topics?name=" + name))
                .header("Authorization", "Bearer " + jwtToken)
                .POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
        });
    }

    private void handleSnsSubscribe(String topicName) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Subscribe to " + topicName);
        
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        ComboBox<String> protocolBox = new ComboBox<>();
        protocolBox.getItems().addAll("sqs", "email", "http");
        protocolBox.setValue("sqs");
        TextField endpointField = new TextField("my-queue");
        
        grid.add(new Label("Protocol:"), 0, 0); grid.add(protocolBox, 1, 0);
        grid.add(new Label("Endpoint:"), 0, 1); grid.add(endpointField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(API_BASE + "/messaging/topics/" + topicName + "/subscribe?protocol=" + protocolBox.getValue() + "&endpoint=" + endpointField.getText()))
                        .header("Authorization", "Bearer " + jwtToken)
                        .POST(HttpRequest.BodyPublishers.noBody()).build();
                    httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
                } catch(Exception ex) {}
            }
            return true;
        });
        dialog.showAndWait();
    }

    private void setupSqsView(HBox actions, StackPane container, Button refreshBtn) {
        Button createBtn = new Button("Create Queue");
        styleActionBtn(createBtn, true);
        createBtn.setOnAction(e -> handleCreateSqs());
        actions.getChildren().add(0, createBtn);

        TableView<SqsQueueRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<SqsQueueRow, String> nameCol = new TableColumn<>("Queue Name");
        nameCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().name));
        
        TableColumn<SqsQueueRow, String> urlCol = new TableColumn<>("Queue URL");
        urlCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().url));
        
        table.getColumns().addAll(nameCol, urlCol);
        
        GridPane sqsGrid = new GridPane();
        sqsGrid.setHgap(15);
        sqsGrid.setVgap(15);
        sqsGrid.setPadding(new Insets(0, 0, 15, 0));
        
        VBox qt1 = createWidget("Queues", "Total Queues: 0");
        VBox qt2 = createWidget("In-Flight Messages", "Avg Depth: " + getTelemetry("inFlightMessages", "0"));
        VBox qt3 = createWidget("Messages Received", "Last 24h: " + getTelemetry("deliveries24h", "0"));
        VBox qt4 = createWidget("Dead Letter Queues", "Configured: 1");
        
        sqsGrid.addRow(0, qt1, qt2, qt3, qt4);
        VBox sqsContent = new VBox(sqsGrid, table);
        VBox.setVgrow(table, Priority.ALWAYS);

        Runnable refresh = () -> {
            fetchList("/messaging/queues", res -> {
                try {
                    JsonNode root = objectMapper.readTree(res);
                    table.getItems().clear();
                    if (root.isArray()) {
                        for (JsonNode item : root) {
                            table.getItems().add(new SqsQueueRow(
                                item.has("name") ? item.get("name").asText() : "N/A",
                                item.has("queueUrl") ? item.get("queueUrl").asText() : "N/A"
                            ));
                        }
                    }
                    Platform.runLater(() -> {
                        ((Label)qt1.getChildren().get(1)).setText("Total Queues: " + table.getItems().size());
                    });
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        };

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                java.util.Map<String, String> info = new java.util.LinkedHashMap<>();
                info.put("Queue Name", val.name);
                info.put("Queue URL", val.url);
                showDetails(val.name, info);
            }
        });

        container.getChildren().add(sqsContent);
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void handleCreateSqs() {
        TextInputDialog d = new TextInputDialog();
        d.setTitle("Create SQS Queue");
        d.setHeaderText("Enter queue name:");
        d.showAndWait().ifPresent(name -> {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/messaging/queues?name=" + name))
                .header("Authorization", "Bearer " + jwtToken)
                .POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
        });
    }

    private void setupSecurityGroupsView(HBox actions, StackPane container, Button refreshBtn) {
        Button createBtn = new Button("Create security group");
        styleActionBtn(createBtn, true);
        actions.getChildren().add(0, createBtn);

        TableView<SecurityGroupRow> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<SecurityGroupRow, String> idCol = new TableColumn<>("Security group ID");
        idCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().id));
        
        TableColumn<SecurityGroupRow, String> nameCol = new TableColumn<>("Security group name");
        nameCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().name));
        
        TableColumn<SecurityGroupRow, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().description));
        
        table.getColumns().addAll(idCol, nameCol, descCol);
        container.getChildren().add(table);

        Runnable refresh = () -> {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/firewall/groups"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET().build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                Platform.runLater(() -> {
                    table.getItems().clear();
                    if (res.statusCode() == 200) {
                        for (String item : res.body().split("\\{")) {
                            if (item.contains("\"groupName\"")) {
                                table.getItems().add(new SecurityGroupRow(
                                    getJsonValue(item, "id"),
                                    getJsonValue(item, "groupName"),
                                    getJsonValue(item, "description"),
                                    getJsonValue(item, "vpcId")
                                ));
                            }
                        }
                    }
                });
            });
        };
        refreshBtn.setOnAction(e -> refresh.run());
        refresh.run();
    }

    private void setupAccountView(StackPane container) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.getStyleClass().add("MiniCloud-card");

        Label header = new Label("My Account Profile");
        header.setFont(Font.font("Outfit", FontWeight.BOLD, 24));
        header.setTextFill(Color.web("#232f3e"));

        GridPane grid = new GridPane();
        grid.setHgap(40); grid.setVgap(20);

        Label loading = new Label("Loading account details...");
        layout.getChildren().addAll(header, new Separator(), loading);
        container.getChildren().add(layout);

        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/auth/me"))
            .header("Authorization", "Bearer " + jwtToken)
            .GET().build();
        
        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
            Platform.runLater(() -> {
                layout.getChildren().remove(loading);
                if (res.statusCode() == 200) {
                    String body = res.body();
                    addAccountField(grid, 0, "Account ID:", getJsonValue(body, "accountId"));
                    addAccountField(grid, 1, "Username/Email:", getJsonValue(body, "username"));
                    addAccountField(grid, 2, "Account Alias:", getJsonValue(body, "accountAlias"));
                    addAccountField(grid, 3, "Account Balance:", "$" + getJsonValue(body, "balance"));
                    addAccountField(grid, 4, "Account Status:", getJsonValue(body, "status"));
                    layout.getChildren().add(grid);
                } else {
                    layout.getChildren().add(new Label("Error loading profile data."));
                }
            });
        });
    }

    private void addAccountField(GridPane grid, int row, String label, String value) {
        Label l = new Label(label);
        l.setFont(Font.font(null, FontWeight.BOLD, 14));
        l.setTextFill(Color.GRAY);
        Label v = new Label(value);
        v.setFont(Font.font(14));
        grid.add(l, 0, row);
        grid.add(v, 1, row);
    }

    private void handleLifecycleEC2(InstanceRow row, String action, Runnable refresh) {
        if (row == null) return;
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/compute/" + row.id + "/" + action))
            .header("Authorization", "Bearer " + jwtToken)
            .POST(HttpRequest.BodyPublishers.noBody()).build();
        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
            Platform.runLater(refresh);
        });
    }

    private void showBucketExplorer(String bucketName) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("S3 Bucket Explorer: " + bucketName);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(16));
        layout.getStyleClass().add("content-pane");

        HBox breadcrumbs = new HBox(5);
        breadcrumbs.setAlignment(Pos.CENTER_LEFT);
        
        TableView<String[]> fileTable = new TableView<>();
        fileTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<String[], String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue()[0]));
        TableColumn<String[], String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue()[1]));
        fileTable.getColumns().addAll(nameCol, sizeCol);

        java.util.concurrent.atomic.AtomicReference<String> currentPrefix = new java.util.concurrent.atomic.AtomicReference<>("");
        final Runnable[] refreshFilesRef = new Runnable[1];

        refreshFilesRef[0] = () -> {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/buckets/" + bucketName + "/files"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET().build();
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                try {
                    final JsonNode root = objectMapper.readTree(res.body());
                    Platform.runLater(() -> {
                        fileTable.getItems().clear();
                        String prefix = currentPrefix.get();
                        
                        // Update Breadcrumbs
                        breadcrumbs.getChildren().clear();
                        Button rootBtn = new Button(bucketName);
                        rootBtn.getStyleClass().add("MiniCloud-card-link");
                        rootBtn.setOnAction(e -> { currentPrefix.set(""); refreshFilesRef[0].run(); });
                        breadcrumbs.getChildren().add(rootBtn);
                        
                        if (!prefix.isEmpty()) {
                            String[] parts = prefix.split("/");
                            StringBuilder pathAccumulator = new StringBuilder();
                            for (String part : parts) {
                                if (part.isEmpty()) continue;
                                pathAccumulator.append(part).append("/");
                                final String targetPath = pathAccumulator.toString();
                                breadcrumbs.getChildren().add(new Label(">"));
                                Button b = new Button(part);
                                b.getStyleClass().add("MiniCloud-card-link");
                                b.setOnAction(e -> { currentPrefix.set(targetPath); refreshFilesRef[0].run(); });
                                breadcrumbs.getChildren().add(b);
                            }
                        }

                        java.util.Set<String> folders = new java.util.TreeSet<>();
                        java.util.List<String[]> filesAtLevel = new java.util.ArrayList<>();

                        if (root.isArray()) {
                            for (JsonNode node : root) {
                                String fullName = node.get("fileName").asText();
                                if (fullName.startsWith(prefix)) {
                                    String relative = fullName.substring(prefix.length());
                                    if (relative.contains("/")) {
                                        folders.add(relative.substring(0, relative.indexOf("/") + 1));
                                    } else if (!relative.isEmpty()) {
                                        filesAtLevel.add(new String[]{relative, node.get("size").asText() + " B"});
                                    }
                                }
                            }
                        }

                        for (String f : folders) {
                            fileTable.getItems().add(new String[]{"[Folder] " + f, "-"});
                        }
                        fileTable.getItems().addAll(filesAtLevel);
                    });
                } catch (Exception ex) { ex.printStackTrace(); }
            });
        };

        fileTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String[] selected = fileTable.getSelectionModel().getSelectedItem();
                if (selected != null && selected[0].startsWith("[Folder] ")) {
                    String folderName = selected[0].replace("[Folder] ", "");
                    currentPrefix.set(currentPrefix.get() + folderName);
                    refreshFilesRef[0].run();
                }
            }
        });

        Button refreshBtn = new Button("Refresh");
        styleActionBtn(refreshBtn, false);
        refreshBtn.setOnAction(e -> refreshFilesRef[0].run());
        
        Button uploadBtn = new Button("Upload");
        styleActionBtn(uploadBtn, true);

        layout.getChildren().addAll(new Label("S3 Buckets > " + bucketName), breadcrumbs, fileTable, new HBox(10, refreshBtn, uploadBtn));
        stage.setScene(new Scene(layout, 600, 450));
        refreshFilesRef[0].run();
        stage.show();
    }

    private void handleInvokeLambda(LambdaFunctionRow row) {
        if (row == null) return;
        Stage stage = new Stage();
        stage.setTitle("Invoke " + row.name);
        
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("content-pane");
        
        Label title = new Label("Configure Test Event");
        title.getStyleClass().add("details-title");
        
        TextArea payloadArea = new TextArea("{\n  \"key1\": \"value1\",\n  \"key2\": \"value2\"\n}");
        payloadArea.setStyle("-fx-font-family: 'Consolas', 'Monospace';");
        payloadArea.setPrefRowCount(8);
        
        Button invokeBtn = new Button("Invoke");
        styleActionBtn(invokeBtn, true);
        
        TextArea resultArea = new TextArea("Results will appear here...");
        resultArea.setEditable(false);
        resultArea.setPrefRowCount(10);
        resultArea.setStyle("-fx-font-family: 'Consolas', 'Monospace'; -fx-background-color: #f8f9fa;");

        invokeBtn.setOnAction(e -> {
            invokeBtn.setDisable(true);
            resultArea.setText("Invoking...");
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/compute/lambda/" + row.name + "/invoke"))
                .header("Authorization", "Bearer " + jwtToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payloadArea.getText())).build();
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(r -> {
                Platform.runLater(() -> {
                    resultArea.setText(r.body());
                    invokeBtn.setDisable(false);
                });
            });
        });

        layout.getChildren().addAll(title, new Label("Payload (JSON):"), payloadArea, invokeBtn, new Label("Execution Result:"), resultArea);
        stage.setScene(new Scene(layout, 550, 600));
        stage.show();
    }

    private void handleTerminateEC2(InstanceRow row, Runnable refresh) {
        if (row == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Terminate instance " + row.id + "? This action cannot be undone.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Terminate Instance");
        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/compute/" + row.id))
                    .header("Authorization", "Bearer " + jwtToken)
                    .DELETE().build();
                httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(r -> {
                    Platform.runLater(refresh);
                });
            }
        });
    }

    private void handleDeleteRDS(DatabaseRow row, Runnable refresh) {
        if (row == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete database " + row.id + "? This action cannot be undone.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Delete Database");
        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                // Find ID from name in backend or just use name if ID is name
                HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/database/" + row.id))
                    .header("Authorization", "Bearer " + jwtToken)
                    .DELETE().build();
                httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(r -> {
                    Platform.runLater(refresh);
                });
            }
        });
    }

    private void handleDeleteS3(BucketRow row, Runnable refresh) {
        if (row == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete bucket " + row.name + " and all its contents? This action cannot be undone.", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Delete Bucket");
        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/storage/" + row.name))
                    .header("Authorization", "Bearer " + jwtToken)
                    .DELETE().build();
                httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(r -> {
                    Platform.runLater(refresh);
                });
            }
        });
    }

    public static class SecurityGroupRow {
        public String id, name, description, vpcId;
        public SecurityGroupRow(String id, String name, String description, String vpcId) {
            this.id = id; this.name = name; this.description = description; this.vpcId = vpcId;
        }
    }

    public static class FirewallRuleRow {
        public String type, protocol, port, source, desc;
        public FirewallRuleRow(String type, String protocol, String port, String source, String desc) {
            this.type = type; this.protocol = protocol; this.port = port; this.source = source; this.desc = desc;
        }
    }

    public static class LambdaFunctionRow {
        public String name, runtime, handler, code;
        public LambdaFunctionRow(String name, String runtime, String handler, String code) {
            this.name = name; this.runtime = runtime; this.handler = handler; this.code = code;
        }
    }

    public static class SqsQueueRow {
        public String name, url;
        public SqsQueueRow(String name, String url) {
            this.name = name; this.url = url;
        }
    }

    public static class SnsTopicRow {
        public String name, arn;
        public SnsTopicRow(String name, String arn) {
            this.name = name; this.arn = arn;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setupBackupView(StackPane container) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: white;");

        Label header = new Label("Backup & Recovery Dashboard");
        header.setFont(Font.font("Outfit", FontWeight.BOLD, 24));

        HBox actions = new HBox(10);
        Button triggerBtn = new Button("Trigger Manual Backup");
        triggerBtn.getStyleClass().add("aws-btn-primary");
        
        ComboBox<String> serviceBox = new ComboBox<>();
        serviceBox.getItems().addAll("compute", "iam", "billing", "storage");
        serviceBox.setValue("compute");
        
        actions.getChildren().addAll(new Label("Service:"), serviceBox, triggerBtn);

        TableView<BackupRecordDto> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BackupRecordDto, String> nameCol = new TableColumn<>("Snapshot Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        TableColumn<BackupRecordDto, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<BackupRecordDto, String> dateCol = new TableColumn<>("Timestamp");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        TableColumn<BackupRecordDto, Long> sizeCol = new TableColumn<>("Size (Bytes)");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("sizeBytes"));

        table.getColumns().addAll(nameCol, statusCol, dateCol, sizeCol);

        Runnable refreshBackup = () -> {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/backups/list"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET().build();
            
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                try {
                    if (res.statusCode() == 200) {
                        List<BackupRecordDto> history = objectMapper.readValue(res.body(), 
                            new com.fasterxml.jackson.core.type.TypeReference<List<BackupRecordDto>>() {});
                        Platform.runLater(() -> table.setItems(FXCollections.observableArrayList(history)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        };

        triggerBtn.setOnAction(e -> {
            String url = String.format("%s/backups/trigger?service=%s&engine=postgres&db=minicloud_db&user=root&pass=pass", 
                API_BASE, serviceBox.getValue());
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + jwtToken)
                .POST(HttpRequest.BodyPublishers.noBody()).build();
            
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                Platform.runLater(refreshBackup);
            });
        });

        refreshBackup.run();
        layout.getChildren().addAll(header, actions, table);
        container.getChildren().add(layout);
    }
}
