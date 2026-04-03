module com.minicloud.admin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires spring.context;
    requires spring.web;
    requires spring.beans;
    requires spring.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires java.management;
    requires jdk.management;
    requires static lombok;
    requires minicloud.common; // This will be an automatic module

    opens com.minicloud.admin to javafx.fxml, spring.core;
    opens com.minicloud.admin.controller to javafx.fxml, spring.core;
    
    exports com.minicloud.admin;
}
