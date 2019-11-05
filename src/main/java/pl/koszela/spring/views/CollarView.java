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
import pl.koszela.spring.entities.Collar;
import pl.koszela.spring.entities.Windows;
import pl.koszela.spring.repositories.CollarRepository;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.service.NotificationInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Route(value = CollarView.COLLAR, layout = MainView.class)
public class CollarView extends VerticalLayout implements GridInteraface, BeforeLeaveObserver {
    static final String COLLAR = "collar";

    private CollarRepository collarRepository;

    private Grid<Collar> treeGrid = new Grid<>();
    private Set<Collar> setCollars = (Set<Collar>) VaadinSession.getCurrent().getSession().getAttribute("collar");
    private Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
    private Binder<Collar> binder;

    @Autowired
    public CollarView(CollarRepository collarRepository) {
        this.collarRepository = Objects.requireNonNull(collarRepository);

        if (setCollars == null && setWindows != null) {
            setCollars = new HashSet<>();
            List<Collar> all = collarRepository.findAll();
            for (Collar collar : all) {
                for (Windows windows : setWindows) {
                    if (windows.getSize().equals(collar.getSize()) && windows.getManufacturer().equals(collar.getManufacturer())) {
                        setCollars.add(collar);
                    }
                }
            }
        }
        add(createGridd());
    }

    private Grid createGridd() {
        Grid.Column<Collar> nameColumn = treeGrid.addColumn(Collar::getName).setHeader("Nazwa");
        treeGrid.addColumn(Collar::getSize).setHeader("Rozmiar").setSortable(true);
        treeGrid.addColumn(Collar::getManufacturer).setHeader("Producent");
        Grid.Column<Collar> quantityColumn = treeGrid.addColumn(Collar::getQuantity).setHeader("Ilość");
        Grid.Column<Collar> discountColumn = treeGrid.addColumn(Collar::getDiscount).setHeader("Rabat");
        Grid.Column<Collar> detalPriceColumn = treeGrid.addColumn(Collar::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<Collar> purchasePriceColumn = treeGrid.addColumn(Collar::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<Collar> allPriceDetalColumn = treeGrid.addColumn(Collar::getAllpriceAfterDiscount).setHeader("Razem Detal");
        Grid.Column<Collar> allPricePurchaseColumn = treeGrid.addColumn(Collar::getAllpricePurchase).setHeader("Razem zakup");
        Grid.Column<Collar> profitColumn = treeGrid.addColumn(Collar::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(Collar.class);

        treeGrid.getEditor().setBinder(binder);

        TextField discountEditField = new TextField();
//        addEnterEvent(treeGrid, discountEditField);
        binder.forField(discountEditField)
                .withConverter(new StringToIntegerConverter("Błąd"))
                .bind(Collar::getDiscount, Collar::setDiscount);
        itemClickListener(discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = new TextField();
        binder.forField(quantityField)
                .withConverter(new StringToDoubleConverter("Błąd"))
                .bind(Collar::getQuantity, Collar::setQuantity);
//        addEnterEvent(treeGrid, quantityField);
        itemClickListener(quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener();

        readBeans(binder);

        if(setCollars != null) {
            treeGrid.setDataProvider(new ListDataProvider<>(setCollars));
        }
        treeGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        treeGrid.setMinHeight("700px");
        return treeGrid;
    }

    @Override
    public TreeGrid createGrid() {
        return null;
    }

    @Override
    public void itemClickListener(TextField textField) {
        treeGrid.addItemDoubleClickListener(event -> {
            treeGrid.getEditor().editItem(event.getItem());
            textField.focus();
        });
    }

    @Override
    public void closeListener() {
        treeGrid.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null) {
                Collar collar = binder.getBean();
                collar.setAllpricePurchase(BigDecimal.valueOf(collar.getQuantity() * collar.getUnitDetalPrice() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                collar.setAllpriceAfterDiscount(BigDecimal.valueOf(collar.getQuantity() * collar.getUnitDetalPrice() * (100 - collar.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                collar.setAllprofit(BigDecimal.valueOf(collar.getAllpriceAfterDiscount() - collar.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                if (binder.getBean().getDiscount() <= 30) {
                    binder.setBean(collar);
                } else {
                    collar.setDiscount(30);
                    NotificationInterface.notificationOpen("Maksymalny rabat to 30 %", NotificationVariant.LUMO_ERROR);
                    binder.setBean(collar);
                }
            }
        });
    }

    @Override
    public TreeData<Accesories> addItems(List list) {
        return null;
    }

    @Override
    public TextField editField(StringToIntegerConverter stringToIntegerConverter, StringToDoubleConverter stringToDoubleConverter) {
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
        if(setCollars != null) {
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
