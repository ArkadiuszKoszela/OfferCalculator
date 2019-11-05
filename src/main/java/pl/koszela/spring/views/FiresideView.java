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
import pl.koszela.spring.entities.Fireside;
import pl.koszela.spring.entities.Windows;
import pl.koszela.spring.repositories.FiresideRepository;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.service.NotificationInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Route(value = FiresideView.FIRESIDE, layout = MainView.class)
public class FiresideView extends VerticalLayout implements GridInteraface, BeforeLeaveObserver {
    static final String FIRESIDE = "fireside";

    private FiresideRepository firesideRepository;

    private Grid<Fireside> treeGrid = new Grid<>();
    private Set<Fireside> setFireside = (Set<Fireside>) VaadinSession.getCurrent().getSession().getAttribute("fireside");
    private Set<Windows> setWindows = (Set<Windows>) VaadinSession.getCurrent().getSession().getAttribute("windowsAfterChoose");
    private Binder<Fireside> binder;

    @Autowired
    public FiresideView(FiresideRepository firesideRepository) {
        this.firesideRepository = Objects.requireNonNull(firesideRepository);

        add(createGridd());
    }

    private Grid createGridd() {
        Grid.Column<Fireside> nameColumn = treeGrid.addColumn(Fireside::getName).setHeader("Nazwa");
        treeGrid.addColumn(Fireside::getCategory).setHeader("Kategoria").setSortable(true);
        treeGrid.addColumn(Fireside::getManufacturer).setHeader("Producent");
        Grid.Column<Fireside> quantityColumn = treeGrid.addColumn(Fireside::getQuantity).setHeader("Ilość");
        Grid.Column<Fireside> discountColumn = treeGrid.addColumn(Fireside::getDiscount).setHeader("Rabat");
        Grid.Column<Fireside> detalPriceColumn = treeGrid.addColumn(Fireside::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<Fireside> purchasePriceColumn = treeGrid.addColumn(Fireside::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<Fireside> allPriceDetalColumn = treeGrid.addColumn(Fireside::getAllpriceAfterDiscount).setHeader("Razem Detal");
        Grid.Column<Fireside> allPricePurchaseColumn = treeGrid.addColumn(Fireside::getAllpricePurchase).setHeader("Razem zakup");
        Grid.Column<Fireside> profitColumn = treeGrid.addColumn(Fireside::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(Fireside.class);

        treeGrid.getEditor().setBinder(binder);

        TextField discountEditField = new TextField();
//        addEnterEvent(treeGrid, discountEditField);
        binder.forField(discountEditField)
                .withConverter(new StringToIntegerConverter("Błąd"))
                .bind(Fireside::getDiscount, Fireside::setDiscount);
        itemClickListener(discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = new TextField();
        binder.forField(quantityField)
                .withConverter(new StringToDoubleConverter("Błąd"))
                .bind(Fireside::getQuantity, Fireside::setQuantity);
//        addEnterEvent(treeGrid, quantityField);
        itemClickListener(quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener();

        readBeans(binder);

        if(setFireside != null) {
            treeGrid.setDataProvider(new ListDataProvider<>(setFireside));
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
                Fireside fireside = binder.getBean();
                fireside.setAllpricePurchase(BigDecimal.valueOf(fireside.getQuantity() * fireside.getUnitDetalPrice() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                fireside.setAllpriceAfterDiscount(BigDecimal.valueOf(fireside.getQuantity() * fireside.getUnitDetalPrice() * (100 - fireside.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                fireside.setAllprofit(BigDecimal.valueOf(fireside.getAllpriceAfterDiscount() - fireside.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                if (binder.getBean().getDiscount() <= 30) {
                    binder.setBean(fireside);
                } else {
                    fireside.setDiscount(30);
                    NotificationInterface.notificationOpen("Maksymalny rabat to 30 %", NotificationVariant.LUMO_ERROR);
                    binder.setBean(fireside);
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
    public ComponentRenderer<VerticalLayout, Fireside> createComponent() {
        return new ComponentRenderer<>(fireside -> {
            Checkbox mainCheckBox = new Checkbox("Dodać ?");
            mainCheckBox.setValue(fireside.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                fireside.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    }

    private void readBeans(Binder<Fireside> binder) {
        if(setFireside != null) {
            for (Fireside fireside : setFireside) {
                fireside.setAllpricePurchase(BigDecimal.valueOf(fireside.getUnitDetalPrice() * fireside.getQuantity() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                fireside.setAllpriceAfterDiscount(BigDecimal.valueOf(fireside.getUnitDetalPrice() * fireside.getQuantity() * (100 - fireside.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                fireside.setAllprofit(BigDecimal.valueOf(fireside.getAllpriceAfterDiscount() - fireside.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                binder.setBean(fireside);
            }
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("collar", setFireside);
        action.proceed();
    }

}