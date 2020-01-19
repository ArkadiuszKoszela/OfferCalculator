package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.main.Accessories;
import pl.koszela.spring.entities.main.Collar;
import pl.koszela.spring.entities.main.Windows;
import pl.koszela.spring.repositories.CollarRepository;
import pl.koszela.spring.service.GridInteraface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Route(value = CollarView.COLLAR, layout = MainView.class)
public class CollarView extends VerticalLayout implements GridInteraface<Collar>, BeforeLeaveObserver {
    static final String COLLAR = "collar";

    private CollarRepository collarRepository;

    private Grid<Collar> grid = new Grid<>();
    private Optional<Set<Collar>> optionalCollars = Optional.ofNullable((Set<Collar>) VaadinSession.getCurrent().getSession().getAttribute("collar"));
    private Set<Collar> setCollars = optionalCollars.orElse(new HashSet<>());
    private Optional<Set<Windows>> optionalWindows = Optional.ofNullable((Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windows"));
    private Set<Windows> setWindows = optionalWindows.orElse(new HashSet<>());
    private Binder<Collar> binder;

    @Autowired
    public CollarView(CollarRepository collarRepository) {
        this.collarRepository = Objects.requireNonNull(collarRepository);

        List<Collar> all = collarRepository.findAll();
        for (Collar collar : all) {
            for (Windows windows : setWindows) {
                if (windows.getSize().equals(collar.getSize()) && windows.getManufacturer().equals(collar.getManufacturer())) {
                    if(windows.isOffer()){
                        setCollars.add(collar);
                    }
                }
            }
        }
        add(createGrida());
    }

    public Grid<Collar> createGrida() {
        grid.addColumn(Collar::getName).setHeader("Nazwa");
        GridInteraface.super.createGridd(grid, binder);
        grid.setDataProvider(new ListDataProvider<>(setCollars));
        return grid;
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("collar", setCollars);
        action.proceed();
    }
}
