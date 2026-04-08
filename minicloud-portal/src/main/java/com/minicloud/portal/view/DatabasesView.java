package com.minicloud.portal.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * RDS-style Database Management View.
 */
@Route(value = "databases", layout = MainView.class)
@PageTitle("Databases | MiniCloud Console")
public class DatabasesView extends VerticalLayout {

    public DatabasesView() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        add(new H2("Relational Database Service (RDS)"));

        Grid<DbData> grid = new Grid<>(DbData.class, false);
        grid.addColumn(DbData::getDbInstanceId).setHeader("DB Instance").setAutoWidth(true);
        grid.addComponentColumn(this::createStatusBadge).setHeader("Status").setAutoWidth(true);
        grid.addColumn(DbData::getEngine).setHeader("Engine").setAutoWidth(true);
        grid.addColumn(DbData::getEndpoint).setHeader("Endpoint").setAutoWidth(true);
        grid.addColumn(DbData::getClassType).setHeader("Class").setAutoWidth(true);

        grid.setItems(
            new DbData("minicloud-prod-db", "Available", "PostgreSQL 14.7", "prod.cluster.minicloud.io", "db.t3.large"),
            new DbData("user-auth-db", "Available", "MySQL 8.0.28", "auth.minicloud.io", "db.t3.micro"),
            new DbData("analytics-replica", "Backing up", "PostgreSQL 14.7", "analytics.minicloud.io", "db.m5.xlarge")
        );

        add(grid);
    }

    private Span createStatusBadge(DbData db) {
        Span badge = new Span(db.getStatus());
        badge.addClassNames("status-badge");
        if (db.getStatus().equals("Available")) {
            badge.addClassName("status-up");
        } else {
            badge.getStyle().set("background-color", "#dbeafe").set("color", "#1e40af");
        }
        return badge;
    }

    @Data
    @AllArgsConstructor
    public static class DbData {
        private String dbInstanceId;
        private String status;
        private String engine;
        private String endpoint;
        private String classType;
    }
}
