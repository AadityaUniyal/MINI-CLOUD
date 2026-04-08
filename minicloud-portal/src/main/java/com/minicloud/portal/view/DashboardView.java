package com.minicloud.portal.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Premium Dashboard View with Resource Summaries and Physical Status Display.
 */
@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Dashboard | MiniCloud Console")
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setSpacing(true);
        setPadding(true);
        addClassName("dashboard-view");

        add(new H2("Cloud Overview"));

        // Resource Summary Cards
        HorizontalLayout summaryLayout = new HorizontalLayout();
        summaryLayout.setWidthFull();
        summaryLayout.add(
            createSummaryCard("Compute Instances", "12", "Active", "status-up"),
            createSummaryCard("RDS Databases", "4", "Healthy", "status-up"),
            createSummaryCard("S3 Buckets", "8", "Online", "status-up"),
            createSummaryCard("Monthly Spend", "$142.50", "Budget: $200", "")
        );
        add(summaryLayout);

        // Physical Working Project Display (Microservices Health)
        add(new H2("System Physical Status"));
        Div healthGrid = new Div();
        healthGrid.getStyle().set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fill, minmax(200px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%");

        healthGrid.add(
            createHealthBadge("API Gateway"),
            createHealthBadge("IAM Service"),
            createHealthBadge("Compute Engine"),
            createHealthBadge("Storage Service"),
            createHealthBadge("Database Service"),
            createHealthBadge("Eureka Cluster"),
            createHealthBadge("Monitoring Hub"),
            createHealthBadge("Billing Engine"),
            createHealthBadge("Notification Bus")
        );
        add(healthGrid);
    }

    private Div createSummaryCard(String title, String value, String subtitle, String statusClass) {
        Div card = new Div();
        card.addClassName("summary-card");
        card.setWidth("25%");

        Span titleSpan = new Span(title);
        titleSpan.addClassName("card-title");

        Div valueDiv = new Div(new Span(value));
        valueDiv.addClassName("card-value");

        Span subSpan = new Span(subtitle);
        subSpan.getStyle().set("font-size", "0.8rem");
        if (!statusClass.isEmpty()) {
            subSpan.addClassNames("status-badge", statusClass);
        } else {
            subSpan.getStyle().set("color", "var(--aws-text-sub)");
        }

        card.add(titleSpan, valueDiv, subSpan);
        return card;
    }

    private Div createHealthBadge(String serviceName) {
        Div container = new Div();
        container.getStyle()
                .set("background", "var(--aws-card-bg)")
                .set("border", "1px solid var(--aws-border)")
                .set("border-radius", "8px")
                .set("padding", "1rem")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "space-between");

        Span name = new Span(serviceName);
        name.getStyle().set("font-weight", "600").set("font-size", "0.9rem");

        Span dot = new Span();
        dot.getStyle()
                .set("width", "10px")
                .set("height", "10px")
                .set("background-color", "#22c55e")
                .set("border-radius", "50%")
                .set("box-shadow", "0 0 8px #22c55e");

        container.add(name, dot);
        return container;
    }
}
