package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.*;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.Tiles;
import pl.koszela.spring.service.MenuBarInterface;
import pl.koszela.spring.service.SaveUsers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;

@Route(value = OfferView.CREATE_OFFER, layout = MainView.class)
public class OfferView extends VerticalLayout implements MenuBarInterface {

    public static final String CREATE_OFFER = "createOffer";

    private NumberField tilesDiscount = new NumberField("Rabat dachówki");
    private NumberField accesoriesDiscount = new NumberField("Rabat akcesoria");
    private NumberField windowsDiscount = new NumberField("Rabat okna/kołnierz");
    private VerticalLayout layout = new VerticalLayout();

    @Autowired
    public OfferView() {

        add(menu());
        add(addLayout());
    }

    private VerticalLayout addLayout() {
        FormLayout.ResponsiveStep step = new FormLayout.ResponsiveStep("5px", 3);
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(step);
        /*resultAccesories.setItems(resultAccesories());*/
        formLayout.add(settingsNumberFields(tilesDiscount), settingsNumberFields(accesoriesDiscount), settingsNumberFields(windowsDiscount));
        layout.add(formLayout);
        layout.add(createTiles());
        /*layout.add(createGridAccesories());*/
        return layout;
    }

    private NumberField settingsNumberFields(NumberField numberField) {
        numberField.setValue(0d);
        numberField.setMin(0);
        numberField.setMax(30);
        numberField.setHasControls(true);
        numberField.setSuffixComponent(new Span("%"));
        return numberField;
    }

    private TreeGrid<Tiles> createTiles() {
        TreeGrid<Tiles> treeGrid = new TreeGrid<>();
        BigDecimal customerDiscount = new BigDecimal(tilesDiscount.getValue()).add(new BigDecimal(100));

        Grid.Column<Tiles> priceListName = treeGrid.addHierarchyColumn(Tiles::getPriceListName).setHeader("Nazwa cennika");
        Grid.Column<Tiles> name = treeGrid.addColumn(Tiles::getName).setHeader("Kategoria");
        Grid.Column<Tiles> quantity = treeGrid.addColumn(Tiles::getQuantity).setHeader("Ilość");
        Grid.Column<Tiles> discount = treeGrid.addColumn(Tiles::getDiscount).setHeader("Rabat");
        Grid.Column<Tiles> price = treeGrid.addColumn(Tiles::getPrice).setHeader("Cena jedn.");
        Grid.Column<Tiles> totalPrice = treeGrid.addColumn(Tiles::getTotalPrice).setHeader("Total klient");
        Grid.Column<Tiles> totalProfit = treeGrid.addColumn(Tiles::getTotalProfit).setHeader("Total zysk").setAutoWidth(true);
        Grid.Column<Tiles> purchasePrice = treeGrid.addColumn(tiles -> tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(BigDecimal.valueOf(130), 2, RoundingMode.HALF_UP)).setHeader("Cena zakupu");
        Grid.Column<Tiles> priceAfterDiscount = treeGrid.addColumn(tiles -> tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(new BigDecimal(tiles.getDiscount()).add(new BigDecimal(100)), 2, RoundingMode.HALF_UP)).setHeader("Cena po rabacie");
        Grid.Column<Tiles> profit = treeGrid.addColumn(tiles -> (tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(new BigDecimal(tiles.getDiscount()).add(new BigDecimal(100)), 2, RoundingMode.HALF_UP).subtract(tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(BigDecimal.valueOf(130), 2, RoundingMode.HALF_UP)))).setHeader("Zysk");

        HeaderRow filterRow = treeGrid.appendHeaderRow();
        FooterRow footerRow = treeGrid.appendFooterRow();

        TextField filter = new TextField();
        TreeDataProvider<Tiles> treeDataProvider = new TreeDataProvider<>(getTilesTreeData());
        filter.addValueChangeListener(event -> treeDataProvider.setFilter(
                tiles -> StringUtils.containsIgnoreCase(tiles.getPriceListName(), filter.getValue())));

        filter.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(priceListName).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");

        Button calculate = new Button("Refresh");
        calculate.addClickListener(buttonClickEvent -> {
            if (tilesDiscount.getValue() > 30) {
                tilesDiscount.setValue(30d);
                getNotificationError("Maksymalny rabat to 30 %");
            }
            List<Tiles> parents = treeGrid.getDataCommunicator().fetchFromProvider(0, 13).collect(Collectors.toList());
            Map<BigDecimal, String> allPriceAfrterDiscount = new HashMap<>();
            Map<BigDecimal, String> allPriceProfit = new HashMap<>();
            for (Tiles parent : parents) {
                HierarchicalQuery<Tiles, com.vaadin.flow.function.SerializablePredicate<Tiles>> hierarchicalQuery1 = new HierarchicalQuery<>(null, parent);
                List<Tiles> childrens = treeGrid.getDataProvider().fetchChildren(hierarchicalQuery1).collect(Collectors.toList());
                parent.setPricePurchase((parent.getPrice().multiply(new BigDecimal(parent.getQuantity())).multiply(new BigDecimal(100))).divide(BigDecimal.valueOf(130), 2, RoundingMode.HALF_UP));
                parent.setPriceAfterDiscount(parent.getPrice().multiply(new BigDecimal(parent.getQuantity())).multiply(new BigDecimal(100)).divide((new BigDecimal(parent.getDiscount()).add(new BigDecimal(100))), 2, RoundingMode.HALF_UP));
                parent.setProfit(parent.getPriceAfterDiscount().subtract(parent.getPricePurchase()));
                allPriceAfrterDiscount.put(parent.getPriceAfterDiscount(), parent.getPriceListName());
                allPriceProfit.put(parent.getProfit(), parent.getPriceListName());
                treeGrid.getDataCommunicator().refresh(parent);
                for (Tiles children : childrens) {
                    children.setPricePurchase(children.getPrice().multiply(new BigDecimal(children.getQuantity())).multiply(new BigDecimal(100)).divide(BigDecimal.valueOf(130), 2, RoundingMode.HALF_UP));
                    children.setPriceAfterDiscount(children.getPrice().multiply(new BigDecimal(children.getQuantity())).multiply(new BigDecimal(100)).divide((new BigDecimal(children.getDiscount()).add(new BigDecimal(100))), RoundingMode.HALF_UP));
                    children.setProfit(children.getPriceAfterDiscount().subtract(children.getPricePurchase()));
                    allPriceAfrterDiscount.put(children.getPriceAfterDiscount(), children.getPriceListName());
                    allPriceProfit.put(children.getProfit(), children.getPriceListName());
                    treeGrid.getDataCommunicator().refresh(children);
                }
                BigDecimal totalPriceValue = BigDecimal.ZERO;
                BigDecimal totalProfitValue = BigDecimal.ZERO;
                for (Map.Entry<BigDecimal, String> values : allPriceAfrterDiscount.entrySet()) {
                    if (values.getValue().equals(parent.getPriceListName())) {
                        totalPriceValue = totalPriceValue.add(values.getKey());
                    }
                }
                for (Map.Entry<BigDecimal, String> values : allPriceProfit.entrySet()) {
                    if (values.getValue().equals(parent.getPriceListName())) {
                        totalProfitValue = totalProfitValue.add(values.getKey());
                    }
                }
                parent.setTotalPrice(totalPriceValue);
                parent.setTotalProfit(totalProfitValue);
            }
            treeGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        });

        footerRow.getCell(priceListName).setComponent(calculate);
        treeGrid.setDataProvider(treeDataProvider);
        treeGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        Binder<Tiles> binder = new Binder<>(Tiles.class);
        Editor<Tiles> editor = treeGrid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField editPrice = new TextField();
        binder.forField(editPrice)
                .withConverter(
                        new StringToBigDecimalConverter("Age must be a number."))
                .withStatusLabel(validationStatus).bind("price");
        price.setEditorComponent(editPrice);

        TextField editDiscount = new TextField();
        binder.forField(editDiscount)
                .withConverter(
                        new StringToIntegerConverter("Age must be a number."))
                .withStatusLabel(validationStatus).bind("discount");
        discount.setEditorComponent(editDiscount);

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<Tiles> editorColumn = treeGrid.addComponentColumn(tiles -> {
            Button edit = new Button("Edit");
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                editor.editItem(tiles);
                editPrice.focus();
                editDiscount.focus();
                tiles.setDiscount(Integer.valueOf(editDiscount.getValue()));
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        }).setHeader("Edit");
        treeGrid.addComponentColumn(tiles -> {
            return new Select("Główna", "Opcjonalna");
        }).setHeader("Wydruk");

        editor.addOpenListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button(new Icon(VaadinIcon.CHECK), e -> {
            editor.save();
        });
        save.addClassName("save");

        Button cancel = new Button(new Icon(VaadinIcon.CLOSE), e -> editor.cancel());
        cancel.addClassName("cancel");

        treeGrid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);

        treeGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        return treeGrid;
    }

    private TreeData<Tiles> getTilesTreeData() {
        TreeData<Tiles> treeData1 = new TreeData<>();
        List<List<Tiles>> all = (List<List<Tiles>>) VaadinSession.getCurrent().getAttribute("resultTiles");
        if (all != null) {
            all.forEach(e -> e.sort(Comparator.comparing(Tiles::getId)));

            List<BigDecimal> bigDecimalList = new ArrayList<>();
            for (List<Tiles> tiles : all) {
                BigDecimal bigDecimal = BigDecimal.ZERO;
                List<BigDecimal> wyniki = new ArrayList<>();
                tiles.forEach(e -> {
                    wyniki.add(e.getPrice().multiply(new BigDecimal(e.getQuantity())).multiply(new BigDecimal(100)).divide(BigDecimal.valueOf(130), 2, RoundingMode.HALF_UP));
                });
                for (BigDecimal bigDecimal1 : wyniki) {
                    if (bigDecimal1 != null) {
                        bigDecimal = bigDecimal.add(bigDecimal1);
                    }
                }
                bigDecimalList.add(bigDecimal);
                tiles.get(0).setTotalPrice(bigDecimalList.iterator().next());
            }
            for (List<Tiles> tiles : all) {
                for (int j = 0; j < tiles.size(); j++) {
                    if (j == 0) {
                        treeData1.addItem(null, tiles.get(0));
                    } else {
                        treeData1.addItem(tiles.get(0), tiles.get(j));
                    }
                }
            }
        }
        return treeData1;
    }

    @Override
    public MenuBar menu() {
        MenuBar menuBar = new MenuBar();
        Button button = new Button("Utwórz ofertę");
        button.addClickListener(buttonClickEvent -> {
            VaadinSession.getCurrent().getAttribute("resultTiles");
            VaadinSession.getCurrent().setAttribute("resultTiles", null);
        });
        menuBar.addItem(button);
        return menuBar;
    }
}
