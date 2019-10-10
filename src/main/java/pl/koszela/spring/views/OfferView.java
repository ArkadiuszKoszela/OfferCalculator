package pl.koszela.spring.views;

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
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.koszela.spring.entities.Category;
import pl.koszela.spring.entities.Tiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static pl.koszela.spring.inputFields.ServiceNotification.getNotificationError;

@Route(value = OfferView.CREATE_OFFER, layout = MainView.class)
public class OfferView extends VerticalLayout {

    static final String CREATE_OFFER = "createOffer";

    private NumberField tilesDiscount = new NumberField("Rabat dachówki");
    private NumberField accesoriesDiscount = new NumberField("Rabat akcesoria");
    private NumberField windowsDiscount = new NumberField("Rabat okna/kołnierz");
    private VerticalLayout layout = new VerticalLayout();
    private TreeGrid<Tiles> treeGrid = new TreeGrid<>();

    @Autowired
    public OfferView() {
        add(addLayout());
    }

    private VerticalLayout addLayout() {
        FormLayout.ResponsiveStep step = new FormLayout.ResponsiveStep("5px", 3);
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(step);
        formLayout.add(settingsNumberFields(tilesDiscount), settingsNumberFields(accesoriesDiscount), settingsNumberFields(windowsDiscount));
        layout.add(formLayout);
        layout.add(createTiles());
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

        Grid.Column<Tiles> priceListName = treeGrid.addHierarchyColumn(Tiles::getPriceListName).setHeader("Nazwa cennika").setAutoWidth(true);
        Grid.Column<Tiles> name = treeGrid.addColumn(Tiles::getName).setHeader("Kategoria");
        Grid.Column<Tiles> quantity = treeGrid.addColumn(Tiles::getQuantity).setHeader("Ilość");
        Grid.Column<Tiles> discount = treeGrid.addColumn(Tiles::getDiscount).setHeader("Rabat");
        Grid.Column<Tiles> price = treeGrid.addColumn(Tiles::getPrice).setHeader("Cena jedn.");
        treeGrid.addColumn(Tiles::getTotalPrice).setHeader("Total klient");
        treeGrid.addColumn(Tiles::getTotalProfit).setHeader("Total zysk");
        treeGrid.addColumn(tiles -> tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(BigDecimal.valueOf(130), 2, RoundingMode.HALF_UP)).setHeader("Cena zakupu");
        treeGrid.addColumn(tiles -> tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(new BigDecimal(tiles.getDiscount()).add(new BigDecimal(100)), 2, RoundingMode.HALF_UP)).setHeader("Cena po rabacie");
        treeGrid.addColumn(tiles -> (tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(new BigDecimal(tiles.getDiscount()).add(new BigDecimal(100)), 2, RoundingMode.HALF_UP).subtract(tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(BigDecimal.valueOf(130), 2, RoundingMode.HALF_UP)))).setHeader("Zysk");

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

        Button calculate = getButtonRefresh(treeGrid);

        footerRow.getCell(priceListName).setComponent(calculate);
        treeGrid.setDataProvider(treeDataProvider);

        Editor(treeGrid, discount, price);
        return treeGrid;
    }

