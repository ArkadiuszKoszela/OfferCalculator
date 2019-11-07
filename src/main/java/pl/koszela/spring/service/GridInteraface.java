package pl.koszela.spring.service;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import pl.koszela.spring.entities.BaseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public interface GridInteraface<E> {
    Grid<E> createGrid();

    default void itemClickListener(Grid<E> grid, TextField textField) {
        grid.addItemDoubleClickListener(event -> {
            grid.getEditor().editItem(event.getItem());
            textField.focus();
        });
    }

    default void closeListener(Grid<E> grid, Binder<? extends BaseEntity> binder, BaseEntity baseEntity) {
        grid.getEditor().addCloseListener(event -> {
            if (binder.getBean() != null) {
//                baseEntity = binder.getBean();
                BaseEntity bean = binder.getBean();
                bean.setUnitDetalPrice(new BigDecimal(bean.getUnitDetalPrice() * (100 - bean.getDiscount()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bean.setAllpriceAfterDiscount(new BigDecimal(bean.getUnitDetalPrice() * bean.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bean.setAllpricePurchase(new BigDecimal(bean.getUnitPurchasePrice() * bean.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                bean.setAllprofit(new BigDecimal(bean.getAllpriceAfterDiscount() - bean.getAllpricePurchase()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                if (bean.getDiscount() <= 30) {
//                    binder.setBean(?);
                } else {
                    bean.setDiscount(30);
                    NotificationInterface.notificationOpen("Maksymalny rabat to 30 %", NotificationVariant.LUMO_ERROR);
//                    binder.setBean();
                }
            }
            grid.getDataProvider().refreshAll();
        });
    }

    TreeData addItems(List list);

    TextField editField(StringToIntegerConverter stringToIntegerConverter, StringToDoubleConverter stringToDoubleConverter);

    ComponentRenderer createComponent();

    default void addEnterEvent(Grid<E> treeGrid, TextField textField) {
        textField.getElement()
                .addEventListener("keydown",
                        event -> treeGrid.getEditor().cancel())
                .setFilter("event.key === 'Enter'");
    }
}