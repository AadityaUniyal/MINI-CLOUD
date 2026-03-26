package com.minicloud.ui;

import com.minicloud.MiniCloudApplication;
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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class MiniCloudFxApp extends Application {

    private ConfigurableApplicationContext springContext;
    private Stage primaryStage;
    private String jwtToken;
    private String username;
    private String accountId;
    private String currentTab = "Compute";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String API_BASE = "http://localhost:8080/api";
    private VBox detailsPane;
    private javafx.animation.Timeline autoRefreshTimeline;

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
        String css = getClass().getResource("/style.css").toExternalForm();
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

        Label title = new Label("aws");
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

        Hyperlink regLink = new Hyperlink("Create a new AWS account");
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

        Label title = new Label("Create a new AWS account");
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
        aliasField.setPromptText("AWS account alias");
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
                        .uri(URI.create(API_BASE + "/auth/register-aws"))
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
                    .uri(URI.create(API_BASE + "/auth/login-aws"))
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

        // --- AWS Header ---
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 20, 0, 20));
        header.getStyleClass().add("aws-header");
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

        Label awsLogo = new Label("aws");
        awsLogo.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        awsLogo.setTextFill(Color.WHITE);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search services, features, blogs...");
        searchBar.setPrefWidth(450);
        searchBar.getStyleClass().add("aws-search-bar");

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

        header.getChildren().addAll(awsLogo, servicesBox, searchBar, spacerH, bellIcon, regionSelector, userLabel, logoutBtn);
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

        TreeItem<String> s3 = new TreeItem<>("\u1F4E6 S3");
        s3.getChildren().addAll(new TreeItem<>("Buckets"));
        
        TreeItem<String> vpc = new TreeItem<>("\uD83D\uDDA5 VPC"); // Monitor icon
        vpc.getChildren().addAll(new TreeItem<>("Your VPCs"), new TreeItem<>("Subnets"), new TreeItem<>("Security Groups"));

        TreeItem<String> route53 = new TreeItem<>("\uD83C\uDF10 Route 53"); // Globe icon
        route53.getChildren().addAll(new TreeItem<>("Hosted Zones"));

        TreeItem<String> cf = new TreeItem<>("\uD83D\uDCDC CloudFormation"); // Scroll icon
        cf.getChildren().addAll(new TreeItem<>("Stacks"));

        TreeItem<String> security = new TreeItem<>("\uD83D\uDEE1 Security"); // Shield icon
        security.getChildren().addAll(new TreeItem<>("GuardDuty Findings"), new TreeItem<>("WAF Rules"), new TreeItem<>("IAM Groups"));

        TreeItem<String> billing = new TreeItem<>("\u1F4B0 Billing");
        billing.getChildren().addAll(new TreeItem<>("Cost Explorer"));

        TreeItem<String> account = new TreeItem<>("\u1F4B3 My Account");

        navRoot.getChildren().addAll(home, ec2, rds, s3, vpc, route53, cf, security, billing, account);
        navTree.setRoot(navRoot);
        navTree.setShowRoot(false);
        navTree.getStyleClass().add("aws-tree");
        
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
                String cleanVal = val.replaceAll("[^\\w\\s]+", "").trim();
                this.currentTab = cleanVal;
                viewTitle.setText(cleanVal);
                breadcrumbs.setText("Services > " + (newVal.getParent() != null && newVal.getParent() != navRoot ? newVal.getParent().getValue().replaceAll("[^\\w\\s]+", "").trim() + " > " : "") + cleanVal);
                updateMainView(cleanVal, topActionArea, mainContainer);
            }
        });

        // Master-Detail Pane (Collapsible)
        VBox detailsPane = new VBox(10);
        detailsPane.getStyleClass().add("aws-details-pane");
        detailsPane.setVisible(false);
        detailsPane.setManaged(false);
        this.detailsPane = detailsPane; // Store in field

        mainContentWrapper.getChildren().addAll(contentHeader, topActionArea, mainContainer, detailsPane);
        root.setCenter(mainContentWrapper);

        // Services Menu Popover
        servicesBox.setOnMouseClicked(e -> showServicesMenu(servicesBox));

        // Search Bar Logic
        searchBar.textProperty().addListener((obs, old, text) -> {
            filterSidebar(navRoot, text);
        });

        Scene scene = new Scene(root, 1200, 800);
        applyStyles(scene);
        primaryStage.setScene(scene);
        
        navTree.getSelectionModel().select(home);
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
            case "EC2": setupInstancesView(actions, container, refreshBtn); break;
            case "RDS": setupDatabasesView(actions, container, refreshBtn); break;
            case "S3": setupBucketsView(actions, container, refreshBtn); break;
            case "VPC": setupVpcView(actions, container, refreshBtn); break;
            case "Subnets": setupSubnetView(actions, container, refreshBtn); break;
            case "Route 53": setupRoute53View(actions, container, refreshBtn); break;
            case "CloudFormation": setupCloudFormationView(actions, container, refreshBtn); break;
            case "WAF": setupWafView(actions, container, refreshBtn); break;
            case "Security Hub": setupGuardDutyView(actions, container, refreshBtn); break;
            case "IAM": setupUsersView(actions, container, refreshBtn); break;
            case "Billing": setupBillingView(actions, container, refreshBtn); break;
            case "My Account": setupAccountView(container); break;
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

        // Fetch counts
        fetchList("/compute/list", res -> ec2Count.setText("EC2 Instances: " + (res.split("\\{").length-1)));
        fetchList("/storage", res -> s3Count.setText("S3 Buckets: " + (res.split("\\{").length-1)));
        fetchList("/database/instances", res -> rdsCount.setText("RDS Databases: " + (res.split("\\{").length-1)));
        fetchList("/firewall/vpcs", res -> vpcCount.setText("VPCs: " + (res.split("\\{").length-1)));

        // Widget 2: Security Health
        VBox securityStatus = createWidget("Security Health", "GuardDuty & WAF findings.");
        Label findingsLabel = new Label("Findings: Loading...");
        securityStatus.getChildren().add(findingsLabel);
        fetchList("/security/findings", res -> {
            int count = res.split("\\{").length - 1;
            findingsLabel.setText(count + " Security Findings detected");
            findingsLabel.setTextFill(count > 0 ? Color.RED : Color.GREEN);
        });

        // Widget 3: AWS Health
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

        container.getChildren().add(grid);
    }

    private VBox createWidget(String title, String subtitle) {
        VBox card = new VBox(10);
        card.getStyleClass().add("aws-card");
        card.setMinWidth(400);
        card.setMinHeight(200);

        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("aws-card-title");
        
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
        container.getChildren().add(table);

        Button terminateBtn = new Button("Terminate Instance");
        styleActionBtn(terminateBtn, false);
        terminateBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        
        Runnable refresh = () -> {
            fetchList("/compute/list", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) {
                    if (item.contains("\"id\"")) {
                        table.getItems().add(new InstanceRow(
                            getJsonValue(item, "name"),
                            getJsonValue(item, "id"),
                            getJsonValue(item, "status"),
                            getJsonValue(item, "instanceType"),
                            getJsonValue(item, "publicIp")
                        ));
                    }
                }
            });
        };

        terminateBtn.setOnAction(e -> handleTerminateEC2(table.getSelectionModel().getSelectedItem(), refresh));
        actions.getChildren().add(1, terminateBtn);

        refreshBtn.setOnAction(e -> refresh.run());
        styleActionBtn(refreshBtn, false);
        refresh.run();
        addAutoRefreshToggle(actions, refresh);
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
        container.getChildren().add(table);

        Button deleteBtn = new Button("Delete Database");
        styleActionBtn(deleteBtn, false);
        deleteBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        Runnable refresh = () -> {
            fetchList("/database/instances", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) {
                    if (item.contains("\"name\"")) {
                        table.getItems().add(new DatabaseRow(
                            getJsonValue(item, "id"),
                            getJsonValue(item, "engine"),
                            getJsonValue(item, "status"),
                            getJsonValue(item, "dbInstanceClass")
                        ));
                    }
                }
            });
        };
        
        deleteBtn.setOnAction(e -> handleDeleteRDS(table.getSelectionModel().getSelectedItem(), refresh));
        actions.getChildren().add(1, deleteBtn);

        refreshBtn.setOnAction(e -> refresh.run());
        styleActionBtn(refreshBtn, false);
        refresh.run();
        addAutoRefreshToggle(actions, refresh);
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
        
        TableColumn<BucketRow, String> regionCol = new TableColumn<>("AWS Region");
        regionCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().region));
        
        TableColumn<BucketRow, String> accessCol = new TableColumn<>("Access");
        accessCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().access));

        table.getColumns().addAll(nameCol, regionCol, accessCol);
        
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
        container.getChildren().add(table);

        Button deleteBtn = new Button("Delete Bucket");
        styleActionBtn(deleteBtn, false);
        deleteBtn.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        Runnable refresh = () -> {
            fetchList("/buckets", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) {
                    if (item.contains("\"name\"")) {
                        table.getItems().add(new BucketRow(
                            getJsonValue(item, "name"),
                            getJsonValue(item, "region"),
                            getJsonValue(item, "accessControl")
                        ));
                    }
                }
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
            TextInputDialog d = new TextInputDialog("new-user");
            d.setTitle("Create IAM User");
            d.setHeaderText("Specify IAM Username");
            d.showAndWait().ifPresent(name -> {
                HttpRequest postReq = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/auth/iam/create?iamUsername=" + name + "&password=password123"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .POST(HttpRequest.BodyPublishers.noBody()).build();
                httpClient.sendAsync(postReq, HttpResponse.BodyHandlers.ofString()).thenAccept(r -> {
                    Platform.runLater(refreshUsers);
                });
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
        summaryHeader.getStyleClass().add("aws-card-title");
        
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
        container.getChildren().add(table);
        Runnable refresh = () -> {
            fetchList("/vpc/list", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) {
                    if (item.contains("\"id\"")) {
                        table.getItems().add(new VpcRow(
                            getJsonValue(item, "id"),
                            getJsonValue(item, "name"),
                            getJsonValue(item, "cidrBlock"),
                            getJsonValue(item, "state")
                        ));
                    }
                }
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
        container.getChildren().add(table);
        Runnable refresh = () -> {
            fetchList("/dns/zones", res -> {
                table.getItems().clear();
                for (String item : res.split("\\{")) if (item.contains("\"name\"")) {
                    table.getItems().add(new DnsRow(getJsonValue(item, "id"), getJsonValue(item, "name"), "Public", ""));
                }
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
    public static class InstanceRow { public String name, id, state, type, publicIp; public InstanceRow(String n, String i, String s, String t, String ip) { name=n; id=i; state=s; type=t; publicIp=ip; } }
    public static class DatabaseRow { public String id, engine, status, dbClass; public DatabaseRow(String i, String e, String s, String c) { id=i; engine=e; status=s; dbClass=c; } }
    public static class BucketRow { public String name, region, access; public BucketRow(String n, String r, String a) { name=n; region=r; access=a; } }
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
        TextInputDialog dialog = new TextInputDialog("my-instance");
        dialog.setTitle("Launch EC2 Instance");
        dialog.setHeaderText("Specify Instance Name");
        dialog.showAndWait().ifPresent(name -> {
            String body = String.format("{\"instanceName\":\"%s\", \"instanceType\":\"t2.micro\", \"vpcId\":\"vpc-default\", \"subnetId\":\"subnet-default\"}", name);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/compute/launch"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                if (onComplete != null) Platform.runLater(onComplete);
            });
        });
    }

    private void handleCreateRDS(Runnable onComplete) {
        TextInputDialog nameDialog = new TextInputDialog("my-db");
        nameDialog.setTitle("Create RDS Database");
        nameDialog.setHeaderText("Database Name");
        nameDialog.showAndWait().ifPresent(name -> {
            String body = String.format("{\"name\":\"%s\", \"dbName\":\"%s_db\", \"rootPassword\":\"password\", \"engine\":\"mysql\", \"vpcId\":\"vpc-default\", \"subnetId\":\"subnet-default\"}", name, name);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/database/provision"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                if (onComplete != null) Platform.runLater(onComplete);
            });
        });
    }

    private void handleCreateS3(Runnable onComplete) {
        TextInputDialog dialog = new TextInputDialog("my-bucket");
        dialog.setTitle("Create S3 Bucket");
        dialog.setHeaderText("Bucket Name");
        dialog.showAndWait().ifPresent(name -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE + "/buckets?name=" + name))
                    .header("Authorization", "Bearer " + jwtToken)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                if (onComplete != null) Platform.runLater(onComplete);
            });
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
        dialog.setTitle("Instance Details - AWS Simulation");
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
            String[] sparts = selectedItem.split(" \\| ");
            header.setText("S3 Bucket: " + sparts[0]);
            addGridRow(grid, 0, "Region:", sparts[1]);
            addGridRow(grid, 1, "Access:", sparts[2]);
            addGridRow(grid, 2, "ARN:", "arn:aws:s3:::" + sparts[0]);
        }

        box.getChildren().addAll(header, new Separator(), grid);
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
            ObjectMapper mapper = new ObjectMapper();
            // Wrap in braces if it's a snippet from a split
            String cleanJson = json.trim();
            if (!cleanJson.startsWith("{")) cleanJson = "{" + cleanJson;
            if (!cleanJson.endsWith("}")) cleanJson = cleanJson + "}";
            
            JsonNode root = mapper.readTree(cleanJson);
            JsonNode node = root.get(key);
            return node != null ? node.asText() : "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    private void addAutoRefreshToggle(HBox actions, Runnable refreshTask) {
        CheckBox cb = new CheckBox("Auto-refresh");
        cb.getStyleClass().add("aws-label");
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
        layout.getStyleClass().add("aws-card");

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

    private void showBucketExplorer(String bucketName) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("S3 Bucket Explorer: " + bucketName);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        TableView<String[]> fileTable = new TableView<>();
        fileTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<String[], String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue()[0]));
        TableColumn<String[], String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue()[1]));
        fileTable.getColumns().addAll(nameCol, sizeCol);

        Runnable refreshFiles = () -> {
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE + "/buckets/" + bucketName + "/files"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET().build();
            httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
                Platform.runLater(() -> {
                    fileTable.getItems().clear();
                    if (res.statusCode() == 200) {
                        for (String line : res.body().split("\\{")) {
                            if (line.contains("\"fileName\"")) {
                                String fname = getJsonValue(line, "fileName");
                                String fsize = getJsonValue(line, "size");
                                fileTable.getItems().add(new String[]{fname, fsize + " bytes"});
                            }
                        }
                    }
                });
            });
        };

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> refreshFiles.run());
        
        Button uploadBtn = new Button("Upload File (Simulated)");
        styleActionBtn(uploadBtn, true);
        uploadBtn.setOnAction(e -> {
            new Alert(Alert.AlertType.INFORMATION, "File upload simulated for bucket: " + bucketName).show();
        });

        layout.getChildren().addAll(new Label("Files in " + bucketName), fileTable, new HBox(10, refreshBtn, uploadBtn));
        stage.setScene(new Scene(layout, 500, 400));
        refreshFiles.run();
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

    public static void main(String[] args) {
        launch(args);
    }
}
