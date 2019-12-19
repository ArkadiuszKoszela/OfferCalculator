package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.Windows;
import pl.koszela.spring.repositories.WindowsRepository;
import pl.koszela.spring.service.GridInteraface;

import java.util.*;
import java.util.stream.Collectors;

@Route(value = WindowsView.WINDOWS, layout = MainView.class)
public class WindowsView extends VerticalLayout implements GridInteraface<Windows>, BeforeLeaveObserver {

    static final String WINDOWS = "windows";

    private WindowsRepository windowsRepository;
    private Optional<Set<Windows>> optionalWindows = Optional.ofNullable((Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windows"));
    private Set<Windows> setWindows = optionalWindows.orElse(new HashSet<>());

    private TreeGrid<Windows> treeGrid = new TreeGrid<>();
    private Binder<Windows> binder;

    @Autowired
    public WindowsView(WindowsRepository windowsRepository) {
        this.windowsRepository = Objects.requireNonNull(windowsRepository);

        setWindows = allWindows();
        add(createGrida());
    }

    public TreeGrid<Windows> createGrida() {
        treeGrid.addHierarchyColumn(Windows::getName).setHeader("Nazwa");
        GridInteraface.super.createGridd(treeGrid, binder);
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(new ArrayList())));
        return treeGrid;
    }

    public TreeData<Windows> addItems(List list) {
        TreeData<Windows> treeData = new TreeData<>();
        Set<Windows> collect = setWindows.stream().filter(e -> e.getSize().equals("55x78")).collect(Collectors.toSet());
        for (Windows parent : collect) {
            List<Windows> childrens = setWindows.stream().filter(e -> !e.getSize().equals("55x78")).collect(Collectors.toList());
            for (int i = 0; i < childrens.size(); i++) {
                if (i == 0) {
                    treeData.addItem(null, parent);
                } else if (childrens.get(i).getName().equals(parent.getName())) {
                    treeData.addItem(parent, childrens.get(i));
                }
            }
        }
        return treeData;
    }

    private Set<Windows> allWindows() {
        return new HashSet<>(windowsRepository.findAll());
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        Set<Windows> trueOffer = new HashSet<>();
        for (Windows windows : setWindows) {
            if (windows.isOffer()) {
                trueOffer.add(windows);
            }
        }
        VaadinSession.getCurrent().getSession().setAttribute("windows", setWindows);
        VaadinSession.getCurrent().getSession().setAttribute("windowsAfterChoose", trueOffer);
        action.proceed();
    }
}