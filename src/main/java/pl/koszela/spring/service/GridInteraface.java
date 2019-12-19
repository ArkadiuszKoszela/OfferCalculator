package pl.koszela.spring.service;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import pl.koszela.spring.entities.main.Accessories;
import pl.koszela.spring.entities.main.BaseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import static pl.koszela.spring.service.CalculatePrices.calculateDetalPrice;

public interface GridInteraface<E extends BaseEntity> {

    default Grid<E> createGridd(Grid<E> treeGrid, Binder<E> binder) {
        Grid.Column<E> quantityColumn = treeGrid.addColumn(E::getQuantity).setHeader("Ilość");
        Grid.Column<E> discountColumn = treeGrid.addColumn(E::getDiscount).setHeader("Rabat");
        Grid.Column<E> detalPriceColumn = treeGrid.addColumn(E::getUnitDetalPrice).setHeader("Cena jedn. detal");
        Grid.Column<E> purchasePriceColumn = treeGrid.addColumn(E::getUnitPurchasePrice).setHeader("Cena jedn. zakup");
        Grid.Column<E> allPricePurchaseColumn = treeGrid.addColumn(E::getAllpricePurchase).setHeader("Razem zakup");
        Grid.Column<E> allPriceDetalColumn = treeGrid.addColumn(E::getAllpriceAfterDiscount).setHeader("Razem Detal");
        Grid.Column<E> profitColumn = treeGrid.addColumn(E::getAllprofit).setHeader("Zysk");
        treeGrid.addColumn(createComponent()).setHeader("Opcje");
        treeGrid.addComponentColumn(value -> {
            Button button = new Button("Szczegóły", event -> {
                Dialog dialog = createDialog(value);
                dialog.open();
            });
            return button;
        });

        binder = new Binder<>();
        treeGrid.getEditor().setBinder(binder);

        TextField discountEditField = bindTextFieldToInteger(binder, new StringToIntegerConverter("Błąd"), E::getDiscount, E::setDiscount);
        itemClickListener(treeGrid, discountEditField);
        discountColumn.setEditorComponent(discountEditField);

        TextField quantityField = bindTextFieldToDouble(binder, new StringToDoubleConverter("Błąd"), E::getQuantity, E::setQuantity);
        itemClickListener(treeGrid, quantityField);
        quantityColumn.setEditorComponent(quantityField);

        closeListener(treeGrid, binder);
        settingsGrid(treeGrid);
        return treeGrid;
    }

