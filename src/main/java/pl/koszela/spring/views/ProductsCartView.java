package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import pl.koszela.spring.entities.main.*;
import pl.koszela.spring.repositories.AccesoriesRepository;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.service.ProductsCartInterface;
import pl.koszela.spring.staticField.TitleNumberFields;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Route(value = ProductsCartView.PRODUCTS_CART, layout = MainView.class)
public class ProductsCartView extends VerticalLayout implements ProductsCartInterface {
    static final String PRODUCTS_CART = "productsCart";

    Optional<List<Gutter>> optionalGutterSet = Optional.ofNullable((List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter"));
    List<Gutter> gutterSet = optionalGutterSet.orElse(new ArrayList<>());
    Optional<Set<Tiles>> optionalTilesSet = Optional.ofNullable((Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles"));
    Set<Tiles> tilesSet = optionalTilesSet.orElse(new HashSet<>());
    Optional<Set<Accessories>> optionalAccessoriesSet = Optional.ofNullable((Set<Accessories>) VaadinSession.getCurrent().getSession().getAttribute("accesories"));
    Set<Accessories> accessoriesSet = optionalAccessoriesSet.orElse(new HashSet<>());
    Optional<Set<Collar>> optionalCollarSet = Optional.ofNullable((Set<Collar>) VaadinSession.getCurrent().getSession().getAttribute("collar"));
    Set<Collar> collarSet = optionalCollarSet.orElse(new HashSet<>());
    Optional<Set<Windows>> optionalWindowsSet = Optional.ofNullable((Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windows"));
    Set<Windows> windowsSet = optionalWindowsSet.orElse(new HashSet<>());
    Optional<Set<AccessoriesWindows>> optionalAccessoriesWindowsSet = Optional.ofNullable((Set<AccessoriesWindows>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesWindows"));
    Set<AccessoriesWindows> accessoriesWindowsSet = optionalAccessoriesWindowsSet.orElse(new HashSet<>());

    public ProductsCartView() {
        Div value = new Div();
        value.setText("KOSZYK");
        add(value);
//        Button button = new Button("Generuj ofertę", event -> );
        add(hm(new ArrayList<>(gutterSet)));
        add(hm(new ArrayList<>(tilesSet)));
        add(hm(new ArrayList<>(accessoriesSet)));
        add(hm(new ArrayList<>(collarSet)));
        add(hm(new ArrayList<>(windowsSet)));
        add(hm(new ArrayList<>(accessoriesWindowsSet)));
    }

}
