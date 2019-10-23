package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import pl.koszela.spring.entities.CategoryTiles;
import pl.koszela.spring.entities.EntityGutter;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.repositories.GutterRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;

@Route(value = GutterView.GUTTER_VIEW, layout = MainView.class)
public class GutterView extends VerticalLayout {

    public static final String GUTTER_VIEW = "gutter";

    private GutterRepository gutterRepository;

    private TreeGrid<EntityGutter> grid = new TreeGrid<>();
    private List<EntityGutter> list = (List<EntityGutter>) VaadinSession.getCurrent().getSession().getAttribute("allGutter");

    public GutterView(GutterRepository gutterRepository) {
        this.gutterRepository = Objects.requireNonNull(gutterRepository);

        add(checkboxGroupType());
        add(createGrid());
    }

    private TreeData<EntityGutter> allGutter(List<EntityGutter> list) {
        TreeData<EntityGutter> treeData = new TreeData<>();
        for (EntityGutter gutter : list) {
            gutter.setUnitPricePurchase(new BigDecimal(gutter.getUnitPriceDetal() * 0.7).setScale(2, RoundingMode.HALF_UP).doubleValue());
            gutter.setAllPriceDetal(new BigDecimal(gutter.getQuantity() * gutter.getUnitPriceDetal()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            gutter.setAllPricePurchase(new BigDecimal(gutter.getQuantity() * gutter.getUnitPricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            gutter.setProfit(new BigDecimal(gutter.getAllPriceDetal() - gutter.getAllPricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        if (list != null) {
            List<EntityGutter> parents = list.stream().filter(e -> e.getName().equals("rynna 3mb")).collect(Collectors.toList());
            for (EntityGutter parent : parents) {
                List<EntityGutter> childrens = list.stream().filter(e -> e.getCategory().equals(parent.getCategory())).collect(Collectors.toList());
                for (int i = 0; i < childrens.size(); i++) {
                    if (i == 0) {
                        treeData.addItem(null, parent);
                    } else if (!childrens.get(i).getName().equals("rynna 3mb")) {
                        treeData.addItem(parent, childrens.get(i));
                    }
                }
            }
        }
        return treeData;
    }

    private TreeGrid<EntityGutter> createGrid() {
        Grid.Column<EntityGutter> nameColumn = grid.addHierarchyColumn(EntityGutter::getName).setHeader("Nazwa");
        Grid.Column<EntityGutter> categoryColumn = grid.addColumn(EntityGutter::getCategory).setHeader("Kategoria");
        Grid.Column<EntityGutter> quantityColumn = grid.addColumn(EntityGutter::getQuantity).setHeader("Ilość");
        Grid.Column<EntityGutter> discountColumn = grid.addColumn(EntityGutter::getDiscount).setHeader("Rabat");
        grid.addColumn(EntityGutter::getTotalPrice).setHeader("Total klient");
        grid.addColumn(EntityGutter::getTotalProfit).setHeader("Total zysk");
        Grid.Column<EntityGutter> unitPurchaseColumn = grid.addColumn(EntityGutter::getUnitPricePurchase).setHeader("Cena jedn. zakup");
        Grid.Column<EntityGutter> unitDetalColumn = grid.addColumn(EntityGutter::getUnitPriceDetal).setHeader("Cena jedn. detal");
        Grid.Column<EntityGutter> allPurchaseColumn = grid.addColumn(EntityGutter::getAllPricePurchase).setHeader("Razem cena netto");
        Grid.Column<EntityGutter> allDetalColumn = grid.addColumn(EntityGutter::getAllPriceDetal).setHeader("Razem cena detal");
        Grid.Column<EntityGutter> protitColumn = grid.addColumn(EntityGutter::getProfit).setHeader("Zysk");
        grid.addColumn(createCheckboxes()).setHeader("Opcje");

        Binder<EntityGutter> binder = new Binder<>(EntityGutter.class);
        grid.getEditor().setBinder(binder);
        editor(binder, discountColumn, quantityColumn);
        FooterRow footerRow = grid.appendFooterRow();

        Button calculate = refreshButton(grid);

        footerRow.getCell(nameColumn).setComponent(calculate);

        grid.setDataProvider(new TreeDataProvider<>(allGutter(list)));
        grid.getColumns().forEach(e -> e.setAutoWidth(true));
        grid.setMinHeight("750px");
        return grid;
    }

    private ComponentRenderer<VerticalLayout, EntityGutter> createCheckboxes() {
        return new ComponentRenderer<>(gutter -> {
            Checkbox mainCheckBox = new Checkbox("Główna");
            Checkbox optionCheckbox = new Checkbox("Opcjonalna");
            mainCheckBox.setValue(gutter.isMain());
            optionCheckbox.setValue(gutter.isOption());
            mainCheckBox.addValueChangeListener(event -> {
                gutter.setMain(event.getValue());
                gutter.setOption(!event.getValue());
                optionCheckbox.setValue(!mainCheckBox.getValue());
            });
            optionCheckbox.addValueChangeListener(event -> {
                gutter.setMain(!event.getValue());
                gutter.setOption(event.getValue());
                mainCheckBox.setValue(!optionCheckbox.getValue());
            });
            return new VerticalLayout(mainCheckBox, optionCheckbox);
        });
    }

    private void editor(Binder<EntityGutter> binder, Grid.Column<EntityGutter> discountColumn, Grid.Column<EntityGutter> quantityColumn) {

        TextField discountField = new TextField();
        addEnterEvent(discountField);
        binder.forField(discountField)
                .withConverter(new StringToIntegerConverter("Błąd"))
                .bind(EntityGutter::getDiscount, EntityGutter::setDiscount);
        discountColumn.setEditorComponent(discountField);

        TextField quantityField = new TextField();
        addEnterEvent(quantityField);
        binder.forField(quantityField)
                .withConverter(new StringToDoubleConverter("Błąd"))
                .bind(EntityGutter::getQuantity, EntityGutter::setQuantity);
        quantityColumn.setEditorComponent(quantityField);

        addClickListener(discountField);

        addCloseListener(binder);
    }

    private void addEnterEvent(TextField textField) {
        textField.getElement()
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
        radioButtonGroup.setItems("Stalowa", "PCV", "Tytan-cynk", "Ocynk", "Wszystko");
        radioButtonGroup.addValueChangeListener(e -> {
            String value = e.getValue();
            if (value.equals("Stalowa")) {
                List<EntityGutter> searchCategory = list.stream().filter(f -> f.getCategory().contains("Flamingo")).collect(Collectors.toList());
                grid.setDataProvider(new TreeDataProvider<>(allGutter(searchCategory)));
                grid.getDataProvider().refreshAll();
            } else if (value.equals("PCV")) {
                List<EntityGutter> searchCategory = list.stream().filter(f -> f.getCategory().contains("Bryza")).collect(Collectors.toList());
                grid.setDataProvider(new TreeDataProvider<>(allGutter(searchCategory)));
                grid.getDataProvider().refreshAll();
            }else if(value.equals("Wszystko")){
                grid.setDataProvider(new TreeDataProvider<>(allGutter(list)));
                grid.getDataProvider().refreshAll();
            }
        });
        return radioButtonGroup;
    }

    private Button refreshButton(TreeGrid<EntityGutter> treeGrid) {
        return new Button("Refresh", buttonClickEvent -> {
            List<EntityGutter> mainsInPriceList = list.stream().filter(e -> e.getName().equals("rynna 3mb")).collect(Collectors.toList());
            for (EntityGutter gutter : mainsInPriceList) {
                List<EntityGutter> onePriceList = list.stream().filter(e -> e.getCategory().equals(gutter.getCategory())).collect(Collectors.toList());
                Double totalPrice = onePriceList.stream().map(EntityGutter::getAllPriceDetal).reduce(Double::sum).get();
                Double totalProfit = onePriceList.stream().map(EntityGutter::getProfit).reduce(Double::sum).get();
                gutter.setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2, RoundingMode.HALF_UP));
                gutter.setTotalProfit(BigDecimal.valueOf(totalProfit).setScale(2, RoundingMode.HALF_UP));
            }
            treeGrid.getDataProvider().refreshAll();
        });
    }
}