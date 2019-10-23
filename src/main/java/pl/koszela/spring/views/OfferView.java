package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.CategoryTiles;
import pl.koszela.spring.entities.Tiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;

@Route(value = OfferView.CREATE_OFFER, layout = MainView.class)
public class OfferView extends VerticalLayout {

    static final String CREATE_OFFER = "createOffer";

    private TreeGrid<Tiles> treeGrid = new TreeGrid<>();
    private Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTilesFromRepo");

    @Autowired
    public OfferView() {
        add(createTilesGrid());
    }

    private TreeGrid<Tiles> createTilesGrid() {
        Grid.Column<Tiles> priceListName = treeGrid.addHierarchyColumn(Tiles::getPriceListName).setHeader("Nazwa cennika").setAutoWidth(true);
        Grid.Column<Tiles> nameColumn = treeGrid.addColumn(Tiles::getName).setHeader("Kategoria");
        treeGrid.addColumn(Tiles::getQuantity).setHeader("Ilość");
        Grid.Column<Tiles> discount = treeGrid.addColumn(Tiles::getDiscount).setHeader("Rabat");
        treeGrid.addColumn(Tiles::getPriceDetalUnit).setHeader("Cena detal");
        treeGrid.addColumn(Tiles::getPriceFromRepo).setHeader("Cena zakupu");
        treeGrid.addColumn(Tiles::getTotalPrice).setHeader("Total klient");
        treeGrid.addColumn(Tiles::getTotalProfit).setHeader("Total zysk");
        treeGrid.addColumn(Tiles::getAllpricePurchase).setHeader("Cena zakupu");
        treeGrid.addColumn(Tiles::getAllpriceAfterDiscount).setHeader("Cena netto po rabacie");
        treeGrid.addColumn(Tiles::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createCheckboxes()).setHeader("Opcje");

        Binder<Tiles> binder = new Binder<>(Tiles.class);
        FooterRow footerRow = treeGrid.appendFooterRow();
        treeGrid.getEditor().setBinder(binder);

        TextField discountEditField = editDiscountField(discount, binder);

        itemClickListener(discountEditField);

        closeListener(binder);

        Button calculate = refreshButton(treeGrid);

        readBeans(binder);
        footerRow.getCell(priceListName).setComponent(calculate);
        treeGrid.setDataProvider(new TreeDataProvider<>(getTilesTreeDataFromRepo()));

        treeGrid.setMinHeight("850px");
        nameColumn.setAutoWidth(true);
        return treeGrid;
    }

    private ComponentRenderer<VerticalLayout, Tiles> createCheckboxes() {
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

    private TextField editDiscountField(Grid.Column<Tiles> discount, Binder<Tiles> binder) {
        TextField discountEditField = new TextField();
        addEnterEvent(discountEditField);
        binder.forField(discountEditField)
                .withConverter(new StringToIntegerConverter("Błąd"))
                .bind(Tiles::getDiscount, Tiles::setDiscount);
        discount.setEditorComponent(discountEditField);
        return discountEditField;
    }

    private void addEnterEvent(TextField discountEditField) {
        discountEditField.getElement()
                .addEventListener("keydown",
                        event -> treeGrid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
    }

    private void itemClickListener(TextField disc) {
        treeGrid.addItemDoubleClickListener(event -> {
            treeGrid.getEditor().editItem(event.getItem());
            disc.focus();
        });
    }

    private void closeListener(Binder<Tiles> binder) {
        treeGrid.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null) {
                Tiles tiles = binder.getBean();
                tiles.setAllpricePurchase((tiles.getPriceDetalUnit().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(70))).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                tiles.setAllpriceAfterDiscount((tiles.getPriceDetalUnit().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100).subtract(new BigDecimal(tiles.getDiscount())))).divide((new BigDecimal(100)), 2, RoundingMode.HALF_UP));
                tiles.setAllprofit(tiles.getAllpriceAfterDiscount().subtract(tiles.getAllpricePurchase()));
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

    private void readBeans(Binder<Tiles> binder) {
        for (Tiles tiles : set) {
            tiles.setAllpricePurchase((tiles.getPriceDetalUnit().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(70))).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            tiles.setAllpriceAfterDiscount((tiles.getPriceDetalUnit().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100).subtract(new BigDecimal(tiles.getDiscount())))).divide((new BigDecimal(100)), 2, RoundingMode.HALF_UP));
            tiles.setAllprofit(tiles.getAllpriceAfterDiscount().subtract(tiles.getAllpricePurchase()));
            binder.setBean(tiles);
        }
    }

    private Button refreshButton(TreeGrid<Tiles> treeGrid) {
        return new Button("Refresh", buttonClickEvent -> {
            Set<Tiles> mainsInPriceList = set.stream().filter(e -> e.getName().equals(CategoryTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles tiles : mainsInPriceList) {
                Set<Tiles> onePriceList = set.stream().filter(e -> e.getPriceListName().equals(tiles.getPriceListName())).collect(Collectors.toSet());
                BigDecimal totalPrice = onePriceList.stream().map(Tiles::getAllpriceAfterDiscount).reduce(BigDecimal::add).get();
                BigDecimal totalProfit = onePriceList.stream().map(Tiles::getAllprofit).reduce(BigDecimal::add).get();
                tiles.setTotalPrice(totalPrice);
                tiles.setTotalProfit(totalProfit);
            }
            treeGrid.getDataProvider().refreshAll();
        });
    }

    private TreeData<Tiles> getTilesTreeDataFromRepo() {
        TreeData<Tiles> treeData = new TreeData<>();
        if (set != null) {
            Set<Tiles> parents = set.stream().filter(e -> e.getName().equals(CategoryTiles.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles parent : parents) {
                List<Tiles> childrens = set.stream().filter(e -> e.getPriceListName().equals(parent.getPriceListName())).collect(Collectors.toList());
                for (int i = 0; i < childrens.size(); i++) {
                    if (i == 0) {
                        treeData.addItem(null, parent);
                    } else if (!childrens.get(i).getName().equals(CategoryTiles.DACHOWKA_PODSTAWOWA.toString())) {
                        treeData.addItem(parent, childrens.get(i));
                    }
                }
            }
        }
        return treeData;
    }
}