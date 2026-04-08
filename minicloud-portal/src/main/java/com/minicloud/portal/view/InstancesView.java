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
 * EC2-style Instances Management View.
 */
@Route(value = "instances", layout = MainView.class)
@PageTitle("Instances | MiniCloud Console")
public class InstancesView extends VerticalLayout {

    public InstancesView() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        add(new H2("Compute Instances (EC2)"));

        Grid<InstanceData> grid = new Grid<>(InstanceData.class, false);
        grid.addColumn(InstanceData::getInstanceId).setHeader("Instance ID").setAutoWidth(true);
        grid.addComponentColumn(this::createStatusBadge).setHeader("Status").setAutoWidth(true);
        grid.addColumn(InstanceData::getType).setHeader("Type").setAutoWidth(true);
        grid.addColumn(InstanceData::getPublicIp).setHeader("Public IP").setAutoWidth(true);
        grid.addColumn(InstanceData::getRegion).setHeader("Region").setAutoWidth(true);

        grid.setItems(
            new InstanceData("i-0a823f4b", "Running", "t3.medium", "54.210.12.88", "us-east-1a"),
            new InstanceData("i-0912bc31", "Running", "t3.micro", "3.88.192.121", "us-east-1b"),
            new InstanceData("i-0c12345a", "Stopped", "m5.large", "-", "us-east-1a"),
            new InstanceData("i-0777bbcc", "Running", "t2.small", "34.201.44.10", "us-east-1c")
        );

        add(grid);
    }

    private Span createStatusBadge(InstanceData instance) {
        Span badge = new Span(instance.getStatus());
        badge.addClassNames("status-badge");
        if (instance.getStatus().equals("Running")) {
            badge.addClassName("status-up");
        } else {
            badge.addClassName("status-down");
        }
        return badge;
    }

    @Data
    @AllArgsConstructor
    public static class InstanceData {
        private String instanceId;
        private String status;
        private String type;
        private String publicIp;
        private String region;
    }
}
