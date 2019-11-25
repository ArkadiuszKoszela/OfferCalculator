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
import pl.koszela.spring.entities.main.Accessories;
import pl.koszela.spring.entities.main.Collar;
import pl.koszela.spring.entities.main.Windows;
import pl.koszela.spring.repositories.main.CollarRepository;
import pl.koszela.spring.service.GridInteraface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Route(value = CollarView.COLLAR, layout = MainView.class)
public class CollarView extends VerticalLayout implements GridInteraface<Collar>, BeforeLeaveObserver {
    static final String COLLAR = "collar";

    private CollarRepository collarRepository;

    private Grid<Collar> grid = new Grid<>();
    private Set<Collar> setCollars = (Set<Collar>) VaadinSession.getCurrent().getSession().getAttribute("collar");
    private Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
    private Binder<Collar> binder;

    @Autowired
    public CollarView(CollarRepository collarRepository) {
        this.collarRepository = Objects.requireNonNull(collarRepository);

        setCollars = new HashSet<>();
        List<Collar> all = collarRepository.findAll();
        for (Collar collar : all) {
            for (Windows windows : setWindows) {
                if (windows.getSize().equals(collar.getSize()) && windows.getManufacturer().equals(collar.getManufacturer())) {
                    setCollars.add(collar);
                }
            }
        }
        add(createGrid());
    }

    public Grid createGrid() {
        Grid.Column<Collar> nameColumn = grid.addColumn(Collar::getName).setHeader("Nazwa");
        grid.addColumn(Collar::getSize).setHeader("Rozmiar").setSortable(true);
        grid.addColumn(Collar::getManufacturer).setHeader("Producent");
        Grid.Column<Collar> quantityColumn = grid.addColumn(Collar::getQuantity).setHeader("Ilość");
        Grid.Column<Collar> discountColumn = grid.addColumn(Collar::getDiscount).setHeader("Rabat");
        Grid.Column<Collar> detalPriceColumn = grid.addColumn(Collar::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<Collar> purchasePriceColumn = grid.addColumn(Collar::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<Collar> allPriceDetalColumn = grid.addColumn(Collar::getAllpriceAfterDiscount).setHeader("Razem Detal");
        Grid.Column<Collar> allPricePurchaseColumn = grid.addColumn(Collar::getAllpricePurchase).setHeader("Razem zakup");
        Grid.Column<Collar> profitColumn = grid.addColumn(Collar::getAllprofit).setHeader("Zysk");
        grid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(Collar.class);
        grid.getEditor().setBinder(binder);

        TextField discountEditField = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Collar::getDiscount, Collar::setDiscount);
        itemClickListener(grid, discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = bindTextFieldToDouble(binder, new StringToDoubleConverter("Błąd"), Collar::getQuantity, Collar::setQuantity);
        itemClickListener(grid, quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener(grid, binder);

        readBeans(binder);

        if (setCollars != null) {
            grid.setDataProvider(new ListDataProvider<>(setCollars));
        }

        settingsGrid(grid);
        return grid;
    }

    @Override
    public TreeData<Accessories> addItems(List list) {
        return null;
    }

    @Override
    public ComponentRenderer<VerticalLayout, Collar> createComponent() {
        return new ComponentRenderer<>(collar -> {
            Checkbox mainCheckBox = new Checkbox("Dodać ?");
            mainCheckBox.setValue(collar.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                collar.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    }

    private void readBeans(Binder<Collar> binder) {
        if (setCollars != null) {
            for (Collar collar : setCollars) {
                collar.setAllpricePurchase(BigDecimal.valueOf(collar.getUnitDetalPrice() * collar.getQuantity() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                collar.setAllpriceAfterDiscount(BigDecimal.valueOf(collar.getUnitDetalPrice() * collar.getQuantity() * (100 - collar.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                collar.setAllprofit(BigDecimal.valueOf(collar.getAllpriceAfterDiscount() - collar.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                binder.setBean(collar);
            }
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("collar", setCollars);
        action.proceed();
    }
}
