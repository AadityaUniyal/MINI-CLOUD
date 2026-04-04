package com.minicloud.admin;

import com.formdev.flatlaf.FlatDarkLaf;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import atlantafx.base.theme.PrimerDark;
import com.minicloud.admin.MiniCloudApplication;

import javax.swing.UIManager;

public class App extends Application {
    private ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        // Apply JavaFX Theme
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        
        // Apply Swing Theme (FlatLaf)
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        springContext = new SpringApplicationBuilder(MiniCloudApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        primaryStage.setTitle("MiniCloud Console");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
