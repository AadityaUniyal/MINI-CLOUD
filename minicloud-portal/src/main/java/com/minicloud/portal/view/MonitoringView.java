package com.minicloud.portal.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * CloudWatch-style Monitoring View.
 */
@Route(value = "monitoring", layout = MainView.class)
@PageTitle("Monitoring | MiniCloud Console")
public class MonitoringView extends VerticalLayout {

    public MonitoringView() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        add(new H2("CloudWatch Monitoring"));

        HorizontalLayout metrics = new HorizontalLayout();
        metrics.setWidthFull();
        metrics.add(
            createMetricCard("CPU Utilization (Avg)", "14.2%", "#22c55e"),
            createMetricCard("Memory Usage (Total)", "12.4 GB", "#3b82f6"),
            createMetricCard("Network In (Last 24h)", "4.8 GB", "#a855f7"),
            createMetricCard("Active Connections", "1,244", "#f59e0b")
        );
        add(metrics);

        Div graphPlaceholder = new Div();
        graphPlaceholder.getStyle()
                .set("background", "var(--aws-card-bg)")
                .set("border", "1px solid var(--aws-border)")
                .set("border-radius", "8px")
                .set("height", "400px")
                .set("width", "100%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("color", "var(--aws-text-sub)")
                .set("font-style", "italic");
        graphPlaceholder.setText("[ Live Metric Visualization Graph Layer ]");
        
        add(graphPlaceholder);
    }

    private Div createMetricCard(String label, String value, String accentColor) {
        Div card = new Div();
        card.addClassName("summary-card");
        card.setWidth("25%");
        card.getStyle().set("border-left", "4px solid " + accentColor);

        Span lSpan = new Span(label);
        lSpan.addClassName("card-title");

        Div vDiv = new Div(new Span(value));
        vDiv.addClassName("card-value");
        vDiv.getStyle().set("font-size", "1.5rem");

        card.add(lSpan, vDiv);
        return card;
    }
}
