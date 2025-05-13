package com.dss.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class MainLayout extends AppLayout {

    public MainLayout() {
        Tab overviewTab = new Tab(new RouterLink("Overview", OverviewView.class));
        Tab adminTab = new Tab(new RouterLink("Admin", AdminView.class));

        Tabs tabs = new Tabs(overviewTab, adminTab);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        addToNavbar(tabs);
    }
}
