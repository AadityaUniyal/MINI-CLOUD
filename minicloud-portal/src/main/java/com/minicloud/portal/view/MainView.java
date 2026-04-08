package com.minicloud.portal.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

/**
 * Enhanced MainView with AWS Console Aesthetics.
 */
@Route("")
@PageTitle("MiniCloud | Management Console")
@StyleSheet("context://styles/minicloud-theme.css")
public class MainView extends AppLayout {

    public MainView() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1();
        logo.addClassName("minicloud-logo");
        
        Span mini = new Span("Mini");
        Span cloud = new Span("Cloud");
        cloud.getStyle().set("color", "var(--aws-orange)");
        
        logo.add(mini, cloud);
        logo.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.addClassName("minicloud-header");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");
        
        addToNavbar(header);
    }

    private void createDrawer() {
        Tabs tabs = new Tabs(
                createNavTab(VaadinIcon.DASHBOARD, "Dashboard",   DashboardView.class),
                createNavTab(VaadinIcon.TERMINAL,  "System Console", ConsoleView.class),
                createNavTab(VaadinIcon.SERVER,    "Instances",   InstancesView.class),
                createNavTab(VaadinIcon.DATABASE,  "Databases",   DatabasesView.class),
                createNavTab(VaadinIcon.STORAGE,   "Buckets",     BucketsView.class),
                createNavTab(VaadinIcon.CHART,     "Monitoring",  MonitoringView.class),
                createNavTab(VaadinIcon.CREDIT_CARD, "Billing",   BillingView.class)
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
    }

    private <T extends com.vaadin.flow.component.Component> Tab createNavTab(
            VaadinIcon icon, String label, Class<T> target) {
        RouterLink link = new RouterLink();
        link.add(icon.create(), new Span(label));
        link.setRoute(target);
        link.getStyle().set("display", "flex").set("align-items", "center").set("gap", "0.5em");
        return new Tab(link);
    }
}
