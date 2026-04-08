package com.minicloud.portal.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Premium Billing & Cost Explorer View.
 */
@Route(value = "billing", layout = MainView.class)
@PageTitle("Billing | MiniCloud Console")
public class BillingView extends VerticalLayout {

    public BillingView() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        add(new H2("Billing and Cost Management"));

        // Billing Summary
        HorizontalLayout costLayout = new HorizontalLayout();
        costLayout.setWidthFull();
        costLayout.add(
            createCostCard("Estimated Costs (MTD)", "$142.50", "status-up"),
            createCostCard("Forecasted (Final)", "$185.20", ""),
            createCostCard("Last Month's Bill", "$167.44", ""),
            createCostCard("Free Tier Usage", "64%", "status-up")
        );
        add(costLayout);

        add(new H2("Recent Invoices"));
        Grid<InvoiceData> grid = new Grid<>(InvoiceData.class, false);
        grid.addColumn(InvoiceData::getDate).setHeader("Invoice Date").setAutoWidth(true);
        grid.addColumn(InvoiceData::getAmount).setHeader("Amount").setAutoWidth(true);
        grid.addComponentColumn(this::createStatusBadge).setHeader("Status").setAutoWidth(true);
        grid.addColumn(InvoiceData::getDownloadLink).setHeader("Action").setAutoWidth(true);

        grid.setItems(
            new InvoiceData("Oct 01, 2023", "$167.44", "Paid", "Download PDF"),
            new InvoiceData("Sep 01, 2023", "$155.10", "Paid", "Download PDF"),
            new InvoiceData("Aug 01, 2023", "$142.05", "Paid", "Download PDF")
        );
        add(grid);
    }

    private Div createCostCard(String label, String value, String statusClass) {
        Div card = new Div();
        card.addClassName("summary-card");
        card.setWidth("25%");

        Span lSpan = new Span(label);
        lSpan.addClassName("card-title");

        Div vDiv = new Div(new Span(value));
        vDiv.addClassName("card-value");

        if (!statusClass.isEmpty()) {
            vDiv.getStyle().set("color", "var(--aws-orange)");
        }

        card.add(lSpan, vDiv);
        return card;
    }

    private Span createStatusBadge(InvoiceData invoice) {
        Span badge = new Span(invoice.getStatus());
        badge.addClassNames("status-badge", "status-up");
        return badge;
    }

    @Data
    @AllArgsConstructor
    public static class InvoiceData {
        private String date;
        private String amount;
        private String status;
        private String downloadLink;
    }
}
