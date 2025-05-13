package com.dss.view;

import com.dss.common.base.BaseView;
import com.dss.model.College;
import com.dss.model.Student;
import com.dss.presenter.OverviewPresenter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Route(value = "overview", layout = MainLayout.class)
@PageTitle("College Overview")
public class OverviewView extends BaseView<OverviewPresenter> {

    private TreeGrid<Object> treeGrid = new TreeGrid<>();

    @Override
    public void init() {
        setSizeFull();
        add(new H2("Overview"));

        treeGrid.addHierarchyColumn(item -> {
            if (item instanceof College) {
                return ((College) item).getName();
            } else if (item instanceof Student) {
                return "↳ " + ((Student) item).getName();
            }
            return "";
        }).setHeader("Name");

        treeGrid.addColumn(item -> {
            if (item instanceof College) {
                return ((College) item).getCity();
            } else if (item instanceof Student) {
                return ((Student) item).getEmail();
            }
            return "";
        }).setHeader("Details");

        treeGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        add(treeGrid);

        getPresenter().loadCollegeHierarchy();
    }

    public void showCollegeTree(Map<College, List<Student>> data) {
        treeGrid.setItems(
                new ArrayList<>(data.keySet()),
                college -> (Collection<Object>) (Collection<?>) data.getOrDefault(college, List.of())
        );
    }

}