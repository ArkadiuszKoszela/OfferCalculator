package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import pl.koszela.spring.entities.tiles.CategoryOfTiles;
import pl.koszela.spring.entities.tiles.Tiles;
import pl.koszela.spring.service.GridInteraface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;

@Route(value = OfferView.CREATE_OFFER, layout = MainView.class)
public class OfferView extends VerticalLayout implements GridInteraface {

    static final String CREATE_OFFER = "createOffer";

    private TreeGrid<Tiles> treeGrid = new TreeGrid<>();
    private Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles");
    private Binder<Tiles> binder;

    public OfferView() {
        add(createGrid());
    }

    @Override
    public TreeGrid createGrid() {
        Grid.Column<Tiles> priceListName = treeGrid.addHierarchyColumn(Tiles::getPriceListName).setHeader("Nazwa cennika");
        Grid.Column<Tiles> nameColumn = treeGrid.addColumn(Tiles::getName).setResizable(true).setHeader("Kategoria");
        treeGrid.addColumn(Tiles::getQuantity).setHeader("Ilość");
        Grid.Column<Tiles> discount = treeGrid.addColumn(Tiles::getDiscount).setHeader("Rabat");
        treeGrid.addColumn(Tiles::getUnitDetalPrice).setHeader("Cena detal");
        treeGrid.addColumn(Tiles::getUnitPurchasePrice).setHeader("Cena zakupu");
        treeGrid.addColumn(Tiles::getTotalPrice).setHeader("Total klient");
        treeGrid.addColumn(Tiles::getTotalProfit).setHeader("Total zysk");
        treeGrid.addColumn(Tiles::getAllpricePurchase).setHeader("Cena zakupu");
        treeGrid.addColumn(Tiles::getAllpriceAfterDiscount).setHeader("Cena netto po rabacie");
        treeGrid.addColumn(Tiles::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createCheckboxes()).setHeader("Opcje");

        binder = new Binder<>(Tiles.class);
        FooterRow footerRow = treeGrid.appendFooterRow();
        treeGrid.getEditor().setBinder(binder);

        TextField discountEditField = editField(new StringToIntegerConverter("Błąd"), new StringToDoubleConverter("Błąd"));
        discount.setEditorComponent(discountEditField);

        itemClickListener(discountEditField);

        closeListener();

        Button calculate = refreshButton(treeGrid);

        readBeans(binder);
        footerRow.getCell(priceListName).setComponent(calculate);
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(new ArrayList())));
        treeGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        treeGrid.setMinHeight("600px");
        return treeGrid;
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
                Tiles tiles = binder.getBean();
                tiles.setAllpricePurchase(BigDecimal.valueOf(tiles.getUnitDetalPrice() * tiles.getQuantity() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                tiles.setAllpriceAfterDiscount(BigDecimal.valueOf(tiles.getUnitDetalPrice() * tiles.getQuantity() * (100 - tiles.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                tiles.setAllprofit(BigDecimal.valueOf(tiles.getAllpriceAfterDiscount() - tiles.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                if (binder.getBean().getDiscount() <= 30) {
                    binder.setBean(tiles);
                } else {
                    tiles.setDiscount(30);
                    getNotificationError("Maksymalny rabat to 30 %");
                    binder.setBean(tiles);
                }
            }
        });
    }

    @Override
    public TreeData<Tiles> addItems(List list) {
        TreeData<Tiles> treeData = new TreeData<>();
        if (set != null) {
            Set<Tiles> parents = set.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles parent : parents) {
                List<Tiles> childrens = set.stream().filter(e -> e.getPriceListName().equals(parent.getPriceListName())).collect(Collectors.toList());
                for (int i = 0; i < childrens.size(); i++) {
                    if (i == 0) {
                        treeData.addItem(null, parent);
                    } else if (!childrens.get(i).getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())) {
                        treeData.addItem(parent, childrens.get(i));
                    }
                }
            }
        }
        return treeData;
    }

    @Override
    public TextField editField(StringToIntegerConverter stringToIntegerConverter, StringToDoubleConverter stringToDoubleConverter) {
        TextField textField = new TextField();
        addEnterEvent(treeGrid, textField);
        binder.forField(textField)
                .withConverter(stringToIntegerConverter)
                .bind(Tiles::getDiscount, Tiles::setDiscount);
        return textField;
    }

    private Button refreshButton(TreeGrid<Tiles> treeGrid) {
        return new Button("Refresh", buttonClickEvent -> {
            Set<Tiles> mainsInPriceList = set.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles tiles : mainsInPriceList) {
                Set<Tiles> onePriceList = set.stream().filter(e -> e.getPriceListName().equals(tiles.getPriceListName())).collect(Collectors.toSet());

                Double totalPrice = onePriceList.stream().map(Tiles::getAllpriceAfterDiscount).reduce(Double::sum).get();
                Double totalProfit = onePriceList.stream().map(Tiles::getAllprofit).reduce(Double::sum).get();
                tiles.setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2, RoundingMode.HALF_UP));
                tiles.setTotalProfit(BigDecimal.valueOf(totalProfit).setScale(2, RoundingMode.HALF_UP));
            }
            treeGrid.getDataProvider().refreshAll();
        });
    }

    @Override
    public ComponentRenderer<VerticalLayout, Tiles> createCheckboxes() {
        return new ComponentRenderer<>(tiles -> {
            Checkbox mainCheckBox = new Checkbox("Główna");
            Checkbox optionCheckbox = new Checkbox("Opcjonalna");
            mainCheckBox.setValue(tiles.isMain());
            optionCheckbox.setValue(tiles.isOption());
            mainCheckBox.addValueChangeListener(event -> {
                tiles.setMain(event.getValue());
                tiles.setOption(!event.getValue());
                optionCheckbox.setValue(!mainCheckBox.getValue());
            });
            optionCheckbox.addValueChangeListener(event -> {
                tiles.setMain(!event.getValue());
                tiles.setOption(event.getValue());
                mainCheckBox.setValue(!optionCheckbox.getValue());
            });
            return new VerticalLayout(mainCheckBox, optionCheckbox);
        });
    }

    private void readBeans(Binder<Tiles> binder) {
        for (Tiles tiles : set) {
            tiles.setAllpricePurchase(BigDecimal.valueOf(tiles.getUnitDetalPrice() * tiles.getQuantity() * 70 / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
            tiles.setAllpriceAfterDiscount(BigDecimal.valueOf(tiles.getUnitDetalPrice() * tiles.getQuantity() * (100 - tiles.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
            tiles.setAllprofit(BigDecimal.valueOf(tiles.getAllpriceAfterDiscount() - tiles.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            binder.setBean(tiles);
        }
    }
}
