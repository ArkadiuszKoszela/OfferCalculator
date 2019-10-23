package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import pl.koszela.spring.entities.EntityGutter;
import pl.koszela.spring.repositories.GutterRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;

@Route(value = GutterView.GUTTER_VIEW, layout = MainView.class)
public class GutterView extends VerticalLayout {

    public static final String GUTTER_VIEW = "gutter";

    private GutterRepository gutterRepository;

    private Grid<EntityGutter> grid = new Grid<>(EntityGutter.class);
    private List<EntityGutter> list;

    public GutterView(GutterRepository gutterRepository) {
        this.gutterRepository = Objects.requireNonNull(gutterRepository);

        allGutter();
        add(checkboxGroupType());
        add(checkboxGroupDiatemter());
        add(buttonSearch());
        add(createGrid());
    }

    private List<EntityGutter> allGutter() {
        list = gutterRepository.findAll();
        for (EntityGutter gutter : list) {
            gutter.setQuantity(1);
            gutter.setUnitPricePurchase(new BigDecimal(gutter.getUnitPriceDetal() * 0.7).setScale(2, RoundingMode.HALF_UP).doubleValue());
            gutter.setAllPriceDetal(new BigDecimal(gutter.getQuantity() * gutter.getUnitPriceDetal()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            gutter.setAllPricePurchase(new BigDecimal(gutter.getQuantity() * gutter.getUnitPricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            gutter.setProfit(new BigDecimal(gutter.getAllPriceDetal() - gutter.getAllPricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        return list;
    }

    private Grid<EntityGutter> createGrid() {
        /*Grid.Column<EntityGutter> nameColumn = */
//        grid.addColumn(EntityGutter::getName).setHeader("Nazwa");
//        Grid.Column<EntityGutter> quantityColumn = grid.addColumn(EntityGutter::getQuantity).setHeader("Ilość");
//        Grid.Column<EntityGutter> unitPurchaseColumn = grid.addColumn(EntityGutter::getUnitPricePurchase).setHeader("Cena jedn. zakup");
//        Grid.Column<EntityGutter> unitDetalColumn = grid.addColumn(EntityGutter::getUnitPriceDetal).setHeader("Cena jedn. detal");
//        Grid.Column<EntityGutter> allPurchaseColumn = grid.addColumn(EntityGutter::getAllPricePurchase).setHeader("Razem cena netto");
//        Grid.Column<EntityGutter> allDetalColumn = grid.addColumn(EntityGutter::getAllPriceDetal).setHeader("Razem cena detal");
//        Grid.Column<EntityGutter> protitColumn = grid.addColumn(EntityGutter::getProfit).setHeader("Zysk");

        grid.setItems(list);
        grid.removeColumnByKey("id");
        grid.setColumns("name", "quantity", "discount", "unitPricePurchase", "unitPriceDetal", "allPricePurchase", "allPriceDetal", "profit");
        grid.getColumnByKey("name").setHeader("Nazwa");
        grid.getColumnByKey("quantity").setHeader("Ilość");
        grid.getColumnByKey("discount").setHeader("Rabat");
        grid.getColumnByKey("unitPricePurchase").setHeader("Cena jedn. zakup");
        grid.getColumnByKey("unitPriceDetal").setHeader("Cena jedn. detal");
        grid.getColumnByKey("allPricePurchase").setHeader("Razem cena netto");
        grid.getColumnByKey("allPriceDetal").setHeader("Razem cena detal");
        grid.getColumnByKey("profit").setHeader("Zysk");

        editor();

        grid.getColumns().forEach(e -> e.setAutoWidth(true));
        grid.setMinHeight("750px");
        return grid;
    }

    private void editor() {
        Binder<EntityGutter> binder = new Binder<>(EntityGutter.class);
        grid.getEditor().setBinder(binder);

        TextField discountField = new TextField();
        addEnterEvent(discountField);
        binder.forField(discountField)
                .withConverter(new StringToIntegerConverter("Błąd"))
                .bind(EntityGutter::getDiscount, EntityGutter::setDiscount);
        grid.getColumnByKey("discount").setEditorComponent(discountField);

        addClickListener(discountField);

        addCloseListener(binder);
    }

    private void addEnterEvent(TextField discountEditField) {
        discountEditField.getElement()
                .addEventListener("keydown",
                        event -> grid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
    }

    private void addClickListener(TextField discountField) {
        grid.addItemDoubleClickListener(e -> {
            grid.getEditor().editItem(e.getItem());
            discountField.focus();
        });
    }

    private void addCloseListener(Binder<EntityGutter> binder) {
        grid.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null) {
                EntityGutter gutter = binder.getBean();
                gutter.setUnitPriceDetal(new BigDecimal(gutter.getUnitPriceDetal() * (100 - gutter.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllPriceDetal(new BigDecimal(gutter.getUnitPriceDetal() * gutter.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllPricePurchase(new BigDecimal(gutter.getUnitPricePurchase() * gutter.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setProfit(new BigDecimal(gutter.getAllPriceDetal() - gutter.getAllPricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                if (binder.getBean().getDiscount() <= 30) {
                    binder.setBean(gutter);
                } else {
                    gutter.setDiscount(30);
                    getNotificationError("Maksymalny rabat to 30 %");
                    binder.setBean(gutter);
                }
            }
        });
    }

    private RadioButtonGroup<String> checkboxGroupType() {
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Stalowa", "PCV", "Tytan-cynk", "Ocynk");
        return radioButtonGroup;

    }

    private RadioButtonGroup<String> checkboxGroupDiatemter() {
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("125/90", "125/100", "150/100");
        return radioButtonGroup;
    }

    private Button buttonSearch() {
        return new Button("Szukaj", e -> {

        });
    }
}
