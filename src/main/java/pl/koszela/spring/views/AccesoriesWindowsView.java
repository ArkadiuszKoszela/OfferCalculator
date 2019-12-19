package pl.koszela.spring.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.AccessoriesWindows;
import pl.koszela.spring.entities.main.Windows;
import pl.koszela.spring.repositories.AccesoriesWindowsRepository;
import pl.koszela.spring.service.GridInteraface;

import java.util.*;

@Route(value = AccesoriesWindowsView.ACCESORIES_WINDOWS, layout = MainView.class)
public class AccesoriesWindowsView extends VerticalLayout implements GridInteraface<AccessoriesWindows>, BeforeLeaveObserver {
    static final String ACCESORIES_WINDOWS = "accesoriesWindows";

    private AccesoriesWindowsRepository accesoriesWindowsRepository;

    private Grid<AccessoriesWindows> treeGrid = new Grid<>();
    private Optional<Set<AccessoriesWindows>> optionalAccessoriesWindows = Optional.ofNullable((Set<AccessoriesWindows>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesWindows"));
    private Set<AccessoriesWindows> setAccessoriesWindows = optionalAccessoriesWindows.orElse(new HashSet<>());
    private Optional<Set<Windows>> optionalWindows = Optional.ofNullable((Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose"));
    private Set<Windows> setWindows = optionalWindows.orElse(new HashSet<>());
    private Binder<AccessoriesWindows> binder;

    @Autowired
    public AccesoriesWindowsView(AccesoriesWindowsRepository accesoriesWindowsRepository) {
        this.accesoriesWindowsRepository = Objects.requireNonNull(accesoriesWindowsRepository);

        List<AccessoriesWindows> all = accesoriesWindowsRepository.findAll();
        for (AccessoriesWindows accessoriesWindows : all) {
            for (Windows windows : setWindows) {
                if (windows.getSize().equals(accessoriesWindows.getSize()) && windows.getManufacturer().equals(accessoriesWindows.getManufacturer())) {
                    setAccessoriesWindows.add(accessoriesWindows);
                }
            }
        }
        add(createGrida());
    }

    public Grid<AccessoriesWindows> createGrida() {
        treeGrid.addColumn(AccessoriesWindows::getName).setHeader("Nazwa");
        GridInteraface.super.createGridd(treeGrid, binder);
        treeGrid.setDataProvider(new ListDataProvider<>(setAccessoriesWindows));
        return treeGrid;
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("accesoriesWindows", setAccessoriesWindows);
        action.proceed();
    }
}
