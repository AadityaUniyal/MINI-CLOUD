package com.minicloud.portal.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("MiniCloud | User Portal")
public class MainView extends AppLayout {

    public MainView() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("MiniCloud");
        logo.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        Tabs tabs = new Tabs(
                createTab(VaadinIcon.DASHBOARD, "Dashboard"),
                createTab(VaadinIcon.SERVER, "Instances"),
                createTab(VaadinIcon.DATABASE, "Databases"),
                createTab(VaadinIcon.STORAGE, "Buckets"),
                createTab(VaadinIcon.CHART, "Monitoring"),
                createTab(VaadinIcon.CREDIT_CARD, "Billing")
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
    }

    private Tab createTab(VaadinIcon icon, String label) {
        Tab tab = new Tab();
        HorizontalLayout layout = new HorizontalLayout(icon.create(), new Span(label));
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        tab.add(layout);
        return tab;
    }
}
