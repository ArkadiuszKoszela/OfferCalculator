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
import pl.koszela.spring.entities.main.CategoryOfTiles;
import pl.koszela.spring.entities.main.Tiles;
import pl.koszela.spring.service.GridInteraface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = OfferView.CREATE_OFFER, layout = MainView.class)
public class OfferView extends VerticalLayout implements GridInteraface<Tiles> {

    static final String CREATE_OFFER = "createOffer";

    private TreeGrid<Tiles> treeGrid = new TreeGrid<>();
    private Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("tiles");
    private Binder<Tiles> binder;

    public OfferView() {
        add(createGrid());
    }

    public TreeGrid createGrid() {
        Grid.Column<Tiles> priceListName = treeGrid.addHierarchyColumn(Tiles::getManufacturer).setHeader("Nazwa cennika");
        Grid.Column<Tiles> nameColumn = treeGrid.addColumn(Tiles::getName).setResizable(true).setHeader("Kategoria");
        Grid.Column<Tiles> quantityColumn = treeGrid.addColumn(Tiles::getQuantity).setHeader("Ilość");
        Grid.Column<Tiles> discount = treeGrid.addColumn(Tiles::getDiscount).setHeader("Rabat");
        treeGrid.addColumn(Tiles::getUnitDetalPrice).setHeader("Cena detal");
        treeGrid.addColumn(Tiles::getUnitPurchasePrice).setHeader("Cena zakupu");
        treeGrid.addColumn(Tiles::getTotalPrice).setHeader("Total klient");
        treeGrid.addColumn(Tiles::getTotalProfit).setHeader("Total zysk");
        treeGrid.addColumn(Tiles::getAllpriceAfterDiscount).setHeader("Cena razem detal");
        treeGrid.addColumn(Tiles::getAllpricePurchase).setHeader("Cena razem zakup");
        treeGrid.addColumn(Tiles::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(Tiles.class);
        FooterRow footerRow = treeGrid.appendFooterRow();
        treeGrid.getEditor().setBinder(binder);

        TextField discountEditField = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Tiles::getDiscount, Tiles::setDiscount);
        itemClickListener(treeGrid, discountEditField);
        discount.setEditorComponent(discountEditField);

        TextField quantityField = bindTextFieldToDouble(binder, new StringToDoubleConverter("Błąd"), Tiles::getQuantity, Tiles::setQuantity);
        itemClickListener(treeGrid, quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener(treeGrid, binder);

        Button calculate = refreshButton(treeGrid);

        readBeans(set);
        footerRow.getCell(priceListName).setComponent(calculate);
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(new ArrayList())));

        settingsGrid(treeGrid);
        return treeGrid;
    }

    @Override
    public TreeData<Tiles> addItems(List list) {
        TreeData<Tiles> treeData = new TreeData<>();
        if (set != null) {
            Set<Tiles> parents = set.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles parent : parents) {
                List<Tiles> childrens = set.stream().filter(e -> e.getManufacturer().equals(parent.getManufacturer())).collect(Collectors.toList());
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

    private Button refreshButton(TreeGrid<Tiles> treeGrid) {
        return new Button("Refresh", buttonClickEvent -> {
            Set<Tiles> mainsInPriceList = set.stream().filter(e -> e.getName().equals(CategoryOfTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles tiles : mainsInPriceList) {
                Set<Tiles> onePriceList = set.stream().filter(e -> e.getManufacturer().equals(tiles.getManufacturer())).collect(Collectors.toSet());

                Double totalPrice = onePriceList.stream().map(Tiles::getAllpriceAfterDiscount).reduce(Double::sum).get();
                Double totalProfit = onePriceList.stream().map(Tiles::getAllprofit).reduce(Double::sum).get();
                tiles.setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2, RoundingMode.HALF_UP));
                tiles.setTotalProfit(BigDecimal.valueOf(totalProfit).setScale(2, RoundingMode.HALF_UP));
            }
            treeGrid.getDataProvider().refreshAll();
        });
    }

    @Override
    public ComponentRenderer<VerticalLayout, Tiles> createComponent() {
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
}