    private Button getButtonRefresh(TreeGrid<Tiles> treeGrid) {
        Button refresh = new Button("Refresh");
        refresh.addClickListener(buttonClickEvent -> {
            List<Tiles> parents = treeGrid.getDataCommunicator().fetchFromProvider(0, 13).collect(Collectors.toList());
            List<Tiles> list = new ArrayList<>();
            for (Tiles parent : parents) {
                HierarchicalQuery<Tiles, SerializablePredicate<Tiles>> hierarchicalQuery1 = new HierarchicalQuery<>(null, parent);
                List<Tiles> childrens = treeGrid.getDataProvider().fetchChildren(hierarchicalQuery1).collect(Collectors.toList());
                parent.setPricePurchase((parent.getPrice().multiply(new BigDecimal(parent.getQuantity())).multiply(new BigDecimal(70))).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                parent.setPriceAfterDiscount((parent.getPrice().multiply(new BigDecimal(parent.getQuantity())).multiply(new BigDecimal(100).subtract(new BigDecimal(parent.getDiscount())))).divide((new BigDecimal(100)), 2, RoundingMode.HALF_UP));
                parent.setProfit(parent.getPriceAfterDiscount().subtract(parent.getPricePurchase()));
                list.add(parent);
                treeGrid.getDataCommunicator().refresh(parent);
                for (Tiles children : childrens) {
                    children.setPricePurchase(children.getPrice().multiply(new BigDecimal(children.getQuantity())).multiply(new BigDecimal(70)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    children.setPriceAfterDiscount((children.getPrice().multiply(new BigDecimal(children.getQuantity())).multiply(new BigDecimal(100).subtract(new BigDecimal(children.getDiscount())))).divide((new BigDecimal(100)), RoundingMode.HALF_UP));
                    children.setProfit(children.getPriceAfterDiscount().subtract(children.getPricePurchase()));
                    list.add(children);
                    treeGrid.getDataCommunicator().refresh(children);
                }
                BigDecimal totalPriceValue = BigDecimal.ZERO;
                BigDecimal totalProfitValue = BigDecimal.ZERO;
                for (Tiles tiles : list) {
                    if (tiles.getPriceListName().equals(parent.getPriceListName())) {
                        totalPriceValue = totalPriceValue.add(tiles.getPriceAfterDiscount());
                        totalProfitValue = totalProfitValue.add(tiles.getProfit());
                    }
                }
                parent.setTotalPrice(totalPriceValue);
                parent.setTotalProfit(totalProfitValue);
                VaadinSession.getCurrent().setAttribute("allTiles", list);
                VaadinSession.getCurrent().setAttribute("childrens", childrens);
                VaadinSession.getCurrent().setAttribute("parents", parents);
            }
        });
        return refresh;
    }

    private void Editor(TreeGrid<Tiles> treeGrid, Grid.Column<Tiles> discount, Grid.Column<Tiles> price) {
        Binder<Tiles> binder = new Binder<>(Tiles.class);
        Editor<Tiles> editor = treeGrid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

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
                editDiscount.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        }).setHeader("Edit");

        treeGrid.addComponentColumn(tiles -> {
            return new Select<>("Główna", "Opcjonalna");
        }).setHeader("Wydruk");

        editor.addOpenListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button(new Icon(VaadinIcon.CHECK), e -> {
            if (Integer.parseInt(editDiscount.getValue()) > 30) {
                editDiscount.setValue("30");
                getNotificationError("Maksymalny rabat to 30 %");
            }
            editor.save();
        });

        Button cancel = new Button(new Icon(VaadinIcon.CLOSE), e -> editor.cancel());
        cancel.addClassName("cancel");

        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);
    }

    private TreeData<Tiles> getTilesTreeData() {
        TreeData<Tiles> treeData1 = new TreeData<>();
        List<List<Tiles>> all = (List<List<Tiles>>) VaadinSession.getCurrent().getAttribute("resultTiles");
        Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getAttribute("allTilesFromRepo");
        if (set != null) {
            List<List<Tiles>> allFromRepo = loadDataUser();
            allFromRepo.forEach(e -> e.sort(Comparator.comparing(Tiles::getId)));
            for (List<Tiles> tiles : allFromRepo) {
                for (int j = 0; j < tiles.size(); j++) {
                    if (j == 0) {
                        treeData1.addItem(null, tiles.get(0));
                    } else {
                        treeData1.addItem(tiles.get(0), tiles.get(j));
                    }
                }
            }
        }
        if (all != null && set == null) {
            all.forEach(e -> e.sort(Comparator.comparing(Tiles::getId)));
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

    private List<List<Tiles>> loadDataUser() {
        List<List<Tiles>> all = new ArrayList<>();
        List<Tiles> parents = getParents();
        for (Tiles tiles : parents) {
            List<Tiles> childrens = getChildrens(tiles.getPriceListName());
            childrens.add(tiles);
            all.add(getChildrens(tiles.getPriceListName()));
        }
        return all;
    }

    private List<Tiles> getChildrens(String parent) {
        Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getAttribute("allTilesFromRepo");
        List<Tiles> list = new ArrayList<>(set);
        List<Tiles> childrens = new ArrayList<>();
        if (list.size() > 0) {
            list.forEach(e -> {
                if (e.getPriceListName().equals(parent)) {
                    childrens.add(e);
                }
            });
        }
        return childrens;
    }

    private List<Tiles> getParents() {
        Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getAttribute("allTilesFromRepo");
        List<Tiles> list = new ArrayList<>(set);
        List<Tiles> parents = new ArrayList<>();
        if (list.size() > 0) {
            list.forEach(e -> {
                if (e.getName().equals(Category.DACHOWKA_PODSTAWOWA.toString())) {
                    parents.add(e);
                }
            });
        }
        return parents;
    }
}