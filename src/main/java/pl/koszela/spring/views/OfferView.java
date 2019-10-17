package pl.koszela.spring.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.*;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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

import static pl.koszela.spring.entities.OptionEnum.GLOWNA;
import static pl.koszela.spring.entities.OptionEnum.OPCJONALNA;
import static pl.koszela.spring.service.ServiceNotification.getNotificationError;
import static pl.koszela.spring.service.ServiceNotification.getNotificationSucces;

@Route(value = OfferView.CREATE_OFFER, layout = MainView.class)
public class OfferView extends VerticalLayout {

    static final String CREATE_OFFER = "createOffer";

    private NumberField tilesDiscount = new NumberField("Rabat dachówki");
    private NumberField accesoriesDiscount = new NumberField("Rabat akcesoria");
    private NumberField windowsDiscount = new NumberField("Rabat okna/kołnierz");
    private VerticalLayout layout = new VerticalLayout();
    private TreeGrid<Tiles> treeGrid = new TreeGrid<>();
    private Set<Tiles> set = (Set<Tiles>) VaadinSession.getCurrent().getSession().getAttribute("allTilesFromRepo");

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
        Grid.Column<Tiles> price = treeGrid.addColumn(Tiles::getPrice).setHeader("Cena detal");
        Grid.Column<Tiles> priceRetail = treeGrid.addColumn(Tiles::getPriceFromRepo).setHeader("Cena zakupu");
        treeGrid.addColumn(Tiles::getTotalPrice).setHeader("Total klient");
        treeGrid.addColumn(Tiles::getTotalProfit).setHeader("Total zysk");
        treeGrid.addColumn(tiles -> tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(BigDecimal.valueOf(130), 2, RoundingMode.HALF_UP)).setHeader("Cena zakupu");
        treeGrid.addColumn(tiles -> tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(new BigDecimal(tiles.getDiscount()).add(new BigDecimal(100)), 2, RoundingMode.HALF_UP)).setHeader("Cena po rabacie");
        treeGrid.addColumn(tiles -> (tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(new BigDecimal(tiles.getDiscount()).add(new BigDecimal(100)), 2, RoundingMode.HALF_UP).subtract(tiles.getPrice().multiply(new BigDecimal(tiles.getQuantity())).multiply(new BigDecimal(100)).divide(BigDecimal.valueOf(130), 2, RoundingMode.HALF_UP)))).setHeader("Zysk");
        TreeGrid.Column opcje = treeGrid.addColumn(new ComponentRenderer<>(tiles -> {
            Checkbox main = new Checkbox("Główna");
            Checkbox option = new Checkbox("Opcjonalna");
            main.setValue(tiles.isMain());
            option.setValue(tiles.isOption());
            main.addValueChangeListener(event -> {
                tiles.setMain(event.getValue());
                tiles.setOption(!event.getValue());
                option.setValue(!main.getValue());
            });
            option.addValueChangeListener(event -> {
                tiles.setMain(!event.getValue());
                tiles.setOption(event.getValue());
                main.setValue(!option.getValue());
            });
            return new VerticalLayout(main, option);
        })).setHeader("Opcje");

        FooterRow footerRow = treeGrid.appendFooterRow();
        HeaderRow filterRow = treeGrid.appendHeaderRow();
        TextField filter = new TextField();

        TreeDataProvider<Tiles> treeDataProvider;
        treeDataProvider = new TreeDataProvider<>(getTilesTreeDataFromRepo());
        filter.addValueChangeListener(event -> treeDataProvider.setFilter(
                tiles -> StringUtils.containsIgnoreCase(tiles.getPriceListName(), filter.getValue())));
        getNotificationSucces("Repo - Wczytano dane");

        filter.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(priceListName).setComponent(filter);
        filter.setSizeFull();
        filter.setPlaceholder("Filter");

        Button calculate = getButtonRefreshNew(treeGrid);

        footerRow.getCell(priceListName).setComponent(calculate);
        treeGrid.setDataProvider(treeDataProvider);

        Editor(treeGrid, discount);
        treeGrid.setMinHeight("550px");
        return treeGrid;
    }

    private Button getButtonRefreshNew(TreeGrid<Tiles> treeGrid) {
        Button refresh = new Button("Refresh");
        refresh.addClickListener(buttonClickEvent -> {
            Set<Tiles> parents = set.stream().filter(e -> e.getName().equals(Category.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles parent : parents) {
                parent.setPricePurchase((parent.getPrice().multiply(new BigDecimal(parent.getQuantity())).multiply(new BigDecimal(70))).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                parent.setPriceAfterDiscount((parent.getPrice().multiply(new BigDecimal(parent.getQuantity())).multiply(new BigDecimal(100).subtract(new BigDecimal(parent.getDiscount())))).divide((new BigDecimal(100)), 2, RoundingMode.HALF_UP));
                parent.setProfit(parent.getPriceAfterDiscount().subtract(parent.getPricePurchase()));
                List<Tiles> childrens = set.stream().filter(e -> e.getPriceListName().equals(parent.getPriceListName())).collect(Collectors.toList());
                int priceAfterDiscount = 0;
                int profit = 0;
                for (Tiles tiles : childrens) {
                    priceAfterDiscount = priceAfterDiscount + tiles.getPriceAfterDiscount().intValue();
                    profit = profit + tiles.getProfit().intValue();
                }
                priceAfterDiscount = priceAfterDiscount + parent.getPriceAfterDiscount().intValue();
                profit = profit + parent.getPriceAfterDiscount().intValue();
                for (Tiles children : childrens) {
                    children.setPricePurchase(children.getPrice().multiply(new BigDecimal(children.getQuantity())).multiply(new BigDecimal(70)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    children.setPriceAfterDiscount((children.getPrice().multiply(new BigDecimal(children.getQuantity())).multiply(new BigDecimal(100).subtract(new BigDecimal(children.getDiscount())))).divide((new BigDecimal(100)), RoundingMode.HALF_UP));
                    children.setProfit(children.getPriceAfterDiscount().subtract(children.getPricePurchase()));
                }
                parent.setTotalPrice(BigDecimal.valueOf(priceAfterDiscount));
                parent.setTotalProfit(BigDecimal.valueOf(profit));
            }
            treeGrid.getDataProvider().refreshAll();
        });
        return refresh;
    }

    private void Editor(TreeGrid<Tiles> treeGrid, Grid.Column<Tiles> discount) {
        Binder<Tiles> binder = new Binder<>(Tiles.class);
        Editor<Tiles> editor = treeGrid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField editDiscount = new TextField();
        binder.forField(editDiscount)
                .withConverter(
                        new StringToIntegerConverter("Discount must be a number."))
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
                tiles.setDiscount(Integer.parseInt(editDiscount.getValue()));
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        }).setHeader("Edit");


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

    private TreeData<Tiles> getTilesTreeDataFromRepo() {
        TreeData<Tiles> treeData = new TreeData<>();
        if (set != null) {
            Set<Tiles> parents = set.stream().filter(e -> e.getName().equals(Category.DACHOWKA_PODSTAWOWA.toString())).collect(Collectors.toSet());
            for (Tiles parent : parents) {
                List<Tiles> childrens = set.stream().filter(e -> e.getPriceListName().equals(parent.getPriceListName())).collect(Collectors.toList());
                for (int i = 0; i < childrens.size(); i++) {
                    if (i == 0) {
                        treeData.addItem(null, parent);
                    } else if (!childrens.get(i).getName().equals(Category.DACHOWKA_PODSTAWOWA.toString())) {
                        treeData.addItem(parent, childrens.get(i));
                    }
                }
            }
        }
        return treeData;
    }
}