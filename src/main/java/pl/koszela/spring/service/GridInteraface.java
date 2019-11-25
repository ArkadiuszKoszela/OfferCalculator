package pl.koszela.spring.service;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import pl.koszela.spring.entities.main.BaseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import static pl.koszela.spring.service.CalculatePrices.calculateDetalPrice;

public interface GridInteraface<E> {

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

    TreeData addItems(List<E> list);

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

    ComponentRenderer createComponent();

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
//            binder.setBean(baseEntity);
        }
    }
}