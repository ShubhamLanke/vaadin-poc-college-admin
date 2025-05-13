package com.dss.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.Route;

public class MainView extends VerticalLayout {

    public MainView() {
        setSizeFull();
        setHorizontalComponentAlignment(Alignment.CENTER);

        Tab overviewTab = new Tab("Overview");
        Tab adminTab = new Tab("Admin");

        Tabs tabs = new Tabs(overviewTab, adminTab);
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);

        tabs.addSelectedChangeListener(event -> {
            Tab selected = event.getSelectedTab();

            if (selected.equals(overviewTab)) {
                UI.getCurrent().navigate("overview");
            } else if (selected.equals(adminTab)) {
                UI.getCurrent().navigate("admin");
            }
        });

        H2 title = new H2("The Main View Page.");
        add(title, tabs);
    }
}
