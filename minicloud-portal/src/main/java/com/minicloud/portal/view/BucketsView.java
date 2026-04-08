package com.minicloud.portal.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * S3-style Object Storage Management View.
 */
@Route(value = "buckets", layout = MainView.class)
@PageTitle("Buckets | MiniCloud Console")
public class BucketsView extends VerticalLayout {

    public BucketsView() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        add(new H2("Object Storage (S3)"));

        Grid<BucketData> grid = new Grid<>(BucketData.class, false);
        grid.addColumn(BucketData::getName).setHeader("Bucket Name").setAutoWidth(true);
        grid.addColumn(BucketData::getRegion).setHeader("Region").setAutoWidth(true);
        grid.addComponentColumn(this::createAccessBadge).setHeader("Access").setAutoWidth(true);
        grid.addColumn(BucketData::getCreationDate).setHeader("Creation Date").setAutoWidth(true);

        grid.setItems(
            new BucketData("minicloud-assets-prod", "us-east-1", "Private", LocalDate.of(2023, 1, 15)),
            new BucketData("user-uploads-temp", "us-west-2", "Public", LocalDate.of(2023, 5, 20)),
            new BucketData("system-logs-archive", "eu-central-1", "Private", LocalDate.of(2022, 11, 2))
        );

        add(grid);
    }

    private Span createAccessBadge(BucketData bucket) {
        Span badge = new Span(bucket.getAccess());
        badge.addClassNames("status-badge");
        if (bucket.getAccess().equals("Private")) {
            badge.addClassName("status-up");
        } else {
            badge.addClassName("status-down");
            badge.setText("Public ⚠️");
        }
        return badge;
    }

    @Data
    @AllArgsConstructor
    public static class BucketData {
        private String name;
        private String region;
        private String access;
        private LocalDate creationDate;
    }
}
