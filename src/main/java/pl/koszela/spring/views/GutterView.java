package pl.koszela.spring.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.FooterRow;
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
import pl.koszela.spring.entities.gutter.EntityGutter;
import pl.koszela.spring.service.GridInteraface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import static pl.koszela.spring.service.ServiceNotification.getNotificationError;

@Route(value = GutterView.GUTTER_VIEW, layout = MainView.class)
public class GutterView extends VerticalLayout implements GridInteraface, BeforeLeaveObserver, BeforeEnterObserver {

    public static final String GUTTER_VIEW = "gutter";
    private TreeGrid<EntityGutter> treeGrid = new TreeGrid<>();
    private List<EntityGutter> list = (List<EntityGutter>) VaadinSession.getCurrent().getSession().getAttribute("gutter");
    private Binder<EntityGutter> binder;

    public GutterView() {
        addPriceToList(list);
        add(checkboxGroupType());
        add(createGrid());
    }

    @Override
    public TreeGrid createGrid() {
        com.vaadin.flow.component.grid.Grid.Column<EntityGutter> nameColumn = treeGrid.addHierarchyColumn(EntityGutter::getName).setResizable(true).setHeader("Nazwa");
        com.vaadin.flow.component.grid.Grid.Column<EntityGutter> quantityColumn = treeGrid.addColumn(EntityGutter::getQuantity).setHeader("Ilość");
        com.vaadin.flow.component.grid.Grid.Column<EntityGutter> discountColumn = treeGrid.addColumn(EntityGutter::getDiscount).setHeader("Rabat");
        treeGrid.addColumn(EntityGutter::getTotalPrice).setHeader("Total klient");
        treeGrid.addColumn(EntityGutter::getTotalProfit).setHeader("Total zysk");
        com.vaadin.flow.component.grid.Grid.Column<EntityGutter> unitPurchaseColumn = treeGrid.addColumn(EntityGutter::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        com.vaadin.flow.component.grid.Grid.Column<EntityGutter> unitDetalColumn = treeGrid.addColumn(EntityGutter::getUnitDetalPrice).setHeader("Cena jedn. detal");
        com.vaadin.flow.component.grid.Grid.Column<EntityGutter> allPurchaseColumn = treeGrid.addColumn(EntityGutter::getAllpricePurchase).setHeader("Razem cena netto");
        com.vaadin.flow.component.grid.Grid.Column<EntityGutter> allDetalColumn = treeGrid.addColumn(EntityGutter::getAllpriceAfterDiscount).setHeader("Razem cena detal");
        com.vaadin.flow.component.grid.Grid.Column<EntityGutter> protitColumn = treeGrid.addColumn(EntityGutter::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createCheckboxes()).setHeader("Opcje");

        binder = new Binder<>(EntityGutter.class);
        treeGrid.getEditor().setBinder(binder);

        TextField discountField = new TextField();
        addEnterEvent(treeGrid, discountField);
        binder.forField(discountField)
                .withConverter(new StringToIntegerConverter("Błąd"))
                .bind(EntityGutter::getDiscount, EntityGutter::setDiscount);
        itemClickListener(discountField);
        discountColumn.setEditorComponent(discountField);

        TextField quantityField = new TextField();
        binder.forField(quantityField)
                .withConverter(new StringToDoubleConverter("Błąd"))
                .bind(EntityGutter::getQuantity, EntityGutter::setQuantity);
        addEnterEvent(treeGrid, quantityField);
        itemClickListener(quantityField);
        quantityColumn.setEditorComponent(quantityField);

        FooterRow footerRow = treeGrid.appendFooterRow();

        Button calculate = refreshButton(treeGrid);

        footerRow.getCell(nameColumn).setComponent(calculate);

        closeListener();
        treeGrid.setDataProvider(new TreeDataProvider<>(addItems(list)));
        treeGrid.getColumns().forEach(e -> e.setAutoWidth(true));
        treeGrid.setMinHeight("600px");
        return treeGrid;
    }

    @Override
    public void itemClickListener(TextField textField) {
        treeGrid.addItemDoubleClickListener(e -> {
            treeGrid.getEditor().editItem(e.getItem());
            textField.focus();
        });
    }

    @Override
    public void closeListener() {
        treeGrid.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null) {
                EntityGutter gutter = binder.getBean();
                gutter.setUnitDetalPrice(new BigDecimal(gutter.getUnitDetalPrice() * (100 - gutter.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllpriceAfterDiscount(new BigDecimal(gutter.getUnitDetalPrice() * gutter.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllpricePurchase(new BigDecimal(gutter.getUnitPurchasePrice() * gutter.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllprofit(new BigDecimal(gutter.getAllpriceAfterDiscount() - gutter.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
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

    @Override
    public TreeData<EntityGutter> addItems(List list) {
        TreeData<EntityGutter> treeData = new TreeData<>();
        if ((List<EntityGutter>) list != null) {
            List<EntityGutter> parents = ((List<EntityGutter>) list).stream().filter(gutter -> gutter.getName().equals("rynna 3mb")).collect(Collectors.toList());
            for (EntityGutter parent : parents) {
                List<EntityGutter> childrens = ((List<EntityGutter>) list).stream().filter(gutter -> gutter.getCategory().equals(parent.getCategory()) && !gutter.getUnitDetalPrice().equals(0d)).collect(Collectors.toList());
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

    private void addPriceToList(List<EntityGutter> list) {
        if (list == null) {
            UI.getCurrent().navigate(IncludeDataView.class);
            getNotificationError("Na początku proszę wprowadzić dane !!");
        } else {
            for (EntityGutter gutter : list) {
                gutter.setUnitPurchasePrice(new BigDecimal(gutter.getUnitDetalPrice() * 0.7).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllpriceAfterDiscount(new BigDecimal(gutter.getQuantity() * gutter.getUnitDetalPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllpricePurchase(new BigDecimal(gutter.getQuantity() * gutter.getUnitPurchasePrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                gutter.setAllprofit(new BigDecimal(gutter.getAllpriceAfterDiscount() - gutter.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
        }
    }

    @Override
    public TextField editField(StringToIntegerConverter stringToIntegerConverter, StringToDoubleConverter stringToDoubleConverter) {
        return null;
    }

    @Override
    public ComponentRenderer<VerticalLayout, EntityGutter> createCheckboxes() {
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
                    List<EntityGutter> searchCategory = list.stream().filter(gutter -> gutter.getCategory().contains("Flamingo")/* && !gutter.getUnitDetalPrice().equals(0d)*/).collect(Collectors.toList());
                    treeGrid.setDataProvider(new TreeDataProvider<>(addItems(searchCategory)));
                    treeGrid.getDataProvider().refreshAll();
                    break;
                }
                case "PCV": {
                    List<EntityGutter> searchCategory = list.stream().filter(gutter -> gutter.getCategory().contains("Bryza")/* && !gutter.getUnitDetalPrice().equals(0d)*/).collect(Collectors.toList());
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

    private Button refreshButton(TreeGrid<EntityGutter> treeGrid) {
        return new Button("Refresh", buttonClickEvent -> {
            List<EntityGutter> mainsInPriceList = list.stream().filter(e -> e.getName().equals("rynna 3mb")).collect(Collectors.toList());
            for (EntityGutter gutter : mainsInPriceList) {
                List<EntityGutter> onePriceList = list.stream().filter(e -> e.getCategory().equals(gutter.getCategory())).collect(Collectors.toList());
                Double totalPrice = onePriceList.stream().map(EntityGutter::getAllpriceAfterDiscount).reduce(Double::sum).get();
                Double totalProfit = onePriceList.stream().map(EntityGutter::getAllprofit).reduce(Double::sum).get();
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