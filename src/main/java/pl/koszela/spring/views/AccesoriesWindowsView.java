package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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
import pl.koszela.spring.entities.main.AccessoriesWindows;
import pl.koszela.spring.entities.main.Windows;
import pl.koszela.spring.repositories.AccesoriesWindowsRepository;
import pl.koszela.spring.service.GridInteraface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Route(value = AccesoriesWindowsView.ACCESORIES_WINDOWS, layout = MainView.class)
public class AccesoriesWindowsView extends VerticalLayout implements GridInteraface<AccessoriesWindows>, BeforeLeaveObserver {
    static final String ACCESORIES_WINDOWS = "accesoriesWindows";

    private AccesoriesWindowsRepository accesoriesWindowsRepository;

    private Grid<AccessoriesWindows> treeGrid = new Grid<>();
    private Set<AccessoriesWindows> setAccessoriesWindows = (Set<AccessoriesWindows>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesWindows");
    private Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
    private Binder<AccessoriesWindows> binder;

    @Autowired
    public AccesoriesWindowsView(AccesoriesWindowsRepository accesoriesWindowsRepository) {
        this.accesoriesWindowsRepository = Objects.requireNonNull(accesoriesWindowsRepository);

        if (setAccessoriesWindows == null && setWindows != null) {
            setAccessoriesWindows = new HashSet<>();
            List<AccessoriesWindows> all = accesoriesWindowsRepository.findAll();
            for (AccessoriesWindows accessoriesWindows : all) {
                for (Windows windows : setWindows) {
                    if (windows.getSize().equals(accessoriesWindows.getSize()) && windows.getManufacturer().equals(accessoriesWindows.getManufacturer())) {
                        setAccessoriesWindows.add(accessoriesWindows);
                    }
                }
            }
        }
        add(createGrid());
    }

    public Grid<AccessoriesWindows> createGrid() {
        Grid.Column<AccessoriesWindows> nameColumn = treeGrid.addColumn(AccessoriesWindows::getName).setHeader("Nazwa");
        treeGrid.addColumn(AccessoriesWindows::getSize).setHeader("Rozmiar").setSortable(true);
        treeGrid.addColumn(AccessoriesWindows::getManufacturer).setHeader("Producent");
        Grid.Column<AccessoriesWindows> quantityColumn = treeGrid.addColumn(AccessoriesWindows::getQuantity).setHeader("Ilość");
        Grid.Column<AccessoriesWindows> discountColumn = treeGrid.addColumn(AccessoriesWindows::getDiscount).setHeader("Rabat");
        Grid.Column<AccessoriesWindows> detalPriceColumn = treeGrid.addColumn(AccessoriesWindows::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<AccessoriesWindows> purchasePriceColumn = treeGrid.addColumn(AccessoriesWindows::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<AccessoriesWindows> allPriceDetalColumn = treeGrid.addColumn(AccessoriesWindows::getAllpriceAfterDiscount).setHeader("Razem Detal");
        Grid.Column<AccessoriesWindows> allPricePurchaseColumn = treeGrid.addColumn(AccessoriesWindows::getAllpricePurchase).setHeader("Razem zakup");
        Grid.Column<AccessoriesWindows> profitColumn = treeGrid.addColumn(AccessoriesWindows::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(AccessoriesWindows.class);

        treeGrid.getEditor().setBinder(binder);

        TextField discountEditField= bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), AccessoriesWindows::getDiscount, AccessoriesWindows::setDiscount);
        itemClickListener(treeGrid, discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = bindTextFieldToDouble(binder, new StringToDoubleConverter("Błąd"), AccessoriesWindows::getQuantity, AccessoriesWindows::setQuantity);
        itemClickListener(treeGrid, quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener(treeGrid, binder);

        readBeans(binder);

        if (setAccessoriesWindows != null) {
            treeGrid.setDataProvider(new ListDataProvider<>(setAccessoriesWindows));
        }

        settingsGrid(treeGrid);
        return treeGrid;
    }

    @Override
    public TreeData<AccessoriesWindows> addItems(List list) {
        return null;
    }

    @Override
    public ComponentRenderer<VerticalLayout, AccessoriesWindows> createComponent() {
        return new ComponentRenderer<>(accessoriesWindows -> {
            Checkbox mainCheckBox = new Checkbox("Dodać ?");
            mainCheckBox.setValue(accessoriesWindows.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                accessoriesWindows.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    }

    private void readBeans(Binder<AccessoriesWindows> binder) {
        if (setAccessoriesWindows != null) {
            for (AccessoriesWindows accessoriesWindows : setAccessoriesWindows) {
                accessoriesWindows.setAllpricePurchase(BigDecimal.valueOf(accessoriesWindows.getUnitDetalPrice() * accessoriesWindows.getQuantity() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                accessoriesWindows.setAllpriceAfterDiscount(BigDecimal.valueOf(accessoriesWindows.getUnitDetalPrice() * accessoriesWindows.getQuantity() * (100 - accessoriesWindows.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                accessoriesWindows.setAllprofit(BigDecimal.valueOf(accessoriesWindows.getAllpriceAfterDiscount() - accessoriesWindows.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                binder.setBean(accessoriesWindows);
            }
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("accesoriesWindows", setAccessoriesWindows);
        action.proceed();
    }
}
