package com.minicloud.portal.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Technical Console View showing real-time system logs.
 */
@Route(value = "console", layout = MainView.class)
@PageTitle("System Console | MiniCloud")
public class ConsoleView extends VerticalLayout {

    private final Div consoleOutput = new Div();

    public ConsoleView() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        add(new H2("System Technical Console"));

        consoleOutput.addClassName("console-container");
        consoleOutput.setWidthFull();
        consoleOutput.setHeight("500px");
        
        add(consoleOutput);

        // Simulate some initial logs
        log("INFO", "Initializing MiniCloud Environment...");
        log("INFO", "Connecting to Eureka Discovery Service...");
        log("SUCCESS", "Registered with Eureka at http://minicloud-eureka:8761");
        log("INFO", "Starting IAM Service authorization hooks...");
        log("INFO", "Syncing S3 Buckets from MinIO storage...");
        log("WARN", "Compute instance 'inst-552' reported high CPU usage (88%)");
    }

    private void log(String level, String message) {
        Div line = new Div();
        line.addClassName("console-line");

        Span timestamp = new Span("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "]");
        timestamp.addClassName("console-timestamp");

        Span levelSpan = new Span(" " + level + " ");
        levelSpan.getStyle().set("font-weight", "bold");
        if (level.equals("ERROR")) levelSpan.getStyle().set("color", "#ff4444");
        else if (level.equals("SUCCESS")) levelSpan.getStyle().set("color", "#44ff44");
        else if (level.equals("WARN")) levelSpan.getStyle().set("color", "#ffff44");
        else levelSpan.getStyle().set("color", "#4444ff");

        Span msg = new Span(": " + message);
        
        line.add(timestamp, levelSpan, msg);
        consoleOutput.add(line);
    }
}
