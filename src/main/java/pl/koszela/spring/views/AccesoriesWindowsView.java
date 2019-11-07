package pl.koszela.spring.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
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
import pl.koszela.spring.entities.Accesories;
import pl.koszela.spring.entities.AccesoriesWindows;
import pl.koszela.spring.entities.Collar;
import pl.koszela.spring.entities.Windows;
import pl.koszela.spring.repositories.AccesoriesWindowsRepository;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.service.NotificationInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Route(value = AccesoriesWindowsView.ACCESORIES_WINDOWS, layout = MainView.class)
public class AccesoriesWindowsView extends VerticalLayout implements GridInteraface<AccesoriesWindows>, BeforeLeaveObserver {
    static final String ACCESORIES_WINDOWS = "accesoriesWindows";

    private AccesoriesWindowsRepository accesoriesWindowsRepository;

    private Grid<AccesoriesWindows> treeGrid = new Grid<>();
    private Set<AccesoriesWindows> setAccesoriesWindows = (Set<AccesoriesWindows>) VaadinSession.getCurrent().getSession().getAttribute("accesoriesWindows");
    private Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
    private Binder<AccesoriesWindows> binder;

    @Autowired
    public AccesoriesWindowsView(AccesoriesWindowsRepository accesoriesWindowsRepository) {
        this.accesoriesWindowsRepository = Objects.requireNonNull(accesoriesWindowsRepository);

        if (setAccesoriesWindows == null && setWindows != null) {
            setAccesoriesWindows = new HashSet<>();
            List<AccesoriesWindows> all = accesoriesWindowsRepository.findAll();
            for (AccesoriesWindows accesoriesWindows : all) {
                for (Windows windows : setWindows) {
                    if (windows.getSize().equals(accesoriesWindows.getSize()) && windows.getManufacturer().equals(accesoriesWindows.getManufacturer())) {
                        setAccesoriesWindows.add(accesoriesWindows);
                    }
                }
            }
        }
        add(createGrid());
    }

    @Override
    public Grid<AccesoriesWindows> createGrid() {
        Grid.Column<AccesoriesWindows> nameColumn = treeGrid.addColumn(AccesoriesWindows::getName).setHeader("Nazwa");
        treeGrid.addColumn(AccesoriesWindows::getSize).setHeader("Rozmiar").setSortable(true);
        treeGrid.addColumn(AccesoriesWindows::getManufacturer).setHeader("Producent");
        Grid.Column<AccesoriesWindows> quantityColumn = treeGrid.addColumn(AccesoriesWindows::getQuantity).setHeader("Ilość");
        Grid.Column<AccesoriesWindows> discountColumn = treeGrid.addColumn(AccesoriesWindows::getDiscount).setHeader("Rabat");
        Grid.Column<AccesoriesWindows> detalPriceColumn = treeGrid.addColumn(AccesoriesWindows::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<AccesoriesWindows> purchasePriceColumn = treeGrid.addColumn(AccesoriesWindows::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<AccesoriesWindows> allPriceDetalColumn = treeGrid.addColumn(AccesoriesWindows::getAllpriceAfterDiscount).setHeader("Razem Detal");
        Grid.Column<AccesoriesWindows> allPricePurchaseColumn = treeGrid.addColumn(AccesoriesWindows::getAllpricePurchase).setHeader("Razem zakup");
        Grid.Column<AccesoriesWindows> profitColumn = treeGrid.addColumn(AccesoriesWindows::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(AccesoriesWindows.class);

        treeGrid.getEditor().setBinder(binder);

        TextField discountEditField = new TextField();
        addEnterEvent(treeGrid, discountEditField);
        binder.forField(discountEditField)
                .withConverter(new StringToIntegerConverter("Błąd"))
                .bind(AccesoriesWindows::getDiscount, AccesoriesWindows::setDiscount);
        itemClickListener(treeGrid, discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = new TextField();
        binder.forField(quantityField)
                .withConverter(new StringToDoubleConverter("Błąd"))
                .bind(AccesoriesWindows::getQuantity, AccesoriesWindows::setQuantity);
        addEnterEvent(treeGrid, quantityField);
        itemClickListener(treeGrid, quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener(treeGrid, binder, binder.getBean());

        readBeans(binder);

        if (setAccesoriesWindows != null) {
            treeGrid.setDataProvider(new ListDataProvider<>(setAccesoriesWindows));
        }
        treeGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        treeGrid.setMinHeight("700px");
        return treeGrid;
    }

    @Override
    public TreeData<AccesoriesWindows> addItems(List list) {
        return null;
    }

    @Override
    public TextField editField(StringToIntegerConverter stringToIntegerConverter, StringToDoubleConverter stringToDoubleConverter) {
        return null;
    }

    @Override
    public ComponentRenderer<VerticalLayout, AccesoriesWindows> createComponent() {
        return new ComponentRenderer<>(accesoriesWindows -> {
            Checkbox mainCheckBox = new Checkbox("Dodać ?");
            mainCheckBox.setValue(accesoriesWindows.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                accesoriesWindows.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    }

    private void readBeans(Binder<AccesoriesWindows> binder) {
        if (setAccesoriesWindows != null) {
            for (AccesoriesWindows accesoriesWindows : setAccesoriesWindows) {
                accesoriesWindows.setAllpricePurchase(BigDecimal.valueOf(accesoriesWindows.getUnitDetalPrice() * accesoriesWindows.getQuantity() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                accesoriesWindows.setAllpriceAfterDiscount(BigDecimal.valueOf(accesoriesWindows.getUnitDetalPrice() * accesoriesWindows.getQuantity() * (100 - accesoriesWindows.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                accesoriesWindows.setAllprofit(BigDecimal.valueOf(accesoriesWindows.getAllpriceAfterDiscount() - accesoriesWindows.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                binder.setBean(accesoriesWindows);
            }
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("accesoriesWindows", setAccesoriesWindows);
        action.proceed();
    }
}