    default Dialog createDialog(E value) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        FormLayout.ResponsiveStep responsiveStep = new FormLayout.ResponsiveStep("5px", 3);
        formLayout.setResponsiveSteps(responsiveStep);
        TextField textField = new TextField("nazwa", value.getName(), "nazwa");
        TextField textField1 = new TextField("Zdjęcie", value.getUrlToDownloadFile(), "url");
        NumberField textField2 = createNumberField(value.getBasicDiscount().doubleValue(), "Podstawowy rabat");
        NumberField textField3 = createNumberField(value.getPromotionDiscount().doubleValue(), "Promocja");
        NumberField textField4 = createNumberField(value.getAdditionalDiscount().doubleValue(), "Dodatkowy rabat");
        NumberField textField5 = createNumberField(value.getSkontoDiscount().doubleValue(), "Skonto rabat");
        NumberField textField6 = createNumberField(value.getQuantity(), "Ilość");
        NumberField textField7 = createNumberField(value.getUnitDetalPrice(), "Cena katalogowa");
        NumberField textField8 = createNumberField(value.getUnitPurchasePrice(), "Cena zakupu");
        NumberField textField9 = createNumberField(value.getAllpricePurchase(), "Razem cena zakupu");
        NumberField textField10 = createNumberField(value.getAllpriceAfterDiscount(), "Razem cena po rabacie");
        NumberField textField11 = createNumberField(value.getAllprofit(), "Razem zysk");
        formLayout.add(textField, textField1, textField2, textField3, textField4, textField5, textField6, textField7, textField8, textField9, textField10, textField11);
        dialog.add(formLayout);
        return dialog;
    }

    default NumberField createNumberField(Double value, String name) {
        NumberField numberField = new NumberField(name);
        numberField.setValue(value);
        return numberField;
    }

    default void closeListener(Grid<E> grid, Binder<? extends BaseEntity> binder) {
        grid.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null) {
                BaseEntity bean = binder.getBean();
                if (bean.getDiscount() == null || bean.getQuantity() == null) {
                    bean.setDiscount(0);
                    bean.setQuantity(0d);
                }
                if (bean.getDiscount() > 30) {
                    bean.setDiscount(30);
                    NotificationInterface.notificationOpen("Maksymalny rabat to 30 %", NotificationVariant.LUMO_ERROR);
                }
                bean.setUnitDetalPrice(0d);
                bean.setUnitDetalPrice(calculateDetalPrice(bean));
                bean.setUnitDetalPrice(BigDecimal.valueOf(bean.getUnitDetalPrice() * (100 - bean.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bean.setAllpriceAfterDiscount(BigDecimal.valueOf(bean.getUnitDetalPrice() * bean.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bean.setAllpricePurchase(BigDecimal.valueOf(bean.getUnitPurchasePrice() * bean.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bean.setAllprofit(BigDecimal.valueOf(bean.getAllpriceAfterDiscount() - bean.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            grid.getDataProvider().refreshAll();
        });
    }

    default void settingsGrid(Grid<E> grid) {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.getColumns().forEach(e -> e.setAutoWidth(true));
        grid.setMinHeight("1050px");
    }

    default TextField bindTextFieldToInteger(Binder<E> binder, StringToIntegerConverter stringToIntegerConverter, ValueProvider<E, Integer> valueProvider, Setter<E, Integer> setter) {
        TextField textField = new TextField();
        binder.forField(textField)
                .withConverter(stringToIntegerConverter)
                .bind(valueProvider, setter);
        return textField;
    }

    default TextField bindTextFieldToDouble(Binder<E> binder, StringToDoubleConverter stringToDoubleConverter, ValueProvider<E, Double> valueProvider, Setter<E, Double> setter) {
        TextField textField = new TextField();
        binder.forField(textField)
                .withConverter(stringToDoubleConverter)
                .bind(valueProvider, setter);
        return textField;
    }

    default ComponentRenderer<VerticalLayout, E> createComponent(){
        return new ComponentRenderer<>(e -> {
            Checkbox mainCheckBox = new Checkbox("Dodaj");
            mainCheckBox.setValue(e.isOffer());
            mainCheckBox.addValueChangeListener(event -> {
                e.setOffer(event.getValue());
            });
            return new VerticalLayout(mainCheckBox);
        });
    };

    default void itemClickListener(Grid<E> grid, TextField textField) {
        textField.getElement()
                .addEventListener("keydown",
                        event -> grid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
            textField.focus();
        });
    }

    default void readBeans(Set<? extends BaseEntity> set) {
        for (BaseEntity baseEntity : set) {
            baseEntity.setUnitDetalPrice(0d);
            baseEntity.setUnitDetalPrice(calculateDetalPrice(baseEntity));
            baseEntity.setUnitDetalPrice(BigDecimal.valueOf(baseEntity.getUnitDetalPrice() * (100 - baseEntity.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
            baseEntity.setAllpriceAfterDiscount(BigDecimal.valueOf(baseEntity.getUnitDetalPrice() * baseEntity.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            baseEntity.setAllpricePurchase(BigDecimal.valueOf(baseEntity.getUnitPurchasePrice() * baseEntity.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            baseEntity.setAllprofit(BigDecimal.valueOf(baseEntity.getAllpriceAfterDiscount() - baseEntity.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
    }
}