package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.notification.NotificationVariant;
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
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import pl.koszela.spring.entities.main.Gutter;
import pl.koszela.spring.service.GridInteraface;
import pl.koszela.spring.service.NotificationInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = GutterView.GUTTER_VIEW, layout = MainView.class)
public class GutterView extends VerticalLayout implements GridInteraface<Gutter>, BeforeLeaveObserver, BeforeEnterObserver {

    public static final String GUTTER_VIEW = "gutter";
    private TreeGrid<Gutter> treeGrid = new TreeGrid<>();
    private List<Gutter> list = (List<Gutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
    private Binder<Gutter> binder;

    public GutterView() {
        addPriceToList(list);
        add(checkboxGroupType());
        add(createGrid());
    }

    public TreeGrid createGrid() {
        Grid.Column<Gutter> nameColumn = treeGrid.addHierarchyColumn(Gutter::getName).setResizable(true).setHeader("Nazwa");
        Grid.Column<Gutter> quantityColumn = treeGrid.addColumn(Gutter::getQuantity).setHeader("Ilość");
        Grid.Column<Gutter> discountColumn = treeGrid.addColumn(Gutter::getDiscount).setHeader("Rabat");
        treeGrid.addColumn(Gutter::getTotalPrice).setHeader("Total klient");
        treeGrid.addColumn(Gutter::getTotalProfit).setHeader("Total zysk");
        Column<Gutter> unitPurchaseColumn = treeGrid.addColumn(Gutter::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Column<Gutter> unitDetalColumn = treeGrid.addColumn(Gutter::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<Gutter> allPurchaseColumn = treeGrid.addColumn(Gutter::getAllpricePurchase).setHeader("Razem cena netto");
        Grid.Column<Gutter> allDetalColumn = treeGrid.addColumn(Gutter::getAllpriceAfterDiscount).setHeader("Razem cena detal");
        Grid.Column<Gutter> protitColumn = treeGrid.addColumn(Gutter::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");

        binder = new Binder<>(Gutter.class);
        treeGrid.getEditor().setBinder(binder);

        TextField discountField= bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), Gutter::getDiscount, Gutter::setDiscount);
        itemClickListener(treeGrid, discountField);
        discountColumn.setEditorComponent(discountField);

        TextField quantityField = bindTextFieldToDouble(binder, new StringToDoubleConverter("Błąd"), Gutter::getQuantity, Gutter::setQuantity);
        itemClickListener(treeGrid, quantityField);
        quantityColumn.setEditorComponent(quantityField);

        FooterRow footerRow = treeGrid.appendFooterRow();

        Button calculate = refreshButton(treeGrid);

        footerRow.getCell(nameColumn).setComponent(calculate);

        closeListener(treeGrid, binder);
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(list)));
        settingsGrid(treeGrid);
        return treeGrid;
    }

    @Override
    public TreeData<Gutter> addItems(List list) {
        TreeData<Gutter> treeData = new TreeData<>();
        if ((List<Gutter>) list != null) {
            List<Gutter> parents = ((List<Gutter>) list).stream().filter(gutter -> gutter.getName().equals("rynna 3mb")).collect(Collectors.toList());
            for (Gutter parent : parents) {
                List<Gutter> childrens = ((List<Gutter>) list).stream().filter(gutter -> gutter.getManufacturer().equals(parent.getManufacturer()) && !gutter.getUnitDetalPrice().equals(0d)).collect(Collectors.toList());
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

    private void addPriceToList(List<Gutter> list) {
        if (list == null) {
            UI.getCurrent().navigate(IncludeDataView.class);
            NotificationInterface.notificationOpen("Na początku proszę wprowadzić dane !!", NotificationVariant.LUMO_ERROR);
        } else {
            for (Gutter gutter : list) {
                gutter.setUnitPurchasePrice(new BigDecimal(gutter.getUnitDetalPrice() * 0.7).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllpriceAfterDiscount(new BigDecimal(gutter.getQuantity() * gutter.getUnitDetalPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllpricePurchase(new BigDecimal(gutter.getQuantity() * gutter.getUnitPurchasePrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllprofit(new BigDecimal(gutter.getAllpriceAfterDiscount() - gutter.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
        }
    }

    @Override
    public ComponentRenderer<VerticalLayout, Gutter> createComponent() {
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

    private RadioButtonGroup<String> checkboxGroupType() {
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("Stalowa", "PCV", "Tytan-cynk", "Ocynk", "Wszystko");
        radioButtonGroup.addValueChangeListener(e -> {
            String value = e.getValue();
            switch (value) {
                case "Stalowa": {
                    List<Gutter> searchCategory = list.stream().filter(gutter -> gutter.getCategory().contains("Flamingo")).collect(Collectors.toList());
                    treeGrid.setDataProvider(new TreeDataProvider<>(addItems(searchCategory)));
                    treeGrid.getDataProvider().refreshAll();
                    break;
                }
                case "PCV": {
                    List<Gutter> searchCategory = list.stream().filter(gutter -> gutter.getCategory().contains("Bryza")).collect(Collectors.toList());
                    treeGrid.setDataProvider(new TreeDataProvider<>(addItems(searchCategory)));
                    treeGrid.getDataProvider().refreshAll();
                    break;
                }
                case "Wszystko":
                    treeGrid.setDataProvider(new TreeDataProvider<>(addItems(list)));
                    treeGrid.getDataProvider().refreshAll();
                    break;
            }
        });
        return radioButtonGroup;
    }

    private Button refreshButton(TreeGrid<Gutter> treeGrid) {
        return new Button("Refresh", buttonClickEvent -> {
            List<Gutter> mainsInPriceList = list.stream().filter(e -> e.getName().equals("rynna 3mb")).collect(Collectors.toList());
            for (Gutter gutter : mainsInPriceList) {
                List<Gutter> onePriceList = list.stream().filter(e -> e.getCategory().equals(gutter.getCategory())).collect(Collectors.toList());
                Double totalPrice = onePriceList.stream().map(Gutter::getAllpriceAfterDiscount).reduce(Double::sum).orElse(0d);
                Double totalProfit = onePriceList.stream().map(Gutter::getAllprofit).reduce(Double::sum).orElse(0d);
                gutter.setTotalPrice(BigDecimal.valueOf(totalPrice).setScale(2, RoundingMode.HALF_UP));
                gutter.setTotalProfit(BigDecimal.valueOf(totalProfit).setScale(2, RoundingMode.HALF_UP));
            }
            treeGrid.getDataProvider().refreshAll();
        });
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        VaadinSession.getCurrent().getSession().setAttribute("gutter", list);
        action.proceed();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (list == null) {
            event.rerouteTo(IncludeDataView.class);
        }
    }
}